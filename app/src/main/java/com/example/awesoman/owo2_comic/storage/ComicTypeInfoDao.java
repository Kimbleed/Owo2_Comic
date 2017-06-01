package com.example.awesoman.owo2_comic.storage;

import android.content.Context;

import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.example.awesoman.owo2_comic.model.ComicTypeInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Awesome on 2017/6/1.
 */

public class ComicTypeInfoDao {
    private DaoHelper mDaoHelper;
    private Dao<ComicTypeInfo,Integer> comicTypeDao;

    public ComicTypeInfoDao(Context context) {
        mDaoHelper = DaoHelper.getDaoHelp(context);
        try {
            comicTypeDao = mDaoHelper.getDao(ComicTypeInfo.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ComicTypeInfo getById(int comicTypeNo){
        try {
            List<ComicTypeInfo> listType = comicTypeDao.queryForEq("comic_type_no",comicTypeNo);
            if(listType!=null && listType.size()>0){
                return listType.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ComicTypeInfo getByName(String comicTypeName){
        try {
            List<ComicTypeInfo> listType = comicTypeDao.queryForEq("comic_type_name",comicTypeName);
            if(listType!=null && listType.size()>0){
                return listType.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void add(ComicTypeInfo typeInfo){
        if(getById(typeInfo.getComicTypeNo())==null) {
            try {
                comicTypeDao.create(typeInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void delete(ComicTypeInfo typeInfo){
        try {
            comicTypeDao.delete(typeInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ComicTypeInfo typeInfo) {
        try {
            comicTypeDao.update(typeInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ComicTypeInfo> queryForAll(){
        try {
            return comicTypeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
