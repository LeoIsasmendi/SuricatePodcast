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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import leoisasmendi.android.com.suricatepodcast.parcelable.EpisodeParcelable;


public class PlayListDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlayListDatabase.db";
    private static final String FAVORITES_TABLE_NAME = "PlayListDatabase";
    private static final int DATABASE_VERSION = 1;
    private static PlayListDatabase sInstance;

    public PlayListDatabase(Context context) {
        super(context,
                DATABASE_NAME,//String name
                null,//factory
                DATABASE_VERSION//int version
        );
    }

    public static synchronized PlayListDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new PlayListDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PlaylistDataSource.CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Update database
    }

    public Cursor getPlaylist() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                FAVORITES_TABLE_NAME,  //Nombre de la tabla
                null,  //Lista de Columnas a consultar
                null,  //Columnas para la clausula WHERE
                null,  //Valores a comparar con las columnas del WHERE
                null,  //Agrupar con GROUP BY
                null,  //Condición HAVING para GROUP BY
                null  //Clausula ORDER BY
        );
    }

    public void insertToPlaylist(EpisodeParcelable movie) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PlaylistDataSource.ColumnFavorites.ID_MOVIES, movie.getId());
        values.put(PlaylistDataSource.ColumnFavorites.TITLE, movie.getTitle());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FAVORITES_TABLE_NAME,
                null,
                values);
    }

    public boolean removeFromPlaylist(long playlistId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAVORITES_TABLE_NAME, PlaylistDataSource.ColumnFavorites.ID_MOVIES + "=" + playlistId, null) > 0;
    }

    public Boolean isExistPodcast(long playlistId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String mQuery = "SELECT * " +
                " FROM " + FAVORITES_TABLE_NAME +
                " WHERE " + PlaylistDataSource.ColumnFavorites.ID_MOVIES + " = ?";

        Cursor mCursor = db.rawQuery(mQuery, new String[]{Long.toString(playlistId)});
        return mCursor.getCount() != 0;
    }
}
