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

package leoisasmendi.android.com.suricatepodcast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ShareActionProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import leoisasmendi.android.com.suricatepodcast.data.ItemLoader;
import leoisasmendi.android.com.suricatepodcast.data.ItemsContract;
import leoisasmendi.android.com.suricatepodcast.data.Playlist;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistAdapter;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem;
import leoisasmendi.android.com.suricatepodcast.data.SearchItem;
import leoisasmendi.android.com.suricatepodcast.data.SearchList;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService;
import leoisasmendi.android.com.suricatepodcast.ui.AboutFragment;
import leoisasmendi.android.com.suricatepodcast.ui.DetailFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;
import leoisasmendi.android.com.suricatepodcast.ui.ThemesFragment;
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor>, PlaylistAdapter.OnItemClickListener {

    final String TAG = getClass().getSimpleName();

    private static final String TAG_MAIN = MainFragment.class.getSimpleName();
    private static final String TAG_DETAIL = DetailFragment.class.getSimpleName();
    private static final String TAG_SEARCH = SearchFragment.class.getSimpleName();
    private static final String TAG_ABOUT = AboutFragment.class.getSimpleName();
    private static final String TAG_THEMES = ThemesFragment.class.getSimpleName();
    public static final String Broadcast_PLAY_NEW_AUDIO = "leoisasmendi.android.com.suricatepodcast.PlayNewAudio";


    private FragmentManager fragmentManager;

    private RecyclerView mRecyclerView;
    private PlaylistAdapter mAdapter;

    //List of available Audio files
    private Playlist playlist;

    //List of selected items on SearchView
    private SearchList selectedItems;

    // MEDIA PLAYER
    private MediaPlayerService player;
    boolean serviceBound = false;
    private ServiceConnection serviceConnection;

    // ADS MOB
    InterstitialAd mInterstitialAd;

    public static final String ACTION_DATA_UPDATED = "leoisasmendi.android.com.suricatepodcast.app.ACTION_DATA_UPDATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        fragmentManager = getFragmentManager();
        loadFragment(savedInstanceState);

        //Binding this Client to the AudioPlayer Service
        serviceConnection = getServiceConnection();


        // Iniciar loader
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
                player = binder.getService();
                serviceBound = true;

                Log.d(TAG, "onServiceConnected: Service bound");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceBound = false;
            }
        };
    }

    private void loadAds() {
        AdRequest adRequest = getAdRequestObject();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
    }

    private AdRequest getAdRequestObject() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(getString(R.string.testDeviceAdsId))
                .build();
    }

    private void showAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadAds();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOp1:
                showThemes();
                return true;
            case R.id.menuOp2:
                showAbout();
                return true;
            case R.id.menu_item_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = getString(R.string.share_body_text);
                // TODO: INSERT THE CORRECT URL
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "www.audiosear.ch/audio.mp3");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showThemes() {
        showAds();
        fragmentManager.beginTransaction()
                .replace(R.id.master_container, new ThemesFragment())
                .addToBackStack(TAG_THEMES)
                .commit();
    }

    private void showAbout() {
        showAds();
        fragmentManager.beginTransaction()
                .replace(R.id.master_container, new AboutFragment())
                .addToBackStack(TAG_ABOUT)
                .commit();
    }

    private void loadFragment(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: twoPaneMode " + getResources().getBoolean(R.bool.twoPaneMode));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getResources().getBoolean(R.bool.twoPaneMode)) {

            fragmentTransaction
                    .add(R.id.master_container, new MainFragment(), TAG_MAIN)
                    .add(R.id.detail_container, new DetailFragment(), TAG_DETAIL);

        } else { //Single panel view
            fragmentTransaction
                    .add(R.id.master_container, new MainFragment(), TAG_MAIN);
        }

        fragmentTransaction.commit();
    }

    private void showSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            fragmentTransaction.replace(R.id.detail_container, searchFragment, TAG_SEARCH);
        } else {
            fragmentTransaction.replace(R.id.master_container, searchFragment);
        }

        fragmentTransaction.addToBackStack(TAG_SEARCH).commit();
    }

    private void showDetailFragment(EpisodeParcelable parcelable) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("EXTRA_EPISODE", parcelable);
        detailFragment.setArguments(mBundle);

        fragmentManager.beginTransaction()
                .replace(R.id.master_container, detailFragment)
                .addToBackStack(TAG_DETAIL)
                .commit();
    }

    // SET ACTION BAR TITLE
    public void setActionBarTitle(int resourceId) {
        getSupportActionBar().setTitle(resourceId);
    }


    //    SEARCH BUTTON
    public void doSearch(View v) {
        Log.d(TAG, "doSearch: ");
        selectedItems = new SearchList();
        showSearchFragment();
    }

    //    SEARCH BUTTON
    public void addSelectedItemsToPlaylist(View v) {
        Log.d(TAG, "addSelectedItemsToPlaylist: ");

        if (selectedItems.size() > 0) {

            ContentValues aValue;
            for (PlaylistItem item : selectedItems) {

                Cursor c = getContentResolver().query(DataProvider.CONTENT_URI,
                        null,
                        ItemsContract.Items.ID_PODCAST + " = " + item.getId(),
                        null,
                        null);
                if (c.getCount() == 0) {
                    // not found in database
                    aValue = new ContentValues();
                    aValue.put(ItemsContract.Items.ID_PODCAST, item.getId());
                    aValue.put(ItemsContract.Items.TITLE, item.getTitle());
                    aValue.put(ItemsContract.Items.DURATION, item.getDuration());
                    aValue.put(ItemsContract.Items.AUDIO, item.getAudio());
                    aValue.put(ItemsContract.Items.POSTER, item.getPoster());
                    getContentResolver().insert(DataProvider.CONTENT_URI, aValue);
                }
                c.close();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.main_playlist);
//            mRecyclerView.setHasFixedSize(true);
            mAdapter = new PlaylistAdapter(this, this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(playlist);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }


    // INTERFACES

    @Override
    public void onClick(int position) {
        Log.d(TAG, "onClickFragmentInteraction: playlist item pressed " + position);
        this.playAudio(position);
    }

    @Override
    public void onDeleteItem(int itemId) {
        Log.d(TAG, "onDeleteItem: " + itemId);
        Cursor c = getContentResolver().query(DataProvider.CONTENT_URI,
                null,
                ItemsContract.Items.ID_PODCAST + "=" + itemId,
                null,
                null);
        if (c.getCount() != 0) {
            String where = ItemsContract.Items.ID_PODCAST + "=?";
            String[] args = new String[]{Integer.toString(itemId)};
            getContentResolver().delete(DataProvider.CONTENT_ITEM, where, args);
        }
        c.close();
    }

    @Override
    public void onShowDetail(Cursor item) {
        Log.d(TAG, "onShowDetail: " + item.getString(ItemLoader.Query.TITLE));
        EpisodeParcelable parcelable = new EpisodeParcelable();
        parcelable.setId(item.getInt(ItemLoader.Query.ID_PODCAST));
        parcelable.setTitle(item.getString(ItemLoader.Query.TITLE));
        parcelable.setDuration(item.getString(ItemLoader.Query.DURATION));
        parcelable.setPoster(item.getString(ItemLoader.Query.POSTER));
        showDetailFragment(parcelable);
    }


    @Override
    public void updateSelectedList(SearchItem item) {
        Log.d(TAG, "updateSelectedList: " + item.getTitle());
        if (item.getSelected()) {
            if (!selectedItems.contains(item)) {
                Log.d(TAG, "updateSelectedList: ADDED");
                selectedItems.add(item);
            }
        } else {
            Log.d(TAG, "updateSelectedList: REMOVED");
            selectedItems.remove(item);
        }


        Log.d(TAG, "updateSelectedList: LIST " + selectedItems.size());

    }

    // Cursor loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(this, DataProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}