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

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Helper for loading a list of items or a single item.
 */
public class ItemLoader extends CursorLoader {

//    public static ItemLoader newAllArticlesInstance(Context context) {
//        return new ItemLoader(context, ItemsContract.Items.buildDirUri());
//    }
//
//    public static ItemLoader newInstanceForItemId(Context context, long itemId) {
//        return new ItemLoader(context, ItemsContract.Items.buildItemUri(itemId));
//    }

    public ItemLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public interface Query {
        String[] PROJECTION = {
                ItemsContract.Items._ID,
                ItemsContract.Items.ID_PODCAST,
                ItemsContract.Items.TITLE,
                ItemsContract.Items.DURATION,
                ItemsContract.Items.AUDIO,
                ItemsContract.Items.POSTER
        };

        int _ID = 0;
        int ID_PODCAST = 1;
        int TITLE = 2;
        int DURATION = 3;
        int AUDIO = 4;
        int POSTER = 5;
    }

}
