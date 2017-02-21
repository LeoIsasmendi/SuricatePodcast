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

public class ItemsContract {
    public static final String CONTENT_AUTHORITY = "leoisasmendi.android.com.suricatepodcast.provider.DataProvider";
    public static final Uri BASE_URI = Uri.parse("content://leoisasmendi.android.com.suricatepodcast.provider.DataProvider");

    interface ItemsColumns {
        /**
         * Type: INTEGER PRIMARY KEY AUTOINCREMENT
         */
        String _ID = "_id";
        /**
         * Type: TEXT
         */
        String ID_PODCAST = "podcast";
        /**
         * Type: TEXT NOT NULL
         */
        String TITLE = "title";
        /**
         * Type: TEXT NOT NULL
         */
        String DURATION = "duration";
        /**
         * Type: TEXT NOT NULL
         */
        String AUDIO = "audio";
        /**
         * Type: TEXT NOT NULL
         */
        String POSTER = "poster";
    }

    public static class Items implements ItemsColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/leoisasmendi.android.com.suricatepodcast.provider.DataProvider.items";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/leoisasmendi.android.com.suricatepodcast.provider.DataProvider.items";

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

    private ItemsContract() {
    }
}
