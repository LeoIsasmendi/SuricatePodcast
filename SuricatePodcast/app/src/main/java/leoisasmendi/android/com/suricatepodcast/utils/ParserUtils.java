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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import leoisasmendi.android.com.suricatepodcast.data.ItemLoader;
import leoisasmendi.android.com.suricatepodcast.data.PodcastContract;
import leoisasmendi.android.com.suricatepodcast.data.PlaylistItem;
import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;

public final class ParserUtils {

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
        aValue.put(PodcastContract.PodcastEntry.COLUMN_ID, item.getId());
        aValue.put(PodcastContract.PodcastEntry.COLUMN_TITLE, item.getTitle());
        aValue.put(PodcastContract.PodcastEntry.COLUMN_DURATION, item.getDuration());
        aValue.put(PodcastContract.PodcastEntry.COLUMN_AUDIO, item.getAudio());
        aValue.put(PodcastContract.PodcastEntry.COLUMN_POSTER, item.getPoster());
        aValue.put(PodcastContract.PodcastEntry.COLUMN_DESCRIPTION, item.getDescription());
        return aValue;
    }

    public static PlaylistItem buildPlaylistItem(Cursor mCursor) {
        return new PlaylistItem.Builder(mCursor.getInt(ItemLoader.Query.ID_PODCAST))
                .setTitle(mCursor.getString(ItemLoader.Query.TITLE))
                .setDuration(mCursor.getString(ItemLoader.Query.DURATION))
                .setAudio(mCursor.getString(ItemLoader.Query.AUDIO))
                .setPoster(mCursor.getString(ItemLoader.Query.POSTER))
                .setDescription(mCursor.getString(ItemLoader.Query.DESCRIPTION))
                .build();
    }

    public static String buildTime(long duration) {
        long hours = TimeUnit.SECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(duration) % 60;
        long seconds = TimeUnit.SECONDS.toSeconds(duration) % 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
