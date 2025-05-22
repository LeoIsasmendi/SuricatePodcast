/*
 * The MIT License (MIT)
 * Copyright (c) 2017. Sergio Leonardo Isasmendi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package leoisasmendi.android.com.suricatepodcast.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.media.MediaPlayer.OnBufferingUpdateListener
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnSeekCompleteListener
import android.media.session.MediaSessionManager
import android.os.Binder
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import leoisasmendi.android.com.suricatepodcast.MainActivity
import leoisasmendi.android.com.suricatepodcast.R
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider
import leoisasmendi.android.com.suricatepodcast.utils.ParserUtils
import leoisasmendi.android.com.suricatepodcast.utils.PlaybackStatus
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil
import leoisasmendi.android.com.suricatepodcast.widget.PodcastWidgetProvider

class MediaPlayerService : Service(), OnCompletionListener, OnPreparedListener,
    MediaPlayer.OnErrorListener, OnSeekCompleteListener, MediaPlayer.OnInfoListener,
    OnBufferingUpdateListener, OnAudioFocusChangeListener {
    private val TAG: String = javaClass.getSimpleName()

    // Binder given to clients
    private val iBinder: IBinder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null

    //Used to pause/resume MediaPlayer
    private var resumePosition = 0
    private var audioManager: AudioManager? = null

    //Handle incoming phone calls
    private var ongoingCall = false
    private var phoneStateListener: PhoneStateListener? = null
    private var telephonyManager: TelephonyManager? = null


    //List of available Audio files
    private var audioIndex = -1
    private var activeAudio: PlaylistItem? = null //an object of the currently playing audio
    private var mCursor: Cursor? = null

    private val playNewAudio: BroadcastReceiver = this.newAudioBroadcastReceiver

    //Becoming noisy (headphone removed)
    private val becomingNoisyReceiver: BroadcastReceiver = getBecomingNoisyReceiver()

    //MediaSession
    private var mediaSessionManager: MediaSessionManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var transportControls: MediaControllerCompat.TransportControls? = null

    override fun onCreate() {
        super.onCreate()

        // Perform one-time setup procedures
        mCursor = getContentResolver().query(
            DataProvider.CONTENT_URI,
            null,
            "",
            null,
            null
        )
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener()
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver()
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio()
    }


    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        //Set up MediaPlayer event listeners
        mediaPlayer!!.setOnCompletionListener(this)
        mediaPlayer!!.setOnErrorListener(this)
        mediaPlayer!!.setOnPreparedListener(this)
        mediaPlayer!!.setOnBufferingUpdateListener(this)
        mediaPlayer!!.setOnSeekCompleteListener(this)
        mediaPlayer!!.setOnInfoListener(this)
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer!!.reset()

        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            publishStatus(STATUS_FETCHING)
            // Set the data source to the mediaFile location
            mediaPlayer!!.setDataSource(activeAudio!!.audio)
            mediaPlayer!!.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
            publishStatus(STATUS_ERROR)
            stopSelf()
            Log.d(TAG, "initMediaPlayer: " + R.string.media_player_error_1)
        }
    }

    @Throws(RemoteException::class)
    private fun initMediaSession() {
        if (mediaSessionManager != null) return  //mediaSessionManager exists


        mediaSessionManager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager?
        // Create a new MediaSession
        mediaSession = MediaSessionCompat(getApplicationContext(), "AudioPlayer")
        //Get MediaSessions transport controls
        transportControls = mediaSession!!.getController().getTransportControls()
        //set MediaSession -> ready to receive media commands
        mediaSession!!.setActive(true)
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        //Set mediaSession's MetaData
        updateMetaData()

        // Attach Callback to receive MediaSession updates
        mediaSession!!.setCallback(object : MediaSessionCompat.Callback() {
            // Implement callbacks
            override fun onPlay() {
                super.onPlay()
                resumeMedia()
                publishStatus(STATUS_PLAYING)
                buildNotification(PlaybackStatus.PLAYING)
            }

            override fun onPause() {
                super.onPause()
                pauseMedia()
                publishStatus(STATUS_PAUSED)
                buildNotification(PlaybackStatus.PAUSED)
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                skipToNext()
                updateMetaData()
                buildNotification(PlaybackStatus.PLAYING)
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                skipToPrevious()
                updateMetaData()
                buildNotification(PlaybackStatus.PLAYING)
            }

            override fun onStop() {
                super.onStop()
                publishStatus(STATUS_STOPED)
                removeNotification()
                //Stop the service
                stopSelf()
            }
        })
    }

    private fun publishStatus(status: String) {
        val intent: Intent = Intent(NOTIFICATION)
        if (status == MEDIA_UPDATED) {
            intent.putExtra("EXTRA_TITLE", activeAudio!!.title)
            intent.putExtra("EXTRA_DURATION", activeAudio!!.duration)
        }
        intent.putExtra(STATUS, status)
        sendBroadcast(intent)
    }

    private fun updateMetaData() {
        if (activeAudio != null) {
            val albumArt = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.ic_default_poster
            ) //replace with medias albumArt
            // Update the current metadata
            mediaSession!!.setMetadata(
                MediaMetadataCompat.Builder()
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, activeAudio!!.title)
                    .build()
            )
        }
    }

    private fun buildNotification(playbackStatus: PlaybackStatus?) {
        //TODO: REFACTOR updateWidget method

        updateWidgets(playbackStatus)

        var notificationAction = R.drawable.ic_media_control_pause //needs to be initialized
        var play_pauseAction: PendingIntent? = null

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = R.drawable.ic_media_control_pause
            //create the pause action
            play_pauseAction = playbackAction(1)
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = R.drawable.ic_media_control_play
            //create the play action
            play_pauseAction = playbackAction(0)
        }

        //TODO: replace this with my icon
        val largeIcon = BitmapFactory.decodeResource(
            getResources(),
            R.drawable.ic_default_poster
        ) //replace with your own image

        // Create a new Notification
        val notificationBuilder = NotificationCompat.Builder(this)
            .setShowWhen(false) // Set the Notification style
            //.setStyle(new NotificationCompat.MediaStyle()
            // Attach our MediaSession token
            //      .setMediaSession(mediaSession.getSessionToken())
            // Show our playback controls in the compact notification view.
            //    .setShowActionsInCompactView(0, 1, 2))
            // Set the Notification color
            .setColor(
                ContextCompat.getColor(
                    getBaseContext(),
                    R.color.colorPrimaryDark
                )
            ) // Set the large and small icons
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.ic_headphones) // Set Notification content information
            .setContentText(activeAudio!!.title) // Add playback actions
            .addAction(R.drawable.ic_media_control_prev, "previous", playbackAction(3))
            .addAction(notificationAction, "pause", play_pauseAction)
            .addAction(R.drawable.ic_media_control_next, "next", playbackAction(2))

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
            NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    private fun updateWidgets(playbackStatus: PlaybackStatus?) {
        val view = RemoteViews(getPackageName(), R.layout.podcast_widget_player)

        if (playbackStatus == PlaybackStatus.PLAYING) {
            view.setImageViewResource(R.id.widget_play, R.drawable.media_player_pause_24x24)
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            view.setImageViewResource(R.id.widget_play, R.drawable.media_player_play_24x24)
        }

        view.setTextViewText(R.id.widget_title, activeAudio!!.title)
        view.setTextViewText(R.id.widget_length, activeAudio!!.duration)

        Picasso.get().setLoggingEnabled(true)

        val target: Target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                view.setImageViewBitmap(R.id.widget_thumbail, bitmap)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                view.setImageViewResource(R.id.widget_thumbail, R.drawable.picture)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                view.setImageViewResource(R.id.widget_thumbail, R.drawable.picture)
            }
        }

        Picasso.get()
            .load(activeAudio!!.poster)
            .into(target)


        // Push update for this widget to the home screen
        val thisWidget = ComponentName(this, PodcastWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)
        manager.updateAppWidget(thisWidget, view)
    }

    private fun removeNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun playbackAction(actionNumber: Int): PendingIntent? {
        val playbackAction = Intent(this, MediaPlayerService::class.java)
        when (actionNumber) {
            0 -> {
                // Play
                playbackAction.setAction(ACTION_PLAY)
                return PendingIntent.getService(
                    this,
                    actionNumber,
                    playbackAction,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

            1 -> {
                // Pause
                playbackAction.setAction(ACTION_PAUSE)
                return PendingIntent.getService(
                    this,
                    actionNumber,
                    playbackAction,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

            2 -> {
                // Next track
                playbackAction.setAction(ACTION_NEXT)
                return PendingIntent.getService(
                    this,
                    actionNumber,
                    playbackAction,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

            3 -> {
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS)
                return PendingIntent.getService(
                    this,
                    actionNumber,
                    playbackAction,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

            else -> {}
        }
        return null
    }

    private fun handleIncomingActions(playbackAction: Intent?) {
        if (playbackAction == null || playbackAction.getAction() == null) return

        val actionString = playbackAction.getAction()
        if (actionString.equals(ACTION_PLAY, ignoreCase = true)) {
            transportControls!!.play()
        } else if (actionString.equals(ACTION_PAUSE, ignoreCase = true)) {
            transportControls!!.pause()
        } else if (actionString.equals(ACTION_NEXT, ignoreCase = true)) {
            transportControls!!.skipToNext()
        } else if (actionString.equals(ACTION_PREVIOUS, ignoreCase = true)) {
            transportControls!!.skipToPrevious()
        } else if (actionString.equals(ACTION_STOP, ignoreCase = true)) {
            transportControls!!.stop()
        }
    }

    private fun loadActiveAudio() {
        if (audioIndex != -1 && audioIndex < mCursor!!.getCount()) {
            //index is in a valid range
            mCursor!!.moveToPosition(audioIndex)
            activeAudio = ParserUtils.buildPlaylistItem(mCursor!!)
        } else {
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        // TODO: Return the communication channel to the service.
        return iBinder
    }

    //The system calls this method when an activity, requests the service be started
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            //Load data from SharedPreferences
            val storage = StorageUtil(getApplicationContext())
            audioIndex = storage.loadAudioIndex()
            Log.d(TAG, "onStartCommand: " + R.string.media_player_connecting)
            loadActiveAudio()
        } catch (e: NullPointerException) {
            Log.d(TAG, "onStartCommand: " + R.string.media_player_error_1)
            stopSelf()
        }

        //Request audio focus
        if (!requestAudioFocus()) {
            //Could not gain focus
            stopSelf()
        }

        if (mediaSessionManager == null) {
            try {
                initMediaSession()
                initMediaPlayer()
            } catch (e: RemoteException) {
                e.printStackTrace()
                stopSelf()
            }
            buildNotification(PlaybackStatus.PLAYING)
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            stopMedia()
            mediaPlayer!!.release()
        }
        removeAudioFocus()
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager!!.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        }

        removeNotification()

        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver)
        unregisterReceiver(playNewAudio)

        //clear cached playlist
        StorageUtil(getApplicationContext()).clearCachedAudioPlaylist()
        mCursor!!.close()
    }

    override fun onBufferingUpdate(mediaPlayer: MediaPlayer?, percent: Int) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    override fun onCompletion(mediaPlayer: MediaPlayer?) {
        //Invoked when playback of a media source has completed.
        stopMedia()
        //stop the service
        stopSelf()
    }

    //Handle errors
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        //Invoked when there has been an error during an asynchronous operation
        Log.d(TAG, "onError: " + R.string.media_player_error_2)
        when (what) {
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra
            )

            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR SERVER DIED " + extra
            )

            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR UNKNOWN " + extra
            )
        }
        return false
    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        //Invoked to communicate some info.
        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        //Invoked when the media source is ready for playback.
        Log.d(TAG, "onPrepared: " + R.string.media_player_successful_fetch)
        publishStatus(STATUS_DONE)
        playMedia()
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
        //Invoked indicating the completion of a seek operation.
    }

    override fun onAudioFocusChange(focusState: Int) {
        //Invoked when the audio focus of the system is updated.
        when (focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // resume playback
                if (mediaPlayer == null) initMediaPlayer()
                else if (!mediaPlayer!!.isPlaying()) mediaPlayer!!.start()
                mediaPlayer!!.setVolume(1.0f, 1.0f)
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer!!.isPlaying()) mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->                 // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer!!.isPlaying()) mediaPlayer!!.pause()

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->                 // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer!!.isPlaying()) mediaPlayer!!.setVolume(0.1f, 0.1f)
        }
    }

    private fun requestAudioFocus(): Boolean {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val result = audioManager!!.requestAudioFocus(
            this,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true
        }
        //Could not gain focus
        return false
    }

    private fun removeAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager!!.abandonAudioFocus(this)
    }

    private val newAudioBroadcastReceiver: BroadcastReceiver
        get() = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //Get the new media index form SharedPreferences

                audioIndex = StorageUtil(getApplicationContext()).loadAudioIndex()
                loadActiveAudio()

                //A PLAY_NEW_AUDIO action received
                //reset mediaPlayer to play the new Audio
                stopMedia()
                mediaPlayer!!.reset()
                initMediaPlayer()
                updateMetaData()
                buildNotification(PlaybackStatus.PLAYING)
            }
        }

    private fun register_playNewAudio() {
        //Register playNewMedia receiver
        val filter = IntentFilter(MainActivity.Broadcast_PLAY_NEW_AUDIO)
        registerReceiver(playNewAudio, filter)
    }

    //Handle incoming phone calls
    private fun callStateListener() {
        // Get the telephony manager
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        //Starting listening for PhoneState changes
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_OFFHOOK, TelephonyManager.CALL_STATE_RINGING -> if (mediaPlayer != null) {
                        pauseMedia()
                        ongoingCall = true
                    }

                    TelephonyManager.CALL_STATE_IDLE ->                         // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false
                                resumeMedia()
                            }
                        }
                }
            }
        }
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager!!.listen(
            phoneStateListener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
    }


    //Becoming noisy (headphone removed)
    private fun getBecomingNoisyReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //pause audio on ACTION_AUDIO_BECOMING_NOISY
                pauseMedia()
                buildNotification(PlaybackStatus.PAUSED)
            }
        }
    }


    private fun registerBecomingNoisyReceiver() {
        //register after getting audio focus
        val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(becomingNoisyReceiver, intentFilter)
    }


    //MEDIA PLAYER BASIC CONTROLS
    private fun playMedia() {
        if (!mediaPlayer!!.isPlaying()) {
            Log.d(TAG, "playMedia: " + R.string.media_player_playing)
            mediaPlayer!!.start()
            publishStatus(MEDIA_UPDATED)
            publishStatus(STATUS_PLAYING)
        }
    }

    private fun stopMedia() {
        if (mediaPlayer == null) return
        if (mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.stop()
            publishStatus(STATUS_STOPED)
        }
    }

    private fun pauseMedia() {
        if (mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.pause()
            resumePosition = mediaPlayer!!.getCurrentPosition()
            publishStatus(STATUS_PAUSED)
        }
    }

    private fun resumeMedia() {
        if (!mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.seekTo(resumePosition)
            mediaPlayer!!.start()
            publishStatus(STATUS_PLAYING)
        }
    }

    private fun skipToNext() {
        if (audioIndex == mCursor!!.getCount() - 1) {
            //if last in playlist
            audioIndex = 0
        } else {
            //get next in playlist
            audioIndex = ++audioIndex
        }

        loadActiveAudio()
        //Update stored index
        StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex)
        stopMedia()
        //reset mediaPlayer
        mediaPlayer!!.reset()
        initMediaPlayer()
    }

    private fun skipToPrevious() {
        if (audioIndex == 0) {
            audioIndex = mCursor!!.getCount() - 1
        } else {
            audioIndex = --audioIndex
        }

        loadActiveAudio()
        //Update stored index
        StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex)
        stopMedia()
        //reset mediaPlayer
        mediaPlayer!!.reset()
        initMediaPlayer()
    }

    // INNER CLASS
    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    companion object {
        const val ACTION_PLAY: String = "leoisasmendi.android.com.suricatepodcast.ACTION_PLAY"
        const val ACTION_PAUSE: String = "leoisasmendi.android.com.suricatepodcast.ACTION_PAUSE"
        const val ACTION_PREVIOUS: String =
            "leoisasmendi.android.com.suricatepodcast.ACTION_PREVIOUS"
        const val ACTION_NEXT: String = "leoisasmendi.android.com.suricatepodcast.ACTION_NEXT"
        const val ACTION_STOP: String = "leoisasmendi.android.com.suricatepodcast.ACTION_STOP"

        const val STATUS_ERROR: String = "leoisasmendi.android.com.suricatepodcast.STATUS_ERROR"
        const val STATUS_DONE: String = "leoisasmendi.android.com.suricatepodcast.STATUS_DONE"
        const val STATUS_FETCHING: String =
            "leoisasmendi.android.com.suricatepodcast.STATUS_FETCHING"

        const val STATUS_PLAYING: String = "leoisasmendi.android.com.suricatepodcast.STATUS_PLAYING"
        const val STATUS_STOPED: String = "leoisasmendi.android.com.suricatepodcast.STATUS_STOPED"
        const val STATUS_PAUSED: String = "leoisasmendi.android.com.suricatepodcast.STATUS_PAUSED"
        const val MEDIA_UPDATED: String = "leoisasmendi.android.com.suricatepodcast.MEDIA_UPDATED"
        const val NOTIFICATION: String = "leoisasmendi.android.com.suricatepodcast"
        const val STATUS: String = "status"


        //AudioPlayer notification ID
        private const val NOTIFICATION_ID = 101
    }
}