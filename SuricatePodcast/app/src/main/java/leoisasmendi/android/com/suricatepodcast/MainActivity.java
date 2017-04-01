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

package leoisasmendi.android.com.suricatepodcast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import leoisasmendi.android.com.suricatepodcast.data.ItemLoader;
import leoisasmendi.android.com.suricatepodcast.data.ItemsContract;
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
import leoisasmendi.android.com.suricatepodcast.utils.ParserUtils;
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener {

    final String TAG = getClass().getSimpleName();

    private static final String TAG_MAIN = MainFragment.class.getSimpleName();
    private static final String TAG_DETAIL = DetailFragment.class.getSimpleName();
    private static final String TAG_SEARCH = SearchFragment.class.getSimpleName();
    private static final String TAG_ABOUT = AboutFragment.class.getSimpleName();
    public static final String Broadcast_PLAY_NEW_AUDIO = "leoisasmendi.android.com.suricatepodcast.PlayNewAudio";


    private Toolbar mToolbar;
    private FragmentManager fragmentManager;

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
        initToolbar();
        initAds();
        initFragments();
        initServiceConnection();
        // Google Analytics
        initTracker();
    }

    private void initServiceConnection() {
        //Binding this Client to the AudioPlayer Service
        serviceConnection = new ServiceConnection() {
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

    private void initAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(BuildConfig.INTERSTITIAL_FULL_SCREEN);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initFragments() {
        fragmentManager = getFragmentManager();
        Log.d(TAG, "onCreate: twoPaneMode " + getResources().getBoolean(R.bool.twoPaneMode));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getResources().getBoolean(R.bool.twoPaneMode)) {

            fragmentTransaction
                    .replace(R.id.master_container, new MainFragment(), TAG_MAIN)
                    .replace(R.id.detail_container, new DetailFragment(), TAG_DETAIL);

        } else { //Single panel view
            fragmentTransaction
                    .replace(R.id.master_container, new MainFragment(), TAG_MAIN);
        }

        fragmentTransaction.commit();
    }

    private void initTracker() {
        ((MyApplication) getApplication()).startTracking();
    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice(BuildConfig.TEST_DEVICE_ADS_ID)
                .build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
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

    private void showAbout() {
        showAds();
        fragmentManager.beginTransaction()
                .replace(R.id.master_container, new AboutFragment())
                .addToBackStack(TAG_ABOUT)
                .commit();
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
            // TODO: a better solution, this is a temporal fix
            try {
                unbindService(serviceConnection);
                //service is active
                player.stopSelf();
            } catch (Exception e) {
                Log.d(TAG, "onDestroy: ", e);
            }

        }
    }

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
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
        showDetailFragment(ParserUtils.buildParcelable(item));
    }


    @Override
    public void updateSelectedList(SearchItem item) {

        if (item.getSelected()) {
            if (!selectedItems.contains(item)) {
                Log.d(TAG, "updateSelectedList: ADDED");
                selectedItems.add(item);
            }
        } else {
            Log.d(TAG, "updateSelectedList: REMOVED");
            selectedItems.remove(item);
        }

    }

    // SearchFragment implements
    @Override
    public void searchPodcast() {
        Log.d(TAG, "searchPodcast: ");
        selectedItems = new SearchList();
        showSearchFragment();
    }

    @Override
    public void addSelectedItemsToPlaylist() {
        Log.d(TAG, "addSelectedItemsToPlaylist: ");

        if (selectedItems.size() > 0) {
            for (PlaylistItem item : selectedItems) {
                Cursor c = getContentResolver().query(DataProvider.CONTENT_URI,
                        null,
                        ItemsContract.Items.ID_PODCAST + " = " + item.getId(),
                        null,
                        null);
                if (c.getCount() == 0) {
                    // not found in database
                    getContentResolver().insert(DataProvider.CONTENT_URI, ParserUtils.buildContentValue(item));
                }
                c.close();
                Toast.makeText(this, R.string.items_added, Toast.LENGTH_SHORT).show();
            }
        }

    }

}