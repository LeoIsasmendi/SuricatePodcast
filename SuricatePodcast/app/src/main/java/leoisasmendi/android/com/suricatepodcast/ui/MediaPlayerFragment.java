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

package leoisasmendi.android.com.suricatepodcast.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService;

public class MediaPlayerFragment extends Fragment {

    private EpisodeParcelable mParcelable;
    private BroadcastReceiver receiver;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParcelable = getArguments().getParcelable("EXTRA_MEDIA_INFO");
        }
        initReceiver();
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String string = bundle.getString(MediaPlayerService.STATUS);
                    Log.d(getClass().getSimpleName(), "onReceive: " + string);
                    switch (string) {
                        case MediaPlayerService.STATUS_DONE:
                            stopProgressBar();
                            break;
                        case MediaPlayerService.STATUS_PAUSED:
                            toggleToPause();
                            break;
                        case MediaPlayerService.STATUS_PLAYING:
                            toggleToPlaying();
                            break;
                        case MediaPlayerService.STATUS_FETCHING:
                            startProgressBar();
                            break;
                        case MediaPlayerService.MEDIA_UPDATED:
                            updateData(bundle);
                            break;
                    }
                }
            }
        };
    }

    private void updateData(Bundle bundle) {
        setTitle(bundle.getString("EXTRA_TITLE"));
        setDuration(bundle.getString("EXTRA_DURATION"));
    }

    private void toggleToPause() {
        getView().findViewById(R.id.player_play).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.player_pause).setVisibility(View.GONE);
    }

    private void toggleToPlaying() {
        getView().findViewById(R.id.player_play).setVisibility(View.GONE);
        getView().findViewById(R.id.player_pause).setVisibility(View.VISIBLE);
    }

    private void stopProgressBar() {
        getView().findViewById(R.id.loadingAnimation).setVisibility(View.INVISIBLE);
    }

    private void startProgressBar() {
        getView().findViewById(R.id.loadingAnimation).setVisibility(View.VISIBLE);
    }


    private Intent getServiceIntent(String action) {
        return new Intent(getActivity(), MediaPlayerService.class)
                .setAction(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);
        view.findViewById(R.id.player_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startService(getServiceIntent(MediaPlayerService.ACTION_NEXT));
            }
        });

        view.findViewById(R.id.player_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startService(getServiceIntent(MediaPlayerService.ACTION_PREVIOUS));
            }
        });

        view.findViewById(R.id.player_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startService(getServiceIntent(MediaPlayerService.ACTION_PLAY));
            }
        });

        view.findViewById(R.id.player_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startService(getServiceIntent(MediaPlayerService.ACTION_PAUSE));
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadParcelableIntoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(
                MediaPlayerService.NOTIFICATION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void loadParcelableIntoView() {
        if (mParcelable != null) {
            setTitle(mParcelable.getTitle());
            setDuration(mParcelable.getDuration());
        }
    }

    private void setTitle(String aString) {
        TextView textView = (TextView) getView().findViewById(R.id.player_title);
        setText(textView, aString, R.string.default_detail_title);
    }


    private void setDuration(String aString) {
        TextView textView = (TextView) getView().findViewById(R.id.player_length);
        setText(textView, aString, R.string.default_detail_duration);
    }

    private void setText(TextView textView, String aString, int resource_id) {
        if (aString != null && !aString.isEmpty()) {
            textView.setText(aString);
        } else {
            textView.setText(resource_id);
        }
    }

}
