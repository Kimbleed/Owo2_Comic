package com.example.awesoman.owo2_comic.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.awesoman.owo2_comic.application.MyApplication;
import com.example.awesoman.owo2_comic.bean.ComicBean;
import com.example.awesoman.owo2_comic.sqlite.ComicEntry;
import com.example.awesoman.owo2_comic.sqlite.MySQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2017/2/20.
 * 文件管理器(类)
 */

public class FileManager {

    private Context context;

    private static FileManager fileManager;

    private FileManager(){
        context = MyApplication.getContext();
    }

    public static FileManager getInstance(){
        if(fileManager == null){
            fileManager = new FileManager();
        }
        return fileManager;
    }

    /**
     * 获取以该文件为根目录的漫画名(文件名)
     * return List<String>
     */
    public ArrayList<String> getNameListFromFile(String path){
        int length = 0;
        ArrayList<String> comicsInFile =new ArrayList<>();
        File comicsDir =new File(path);
        if(!comicsDir.exists()){
            comicsDir.mkdir();
        }
        File[] comicsArr = comicsDir.listFiles();
        if(comicsArr!=null)
            length = comicsArr.length;
        for(int i = 0;i<length;i++){
            comicsInFile.add(comicsArr[i].getName());
            LogUtil.i("cenjunhui",comicsArr[i].getName());
        }
        return comicsInFile;
    }

    /**
     * 获取数据库中所有漫画名
     * return List<String>
     */
    public List<String> getComicNameListFromDB(){
        List<String> comicsFromDB = new ArrayList<>();

        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(ComicEntry.COMIC_ALL_TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            do {
                comicsFromDB.add(
                        cursor.getString(
                                cursor.getColumnIndex(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_NAME)
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return comicsFromDB;
    }

    /**
     * 获取某一漫画的章节队列()
     * @param path 漫画文件夹路径
     * @return List<String>
     */
    public List<String> getChapterList(String path){
        File comicFile = new File(path);
        String[] listFileStr = comicFile.list();
        List<String> chapterListString = new ArrayList<>();
        for(int i = 0;i<listFileStr.length;i++){
            if(!listFileStr[i].contains("."))
                chapterListString.add(listFileStr[i]);
        }
        return chapterListString;
    }

    /**
     * 获取不存在于数据库中的漫画名List<String>
     * @return List<String>
     */
    public List<String> getComicNameListUnExist(){
        List<String> listFromDB = getComicNameListFromDB();
        List<String> listInFile = getNameListFromFile(ComicEntry.COMIC_PATH);
        List<String> listUnExist = getNameListFromFile(ComicEntry.COMIC_PATH);
        for(String comicNameDB:listFromDB){
            for(String comicNameFile:listInFile){
                if(comicNameDB.equals(comicNameFile)){
                    listUnExist.remove(getIndexForString(listUnExist,comicNameFile));
                }
            }
        }
        return listUnExist;
    }

    public boolean scanFile(){
        List<String> comicNameUnExistInDB = null;
        if((comicNameUnExistInDB = getComicNameListUnExist()).size()>0){
            updateComicDB(comicNameUnExistInDB);
        }
        return comicNameUnExistInDB.size()>0?true:false;
    }

    /**
     *通过string从List中获取index
     */
    public int getIndexForString(List<String> list,String findStr){
        int length = list.size();
        int index = -1;
        for(int i = 0;i<length;i++){
            if(list.get(i).equals(findStr)){
                index =  i;
            }
        }
        return index;
    }


    /**
     *更新数据库
     * @data 需要插入的漫画名List
     */
    public void updateComicDB(List<String> data){
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(context);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        for(int i =0;i<data.size();i++) {
            if(!data.get(i).equals("")) {
                ContentValues contentValues = new ContentValues();
                //漫画名colum
                contentValues.put(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_NAME, data.get(i));
                //漫画所在路径colum
                contentValues.put(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_PATH,ComicEntry.COMIC_PATH + File.separator + data.get(i));
                //漫画种类 colum  默认1
                contentValues.put(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE, "1");
                Log.i("contentValues", contentValues.toString());
                db.insert(ComicEntry.COMIC_ALL_TABLE_NAME, null, contentValues);
            }
        }
        db.close();
    }

    /**
     * 获取数据库中所有漫画种类
     *
     */
    public List<String> getComicTypeFromDB(){
        List<String > data = new ArrayList<>();
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

//        String sql = "select * from "+ ComicEntry.COMIC_TYPE_TABLE_NAME;
//        Cursor cursor =db.rawQuery(sql,null);
        Cursor cursor = db.query(ComicEntry.COMIC_TYPE_TABLE_NAME,null,null,null,null,null,null);
        if(cursor.getCount()>0) {

            cursor.moveToFirst();
            do {
                data.add(cursor.getString(cursor.getColumnIndex(ComicEntry.TYPE_COLUMNS_NAME_COMIC_NAME)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 从数据库中删除漫画种类
     */
    public void deleteComicType(String type){
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(ComicEntry.TYPE_COLUMNS_NAME_COMIC_NAME,type);
        db.delete(ComicEntry.COMIC_TYPE_TABLE_NAME,ComicEntry.TYPE_COLUMNS_NAME_COMIC_NAME+"=?",new String[]{type});
        db.close();
    }

    /**
     * 往数据库中添加漫画种类
     */
    public void addComicType(String type){
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(ComicEntry.TYPE_COLUMNS_NAME_COMIC_NAME,type);
        db.insert(ComicEntry.COMIC_TYPE_TABLE_NAME,null,value);
        db.close();
    }

    /**
     * 修改漫画String的种类
     */
    public void updateComicType(ComicBean entity, String type){
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE,type);
        db.update(ComicEntry.COMIC_ALL_TABLE_NAME,value,ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE,new String[]{entity.getComicName()});
    }

    /**
     * 修改漫画List的种类
     */
    public void updateComicType(List<ComicBean> comicList, String type){
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        for(ComicBean entity:comicList) {
            ContentValues value = new ContentValues();
            value.put(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE,type);
            db.update(ComicEntry.COMIC_ALL_TABLE_NAME,value,ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE,new String[]{entity.getComicName()});
        }
    }

    /**
     * 获取数据库中，某一种类漫画的全部漫画
     * @param type
     * @return
     */
    public List<ComicBean> getComicMenuFromDB(String type){
        List<ComicBean> data = new ArrayList<>();
        SQLiteOpenHelper sqLiteOpenHelper = new MySQLiteHelper(MyApplication.getContext());
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from "+ ComicEntry.COMIC_ALL_TABLE_NAME;
        if(type !=null && !type.equals("全部"))
            sql+=" where "+ ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE +" = \""+type+"\"";
        Cursor cursor =db.rawQuery(sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                Log.i("getComicMenu_cursor",cursor.getString(cursor.getColumnIndex(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_TYPE)));
                ComicBean comicBean =new ComicBean();
                comicBean.setComicName(cursor.getString(cursor.getColumnIndex(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_NAME)));
                comicBean.setComicPath(cursor.getString(cursor.getColumnIndex(ComicEntry.COMIC_ALL_COLUMNS_NAME_COMIC_PATH)));
                data.add(comicBean);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        for(int i = 0 ;i <data.size();i++){
            Log.i("getComicMenu_"+i,data.get(i).getComicName()+"|"+data.get(i).getComicPath());
        }
        return data;
    }



    /**获取一本漫画的封面
     *
     */
    public Bitmap getSurface(String path){
        Bitmap bitmap = null;

        File comicFile = new File(path);
        String[] listFileStr = comicFile.list();
        List<String> pictureListString = new ArrayList<>();
        if(listFileStr==null || listFileStr.length==0){
            return null;
        }
        for(int i = 0;i<listFileStr.length;i++){
            if(listFileStr[i].contains(".")){
                pictureListString.add(listFileStr[i]);
            }
        }

        if(pictureListString.size()<=0){
            return null;
        }

        if(pictureListString.size()>0) {
            for (String picture : pictureListString) {
                if (picture.equals("surface")) {
                    bitmap = BitmapFactory.decodeFile(path + File.separator + picture);
                    break;
                }
            }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(path + File.separator + pictureListString.get(0));
            }
        }

        return bitmap;
    }

    public Bitmap makeSurface(Bitmap bitmap,float newWidth,float newHeight){
        if(bitmap==null) {
            return null;
        }
//        Log.i("newHeight",newHeight+"");
        Bitmap bitmapCompress = PictureUtil.getInstance().compressImg(bitmap, newHeight);
        bitmapCompress = PictureUtil.getInstance().cripPicture(bitmapCompress,(int)newWidth);
        return bitmapCompress;
    }


    /**
     * 获取某一章节下的所有漫画图片路径
     */
    public List<String> getComicPhotoList(String path,String chapter){
        File chapterFile = new File(path,chapter);
        File[] comicPhotoListFile = chapterFile.listFiles();
        List<String> comicPhotoListString = new ArrayList<>();
        for(int i = 0;i<comicPhotoListFile.length;i++){
            comicPhotoListString.add(comicPhotoListFile[i].getName());
        }
        return comicPhotoListString;
    }



}
