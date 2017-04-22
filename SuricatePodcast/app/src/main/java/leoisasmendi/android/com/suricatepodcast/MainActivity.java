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

import com.google.firebase.analytics.FirebaseAnalytics;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


import io.fabric.sdk.android.Fabric;
import leoisasmendi.android.com.suricatepodcast.data.ItemLoader;
import leoisasmendi.android.com.suricatepodcast.data.PodcastContract;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService;
import leoisasmendi.android.com.suricatepodcast.ui.AboutFragment;
import leoisasmendi.android.com.suricatepodcast.ui.DetailFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MediaPlayerFragment;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;
import leoisasmendi.android.com.suricatepodcast.utils.ParserUtils;
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener {

    final String TAG = getClass().getSimpleName();

    public static final String Broadcast_PLAY_NEW_AUDIO = "leoisasmendi.android.com.suricatepodcast.PlayNewAudio";

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private FragmentManager fragmentManager;

    // MEDIA PLAYER
    private MediaPlayerService player;
    boolean serviceBound = false;
    private ServiceConnection serviceConnection;

    // ADS MOB
    InterstitialAd mInterstitialAd;

    // FIREBASE ANALYTICS
    private FirebaseAnalytics mFirebaseAnalytics;

    public static final String ACTION_DATA_UPDATED = "leoisasmendi.android.com.suricatepodcast.app.ACTION_DATA_UPDATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        initToolbar();
        initAds();
        initFragments();
        initServiceConnection();
        initAnalytics();
    }

    private void initAnalytics() {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.nav_list);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initFragments() {
        fragmentManager = getFragmentManager();
        Log.d(TAG, "onCreate: twoPaneMode " + getResources().getBoolean(R.bool.twoPaneMode));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getResources().getBoolean(R.bool.twoPaneMode)) {

            fragmentTransaction
                    .replace(R.id.master_container, new MainFragment(), MainFragment.class.getSimpleName())
                    .replace(R.id.detail_container, new DetailFragment(), MediaPlayerFragment.class.getSimpleName());

        } else { //Single panel view
            fragmentTransaction
                    .replace(R.id.master_container, new MainFragment(), MainFragment.class.getSimpleName());
        }

        fragmentTransaction.commit();
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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item);
        int id = item.getItemId();

        if (id == R.id.menu_about) {
            showAbout();
            return true;
        }

        if (id == R.id.menu_item_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = getString(R.string.share_body_text);
            // TODO: INSERT THE CORRECT URL
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "www.audiosear.ch/audio.mp3");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.menu_main:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            case R.id.menu_search:
                showSearchFragment();
                return true;
            case R.id.menu_about:
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
            case R.id.menu_exit:
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            default:
                return false;
        }
    }

    private void showAbout() {
        showAds();
        fragmentManager.beginTransaction()
                .replace(R.id.master_container, new AboutFragment())
                .addToBackStack(AboutFragment.class.getSimpleName())
                .commit();
    }


    private void showSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            fragmentTransaction.replace(R.id.detail_container, searchFragment, SearchFragment.class.getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.master_container, searchFragment);
        }

        fragmentTransaction.addToBackStack(SearchFragment.class.getSimpleName()).commit();
    }

    private void showDetailFragment(EpisodeParcelable parcelable) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("EXTRA_EPISODE", parcelable);
        detailFragment.setArguments(mBundle);

        fragmentManager.beginTransaction()
                .replace(R.id.master_container, detailFragment)
                .addToBackStack(DetailFragment.class.getSimpleName())
                .commit();
    }

    private void showPlayerFragment(EpisodeParcelable parcelable) {
        MediaPlayerFragment playerFragment = new MediaPlayerFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("EXTRA_MEDIA_INFO", parcelable);
        playerFragment.setArguments(mBundle);

        fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.master_container, playerFragment)
                .addToBackStack(MediaPlayerFragment.class.getSimpleName())
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
    public void onClick(int position, Cursor item) {
        Log.d(TAG, "onClickFragmentInteraction: playlist item pressed " + position);
        showPlayerFragment(ParserUtils.buildParcelable(item));
        this.playAudio(position);
    }

    @Override
    public void onDeleteItem(int itemId) {
        Log.d(TAG, "onDeleteItem: " + itemId);
        Cursor c = getContentResolver().query(DataProvider.CONTENT_URI,
                null,
                PodcastContract.PodcastEntry.COLUMN_ID + "=" + itemId,
                null,
                null);
        if (c.getCount() != 0) {
            String where = PodcastContract.PodcastEntry.COLUMN_ID + "=?";
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

    // SearchFragment implements
    @Override
    public void searchPodcast() {
        Log.d(TAG, "searchPodcast: ");
        showSearchFragment();
    }

}