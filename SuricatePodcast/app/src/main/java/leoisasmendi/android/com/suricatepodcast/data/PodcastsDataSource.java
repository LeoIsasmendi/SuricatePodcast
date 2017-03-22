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

package leoisasmendi.android.com.suricatepodcast.data;

import android.content.Context;
import android.provider.BaseColumns;

public class PodcastsDataSource {

    //Metadata
    public static final String PODCASTS_TABLE_NAME = "Playlist";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String LONG_TYPE = "long";

    //Quotes fields
    public static class ColumnPodcasts {
        public static final String ID_PODCASTS = BaseColumns._ID;
        public static final String ID_PODCAST = "podcast";
        public static final String TITLE = "title";
        public static final String DURATION = "duration";
        public static final String AUDIO = "audio";
        public static final String POSTER = "poster";
        public static final String DESCRIPTION = "description";
    }

    //Script of creation
    public static final String CREATE_SCRIPT =
            "create table " + PODCASTS_TABLE_NAME + "(" +
                    ColumnPodcasts.ID_PODCASTS + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnPodcasts.ID_PODCAST + " " + LONG_TYPE + " not null," +
                    ColumnPodcasts.TITLE + " " + STRING_TYPE + " not null," +
                    ColumnPodcasts.DURATION + " " + STRING_TYPE + " not null," +
                    ColumnPodcasts.AUDIO + " " + STRING_TYPE + " not null," +
                    ColumnPodcasts.POSTER + " " + STRING_TYPE + " ," +
                    ColumnPodcasts.DESCRIPTION + " " + STRING_TYPE + ")";

    /*local*/
    PodcastsHelper openHelper;

    public PodcastsDataSource(Context context) {
        //New database instance
        openHelper = new PodcastsHelper(context);
    }
}
