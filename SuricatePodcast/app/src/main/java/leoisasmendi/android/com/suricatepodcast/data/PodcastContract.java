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

package leoisasmendi.android.com.suricatepodcast.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PodcastContract {

    // To prevent to someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PodcastContract() {
    }

    public static final String CONTENT_AUTHORITY = "leoisasmendi.android.com.suricatepodcast.provider.DataProvider";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Inner class that defines the table contents */
    public static final class PodcastEntry implements BaseColumns {

        public static final String TABLE_NAME = "Playlist";

        public static final String COLUMN_ID = "podcast";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SHOW_TITLE = "showtitle";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_AUDIO = "audio";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_DESCRIPTION = "description";

        /**
         * Matches: /items/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("items").build();
        }

        /**
         * Matches: /items/[_id]/
         */
        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath("items").appendPath(Long.toString(_id)).build();
        }

        /**
         * Read item ID item detail URI.
         */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
    }


    /* Inner class that defines the table types */
    public static final class PodcastType {
        public static final String STRING_TYPE = "text";
        public static final String INT_TYPE = "integer";
        public static final String LONG_TYPE = "long";
    }
}
