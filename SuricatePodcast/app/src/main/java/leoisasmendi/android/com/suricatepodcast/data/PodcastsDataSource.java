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
        public static final String POSTER = "poster";
        public static final String DURATION = "duration";
        public static final String AUDIO = "audio";
    }

    //Script of creation
    public static final String CREATE_SCRIPT =
            "create table " + PODCASTS_TABLE_NAME + "(" +
                    ColumnPodcasts.ID_PODCASTS + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnPodcasts.ID_PODCAST + " " + LONG_TYPE + " not null," +
                    ColumnPodcasts.TITLE + " " + STRING_TYPE + " not null," +
                    ColumnPodcasts.DURATION + " " + STRING_TYPE + " not null," +
                    ColumnPodcasts.AUDIO + " " + STRING_TYPE + " not null," +
                    ColumnPodcasts.POSTER + " " + STRING_TYPE + ")";


    //Scripts default insertion
    public static final String INSERT_FAKE_DATA_SCRIPT =
            "insert into " + PODCASTS_TABLE_NAME + " values" +
                    "(null," +
                    "0303456," +
                    "\"Joe Rogan\"," +
                    "\"00:25:00\"," +
                    "\"http://image.tmdb.org/t/p/w185/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\"," +
                    "\"https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg\"," +
                    "),"+
                    "(null," +
                    "2," +
                    "\"Cafontia Rogan\"," +
                    "\"00:22:00\"," +
                    "\"http://static.libsyn.com/p/assets/2/3/6/c/236cb6c10b89befa/Keep-Hammering.jpg\"," +
                    "\"https://www.audiosear.ch/media/80de28fbeb78605e66fa8df7d223b584/0/public/audio_file/154656/113974051-startalk-the-joe-rogan-experience.mp3\"," +
                    "),"+
                    "(null," +
                    "0303456," +
                    "\"Jimmi Rogan\"," +
                    "\"01:15:00\"," +
                    "\"http://is4.mzstatic.com/image/thumb/Music62/v4/8e/0a/70/8e0a7014-9ccc-b532-5eb7-2b803d1a571a/source/600x600bb.jpg\"," +
                    "\"https://www.audiosear.ch/media/842dac5e89fcfcc8eaa98c1eeb725286/0/public/audio_file/325944/keephammering008.mp3\"," +
                    ")";
//                    "(null, 246655," + "\"http://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg\")";

    private PodcastsHelper openHelper;

    public PodcastsDataSource(Context context) {
        //New database instance
        openHelper = new PodcastsHelper(context);
    }
}
