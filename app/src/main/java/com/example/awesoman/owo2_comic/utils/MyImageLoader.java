package com.example.awesoman.owo2_comic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Awesome on 2017/3/18.
 */

public class MyImageLoader {

    private static MyImageLoader imageLoader ;

    private MyImageLoader(){

    }

    public static MyImageLoader getInstance(){
        if(imageLoader==null)
            imageLoader = new MyImageLoader();
        return imageLoader;
    }

    private List<String> comicPicPathList = new ArrayList<>();
    private Map<String,SoftReference<Bitmap>> imageCache = new HashMap<String,SoftReference<Bitmap>>();
    //加入单张Bitmap
    public void addBitmapToCache(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 32;
        //强引用Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        if(bitmap!=null)
            LogUtil.i("addBitmapToCache",bitmap.getByteCount()+"");
        //软引用Bitmap对象
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        imageCache.put(path,softBitmap);
    }

    //取出单张Bitmap
    public Bitmap getBitmapByPath(String path){
        //从缓存中取软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap =imageCache.get(path);
        if(softBitmap == null) {
            LogUtil.i("getBitmapByPath","null");
            return null;
        }
        LogUtil.i("getBitmapByPath",path);
        Bitmap bitmap = softBitmap.get();
        return bitmap;
    }

    //按照List<String> comicPicPathList 加入Bitmap
    public void addBitmapsToCache(List<String> paths){
        for(String path:paths) {
            LogUtil.i("addBitmapsToCache",path);
            addBitmapToCache(path);
        }
    }

}
