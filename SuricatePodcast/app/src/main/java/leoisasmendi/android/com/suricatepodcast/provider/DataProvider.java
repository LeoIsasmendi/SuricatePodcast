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

package leoisasmendi.android.com.suricatepodcast.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import leoisasmendi.android.com.suricatepodcast.data.PodcastsHelper;
import leoisasmendi.android.com.suricatepodcast.data.PodcastsDataSource;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;

public class DataProvider extends ContentProvider {

    static final String PROVIDER_NAME = "suricatepodcast";
    static final String URL = "content://" + PROVIDER_NAME + "/data";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> PODCASTS_PROJECTION_MAP;

    static final String _ID = "_id";
    static final int PODCASTS = 1;
    static final int PODCAST_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "podcasts", PODCASTS);
        uriMatcher.addURI(PROVIDER_NAME, "podcasts/#", PODCAST_ID);
    }


    private PodcastsHelper podcastsHelper;

    @Override
    public boolean onCreate() {
        podcastsHelper = new PodcastsHelper(getContext());
        return (podcastsHelper.getWritableDatabase() != null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all podcasts
             */
            case PODCASTS:
                return "vnd.android.cursor.dir/vnd.example.podcasts";
            /**
             * Get a particular podcast
             */
            case PODCAST_ID:
                return "vnd.android.cursor.item/vnd.example.podcasts";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
//        String selection, String[] selectionArgs) {
//            int count = 0;
//            switch (uriMatcher.match(uri)) {
//                case PODCASTS:
//                    count = db.update(STUDENTS_TABLE_NAME, values, selection, selectionArgs);
//                    break;
//
//                case PODCAST_ID:
//                    count = db.update(STUDENTS_TABLE_NAME, values,
//                            _ID + " = " + uri.getPathSegments().get(1) +
//                                    (!TextUtils.isEmpty(selection) ? "
//                            AND (" +selection + ')' : ""), selectionArgs);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unknown URI " + uri );
//            }
//
//            getContext().getContentResolver().notifyChange(uri, null);
//            return count;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = insertToPodcasts(null); //TODO, FIX

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
//            case PODCASTS:
//                count = db.delete(STUDENTS_TABLE_NAME, selection, selectionArgs);
//                break;

            case PODCAST_ID:
                String id = uri.getPathSegments().get(1);
                SQLiteDatabase db = podcastsHelper.getWritableDatabase();
                count = db.delete(PodcastsDataSource.PODCASTS_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND(" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PodcastsDataSource.PODCASTS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case PODCASTS:
                qb.setProjectionMap(PODCASTS_PROJECTION_MAP);
                break;

            case PODCAST_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        SQLiteDatabase db = podcastsHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /*
     * OLD DATABASE MANAGMENT
     */

    public long insertToPodcasts(EpisodeParcelable episode) {
        // Gets the data repository in write mode
        SQLiteDatabase db = podcastsHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PodcastsDataSource.ColumnPodcasts.ID_PODCAST, episode.getId());
        values.put(PodcastsDataSource.ColumnPodcasts.TITLE, episode.getTitle());


        // Insert the new row, returning the primary key value of the new row
        return db.insert(
                PodcastsDataSource.PODCASTS_TABLE_NAME,
                null,
                values);
    }

//    public int removeFromPodcasts(long podcastId) {
//        SQLiteDatabase db = podcastsHelper.getWritableDatabase();
//        return db.delete(PodcastsDataSource.PODCASTS_TABLE_NAME, PodcastsDataSource.ColumnPodcasts.ID_PODCAST + "=" + podcastId, null);
//    }

    public Boolean isExistPodcast(long playlistId) {
        SQLiteDatabase db = podcastsHelper.getReadableDatabase();
        String mQuery = "SELECT * " +
                " FROM " + PodcastsDataSource.PODCASTS_TABLE_NAME +
                " WHERE " + PodcastsDataSource.ColumnPodcasts.ID_PODCAST + " = ?";

        Cursor mCursor = db.rawQuery(mQuery, new String[]{Long.toString(playlistId)});
        return mCursor.getCount() != 0;
    }
}
