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

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import aj.canvas.audiosearch.Audiosearch;
import aj.canvas.audiosearch.model.EpisodeQueryResult;
import aj.canvas.audiosearch.model.EpisodeResult;
import leoisasmendi.android.com.suricatepodcast.BuildConfig;
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
    private AdView mAdView;
    private int currentPage;
    private AudioSearchClient mAudioSearchClient;

    /*local*/
    RecyclerView.LayoutManager mLayoutManager;
    SearchView searchView;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = 1;
        mAudioSearchClient = new AudioSearchClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        searchView = (SearchView) view.findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(getQueryListener());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        checkListenerImplementation(view.getContext());

        mAdapter = new SearchAdapter(getActivity(), new SearchList(), mListener);
        mRecyclerView.setAdapter(mAdapter);

        mAdView = (AdView) view.findViewById(R.id.adBannerView);
        AdRequest adRequest = getAdRequestObject();
        mAdView.loadAd(adRequest);
        return view;
    }

    private SearchView.OnQueryTextListener getQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: ");
                getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.search_list).setVisibility(View.GONE);
                mAudioSearchClient.execute(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    private AdRequest getAdRequestObject() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(getString(R.string.testDeviceAdsId))
                .build();
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
        mAudioSearchClient.cancel(true);
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener {
        void updateSelectedList(SearchItem item);
    }

    private class AudioSearchClient extends AsyncTask<String, Void, EpisodeQueryResult> {
        @Override
        protected EpisodeQueryResult doInBackground(String... strings) {
            try {
                Audiosearch client = new Audiosearch()
                        .setSecret(BuildConfig.CLIENT_SECRET_ID)
                        .setApplicationId(BuildConfig.CLIENT_APPLICATION_ID)
                        .build();

                return client.searchEpisodes(strings[0]).execute().body();

            } catch (Exception e) {
                Log.i(TAG, "doInBackground: Exception" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(EpisodeQueryResult queryResult) {
            super.onPostExecute(queryResult);

            if (queryResult != null) {
                currentPage = 1;
                List<EpisodeResult> episodes = queryResult.getResults();
                SearchList list = new SearchList();

                for (EpisodeResult episode : episodes) {
                    Log.d(TAG, "onPostExecute: episode ->" + episode.getTitle());
                    list.add(new SearchItem(episode.getId(),
                            episode.getTitle(),
                            episode.getAudioFiles().get(0).getDuration(),
                            episode.getAudioFiles().get(0).getMp3(),
                            episode.getImageUrls().getThumb(),
                            episode.getDescription()));
                }

                mAdapter = new SearchAdapter(getActivity(), list, mListener);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Toast.makeText(getActivity().getBaseContext(), getString(R.string.toast_no_query), Toast.LENGTH_SHORT).show();
            }
            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            getView().findViewById(R.id.search_list).setVisibility(View.VISIBLE);
        }


    }
}
