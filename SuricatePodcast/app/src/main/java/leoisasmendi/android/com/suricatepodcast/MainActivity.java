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
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.ui.AboutFragment;
import leoisasmendi.android.com.suricatepodcast.ui.DetailFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MediaPlayerFragment;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;
import leoisasmendi.android.com.suricatepodcast.ui.ThemesFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainListInteractionListener, SearchFragment.OnFragmentInteractionListener {

    final String TAG = getClass().getSimpleName();
    private static final String TAG_MAIN = MainFragment.class.getSimpleName();

    private static final String TAG_DETAIL = DetailFragment.class.getSimpleName();
    private static final String TAG_SEARCH = SearchFragment.class.getSimpleName();
    private static final String TAG_ABOUT = AboutFragment.class.getSimpleName();
    private static final String TAG_THEMES = ThemesFragment.class.getSimpleName();
    private static final String TAG_MEDIAPLAYER = MediaPlayerFragment.class.getSimpleName();
//    private final String TAG_PLAYER = MediaPlayerFragment.class.getSimpleName();

    private FragmentManager fragmentManager;

    private boolean mTwoPane;

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
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_main, new MainFragment(), TAG_MAIN);
            fragmentTransaction.commit();
        }
    }

    private void loadPlaylistData() {
        //LOAD THIS ASYNC WAY
        // Retrieve student records
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

    private void showDetailFragment() {
        DetailFragment detailFragment = (DetailFragment) fragmentManager.findFragmentByTag(TAG_DETAIL);

        if (detailFragment == null) {
            detailFragment = new DetailFragment();
            EpisodeParcelable data = new EpisodeParcelable();
            data.setId(0);
            data.setTitle("Test Title");
            data.setDetail("Bla bla bla and batabla");
            data.setDuration(999);

            Bundle mBundle = new Bundle();
            mBundle.putParcelable("EXTRA_EPISODE", data);
            detailFragment.setArguments(mBundle);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, detailFragment);
        fragmentTransaction.addToBackStack(TAG_DETAIL);
        fragmentTransaction.commit();
    }

    private void showMediaPlayer() {
        Log.i(TAG, "showMediaPlayer: ");
        MediaPlayerFragment mediaPlayer = (MediaPlayerFragment) fragmentManager.findFragmentByTag(TAG_MEDIAPLAYER);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayerFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.media_player_container, mediaPlayer);
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


    // MEDIA PLAYER
    public void playAudio(View v) {
        Log.i(TAG, "playAudio: ");

        Log.i(TAG, "playAudio: updating widget too");
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        getApplicationContext().sendBroadcast(dataUpdatedIntent);

//        String url = "http://........"; // your URL here
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setDataSource(url);
//        mediaPlayer.prepare(); // might take long! (for buffering, etc)
//        mediaPlayer.start();
    }

    public void stopAudio(View v) {
        Log.i(TAG, "stopAudio: ");
        // TODO: stop audio
    }

    public void nextAudio(View v) {
        Log.i(TAG, "nextAudio: ");
        // TODO: next audio
    }

    public void prevAudio(View v) {
        Log.i(TAG, "prevAudio: ");
        // TODO: prev audio
    }

    // INTERFACES
    @Override
    public void onLongClickFragmentInteraction() {
        Log.i(TAG, "onLongClickFragmentInteraction: show detail fragment");
        showDetailFragment();
    }

    @Override
    public void onClickFragmentInteraction() {
        //TODO
        Log.i(TAG, "onClickFragmentInteraction: playlist item pressed");
        showMediaPlayer();
    }

    @Override
    public void onAddObjectToPlaylist(ContentValues aValue) {
        //TODO
        Log.i(TAG, "onLongClickFragmentInteraction: search item selected");
        Uri uri = getContentResolver().insert(
                DataProvider.CONTENT_URI, aValue);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }
}