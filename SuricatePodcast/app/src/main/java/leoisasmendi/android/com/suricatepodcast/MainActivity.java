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
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;
import leoisasmendi.android.com.suricatepodcast.provider.DataProvider;
import leoisasmendi.android.com.suricatepodcast.ui.AboutFragment;
import leoisasmendi.android.com.suricatepodcast.ui.DetailFragment;
import leoisasmendi.android.com.suricatepodcast.ui.MainFragment;
import leoisasmendi.android.com.suricatepodcast.ui.SearchFragment;
import leoisasmendi.android.com.suricatepodcast.ui.ThemesFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private DetailFragment detailFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        loadFragment();
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showThemes() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ThemesFragment themes = new ThemesFragment();
        fragmentTransaction.replace(R.id.activity_main, themes);
        fragmentTransaction.addToBackStack("themes");
        fragmentTransaction.commit();
    }

    private void showAbout() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AboutFragment about = new AboutFragment();
        fragmentTransaction.replace(R.id.activity_main, about);
        fragmentTransaction.addToBackStack("about");
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadFragment() {

        //TODO: load fragments in TabletUI

        if (mainFragment == null) {
            mainFragment = new MainFragment();

        }
        loadPlaylistData();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, mainFragment);
        fragmentTransaction.commit();

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

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, searchFragment);
        fragmentTransaction.addToBackStack("search");
        fragmentTransaction.commit();

    }

    private void showDetailFragment() {

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
        fragmentTransaction.addToBackStack("detail");
        fragmentTransaction.commit();

    }

    // SET ACTION BAR TITLE

    public void setActionBarTitle(int resourceId) {
        getSupportActionBar().setTitle(resourceId);
    }


    //    SEARCH BUTTON
    public void doSearch(View v) {
        final String TAG = getClass().getSimpleName();
        Log.i(TAG, "doSearch: ");
        showSearchFragment();
    }

    //    SEARCH BUTTON
    public void addItemsToPlaylist(View v) {
        final String TAG = getClass().getSimpleName();
        Log.i(TAG, "addItemsToPlaylist: ");
    }

    // INTERFACES
    @Override
    public void onFragmentInteraction() {
        //TODO
        String TAG = getClass().getSimpleName();
        Log.i(TAG, "onFragmentInteraction: playlist item pressed");
        showDetailFragment();

    }

    @Override
    public void onAddObjectToPlaylist(ContentValues aValue) {
        //TODO
        String TAG = getClass().getSimpleName();
        Log.i(TAG, "onFragmentInteraction: search item selected");

        Uri uri = getContentResolver().insert(
                DataProvider.CONTENT_URI, aValue);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }
}