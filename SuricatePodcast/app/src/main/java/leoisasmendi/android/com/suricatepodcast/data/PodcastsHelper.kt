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
package leoisasmendi.android.com.suricatepodcast.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PodcastsHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,  //String name
    null,  //factory
    DATABASE_VERSION //int version
) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        //Script of creation
        val CREATE_SCRIPT =
            "create table " + PodcastContract.PodcastEntry.TABLE_NAME + "(" +
                    PodcastContract.PodcastEntry._ID + " " + PodcastContract.PodcastType.INT_TYPE + " primary key autoincrement," +
                    PodcastContract.PodcastEntry.COLUMN_ID + " " + PodcastContract.PodcastType.LONG_TYPE + " not null," +
                    PodcastContract.PodcastEntry.COLUMN_TITLE + " " + PodcastContract.PodcastType.STRING_TYPE + "," +
                    PodcastContract.PodcastEntry.COLUMN_SHOW_TITLE + " " + PodcastContract.PodcastType.STRING_TYPE + "," +
                    PodcastContract.PodcastEntry.COLUMN_DURATION + " " + PodcastContract.PodcastType.STRING_TYPE + "," +
                    PodcastContract.PodcastEntry.COLUMN_AUDIO + " " + PodcastContract.PodcastType.STRING_TYPE + "," +
                    PodcastContract.PodcastEntry.COLUMN_POSTER + " " + PodcastContract.PodcastType.STRING_TYPE + " ," +
                    PodcastContract.PodcastEntry.COLUMN_DESCRIPTION + " " + PodcastContract.PodcastType.STRING_TYPE + ")"

        sqLiteDatabase.execSQL(CREATE_SCRIPT)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, i: Int, i1: Int) {
        //Update database
    }


    companion object {
        private const val DATABASE_NAME = "PodcastsHelper.db"
        private const val DATABASE_VERSION = 1
        private var sInstance: PodcastsHelper? = null

        @Synchronized
        fun getInstance(context: Context): PodcastsHelper {
            if (sInstance == null) {
                sInstance = PodcastsHelper(context.getApplicationContext())
            }
            return sInstance!!
        }
    }
}
