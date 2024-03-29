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

package leoisasmendi.android.com.suricatepodcast.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import leoisasmendi.android.com.suricatepodcast.MainActivity;
import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.utils.ParserUtils;
import leoisasmendi.android.com.suricatepodcast.utils.PlaybackStatus;
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil;
import leoisasmendi.android.com.suricatepodcast.widget.PodcastWidgetProvider;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    private final String TAG = getClass().getSimpleName();

    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    //Used to pause/resume MediaPlayer
    private int resumePosition;
    private AudioManager audioManager;

    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;


    //List of available Audio files
    private int audioIndex = -1;
    private PlaylistItem activeAudio; //an object of the currently playing audio
    private Cursor mCursor;

    private BroadcastReceiver playNewAudio = getNewAudioBroadcastReceiver();
    //Becoming noisy (headphone removed)
    private BroadcastReceiver becomingNoisyReceiver = getBecomingNoisyReceiver();

    public static final String ACTION_PLAY = "leoisasmendi.android.com.suricatepodcast.ACTION_PLAY";
    public static final String ACTION_PAUSE = "leoisasmendi.android.com.suricatepodcast.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "leoisasmendi.android.com.suricatepodcast.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "leoisasmendi.android.com.suricatepodcast.ACTION_NEXT";
    public static final String ACTION_STOP = "leoisasmendi.android.com.suricatepodcast.ACTION_STOP";

    public static final String STATUS_ERROR = "leoisasmendi.android.com.suricatepodcast.STATUS_ERROR";
    public static final String STATUS_DONE = "leoisasmendi.android.com.suricatepodcast.STATUS_DONE";
    public static final String STATUS_FETCHING = "leoisasmendi.android.com.suricatepodcast.STATUS_FETCHING";

    public static final String STATUS_PLAYING = "leoisasmendi.android.com.suricatepodcast.STATUS_PLAYING";
    public static final String STATUS_STOPED = "leoisasmendi.android.com.suricatepodcast.STATUS_STOPED";
    public static final String STATUS_PAUSED = "leoisasmendi.android.com.suricatepodcast.STATUS_PAUSED";
    public static final String MEDIA_UPDATED = "leoisasmendi.android.com.suricatepodcast.MEDIA_UPDATED";
    public static final String NOTIFICATION = "leoisasmendi.android.com.suricatepodcast";
    public static final String STATUS = "status";


    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;


    public MediaPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Perform one-time setup procedures

        mCursor = getContentResolver().query(DataProvider.CONTENT_URI,
                null,
                "",
                null,
                null);
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver();
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio();
    }


    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            publishStatus(STATUS_FETCHING);
            // Set the data source to the mediaFile location
            mediaPlayer.setDataSource(activeAudio.getAudio());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            publishStatus(STATUS_ERROR);
            stopSelf();
            Log.d(TAG, "initMediaPlayer: " + R.string.media_player_error_1);
        }
    }

    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
                publishStatus(STATUS_PLAYING);
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                publishStatus(STATUS_PAUSED);
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                publishStatus(STATUS_STOPED);
                removeNotification();
                //Stop the service
                stopSelf();
            }

        });
    }

    private void publishStatus(String status) {
        Intent intent = new Intent(NOTIFICATION);
        if (status.equals(MEDIA_UPDATED)) {
            intent.putExtra("EXTRA_TITLE", activeAudio.getTitle());
            intent.putExtra("EXTRA_DURATION", activeAudio.getDuration());
        }
        intent.putExtra(STATUS, status);
        sendBroadcast(intent);
    }

    private void updateMetaData() {

        if (activeAudio != null) {
            Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_default_poster); //replace with medias albumArt
            // Update the current metadata
            mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, activeAudio.getTitle())
                    .build());
        }

    }

    private void buildNotification(PlaybackStatus playbackStatus) {

        //TODO: REFACTOR updateWidget method
        updateWidgets(playbackStatus);

        int notificationAction = R.drawable.ic_media_control_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = R.drawable.ic_media_control_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = R.drawable.ic_media_control_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        //TODO: replace this with my icon
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_default_poster); //replace with your own image

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setShowWhen(false)
                // Set the Notification style
                //.setStyle(new NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                  //      .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                    //    .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark))
                // Set the large and small icons
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_headphones)
                // Set Notification content information
                .setContentText(activeAudio.getTitle())
                // Add playback actions
                .addAction(R.drawable.ic_media_control_prev, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(R.drawable.ic_media_control_next, "next", playbackAction(2));

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void updateWidgets(PlaybackStatus playbackStatus) {

        final RemoteViews view = new RemoteViews(getPackageName(), R.layout.podcast_widget_player);

        if (playbackStatus == PlaybackStatus.PLAYING) {
            view.setImageViewResource(R.id.widget_play, R.drawable.media_player_pause_24x24);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            view.setImageViewResource(R.id.widget_play, R.drawable.media_player_play_24x24);
        }

        view.setTextViewText(R.id.widget_title, activeAudio.getTitle());
        view.setTextViewText(R.id.widget_length, activeAudio.getDuration());

        Picasso.get().setLoggingEnabled(true);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                view.setImageViewBitmap(R.id.widget_thumbail, bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                view.setImageViewResource(R.id.widget_thumbail, R.drawable.picture);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                view.setImageViewResource(R.id.widget_thumbail, R.drawable.picture);
            }
        };

        Picasso.get()
                .load(activeAudio.getPoster())
                .into(target);


        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, PodcastWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_IMMUTABLE);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_IMMUTABLE);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_IMMUTABLE);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, PendingIntent.FLAG_IMMUTABLE);
            default:
                break;
        }
        return null;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    private void loadActiveAudio() {

        if (audioIndex != -1 && audioIndex < mCursor.getCount()) {
            //index is in a valid range
            mCursor.moveToPosition(audioIndex);
            activeAudio = ParserUtils.buildPlaylistItem(mCursor);
        } else {
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }

    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //Load data from SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            audioIndex = storage.loadAudioIndex();
            Log.d(TAG, "onStartCommand: " + R.string.media_player_connecting);
            loadActiveAudio();

        } catch (NullPointerException e) {
            Log.d(TAG, "onStartCommand: " + R.string.media_player_error_1);
            stopSelf();
        }

        //Request audio focus
        if (!requestAudioFocus()) {
            //Could not gain focus
            stopSelf();
        }

        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        removeNotification();

        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);

        //clear cached playlist
        new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();
        mCursor.close();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //Invoked when playback of a media source has completed.
        stopMedia();
        //stop the service
        stopSelf();
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        Log.d(TAG, "onError: " + R.string.media_player_error_2);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        Log.d(TAG, "onPrepared: " + R.string.media_player_successful_fetch);
        publishStatus(STATUS_DONE);
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initMediaPlayer();
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    private BroadcastReceiver getNewAudioBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Get the new media index form SharedPreferences
                audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
                loadActiveAudio();

                //A PLAY_NEW_AUDIO action received
                //reset mediaPlayer to play the new Audio
                stopMedia();
                mediaPlayer.reset();
                initMediaPlayer();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }
        };
    }

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(MainActivity.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    //Handle incoming phone calls
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }


    //Becoming noisy (headphone removed)
    private BroadcastReceiver getBecomingNoisyReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //pause audio on ACTION_AUDIO_BECOMING_NOISY
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
            }
        };
    }


    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }


    //MEDIA PLAYER BASIC CONTROLS

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            Log.d(TAG, "playMedia: " + R.string.media_player_playing);
            mediaPlayer.start();
            publishStatus(MEDIA_UPDATED);
            publishStatus(STATUS_PLAYING);
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            publishStatus(STATUS_STOPED);
        }
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            publishStatus(STATUS_PAUSED);
        }
    }

    private void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
            publishStatus(STATUS_PLAYING);
        }
    }

    private void skipToNext() {

        if (audioIndex == mCursor.getCount() - 1) {
            //if last in playlist
            audioIndex = 0;
        } else {
            //get next in playlist
            audioIndex = ++audioIndex;
        }

        loadActiveAudio();
        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        stopMedia();
        //reset mediaPlayer
        mediaPlayer.reset();
        initMediaPlayer();
    }

    private void skipToPrevious() {

        if (audioIndex == 0) {
            audioIndex = mCursor.getCount() - 1;
        } else {
            audioIndex = --audioIndex;
        }

        loadActiveAudio();
        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        stopMedia();
        //reset mediaPlayer
        mediaPlayer.reset();
        initMediaPlayer();
    }

    // INNER CLASS
    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }
}