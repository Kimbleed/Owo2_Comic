package com.example.awesoman.owo2_comic.storage;

/**
 * Created by Awesome on 2017/5/31.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.awesoman.owo2_comic.model.ComicHistoryInfo;
import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.example.awesoman.owo2_comic.model.ComicTypeInfo;
import com.example.awesoman.owo2_comic.model.MusicTriggerInfo;
import com.example.awesoman.owo2_comic.utils.MyLogger;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by Awesome on 2017/5/23.
 */

public class DaoHelper extends OrmLiteSqliteOpenHelper {
    public static final String TAG = "DaoHelper";

    private static final String DATABASE_NAME= "sqlite-loock.db";

    private static final int VERSION = 1;

    private DaoHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ComicInfo.class);
            TableUtils.createTable(connectionSource, ComicTypeInfo.class);
            TableUtils.createTable(connectionSource, MusicTriggerInfo.class);
            TableUtils.createTable(connectionSource, ComicHistoryInfo.class);
        }
        catch (Exception e){
            Log.i("orm exception",e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //如果版本有更新则会执行onUpgrade方法，
        //更新数据库先删除数据库再创建一个新的
    }

    private static DaoHelper daoHelp;

    public static synchronized DaoHelper getDaoHelp(Context context){
        if(daoHelp == null){
            synchronized (DaoHelper.class){
                if(daoHelp == null) daoHelp = new DaoHelper((context));
            }
        }
        return daoHelp;
    }

    //利用生成的daoHelp对象来生成Dao对象，该对象是处理数据库的关键要素
    private Dao<ComicInfo, Integer> dao;
    public Dao<ComicInfo,Integer> getComicDao()  {
        try {
            if (dao == null) {
                dao = super.getDao(ComicInfo.class);
            }
        }
        catch (Exception e){

        }
        return dao;
    }

    @Override
    public void close(){
        super.close();
        MyLogger.ddLog(TAG).i("--ormlite:--close:");
        dao = null;
    }
}
