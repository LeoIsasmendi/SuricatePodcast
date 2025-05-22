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
package leoisasmendi.android.com.suricatepodcast

import android.app.FragmentManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.database.Cursor
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import leoisasmendi.android.com.suricatepodcast.data.ItemLoader
import leoisasmendi.android.com.suricatepodcast.data.PodcastContract
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService.LocalBinder
import leoisasmendi.android.com.suricatepodcast.ui.AboutFragment
import leoisasmendi.android.com.suricatepodcast.ui.DetailFragment
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment.OnFragmentInteractionListener
import leoisasmendi.android.com.suricatepodcast.ui.MediaPlayerFragment
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment
import leoisasmendi.android.com.suricatepodcast.utils.ParserUtils.buildParcelable
import leoisasmendi.android.com.suricatepodcast.utils.StorageUtil


//import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
//import io.fabric.sdk.android.Fabric;
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnFragmentInteractionListener {
    val TAG: String = javaClass.getSimpleName()

    private var mToolbar: Toolbar? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null

    private var fragmentManager: FragmentManager? = null

    // MEDIA PLAYER
    private var player: MediaPlayerService? = null
    var serviceBound: Boolean = false
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main)
        initToolbar()
        initAds()
        initFragments()
        initServiceConnection()
        initAnalytics()
    }

    private fun initAnalytics() {
        // Obtain the FirebaseAnalytics instance.
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private fun initServiceConnection() {
        //Binding this Client to the AudioPlayer Service
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                val binder = service as LocalBinder
                player = binder.service
                serviceBound = true

                Log.d(TAG, "onServiceConnected: Service bound")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                serviceBound = false
            }
        }
    }

    private fun initAds() {
        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId(BuildConfig.INTERSTITIAL_FULL_SCREEN);
    }

    private fun initToolbar() {
        mToolbar = findViewById<View?>(R.id.toolbar) as Toolbar?
        setSupportActionBar(mToolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setHomeButtonEnabled(true)
        mDrawerLayout = findViewById<View?>(R.id.drawer_layout) as DrawerLayout
        mDrawerToggle = object : ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }
        }
        mDrawerToggle!!.setDrawerIndicatorEnabled(true)
        mDrawerLayout!!.addDrawerListener(mDrawerToggle!!)
        mNavigationView = findViewById<View?>(R.id.nav_list) as NavigationView
        mNavigationView!!.setNavigationItemSelectedListener(this)
    }

    private fun initFragments() {
        fragmentManager = getFragmentManager()
        Log.d(TAG, "onCreate: twoPaneMode " + getResources().getBoolean(R.bool.twoPaneMode))
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            fragmentTransaction
                .replace(
                    R.id.master_container,
                    MainFragment(),
                    MainFragment::class.java.getSimpleName()
                )
                .replace(
                    R.id.detail_container,
                    DetailFragment(),
                    MediaPlayerFragment::class.java.getSimpleName()
                )
        } else { //Single panel view
            fragmentTransaction
                .replace(
                    R.id.master_container,
                    MainFragment(),
                    MainFragment::class.java.getSimpleName()
                )
        }

        fragmentTransaction.commit()
    }

    /*
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
 */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle!!.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onNavigationItemSelected: ")
        val id = item.getItemId()
        var intent: Intent?

        /*
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

         */
        return false
    }

    private fun showAbout() {
        //showAds();
        fragmentManager!!.beginTransaction()
            .replace(R.id.master_container, AboutFragment())
            .addToBackStack(AboutFragment::class.java.getSimpleName())
            .commit()
    }


    private fun showSearchFragment() {
        val searchFragment = SearchFragment()
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            fragmentTransaction.replace(
                R.id.detail_container,
                searchFragment,
                SearchFragment::class.java.getSimpleName()
            )
        } else {
            fragmentTransaction.replace(R.id.master_container, searchFragment)
        }

        fragmentTransaction.addToBackStack(SearchFragment::class.java.getSimpleName()).commit()
    }

    private fun showDetailFragment(parcelable: EpisodeParcelable?) {
        val detailFragment = DetailFragment()
        val mBundle = Bundle()
        mBundle.putParcelable("EXTRA_EPISODE", parcelable)
        detailFragment.setArguments(mBundle)

        fragmentManager!!.beginTransaction()
            .replace(R.id.master_container, detailFragment)
            .addToBackStack(DetailFragment::class.java.getSimpleName())
            .commit()
    }

    private fun showPlayerFragment(parcelable: EpisodeParcelable?) {
        val playerFragment = MediaPlayerFragment()
        val mBundle = Bundle()
        mBundle.putParcelable("EXTRA_MEDIA_INFO", parcelable)
        playerFragment.setArguments(mBundle)

        fragmentManager!!.beginTransaction()
            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
            .replace(R.id.master_container, playerFragment)
            .addToBackStack(MediaPlayerFragment::class.java.getSimpleName())
            .commit()
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("ServiceState", serviceBound)
        super.onSaveInstanceState(savedInstanceState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("ServiceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            // TODO: a better solution, this is a temporal fix
            try {
                unbindService(serviceConnection!!)
                //service is active
                player!!.stopSelf()
            } catch (e: Exception) {
                Log.d(TAG, "onDestroy: ", e)
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    private fun playAudio(audioIndex: Int) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            val storage = StorageUtil(getApplicationContext())
            storage.storeAudioIndex(audioIndex)

            val playerIntent = Intent(this, MediaPlayerService::class.java)
            startService(playerIntent)
            bindService(playerIntent, serviceConnection!!, BIND_AUTO_CREATE)
        } else {
            //Store the new audioIndex to SharedPreferences
            val storage = StorageUtil(getApplicationContext())
            storage.storeAudioIndex(audioIndex)

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            val broadcastIntent: Intent = Intent(Broadcast_PLAY_NEW_AUDIO)
            sendBroadcast(broadcastIntent)
        }
    }


    // INTERFACES
    override fun onClick(position: Int, item: Cursor?) {
        Log.d(TAG, "onClickFragmentInteraction: playlist item pressed " + position)
        showPlayerFragment(buildParcelable(item!!))
        this.playAudio(position)
    }

    override fun onDeleteItem(itemId: Int) {
        Log.d(TAG, "onDeleteItem: " + itemId)
        val c = getContentResolver().query(
            DataProvider.CONTENT_URI,
            null,
            PodcastContract.PodcastEntry.COLUMN_ID + "=" + itemId,
            null,
            null
        )
        if (c!!.getCount() != 0) {
            val where = PodcastContract.PodcastEntry.COLUMN_ID + "=?"
            val args: Array<String?> = arrayOf<String>(itemId.toString()) as Array<String?>
            getContentResolver().delete(DataProvider.CONTENT_ITEM, where, args)
        }
        c.close()
    }

    override fun onShowDetail(item: Cursor?) {
        Log.d(TAG, "onShowDetail: " + item?.getString(ItemLoader.Query.TITLE))
        showDetailFragment(buildParcelable(item!!))
    }

    // SearchFragment implements
    override fun searchPodcast() {
        Log.d(TAG, "searchPodcast: ")
        showSearchFragment()
    }

    companion object {
        const val Broadcast_PLAY_NEW_AUDIO: String =
            "leoisasmendi.android.com.suricatepodcast.PlayNewAudio"

        // ADS MOB
        //InterstitialAd mInterstitialAd;
        // FIREBASE ANALYTICS
        //private FirebaseAnalytics mFirebaseAnalytics;
        const val ACTION_DATA_UPDATED: String =
            "leoisasmendi.android.com.suricatepodcast.app.ACTION_DATA_UPDATED"
    }
}