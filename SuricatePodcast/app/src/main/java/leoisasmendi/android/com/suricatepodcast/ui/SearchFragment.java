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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import leoisasmendi.android.com.suricatepodcast.MainActivity;
import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.data.SearchAdapter;
import leoisasmendi.android.com.suricatepodcast.data.SearchItem;
import leoisasmendi.android.com.suricatepodcast.data.SearchList;

public class SearchFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;
    private AdView mAdView;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        searchView = (SearchView) view.findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: ");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        checkListenerImplementation(view.getContext());
        loadFakeData();
        mRecyclerView.setAdapter(mAdapter);

        mAdView = (AdView) view.findViewById(R.id.adBannerView);
        AdRequest adRequest = getAdRequestObject();
        mAdView.loadAd(adRequest);
        return view;
    }

    private AdRequest getAdRequestObject() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(getString(R.string.testDeviceAdsId))
                .build();
    }

    private void loadFakeData() {
        SearchList playlist;

        playlist = new SearchList();
        playlist.add(new SearchItem(1,
                "Joe Rogan",
                "00:25:00",
                "http://static.libsyn.com/p/assets/2/3/6/c/236cb6c10b89befa/Keep-Hammering.jpg",
                "https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3"));

        playlist.add(new SearchItem(2,
                "Joe Rogan",
                "00:25:00",
                "http://is4.mzstatic.com/image/thumb/Music62/v4/8e/0a/70/8e0a7014-9ccc-b532-5eb7-2b803d1a571a/source/600x600bb.jpg",
                "https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3"));

        mAdapter = new SearchAdapter(getActivity(), playlist, mListener);
        Log.i("MainFragment", "onCreateView: " + mAdapter.getItemCount());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        checkListenerImplementation(context);
    }

    private void checkListenerImplementation(Context context) {
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        ((MainActivity) getActivity()).setActionBarTitle(R.string.search_fragment_title);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener {
        void updateSelectedList(SearchItem item);
    }
}
