package com.example.awesoman.owo2_comic.storage;

import android.content.Context;

import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2017/5/31.
 */

public class ComicInfoDao {
    private DaoHelper mDaoHelper;
    private Dao<ComicInfo,Integer> comicDao;

    public ComicInfoDao(Context context) {
        try {
            mDaoHelper = DaoHelper.getDaoHelp(context);
            comicDao = mDaoHelper.getDao(ComicInfo.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public ComicInfo get(String name){
        try {
            List<ComicInfo> list = comicDao.queryForEq("comic_name",name);
            if(list!=null && list.size()>0){
                return list.get(0);

            }
        }
        catch (Exception e){

        }
        return null;
    }

    public List<ComicInfo> queryForAll(){
        List<ComicInfo> list = new ArrayList<>();
        try {
            list = comicDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void add(ComicInfo info){
        if(get(info.getComicName())==null) {
            try {
                comicDao.create(info);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addList(List<ComicInfo> listAdd){
        List<ComicInfo> listDB = queryForAll();
        for(ComicInfo infoAdd :listAdd){
            boolean isExist = false;
            for(ComicInfo infoDB : listDB){
                if(infoAdd.getComicName().equals(infoDB.getComicName())){
                    isExist = true;
                }
            }
            if(!isExist){
                try {
                    comicDao.create(infoAdd);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(ComicInfo info){
            try {
                comicDao.update(info);
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }

    public boolean delete(ComicInfo info ){
        try {
            ComicInfo infoDelete = get(info.getComicName());
            if(infoDelete!=null) {
                comicDao.delete(info);
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
