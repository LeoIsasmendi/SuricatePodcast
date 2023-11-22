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
import androidx.annotation.Nullable;

import leoisasmendi.android.com.suricatepodcast.data.PodcastContract;
import leoisasmendi.android.com.suricatepodcast.data.PodcastsHelper;

public class DataProvider extends ContentProvider {

    static final String PROVIDER_NAME = "leoisasmendi.android.com.suricatepodcast.provider.DataProvider";
    static final String URL = "content://" + PROVIDER_NAME;
    private static final String BASE_PATH = "/podcast";

    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final Uri CONTENT_ITEM = Uri.parse(URL + BASE_PATH);

    public static final String _ID = "_id";

    static final int PODCAST_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, BASE_PATH, PODCAST_ID);
    }


    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    private PodcastsHelper podcastsHelper;


    @Override
    public boolean onCreate() {
        podcastsHelper = new PodcastsHelper(getContext());
        db = podcastsHelper.getWritableDatabase();

        return (db != null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // Get a particular podcast
            case PODCAST_ID:
                return "leoisasmendi.android.com.suricatepodcast.provider.DataProvider/podcast";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        long rowID = db.insert(PodcastContract.PodcastEntry.TABLE_NAME, "", contentValues);

        // If record is added successfully
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = podcastsHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case PODCAST_ID:
                count = db.delete(PodcastContract.PodcastEntry.TABLE_NAME, selection, selectionArgs);
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
        qb.setTables(PodcastContract.PodcastEntry.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case PODCAST_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        SQLiteDatabase db = podcastsHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        /*
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
}
