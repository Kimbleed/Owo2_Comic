package com.example.awesoman.owo2_comic.storage;

import android.content.Context;

import com.example.awesoman.owo2_comic.model.ComicHistoryInfo;
import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Awesome on 2017/6/1.
 */

public class ComicHistoryDao {
    private DaoHelper mDaoHelper;
    private Dao<ComicHistoryInfo,Integer> mComicHistoryDao;

    public ComicHistoryDao(Context context) {
        mDaoHelper = DaoHelper.getDaoHelp(context);
        try {
            mComicHistoryDao = mDaoHelper.getDao(ComicHistoryInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ComicHistoryInfo get(String name){
        try {
            List<ComicHistoryInfo> historyInfoList = mComicHistoryDao.queryForEq("comic_name",name);
            if(historyInfoList!=null && historyInfoList.size()>0){
                return historyInfoList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(ComicHistoryInfo historyInfo){
        try {
            mComicHistoryDao.create(historyInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ComicHistoryInfo historyInfo){
        try {
            mComicHistoryDao.update(historyInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ComicHistoryInfo historyInfo){
        try {
            mComicHistoryDao.delete(historyInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
