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
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem;
import leoisasmendi.android.com.suricatepodcast.data.PodcastContract;
import leoisasmendi.android.com.suricatepodcast.data.SearchAdapter;
import leoisasmendi.android.com.suricatepodcast.data.SearchList;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.utils.ParserUtils;

public class SearchFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private AdView mAdView;
    private AudioSearchClient mAudioSearchClient;

    /*local*/
    RecyclerView.LayoutManager mLayoutManager;
    SearchView searchView;

    //List of selected items on SearchView
    SearchList selectedItems;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FloatingActionButton) getActivity().findViewById(R.id.contextual_fab)).setImageResource(R.drawable.plus);
        selectedItems = new SearchList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        initSearchView(view);
        initListView(view);
        initAds(view);
        setupFAB();
        return view;
    }

    private void initAds(View view) {
        mAdView = (AdView) view.findViewById(R.id.adBannerView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(BuildConfig.TEST_DEVICE_ADS_ID)
                .build();
        mAdView.loadAd(adRequest);
    }

    private void initListView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchAdapter(getActivity(), null, selectedItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSearchView(View view) {
        searchView = (SearchView) view.findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(getQueryListener());
    }

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.contextual_fab);
        fab.setImageResource(R.drawable.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSelectedItemsToPlaylist();
            }
        });
        fab.setContentDescription(getString(R.string.cd_add_to_main_list_fab));
    }

    private SearchView.OnQueryTextListener getQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: ");
                getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.search_list).setVisibility(View.GONE);
                mAudioSearchClient = new AudioSearchClient();
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
        selectedItems = new SearchList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (mAudioSearchClient != null) {
            mAudioSearchClient.cancel(true);
        }
    }

    public void addSelectedItemsToPlaylist() {
        Log.d(TAG, "addSelectedItemsToPlaylist: ");
        ContentResolver resolver = getActivity().getContentResolver();

        if (selectedItems.size() > 0) {
            int length = selectedItems.size();
            for (int i = 0; i < length; i++) {
                Cursor c = resolver.query(DataProvider.CONTENT_URI,
                        null,
                        PodcastContract.PodcastEntry.COLUMN_ID + " = " + selectedItems.get(i).getId(),
                        null,
                        null);
                if (c.getCount() == 0) {
                    // not found in database
                    resolver.insert(DataProvider.CONTENT_URI, ParserUtils.buildContentValue(selectedItems.get(i)));
                }
                c.close();
                Toast.makeText(getActivity(), R.string.items_added, Toast.LENGTH_SHORT).show();
            }
        }

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
                List<EpisodeResult> episodes = queryResult.getResults();
                SearchList list = new SearchList();

                int length = episodes.size();
                for (int i = 0; i < length; i++) {
                    EpisodeResult episode = episodes.get(i);
                    Log.d(TAG, "onPostExecute: episode ->" + episode.getTitle());
                    list.add(new PlaylistItem.Builder(episode.getId())
                            .setTitle(episode.getTitle())
                            .setShowTitle(episode.getShowTitle())
                            .setDuration(ParserUtils.buildTime(episode.getDuration()))
                            .setAudio(ParserUtils.getMp3(episode.getAudioFiles()))
                            .setPoster(episode.getImageUrls().getThumb())
                            .setDescription(episode.getDescription())
                            .build()
                    );

                }
                mAdapter = new SearchAdapter(getActivity(), list, selectedItems);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Toast.makeText(getActivity().getBaseContext(), getString(R.string.toast_no_query), Toast.LENGTH_SHORT).show();
            }
            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            getView().findViewById(R.id.search_list).setVisibility(View.VISIBLE);
        }

    }
}
