/*
 *
 * The MIT License (MIT)
 * Copyright (c) 2016. Sergio Leonardo Isasmendi
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
 *
 */
package leoisasmendi.android.com.suricatepodcast.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import leoisasmendi.android.com.suricatepodcast.R
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService

class MediaPlayerFragment : Fragment() {
    private var mParcelable: EpisodeParcelable? = null
    private var receiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParcelable = arguments?.getParcelable<EpisodeParcelable?>("EXTRA_MEDIA_INFO")
        }
        initReceiver()
    }

    private fun initReceiver() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val bundle = intent.extras
                if (bundle != null) {
                    val string = bundle.getString(MediaPlayerService.STATUS)
                    Log.d(javaClass.simpleName, "onReceive: $string")
                    when (string) {
                        MediaPlayerService.STATUS_DONE -> stopProgressBar()
                        MediaPlayerService.STATUS_PAUSED -> toggleToPause()
                        MediaPlayerService.STATUS_PLAYING -> toggleToPlaying()
                        MediaPlayerService.STATUS_FETCHING -> startProgressBar()
                        MediaPlayerService.MEDIA_UPDATED -> updateData(bundle)
                    }
                }
            }
        }
    }

    private fun updateData(bundle: Bundle) {
        setTitle(bundle.getString("EXTRA_TITLE"))
        setDuration(bundle.getString("EXTRA_DURATION"))
    }

    private fun toggleToPause() {
        view?.findViewById<View?>(R.id.player_play)?.visibility = View.VISIBLE
        view?.findViewById<View?>(R.id.player_pause)?.visibility = View.GONE
    }

    private fun toggleToPlaying() {
        view?.findViewById<View?>(R.id.player_play)?.visibility = View.GONE
        view?.findViewById<View?>(R.id.player_pause)?.visibility = View.VISIBLE
    }

    private fun stopProgressBar() {
        view?.findViewById<View?>(R.id.loadingAnimation)?.visibility = View.INVISIBLE
    }

    private fun startProgressBar() {
        view?.findViewById<View?>(R.id.loadingAnimation)?.visibility = View.VISIBLE
    }


    private fun getServiceIntent(action: String?): Intent {
        return Intent(activity, MediaPlayerService::class.java)
            .setAction(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_media_player, container, false)
        view.findViewById<View?>(R.id.player_next)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    activity?.startService(getServiceIntent(MediaPlayerService.ACTION_NEXT))
                }
            })

        view.findViewById<View?>(R.id.player_prev)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    activity?.startService(getServiceIntent(MediaPlayerService.ACTION_PREVIOUS))
                }
            })

        view.findViewById<View?>(R.id.player_play)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    activity?.startService(getServiceIntent(MediaPlayerService.ACTION_PLAY))
                }
            })

        view.findViewById<View?>(R.id.player_pause)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    activity?.startService(getServiceIntent(MediaPlayerService.ACTION_PAUSE))
                }
            })
        return view
    }

    override fun onStart() {
        super.onStart()
        loadParcelableIntoView()
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(
            receiver, IntentFilter(
                MediaPlayerService.NOTIFICATION
            )
        )
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver)
    }

    private fun loadParcelableIntoView() {
        if (mParcelable != null) {
            setTitle(mParcelable!!.title)
            setDuration(mParcelable!!.duration)
        }
    }

    private fun setTitle(aString: String?) {
        val textView = view?.findViewById<View?>(R.id.player_title) as TextView
        setText(textView, aString, R.string.default_detail_title)
    }


    private fun setDuration(aString: String?) {
        val textView = view?.findViewById<View?>(R.id.player_length) as TextView
        setText(textView, aString, R.string.default_detail_duration)
    }

    private fun setText(textView: TextView, aString: String?, resourceId: Int) {
        if (aString != null && !aString.isEmpty()) {
            textView.text = aString
        } else {
            textView.setText(resourceId)
        }
    }
}
