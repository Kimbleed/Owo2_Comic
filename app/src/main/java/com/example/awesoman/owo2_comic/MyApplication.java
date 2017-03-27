package com.example.awesoman.owo2_comic;

import android.app.Application;
import android.content.Context;

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
        loadMusicFile();
    }

    public static Context getContext(){
        return context;
    }

    public void loadMusicFile(){
        FileUtils.copyFile(getResources().openRawResource(R.raw.someone_like_you),"someone_like_you.mp3", ComicEntry.MUSIC_PATH);
        FileUtils.copyFile(getResources().openRawResource(R.raw.yui_how_crazy_2),"yui_how_crazy_2.mp3", ComicEntry.MUSIC_PATH);
    }
}
