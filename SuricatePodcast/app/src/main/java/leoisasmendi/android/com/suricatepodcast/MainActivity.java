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
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import leoisasmendi.android.com.suricatepodcast.data.Playlist;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem;
import leoisasmendi.android.com.suricatepodcast.data.SearchItem;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService;
import leoisasmendi.android.com.suricatepodcast.ui.AboutFragment;
import leoisasmendi.android.com.suricatepodcast.ui.DetailFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;
import leoisasmendi.android.com.suricatepodcast.ui.ThemesFragment;
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainListInteractionListener, SearchFragment.OnFragmentInteractionListener {

    final String TAG = getClass().getSimpleName();

    private static final String TAG_MAIN = MainFragment.class.getSimpleName();
    private static final String TAG_DETAIL = DetailFragment.class.getSimpleName();
    private static final String TAG_SEARCH = SearchFragment.class.getSimpleName();
    private static final String TAG_ABOUT = AboutFragment.class.getSimpleName();
    private static final String TAG_THEMES = ThemesFragment.class.getSimpleName();
    public static final String Broadcast_PLAY_NEW_AUDIO = "leoisasmendi.android.com.suricatepodcast.PlayNewAudio";

    private FragmentManager fragmentManager;

    private boolean mTwoPane;

    //List of available Audio files
    private Playlist playlist;

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
    }

    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
                player = binder.getService();
                serviceBound = true;

                Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
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
            case R.id.action_search:
                showSearchFragment();
                return true;
            case R.id.menuOp1:
                showThemes();
                return true;
            case R.id.menuOp2:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showThemes() {
        showAds();
        ThemesFragment themes = (ThemesFragment) fragmentManager.findFragmentByTag(TAG_THEMES);

        if (themes == null) {
            themes = new ThemesFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, themes);
        fragmentTransaction.addToBackStack(TAG_THEMES);
        fragmentTransaction.commit();
    }

    private void showAbout() {
        showAds();
        AboutFragment about = (AboutFragment) fragmentManager.findFragmentByTag(TAG_ABOUT);

        if (about == null) {
            about = new AboutFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, about);
        fragmentTransaction.addToBackStack(TAG_ABOUT);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.podcast_second_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.podcast_second_container, new DetailFragment(), TAG_DETAIL)
                        .commit();
            }
        } else {
            mTwoPane = false;
            loadPlaylistData();
            loadFakeData();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            MainFragment mainFragment = new MainFragment();
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("EXTRA_LIST", playlist);
            mainFragment.setArguments(mBundle);

            fragmentTransaction.replace(R.id.activity_main, mainFragment, TAG_MAIN);
            fragmentTransaction.commit();
        }
    }

    private void loadFakeData() {
        playlist = new Playlist();

        playlist.add(new PlaylistItem(1,
                "Joe Rogan",
                "00:25:00",
                "http://static.libsyn.com/p/assets/0/4/1/e/041e18fe8e7b1c67/rogan.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg"));

        playlist.add(new PlaylistItem(2,
                "Joe Rogan",
                "00:25:00",
                "http://static.libsyn.com/p/assets/2/3/6/c/236cb6c10b89befa/Keep-Hammering.jpg",
                "https://www.audiosear.ch/media/80de28fbeb78605e66fa8df7d223b584/0/public/audio_file/154656/113974051-startalk-the-joe-rogan-experience.mp3"));

        playlist.add(new PlaylistItem(3,
                "Joe Rogan",
                "00:25:00",
                "http://is4.mzstatic.com/image/thumb/Music62/v4/8e/0a/70/8e0a7014-9ccc-b532-5eb7-2b803d1a571a/source/600x600bb.jpg",
                "https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3"));
    }

    private void loadPlaylistData() {
        //LOAD THIS ASYNC WAY
        String URL = "content://suricatepodcast";

        Uri data = Uri.parse(URL);
        Cursor c = getApplicationContext().getContentResolver().query(data, null, null, null, "name");

//        if (c.moveToFirst()) {
//            do {
//                Toast.makeText(this,
//                        c.getString(c.getColumnIndex(StudentsProvider._ID)) +
//                                ", " + c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
//                                ", " + c.getString(c.getColumnIndex(StudentsProvider.GRADE)),
//                        Toast.LENGTH_SHORT).show();
//            } while (c.moveToNext());
//        }
    }

    private void showSearchFragment() {
        SearchFragment searchFragment = (SearchFragment) fragmentManager.findFragmentByTag(TAG_SEARCH);

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mTwoPane) {
            fragmentTransaction.replace(R.id.podcast_second_container, searchFragment, TAG_SEARCH);
        } else {
            fragmentTransaction.replace(R.id.activity_main, searchFragment);
        }

        fragmentTransaction.addToBackStack(TAG_SEARCH);
        fragmentTransaction.commit();
    }

    private void showDetailFragment(EpisodeParcelable parcelable) {
        DetailFragment detailFragment = (DetailFragment) fragmentManager.findFragmentByTag(TAG_DETAIL);

        if (detailFragment == null) {
            detailFragment = new DetailFragment();
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("EXTRA_EPISODE", parcelable);
            detailFragment.setArguments(mBundle);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, detailFragment);
        fragmentTransaction.addToBackStack(TAG_DETAIL);
        fragmentTransaction.commit();
    }

    // SET ACTION BAR TITLE
    public void setActionBarTitle(int resourceId) {
        getSupportActionBar().setTitle(resourceId);
    }


    //    SEARCH BUTTON
    public void doSearch(View v) {
        Log.i(TAG, "doSearch: ");
        showSearchFragment();
    }

    //    SEARCH BUTTON
    public void addItemsToPlaylist(View v) {
        Log.i(TAG, "addItemsToPlaylist: ");
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
    public void onLongClickFragmentInteraction(PlaylistItem item) {
        Log.i(TAG, "onLongClickFragmentInteraction: show detail fragment" + item.getTitle());
        EpisodeParcelable parcelable = new EpisodeParcelable();
        parcelable.setId(item.getId());
        parcelable.setTitle(item.getTitle());
        parcelable.setDuration(item.getDuration());
        parcelable.setPoster(item.getPoster());
        showDetailFragment(parcelable);
    }

    @Override
    public void onClickFragmentInteraction(int position) {
        Log.d(TAG, "onClickFragmentInteraction: playlist item pressed " + position);
        this.playAudio(position);
    }

    @Override
    public void updateSelectedList(SearchItem item) {
        Log.d(TAG, "updateSelectedList: "+ item.getTitle());
    }

    @Override
    public void onAddObjectToPlaylist(SearchItem aValue) {
        //TODO
        Log.i(TAG, "onLongClickFragmentInteraction: search item selected");
//        Uri uri = getContentResolver().insert(
//                DataProvider.CONTENT_URI, aValue);
//
//        Toast.makeText(getBaseContext(),
//                uri.toString(), Toast.LENGTH_LONG).show();
    }
}