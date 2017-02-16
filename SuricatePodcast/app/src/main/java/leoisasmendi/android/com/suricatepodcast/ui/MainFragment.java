/*
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
 */

package leoisasmendi.android.com.suricatepodcast.ui;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import leoisasmendi.android.com.suricatepodcast.MainActivity;
import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistAdapter;
import leoisasmendi.android.com.suricatepodcast.data.ListItem;


public class MainFragment extends Fragment {

    private OnMainListInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_playlist);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        checkListenerImplementation(view.getContext());
        loadFakeData();
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void loadFakeData() {
        List<ListItem> playlist;

        playlist = new ArrayList<>();

        playlist.add(new ListItem(1,
                "Joe Rogan",
                "00:25:00",
                "http://static.libsyn.com/p/assets/0/4/1/e/041e18fe8e7b1c67/rogan.jpg",
                "https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3"));

        playlist.add(new ListItem(2,
                "Joe Rogan",
                "00:25:00",
                "http://static.libsyn.com/p/assets/2/3/6/c/236cb6c10b89befa/Keep-Hammering.jpg",
                "https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3"));

        playlist.add(new ListItem(3,
                "Joe Rogan",
                "00:25:00",
                "http://is4.mzstatic.com/image/thumb/Music62/v4/8e/0a/70/8e0a7014-9ccc-b532-5eb7-2b803d1a571a/source/600x600bb.jpg",
                "https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3"));

        Log.i("MainFragment", "onCreateView: " + mListener.toString());
        mAdapter = new PlaylistAdapter(getActivity(), playlist, mListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        checkListenerImplementation(context);
    }

    private void checkListenerImplementation(Context context) {
        if (context instanceof OnMainListInteractionListener) {
            mListener = (OnMainListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setActionBarTitle(R.string.main_fragment_title);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnMainListInteractionListener {
        // TODO: Update argument type and name
        void onClickFragmentInteraction(ListItem item);

        void onLongClickFragmentInteraction(ListItem item);
    }
}
