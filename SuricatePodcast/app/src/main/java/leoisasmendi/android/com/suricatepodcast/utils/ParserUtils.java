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

package leoisasmendi.android.com.suricatepodcast.utils;

import android.content.ContentValues;
import android.database.Cursor;

import leoisasmendi.android.com.suricatepodcast.data.ItemLoader;
import leoisasmendi.android.com.suricatepodcast.data.ItemsContract;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;

public class ParserUtils {

    public static EpisodeParcelable buildParcelable(Cursor item) {
        EpisodeParcelable parcelable = new EpisodeParcelable();
        parcelable.setId(item.getInt(ItemLoader.Query.ID_PODCAST));
        parcelable.setTitle(item.getString(ItemLoader.Query.TITLE));
        parcelable.setDuration(item.getString(ItemLoader.Query.DURATION));
        parcelable.setPoster(item.getString(ItemLoader.Query.POSTER));
        parcelable.setDescription(item.getString(ItemLoader.Query.DESCRIPTION));
        return parcelable;
    }

    public static ContentValues buildContentValue(PlaylistItem item) {
        ContentValues aValue = new ContentValues();
        aValue.put(ItemsContract.Items.ID_PODCAST, item.getId());
        aValue.put(ItemsContract.Items.TITLE, item.getTitle());
        aValue.put(ItemsContract.Items.DURATION, item.getDuration());
        aValue.put(ItemsContract.Items.AUDIO, item.getAudio());
        aValue.put(ItemsContract.Items.POSTER, item.getPoster());
        aValue.put(ItemsContract.Items.DESCRIPTION, item.getDescription());
        return aValue;
    }
}
