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
package leoisasmendi.android.com.suricatepodcast.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import leoisasmendi.android.com.suricatepodcast.R
import leoisasmendi.android.com.suricatepodcast.data.SearchAdapter
import leoisasmendi.android.com.suricatepodcast.data.SearchList

class SearchFragment : Fragment() {
    private val TAG: String = javaClass.simpleName

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null

    //private AdView mAdView;
    //private var mAudioSearchClient: AudioSearchClient? = null

    /*local*/
    var mLayoutManager: RecyclerView.LayoutManager? = null
    var searchView: SearchView? = null

    //List of selected items on SearchView
    var selectedItems: SearchList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.findViewById<View?>(R.id.contextual_fab) as FloatingActionButton).setImageResource(
            R.drawable.plus
        )
        selectedItems = SearchList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.search_fragment, container, false)
        initSearchView(view)
        initListView(view)
        //initAds(view);
        setupFAB()
        return view
    }

    /*
    private void initAds(View view) {
        mAdView = (AdView) view.findViewById(R.id.adBannerView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(BuildConfig.TEST_DEVICE_ADS_ID)
                .build();
        mAdView.loadAd(adRequest);
    }


     */
    private fun initListView(view: View) {
        mRecyclerView = view.findViewById<View?>(R.id.search_list) as RecyclerView
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView?.setLayoutManager(mLayoutManager)
        mAdapter = SearchAdapter(activity, null, selectedItems)
        mRecyclerView?.setAdapter(mAdapter)
    }

    private fun initSearchView(view: View) {
        searchView = view.findViewById<View?>(R.id.search_input) as SearchView
        searchView!!.setOnQueryTextListener(this.queryListener)
    }

    private fun setupFAB() {
        val fab: FloatingActionButton =
            activity?.findViewById<View?>(R.id.contextual_fab) as FloatingActionButton
        fab.setImageResource(R.drawable.plus)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                addSelectedItemsToPlaylist()
            }
        })
        fab.contentDescription = getString(R.string.cd_add_to_main_list_fab)
    }

    private val queryListener: SearchView.OnQueryTextListener
        get() = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: ")
                view?.findViewById<View?>(R.id.loadingPanel)?.visibility = View.VISIBLE
                view?.findViewById<View?>(R.id.search_list)?.visibility = View.GONE
                //mAudioSearchClient = AudioSearchClient()
                //mAudioSearchClient.execute(query)
                searchView!!.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }

    override fun onPause() {
        //if (mAdView != null) {
        //     mAdView.pause();
        // }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        //if (mAdView != null) {
        //    mAdView.resume();
        //}
        selectedItems = SearchList()
    }

    override fun onDestroy() {
        super.onDestroy()
        //if (mAdView != null) {
        //     mAdView.destroy();
        //}
        //if (mAudioSearchClient != null) {
        //mAudioSearchClient.cancel(true)
        //}
    }

    fun addSelectedItemsToPlaylist() {
        Log.d(TAG, "addSelectedItemsToPlaylist: ")
        //val resolver: ContentResolver = activity?.getContentResolver()
        /*
                if (selectedItems.size > 0) {
                    val length = selectedItems?.size
                    for (i in 0 until length) {
                        val c: Cursor? = resolver.query(
                            DataProvider.CONTENT_URI,
                            null,
                            PodcastContract.PodcastEntry.COLUMN_ID + " = " + selectedItems?.get(i)?.id,
                            null,
                            null
                        )
                        if (c!!.getCount() == 0) {
                            // not found in database
                            /*
                            resolver.insert(
                                DataProvider.CONTENT_URI,
                                ParserUtils.buildContentValue(selectedItems.get(i))
                            )

                             */
                        }
                        c.close()
                        Toast.makeText(getActivity(), R.string.items_added, Toast.LENGTH_SHORT).show()
                    }
                }

         */
    }


    /*
private inner class AudioSearchClient : AsyncTask<String?, Void?, EpisodeQueryResult?>() {
override fun doInBackground(vararg strings: String?): EpisodeQueryResult? {
    try {
        Audiosearch client = new Audiosearch()
                .setSecret(BuildConfig.CLIENT_SECRET_ID)
                .setApplicationId(BuildConfig.CLIENT_APPLICATION_ID)
                .build();

        return client.searchEpisodes(strings[0]).execute().body();

    } catch (Exception e) {
        Log.i(TAG, "doInBackground: Exception" + e.toString());
    }

    return null
}
     */

    /*
    override fun onPostExecute(result: EpisodeQueryResult) {
        super.onPostExecute(result)

        if (queryResult != null) {
            val episodes: MutableList<EpisodeResult> = queryResult.getResults()
            val list: SearchList = SearchList()

            val length = episodes.size
            for (i in 0 until length) {
                val episode: EpisodeResult = episodes.get(i)
                Log.d(TAG, "onPostExecute: episode ->" + episode.title)
                list.add(
                    PlaylistItem.Builder(episode.id)
                        .setTitle(episode.getTitle())
                        .setShowTitle(episode.getShowTitle())
                        .setDuration(ParserUtils.buildTime(episode.getDuration()))
                        .setAudio(ParserUtils.getMp3(episode.getAudioFiles()))
                        .setPoster(episode.getImageUrls().getThumb())
                        .setDescription(episode.getDescription())
                        .build()
                )
            }
            mAdapter = SearchAdapter(getActivity(), list, selectedItems)
            mRecyclerView.setAdapter(mAdapter)
        } else {
            Toast.makeText(
                getActivity().getBaseContext(),
                getString(R.string.toast_no_query),
                Toast.LENGTH_SHORT
            ).show()
        }
        getView()!!.findViewById<View?>(R.id.loadingPanel).setVisibility(View.GONE)
        getView()!!.findViewById<View?>(R.id.search_list).setVisibility(View.VISIBLE)
    }

}
     */
}
