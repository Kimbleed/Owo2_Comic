package com.example.awesoman.owo2_comic.ui.Music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.view.MusicPlayingView;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.ui.MainActivity;
import com.example.awesoman.owo2_comic.sqlite.ComicEntry;
import com.example.awesoman.owo2_comic.utils.SkipUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Awesome on 2017/2/23.
 * 音乐Fragment
 */

public class MusicFragment extends Fragment implements MusicAdapter.IMusicItem,View.OnClickListener {

    /**  - - - - - - - - -    控件部分 开始  - - - - - - - - -   */
    @Bind(R.id.musicLV)
    ListView musicLV;
    @Bind(R.id.backBtn)
    ImageView backBtn;
    @Bind(R.id.titleTxt)
    TextView titleTxt;
    @Bind(R.id.rightLinear)
    LinearLayout rightLinear;

    MusicPlayingView mpv;
    /**  - - - - - - - - -    控件部分 结束  - - - - - - - - -   */




    /**  - - - - - - - - -    变量部分 开始  - - - - - - - - -   */
    //musicLV 的 adapter
    MusicAdapter adapter;

    //音乐名List
    ArrayList<String> musicList = null;

    //文件管理类
    private FileManager fileManager = null;

    MusicService.PlayerBinder binder =null;
    /**  - - - - - - - - -    变量部分 结束  - - - - - - - - -   */



    /**  - - - - - - - - -    常量部分 开始  - - - - - - - - -   */
    public static final int HANDLER_MUSIC_START_ICON = 1005;
    /**  - - - - - - - - -    常量部分 结束  - - - - - - - - -   */

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = ((MusicService.PlayerBinder) service);
            binder.setMusicList(musicList);
            binder.setFragment(MusicFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**  - - - - - - - - -    变量部分 结束  - - - - - - - - -   */

    /**  - - - - - - - - -    常量部分 开始  - - - - - - - - -   */
    /**  - - - - - - - - -    常量部分 结束  - - - - - - - - -   */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music,null);
        ButterKnife.bind(this,view);

        //获取音乐资源list
        fileManager = FileManager.getInstance();
        musicList = fileManager.getNameListFromFile(ComicEntry.getMusicPath());
        adapter = new MusicAdapter(getContext());
        adapter.setData(musicList);
        adapter.setListener(this);
        musicLV.setAdapter(adapter);


        backBtn.setVisibility(View.GONE);
        titleTxt.setText("我的音乐");
        titleTxt.setTextColor(getResources().getColor(R.color.white));
        mpv = new MusicPlayingView(getContext());
        mpv.setColor(getResources().getColor(R.color.white));
        rightLinear.addView(mpv);
        rightLinear.setOnClickListener(this);

        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

        return view;
    }

    @Override
    public void onMusicItemClick(final int index) {
            binder.playMusic(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(binder !=null)
            getActivity().unbindService(conn);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.rightLinear:
                Intent intent = new Intent();
                intent.setClass(getContext(),MusicActivity.class);
                intent.putStringArrayListExtra("musicList",musicList);
                SkipUtil.skip(getContext(),intent,false);
                ((MainActivity)getContext()).jumpAnimation(2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(binder !=null) {
            if (binder.isPlaying())
                mpv.startAnim();
            else
                mpv.stopAnim();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_MUSIC_START_ICON:
                    boolean isPlaying= (boolean)msg.obj;
                    if(isPlaying)
                        mpv.startAnim();
                    else
                        mpv.stopAnim();
                    break;
            }
        }
    };

    public Handler getHandler() {
        return handler;
    }
}
