/*
 * The MIT License (MIT)
 * Copyright (c) 2017. Sergio Leonardo Isasmendi
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

import android.net.Uri
import android.provider.BaseColumns

object PodcastContract {
    const val CONTENT_AUTHORITY: String =
        "leoisasmendi.android.com.suricatepodcast.provider.DataProvider"
    val BASE_URI: Uri = Uri.parse("content://" + CONTENT_AUTHORITY)

    /* Inner class that defines the table contents */
    object PodcastEntry : BaseColumns {
        const val TABLE_NAME: String = "Playlist"

        const val _ID: Int = 0
        const val COLUMN_ID: String = "podcast"
        const val COLUMN_TITLE: String = "title"
        const val COLUMN_SHOW_TITLE: String = "showtitle"
        const val COLUMN_DURATION: String = "duration"
        const val COLUMN_AUDIO: String = "audio"
        const val COLUMN_POSTER: String = "poster"
        const val COLUMN_DESCRIPTION: String = "description"

        /**
         * Matches: /items/
         */
        fun buildDirUri(): Uri? {
            return BASE_URI.buildUpon().appendPath("items").build()
        }

        /**
         * Matches: /items/[_id]/
         */
        fun buildItemUri(_id: Long): Uri? {
            return BASE_URI.buildUpon().appendPath("items").appendPath(_id.toString()).build()
        }

        /**
         * Read item ID item detail URI.
         */
        fun getItemId(itemUri: Uri): Long {
            return itemUri.pathSegments[1].toLong()
        }
    }


    /* Inner class that defines the table types */
    object PodcastType {
        const val STRING_TYPE: String = "text"
        const val INT_TYPE: String = "integer"
        const val LONG_TYPE: String = "long"
    }
}
