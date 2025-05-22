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
package leoisasmendi.android.com.suricatepodcast.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.os.Bundle
import leoisasmendi.android.com.suricatepodcast.data.PodcastContract
import leoisasmendi.android.com.suricatepodcast.data.PodcastsHelper

class DataProvider : ContentProvider() {
    /**
     * Database specific constant declarations
     */
    private var db: SQLiteDatabase? = null
    private var podcastsHelper: PodcastsHelper? = null


    override fun onCreate(): Boolean {
        podcastsHelper = PodcastsHelper(getContext())
        db = podcastsHelper!!.getWritableDatabase()

        return (db != null)
    }

    override fun getType(p0: Uri): String? {
        when (uriMatcher.match(p0)) {
            PODCAST_ID -> return "leoisasmendi.android.com.suricatepodcast.provider.DataProvider/podcast"
            else -> throw IllegalArgumentException("Unsupported URI: " + p0)
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, extras: Bundle?): Int {
        return 0
    }

    override fun insert(uri: Uri, contentValues: ContentValues?, extras: Bundle?): Uri? {
        val rowID = db!!.insert(PodcastContract.PodcastEntry.TABLE_NAME, "", contentValues)

        // If record is added successfully
        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            getContext()!!.getContentResolver().notifyChange(_uri, null)
            return _uri
        }

        throw SQLException("Failed to add a record into " + uri)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String?>?): Int {
        val count: Int
        val db = podcastsHelper!!.getWritableDatabase()
        when (uriMatcher.match(uri)) {
            PODCAST_ID -> count =
                db.delete(PodcastContract.PodcastEntry.TABLE_NAME, selection, selectionArgs)

            else -> throw IllegalArgumentException("Unknown URI " + uri)
        }

        getContext()!!.getContentResolver().notifyChange(uri, null)
        return count
    }

    override fun update(
        p0: Uri,
        p1: ContentValues?,
        p2: String?,
        p3: Array<out String?>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.setTables(PodcastContract.PodcastEntry.TABLE_NAME)

        when (uriMatcher.match(uri)) {
            PODCAST_ID -> qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1))
            else -> {}
        }

        val db = podcastsHelper!!.getReadableDatabase()
        val c = qb.query(
            db, projection, selection,
            selectionArgs, null, null, sortOrder
        )
        /*
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext()!!.getContentResolver(), uri)
        return c
    }

    companion object {
        const val PROVIDER_NAME: String =
            "leoisasmendi.android.com.suricatepodcast.provider.DataProvider"
        val URL: String = "content://" + PROVIDER_NAME
        private const val BASE_PATH = "/podcast"

        @JvmField
        val CONTENT_URI: Uri = Uri.parse(URL)

        @JvmField
        val CONTENT_ITEM: Uri = Uri.parse(URL + BASE_PATH)

        const val _ID: String = "_id"

        const val PODCAST_ID: Int = 2

        val uriMatcher: UriMatcher

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(PROVIDER_NAME, BASE_PATH, PODCAST_ID)
        }
    }
}
