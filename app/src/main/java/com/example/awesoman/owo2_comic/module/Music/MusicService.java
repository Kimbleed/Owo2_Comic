package com.example.awesoman.owo2_comic.module.Music;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.awesoman.owo2_comic.sqlite.ComicEntry;

import java.io.File;
import java.util.List;


/**
 * Created by Awesome on 2017/2/23.
 * 音乐服务Service
 */

public class MusicService extends Service {

    private MediaPlayer mediaPlayer = null;

    private PlayerBinder binder;

    private List<String> data = null;

    private int currentIndex = 0;

    private boolean isPause = true;

    private int playMode = 0;



    private int startIndex = -1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        binder = new PlayerBinder();
        mediaPlayer = new MediaPlayer();

    }

    public class PlayerBinder extends Binder {

        private Context CTX;

        private Fragment fragment;

        private int currentTime = 0;

        public PlayerBinder() {
            Thread thread = new Thread(){
                @Override
                public void run() {
                    for(;;) {
                        while (binder.isPlaying()) {
                            try {
                                if(CTX!=null) {
                                    Message msg = new Message();
                                    msg.what = MusicActivity.HANDLER_SET_CURRENT_TIME;
                                    ((MusicActivity) CTX).getHandler().sendMessage(msg);
                                }
                                Thread.sleep(1000);
                                currentTime += 1000;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            thread.start();
        }

        public void setCTX(Context CTX) {
            this.CTX = CTX;
        }

        public void setFragment(Fragment fragment){
            this.fragment = fragment;
        }
        /**
         * 点击item播放音乐
         *为MusicFragment而设置
         *
         * @param index 歌曲下标
         */
        public void playMusic(int index) {
            //设置service中 记录当前歌曲的int 变量
            currentIndex = index;
            //放歌
            if (!isPause) {
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(ComicEntry.MUSIC_PATH + File.separator + data.get(index));
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            isPause = false;
            mediaPlayer.start();
            Message msg = new Message();
            msg.what = MusicFragment.HANDLER_MUSIC_START_ICON;
            msg.obj = true;
//            ((MusicFragment)fragment).getHandler().sendMessage(msg);
//            Message msgUpdateCurrentTime = new Message();
//            msgUpdateCurrentTime.what = MusicActivity.HANDLER_SET_CURRENT_TIME;
//            LogUtil.i("playMusic","sendMessageDelayed");
//            ((MusicActivity)CTX).getHandler().sendMessageDelayed(msgUpdateCurrentTime,1000);
        }

        /**
         * 为MusicActivity设置
         */
        public void prepareMusic(){
            try {
                if(currentTime <=0) {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(ComicEntry.MUSIC_PATH + File.separator + data.get(currentIndex));
                    mediaPlayer.prepare();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }finally {
                setDuration();
            }
        }


        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPause = true;
                Message msg = new Message();
                msg.what = MusicFragment.HANDLER_MUSIC_START_ICON;
                msg.obj = false;
                ((MusicFragment)fragment).getHandler().sendMessage(msg);
            }
        }

        /**
         * 是否正在播放
         */
        public boolean isPlaying() {
            if (mediaPlayer == null)
                return false;
            return mediaPlayer.isPlaying();
        }

        /**
         * 播放 上/下一首/当前一首
         * 所有情况均需要reset
         *
         * @param flag 0:下一首
         *             1: 上一首
         *             2:当前这首
         */
        public int playForMode(int flag) {
            if (flag == 0) {
                if (currentIndex < (data.size() - 1))
                    currentIndex++;
                else {
                    currentIndex = 0;
                }
            } else if (flag == 1) {
                if (currentIndex <= 0)
                    currentIndex = (data.size() - 1);
                else
                    currentIndex--;
            }
            try {
                mediaPlayer.reset();
                currentTime = 0;
                mediaPlayer.setDataSource(ComicEntry.MUSIC_PATH + File.separator + data.get(currentIndex));
                mediaPlayer.prepare();
                if (!isPause) {
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setTitleTxt(data.get(currentIndex));
            setDuration();
            return currentIndex;
        }

        /**
         * 移动时间
         */
        public void seekTo(int time){
            mediaPlayer.seekTo(time);
            currentTime = time;
            Message msg = new Message();
            msg.what = MusicActivity.HANDLER_SET_CURRENT_TIME;
            ((MusicActivity) CTX).getHandler().sendMessage(msg);
        }

        /**
         * 设置musicList  数据
         *
         * @param musicList
         */
        public void setMusicList(List<String> musicList) {
            data = musicList;
        }


        /**
         * 根据musicName 找 当前index
         *
         * @param musicName
         * @return
         */
        public int searchMusic(String musicName) {
            for (int i = 0; i < data.size(); i++) {
                if (musicName.equals(data.get(i))) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 获取当前播放index
         */
        public int getCurrentIndex() {
            return currentIndex;
        }

        /**
         * 设置播放模式
         * 0.顺序循环播放
         * 1.顺序单次播放
         * 2.单曲循环播放
         * 3.随机播放
         */
        public void setPlayMode(final int playMode) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    switch (playMode) {
                        case 0:
                            playForMode(0);
                            break;
                        case 1:
                            if ((currentIndex + 1) != startIndex)
                                playForMode(0);
                            break;
                        case 2:
                            playForMode(2);
                            break;
                        case 3:
                            int random = (int) Math.random() * (data.size() - 1);
                            playMusic(random);
                            break;
                    }
                }
            });
        }

        public void setTitleTxt(String musicName) {
            if (CTX instanceof MusicActivity) {
                Message msg = new Message();
                msg.what = MusicActivity.HANDLER_SET_TITLE;
                msg.obj = musicName;
                ((MusicActivity) CTX).getHandler().sendMessage(msg);
            }
        }

        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        public void setDuration() {
            if (CTX instanceof MusicActivity) {
                Message msg = new Message();
                msg.what = MusicActivity.HANDLER_SET_DURATION;
                msg.obj = getDuration();
                ((MusicActivity) CTX).getHandler().sendMessage(msg);

                Message msg2 = new Message();
                msg2.what = MusicActivity.HANDLER_SET_CURRENT_TIME;
                ((MusicActivity) CTX).getHandler().sendMessage(msg2);
            }
        }


        public int getCurrentTime(){
            return currentTime;
        }
        public void setCurrentTime(int time){
            currentTime = time;
        }
    }
}
