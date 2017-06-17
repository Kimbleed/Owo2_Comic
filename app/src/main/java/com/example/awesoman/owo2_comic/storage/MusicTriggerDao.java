package com.example.awesoman.owo2_comic.storage;

import android.content.Context;

import com.example.awesoman.owo2_comic.model.ComicTypeInfo;
import com.example.awesoman.owo2_comic.model.MusicTriggerInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Awesome on 2017/6/12.
 */

public class MusicTriggerDao {
    private DaoHelper mDaoHelper;
    private Dao<MusicTriggerInfo, Integer> musicTriggerDao;

    public MusicTriggerDao(Context context) {
        mDaoHelper = DaoHelper.getDaoHelp(context);
        try {
            musicTriggerDao = mDaoHelper.getDao(MusicTriggerInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MusicTriggerInfo> queryForAll(){
        try {
            return musicTriggerDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MusicTriggerInfo get(String musicPath){
        List<MusicTriggerInfo> data = queryForAll();
        for(MusicTriggerInfo info:data){
            if(info.getMusicPath().equals(musicPath))
                return info;
        }
        return null;
    }

    public void add(MusicTriggerInfo info){
        try {
            musicTriggerDao.create(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(MusicTriggerInfo info){
        try {
            musicTriggerDao.update(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(MusicTriggerInfo info){
        try {
            musicTriggerDao.delete(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
