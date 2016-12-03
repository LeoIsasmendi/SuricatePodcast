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

public class PlaylistDataSource {

    //Metadata
    public static final String FAVORITES_TABLE_NAME = "Playlist";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String LONG_TYPE = "long";

    //Quotes fields
    public static class ColumnFavorites {
        public static final String ID_FAVORITES = BaseColumns._ID;
        public static final String ID_MOVIES = "movies";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
    }

    //Script of creation
    public static final String CREATE_SCRIPT =
            "create table " + FAVORITES_TABLE_NAME + "(" +
                    ColumnFavorites.ID_FAVORITES + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnFavorites.ID_MOVIES + " " + LONG_TYPE + " not null," +
                    ColumnFavorites.TITLE + " " + STRING_TYPE + " not null," +
                    ColumnFavorites.POSTER + " " + STRING_TYPE + " not null)";

    //Scripts default insertion
/*    public static final String INSERT_SCRIPT =
            "insert into "+FAVORITES_TABLE_NAME+" values" +
                    "(null,"+
                    "293660," +
                    "\"TITLE\"," +
                    "\"ORIGINAL_TITLE\"," +
                    "\"http://image.tmdb.org/t/p/w185/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\","+
                    "\"OVERVIEW\"," +
                    "\"RELEASE_DATA\"," +
                    "3.3" +
                    ")";
//                    "(null, 246655," + "\"http://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg\")";
*/
    private PlayListDatabase openHelper;

    public PlaylistDataSource(Context context) {
        //New database instance
        openHelper = new PlayListDatabase(context);
    }
}
