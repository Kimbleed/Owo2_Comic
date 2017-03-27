package com.example.awesoman.owo2_comic.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Awesome on 2016/11/5.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME="owe2_comic.db";
    public static int DATA_VERSION = 1;
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_COMIC_IN_MAIN);
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_COMIC_TYPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
