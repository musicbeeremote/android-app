package com.kelsos.mbrc.data;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class Genre implements BaseColumns, GenreColumns {
    private long id;
    private String genreName;
    public static final String TABLE_NAME = "genres";

    public static final String CREATE_TABLE =
            "create table " + TABLE_NAME + "(" + _ID + " integer primary key autoincrement," +
                    GENRE_NAME + " text unique " + ")";
    public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;


    public static Uri URI() {
        return Uri.withAppendedPath(Uri.parse(LibraryProvider.SCHEME +
                LibraryProvider.AUTHORITY), TABLE_NAME);
    }

    public static final int BASE_URI_CODE = 0x31847c3;
    public static final int BASE_ITEM_CODE =  0x1e2395d;

    public static void addMatcherUris(UriMatcher uriMatcher) {
        uriMatcher.addURI(LibraryProvider.AUTHORITY, TABLE_NAME, BASE_URI_CODE);
        uriMatcher.addURI(LibraryProvider.AUTHORITY, TABLE_NAME + "/#", BASE_ITEM_CODE);
    }

    public static final String TYPE_DIR = "vnd.android.cursor.dir/vnd.com.kelsos.mbrc.provider." + TABLE_NAME;
    public static final String TYPE_ITEM = "vnd.android.cursor.item/vnd.com.kelsos.mbrc.provider." + TABLE_NAME;

    public Genre(String genreName) {
        this.id = -1;
        this.genreName = genreName;
    }

    public Genre(final Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(_ID));
        this.genreName = cursor.getString(cursor.getColumnIndex(GENRE_NAME));
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(GENRE_NAME, genreName);
        return values;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
