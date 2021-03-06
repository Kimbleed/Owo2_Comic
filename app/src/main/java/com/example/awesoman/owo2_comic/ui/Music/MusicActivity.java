package com.example.awesoman.owo2_comic.ui.Music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.view.DiscView;
import com.example.awesoman.owo2_comic.view.DragProgressBar;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.utils.DateUtils;
import com.example.awesoman.owo2_comic.utils.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Awesome on 2017/3/3.
 * 音乐播放Activity
 */

public class MusicActivity extends BaseActivity
        implements View.OnClickListener, DragProgressBar.IPlayerProgressBar {


    @Bind(R.id.musicStartBtn)
    ImageView musicStartBtn;
    @Bind(R.id.musicNextBtn)
    ImageView musicNextBtn;
    @Bind(R.id.musicPrevBtn)
    ImageView musicPrevBtn;
    @Bind(R.id.titleTxt)
    TextView titleTxt;
    @Bind(R.id.musicCh)
    TextView musicCh;
    @Bind(R.id.durationTxt)
    TextView durationTxt;
    @Bind(R.id.dragProgressBar)
    DragProgressBar dragProgressBar;
    @Bind(R.id.backBtn)
    ImageView backBtn;
    @Bind(R.id.discview)
    DiscView discView;

    private int duration = 0;

    private ArrayList<String> musicList = null;

    private MusicService.PlayerBinder binder = null;

    public static final int HANDLER_SET_TITLE = 1001;

    public static final int HANDLER_SET_DURATION = 1002;

    public static final int HANDLER_SET_CURRENT_TIME = 1003;


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i("onServiceConnected", service == null ? "null" : "noNull");
            binder = ((MusicService.PlayerBinder) service);
            binder.setMusicList(musicList);
            titleTxt.setText(musicList.get(binder.getCurrentIndex()));
            binder.setCTX(MusicActivity.this);
            if (binder.isPlaying()) {
                musicStartBtn.setBackground(getResources().getDrawable(R.drawable.music_stop));
            } else {
                binder.prepareMusic();
                musicStartBtn.setBackground(getResources().getDrawable(R.drawable.music_start));
            }
            binder.setDuration();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public int getContentViewID() {
        return R.layout.activity_music;
    }

    @Override
    public void initView() {
        initIntent();
        musicStartBtn.setOnClickListener(this);
        musicNextBtn.setOnClickListener(this);
        musicPrevBtn.setOnClickListener(this);
        dragProgressBar.setListener(this);
        backBtn.setOnClickListener(this);
    }

    public void initIntent() {
        Intent intentReceive = getIntent();
        musicList = intentReceive.getExtras().getStringArrayList("musicList");
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.musicStartBtn:
                if (binder.isPlaying()) {
                    musicStartBtn.setBackground(getResources().getDrawable(R.drawable.music_start));
//                    binder.pauseMusic();
                    sendMusicBroadcastMehtod("pause");
                } else {
                    musicStartBtn.setBackground(getResources().getDrawable(R.drawable.music_stop));
                    binder.playMusic(binder.getCurrentIndex());
                    RotateAnimation animation = new RotateAnimation(0,360,discView.getWidth()/2,discView.getHeight()/2);
                    animation.setDuration(20000);
                    animation.setRepeatCount(100);
                    animation.setInterpolator(new LinearInterpolator());
                    discView.setAnimation(animation);
                    animation.start();
//                    sendMusicBroadcastMehtod("pause");
                }
                break;
            case R.id.musicNextBtn:
//                binder.playForMode(0);
                sendMusicBroadcastMehtod("next");
                break;
            case R.id.musicPrevBtn:
//                binder.playForMode(1);
                sendMusicBroadcastMehtod("prev");
                break;
            case R.id.backBtn:
                finish();
        }
    }


    /**
     * 发广播触发服务动作
     * @param method
     *  1.continue
     *  2.pause
     *  3.next
     *  4.prev
     */
    public void sendMusicBroadcastMehtod(String method){
        Intent broadcastIntent = new Intent(getPackageName() + MusicService.BROADCAST_ACTION);
        broadcastIntent.putExtra("method",method);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDown(float progressCurrent) {

    }

    @Override
    public void onMove(float progressCurrent) {
        LogUtil.i("onMove", progressCurrent + "");
        int currentTime = (int) (progressCurrent * duration);
        musicCh.setText(DateUtils.getMinuteDateFormat(currentTime));
    }

    @Override
    public void onUp(float progressCurrent) {
        int currentTime = (int) (progressCurrent * duration);
        binder.seekTo(currentTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binder != null)
            unbindService(conn);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HANDLER_SET_TITLE:
                    String musicName = (String) msg.obj;
                    titleTxt.setText(musicName);
                    break;
                case HANDLER_SET_DURATION:
                    duration = (int) msg.obj;
                    durationTxt.setText(DateUtils.getMinuteDateFormat(duration));
                    break;
                case HANDLER_SET_CURRENT_TIME:
                    LogUtil.i("duration", duration + "");
                    musicCh.setText(DateUtils.getMinuteDateFormat(binder.getCurrentTime()));
                    float percent = (float) binder.getCurrentTime() / duration;
                    LogUtil.i("percent", percent + "");
                    dragProgressBar.setXPoint(percent);
                    break;
            }
        }
    };

    public Handler getHandler() {
        return handler;
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
