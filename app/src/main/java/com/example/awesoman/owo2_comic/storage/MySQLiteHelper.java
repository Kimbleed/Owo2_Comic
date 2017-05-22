package com.example.awesoman.owo2_comic.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.utils.MyLogger;

/**
 * Created by Awesome on 2016/11/5.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG = "MySQLiteHelper";

    public static String DATABASE_NAME="owe2_comic.db";
    public static int DATA_VERSION = 1;
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_COMIC_IN_MAIN);
        MyLogger.ddLog(TAG).i("COMIC_IN_MAIN 表创建");
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_COMIC_TYPE);
        MyLogger.ddLog(TAG).i("COMIC_TYPE 表创建");
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_COMIC_HISTORY);
        MyLogger.ddLog(TAG).i("COMIC_HISTORY 表创建");
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_MUSIC_TRIGGER);
        MyLogger.ddLog(TAG).i("MUSIC_TRIGGER 表创建");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_COMIC_HISTORY);
        MyLogger.ddLog(TAG).i("COMIC_HISTORY 表创建");
        sqLiteDatabase.execSQL(ComicEntry.SQL_CREATE_TABLE_MUSIC_TRIGGER);
        MyLogger.ddLog(TAG).i("MUSIC_TRIGGER 表创建");
    }
}
