package com.example.awesoman.owo2_comic.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.sqlite.ComicEntry;
import com.example.awesoman.owo2_comic.utils.FileUtils;

import java.io.File;

/**
 * Created by Awesome on 2017/2/20.
 * Application
 */

public class MyApplication extends Application {

    private static Context context;
    protected FileManager fileManager;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        fileManager = FileManager.getInstance();
        initDir();

        loadMusicFile();
    }

    public static Context getContext(){
        return context;
    }

    public void initDir(){
        File fileProject = new File(ComicEntry.getProjectPath());
        File fileMusic = new File(  ComicEntry.getMusicPath());
        File fileComic = new File(ComicEntry.getComicPath());
        Log.i("mkdirs","start");
        Log.i("mkdirs",fileProject.getAbsolutePath()+"|exists "+fileProject.exists());
        Log.i("mkdirs",fileMusic.getAbsolutePath()+"|exists "+fileMusic.exists());
        Log.i("mkdirs",fileComic.getAbsolutePath()+"|exists "+fileComic.exists());

        if (!fileProject.exists()) {
            Log.i("mkdirs",fileProject.getAbsolutePath()+"|mkdirs "+fileProject.mkdirs());
        }
        if(!fileMusic.exists()) {
            Log.i("mkdirs", fileMusic.getAbsolutePath() + "|mkdirs " + fileMusic.mkdirs());
        }
        if(!fileComic.exists()){
            Log.i("mkdirs",fileComic.getAbsolutePath()+"|mkdirs "+fileComic.mkdirs());
        }
    }

    public void loadMusicFile(){
        FileUtils.copyFile(getResources().openRawResource(R.raw.someone_like_you),"someone_like_you.mp3", ComicEntry.getMusicPath());
        FileUtils.copyFile(getResources().openRawResource(R.raw.yui_how_crazy_2),"yui_how_crazy_2.mp3", ComicEntry.getMusicPath());
    }
}
