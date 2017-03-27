package com.example.awesoman.owo2_comic.module.Music;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by Awesome on 2017/3/5.
 * 音乐ServiceConnection
 */

public class MusicServiceConnection implements ServiceConnection {

    private int musicIndex;

    public MusicServiceConnection(int musicIndex) {
        this.musicIndex = musicIndex;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
