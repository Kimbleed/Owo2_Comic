package com.example.awesoman.owo2_comic.sqlite;

import android.os.Environment;
import android.provider.BaseColumns;

import java.io.File;

/**
 * Created by Awesome on 2016/11/7.
 */

public class ComicEntry implements BaseColumns {
    public static final String PROJECT_PATH ="Owo2_Comic";
    public static final String DATABASE_PATH = PROJECT_PATH+ File.separator+"databases";
    public static final String DATABASE_NAME = "owe2comic.db";
//    public static final String COMIC_PATH = PROJECT_PATH+File.separator+"files"+File.separator +"ComicAll";
//    public static final String MUSIC_PATH = PROJECT_PATH+File.separator+"files"+File.separator +"MusicAll";
    public static final String COMIC_PATH = "ComicRes";
    public static final String MUSIC_PATH = "MusicRes";


    /**
     * 获取根目录
     */
    public static String getProjectPath(){
        return Environment.getExternalStorageDirectory()+File.separator+PROJECT_PATH;
    }

    /**
     * 获取漫画资源文件夹路径
     * @return
     */
    public static String getComicPath(){
        return Environment.getExternalStorageDirectory()+File.separator+PROJECT_PATH+File.separator+COMIC_PATH;
    }

    /**
     * 获取音乐资源文件夹路径
     */
    public static String getMusicPath(){
        return Environment.getExternalStorageDirectory()+File.separator+PROJECT_PATH+File.separator+MUSIC_PATH;
    }

    //ComicAll表名
    public static final String COMIC_ALL_TABLE_NAME = "ComicInMain";
    //ComicAll列名
    public static final String COMIC_ALL_COLUMNS_NAME_COMIC_TYPE = "ComicType";
    public static final String COMIC_ALL_COLUMNS_NAME_COMIC_NAME = "ComicName";
    public static final String COMIC_ALL_COLUMNS_NAME_COMIC_PATH ="ComicPath";
    //ComicAll创建表格的SQL语句
    public static final String SQL_CREATE_TABLE_COMIC_IN_MAIN =
            "create table " + COMIC_ALL_TABLE_NAME + " (" +
                    _ID + " integer primary key," +
                    COMIC_ALL_COLUMNS_NAME_COMIC_TYPE + " text," +
                    COMIC_ALL_COLUMNS_NAME_COMIC_NAME + " text,   " +
                    COMIC_ALL_COLUMNS_NAME_COMIC_PATH +" text"+")";
    //ComicAll删除表格的SQL语句
    public static final String IN_MAIN_SQL_DELETE_TABLE =
            "drop table if exists " + COMIC_ALL_TABLE_NAME;


    //ComicType表名
    public static final String COMIC_TYPE_TABLE_NAME = "ComicType";
    //ComicType列名
    public static final String TYPE_COLUMNS_NAME_COMIC_NAME = "TypeName";
    public static final String TYPE_COLUMNS_NAME_COMIC_No = "TypeNo";
    //ComicType创建表格的SQL语句
    public static final String SQL_CREATE_TABLE_COMIC_TYPE =
            "create table " + COMIC_TYPE_TABLE_NAME + " (" +
                    _ID + " integer primary key," +
                    TYPE_COLUMNS_NAME_COMIC_NAME + " text," +
                    TYPE_COLUMNS_NAME_COMIC_No + " text" +")";

    //ComicType删除表格的SQL语句
    public static final String TYPE_SQL_DELETE_TABLE =
            "drop table if exists " + COMIC_ALL_TABLE_NAME;
}
