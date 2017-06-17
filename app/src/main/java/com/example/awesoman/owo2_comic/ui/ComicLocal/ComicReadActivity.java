package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.awesoman.owo2_comic.model.ComicHistoryInfo;
import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.example.awesoman.owo2_comic.model.MusicTriggerInfo;
import com.example.awesoman.owo2_comic.storage.ComicEntry;
import com.example.awesoman.owo2_comic.storage.ComicHistoryDao;
import com.example.awesoman.owo2_comic.storage.MusicTriggerDao;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicReadRVAdapter;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.utils.FileUtils;
import com.example.awesoman.owo2_comic.view.ReadComicViewPager;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicReadVPAdapter;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Awesome on 2016/11/11.
 * 漫画阅读Activity
 */

public class ComicReadActivity extends BaseActivity
        implements ReadComicViewPager.IMyViewPager, View.OnClickListener ,ComicReadRVAdapter.IComicReadRVClick{
    @Bind(R.id.vp_read)
    ReadComicViewPager vp_read;
    @Bind(R.id.rv_read)
    RecyclerView rv_read;
    @Bind(R.id.moreSelectLinear)
    LinearLayout moreSelectLinear;
    @Bind(R.id.titleLinear)
    LinearLayout titleLinear;
    @Bind(R.id.backBtn)
    ImageView backBtn;
    @Bind(R.id.titleTxt)
    TextView titleTxt;
    @Bind(R.id.tv_read_mode)
    TextView tv_read_mode;
    @Bind(R.id.tv_read_direction)
    TextView tv_read_direction;


    @Bind(R.id.changeLinear)
    LinearLayout changeLinear;

    RecyclerView recycler_music;



    Dialog dialog;

    private List<String> pages = null;
    float titleStartY, titleEndY, selectStartY, selectEndY;

    private ComicReadVPAdapter comicReadVPAdapter;
    private ComicReadRVAdapter comicReadRVAdapter;
    private MusicTriggrtAdapter musicTriggrtAdapter;
    private String chapterPath = null;
    private String chapter;
    private ComicInfo mComicInfo ;
    private boolean isInVisible;
    private long ctm;
    private List<String> musicList;
    private boolean[]  musicChose;
    private FileManager fileManager;


    public static final int READ_MODE_PAGING_RIGHT = 1;
    public static final int READ_MODE_PAGING_LEFT = 2;
    public static final int READ_MODE_ROLL = 3;

    private boolean isShu = true;

    private ComicHistoryDao comicHistoryDao;
    private  MusicTriggerDao mMusicTriggerDao;

    @Override
    public int getContentViewID() {
        return R.layout.activity_comic_read;
    }

    @Override
    public void initView() {

        comicHistoryDao = new ComicHistoryDao(this);
        mMusicTriggerDao = new MusicTriggerDao(this);
        Bundle bundle = getIntent().getExtras();
        String path = bundle.getString("path");
        chapter = bundle.getString("chapter");
        mComicInfo = (ComicInfo)bundle.get("comic_info");
        fileManager = FileManager.getInstance();
        musicList = fileManager.getNameListFromFile(ComicEntry.getMusicPath(),false);
        musicChose = new boolean[musicList.size()];


        ////右翻页    阅读模式---------------开始
        comicReadVPAdapter = new ComicReadVPAdapter(this);

        chapterPath = path + File.separator + chapter;
        comicReadVPAdapter.setChapterPath(chapterPath);

        pages = FileManager.getInstance().getComicPhotoList(path, chapter);
        for(int i  = 0;i<pages.size();i++){
            if(!FileUtils.judgePhoto(pages.get(i))){
                pages.remove(i);
            }
        }
        comicReadVPAdapter.setPages(pages);

        comicReadVPAdapter.setComicReadVPListener(new ComicReadVPAdapter.IComicReadVPListener() {
            @Override
            public void doubleClick(final boolean isBig) {
//                Log.i("ComicReadActivity","IComicReadVPListener");
                Log.i("CEN", "DoubleClick  >> HANDLER_SECOND_CLICK");
                if (isBig) {
                    vp_read.setNoScroll(isBig);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vp_read.setNoScroll(isBig);
                        }
                    }, 100);
                }
                vp_read.mClickHandler.sendEmptyMessage(ReadComicViewPager.HANDLER_SECOND_CLICK);
            }

            @Override
            public void pageOn(int pageIndex) {
//                rememberHistory(pageIndex);
            }
        });

        vp_read.setAdapter(comicReadVPAdapter);
        vp_read.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                rememberHistory(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_read.setListener(this);
        //右翻页    阅读模式---------------结束


        //上下滚动  阅读模式---------------开始
        //线性布局 垂直方向
        RecyclerView.LayoutManager lm = new LinearLayoutManager(ComicReadActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_read.setLayoutManager(lm);
        //初始化ComicReadRVAdapter
        comicReadRVAdapter = new ComicReadRVAdapter(ComicReadActivity.this);
        comicReadRVAdapter.setChapterPath(chapterPath);
        comicReadRVAdapter.setPages(pages);
        comicReadRVAdapter.setListener(this);
        rv_read.setAdapter(comicReadRVAdapter);
        ComicHistoryInfo historyInfo = comicHistoryDao.get(mComicInfo.getComicName());
        if(historyInfo!=null && historyInfo.getComicChapter().equals(chapter))
            vp_read.setCurrentItem(historyInfo.getComicPage());

        //上下滚动  阅读模式---------------结束

        backBtn.setOnClickListener(this);
        titleTxt.setText(chapter);

        rv_read.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backBtn:
                finish();
                break;
        }
    }

    @Override
    public void onComicReadRvItemClick() {
        showOrHideMoreSelect();
    }

    @Override
    public void clickOnViewPager(int part) {
        if (part == 2)
            vp_read.setCurrentItem(vp_read.getCurrentItem() + 1);
        else if (part == 1) {
            showOrHideMoreSelect();
        } else {
            if (vp_read.getCurrentItem() != 0)
                vp_read.setCurrentItem(vp_read.getCurrentItem() - 1);
        }
    }

    public void initMoreSelect() {
        if (titleStartY >= 0) {
            titleStartY = titleLinear.getY() - titleLinear.getHeight();
            titleEndY = titleLinear.getY();

            selectStartY = moreSelectLinear.getY() + moreSelectLinear.getHeight();
            selectEndY = moreSelectLinear.getY();
        }
    }

    @OnClick(R.id.changeLinear)
    public void showReadModeDialog() {
        if(rv_read.getVisibility() == View.INVISIBLE || rv_read.getVisibility() == View.GONE) {
            tv_read_mode.setText("左右翻页");
            changeReadMode(READ_MODE_ROLL, comicReadVPAdapter.getPageIndex());
        }
        else if(vp_read.getVisibility() == View.INVISIBLE || vp_read.getVisibility() == View.GONE){
            tv_read_mode.setText("上下滚屏");
            changeReadMode(READ_MODE_PAGING_RIGHT, comicReadRVAdapter.getPageIndex());
        }
    }

    @OnClick(R.id.saveLinear)
    public void savePict(){

    }
    @OnClick(R.id.settingLinear)
    public void setting(){
        if(isShu) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
            tv_read_direction.setText("竖屏阅读");
            isShu = false;

        }
        else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            tv_read_direction.setText("横屏阅读");
            isShu = true;
        }
    }

    @OnClick(R.id.musicLinear)
    public void music(){
        showSetMusicDialog();
    }

    public void showOrHideMoreSelect() {
        //title框动画
        ValueAnimator showTitleAni = null;
        //select框动画
        ValueAnimator showSelectAni = null;

        initMoreSelect();

        if (isInVisible) {
            showTitleAni = ObjectAnimator.ofFloat(titleLinear, "y", titleEndY, titleStartY);
            showSelectAni = ObjectAnimator.ofFloat(moreSelectLinear, "y", selectEndY, selectStartY);
        } else {
            showTitleAni = ObjectAnimator.ofFloat(titleLinear, "y", titleStartY, titleEndY);
            showSelectAni = ObjectAnimator.ofFloat(moreSelectLinear, "y", selectStartY, selectEndY);
        }
        //title框动画设置
        showTitleAni.setDuration(200);
        showTitleAni.setInterpolator(new AccelerateInterpolator());

        //select框动画设置
        showSelectAni.setDuration(200);
        showSelectAni.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(showTitleAni).with(showSelectAni);

        if (isInVisible) {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisible(false);
                    isInVisible = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisible(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isInVisible = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


        set.start();

    }

    /**
     * @param flag true可见
     *             false不可见
     */
    public void setVisible(boolean flag) {
        if (flag) {
            moreSelectLinear.setVisibility(View.VISIBLE);
            titleLinear.setVisibility(View.VISIBLE);
        } else {
            moreSelectLinear.setVisibility(View.INVISIBLE);
            titleLinear.setVisibility(View.INVISIBLE);
        }
    }

    public void changeReadMode(int mode, final int pageIndex) {
        if (mode == READ_MODE_PAGING_RIGHT) {
            vp_read.setCurrentItem(pageIndex-1);
            rv_read.setVisibility(View.GONE);
            vp_read.setVisibility(View.VISIBLE);
            showOrHideMoreSelect();
        } else if (mode == READ_MODE_PAGING_LEFT) {

        } else if (mode == READ_MODE_ROLL) {
            Log.i("CEN","pageIndexFrom VP"+pageIndex);
            Log.i("CEN","sizeFrom VP"+comicReadVPAdapter.getPages().size());
            Log.i("CEN","sizeFrom RV"+comicReadRVAdapter.getPages().size());
            //位置貌似不是index  而是 顺位
            ((LinearLayoutManager) rv_read.getLayoutManager()).scrollToPositionWithOffset(pageIndex-1, 0);
            rv_read.setVisibility(View.VISIBLE);
            vp_read.setVisibility(View.GONE);
            showOrHideMoreSelect();
        }
    }

    public void rememberHistory(int page){
        ComicHistoryInfo historyInfo = comicHistoryDao.get(mComicInfo.getComicName());
        if(historyInfo !=null){
            historyInfo.setComicChapter(chapter);
            historyInfo.setComicPage(page);
            comicHistoryDao.update(historyInfo);
        }
        else{
            historyInfo = new ComicHistoryInfo();
            historyInfo.setComicChapter(chapter);
            historyInfo.setComicPage(page);
            historyInfo.setComicName(mComicInfo.getComicName());
            comicHistoryDao.add(historyInfo);
        }
    }

    public void showSetMusicDialog(){
        dialog = new Dialog(this, R.style.customDialog);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_music_list, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        Button btnOk = (Button) layout.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);

        recycler_music = (RecyclerView) layout.findViewById(R.id.recycler_music);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycler_music.setLayoutManager(gridLayoutManager);
        musicTriggrtAdapter = new MusicTriggrtAdapter();
        recycler_music.setAdapter(musicTriggrtAdapter);

        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btn_ok:
//                        fileManager.addComicType(addComicTypeEditTxt.getText().toString());
//                        comicTypeList = fileManager.getComicTypeFromDB();
//                        comicTypeAdapter.setmData(comicTypeList);
//                        comicTypeAdapter.notifyDataSetChanged();
//                        initAddComicInToTypeDialog(addComicTypeEditTxt.getText().toString());
//                        comicTypeRV.scro
                        int position = -1;
                        for(int i = 0;i<musicChose.length;i++){
                            if(musicChose[i]){
                                position = i;
                                break;
                            }
                        }
                        MusicTriggerInfo info = mMusicTriggerDao.get(musicList.get(position));
                        if(info == null){
                            mMusicTriggerDao.add(info);
                        }
                        else{
                            mMusicTriggerDao.update(info);
                        }
                        dialog.dismiss();
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };
        btnOk.setOnClickListener(dialogClickListener);
        btnCancel.setOnClickListener(dialogClickListener);

        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        dialog.onWindowAttributesChanged(lp);
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void setMusic(){

    }

    class MusicTriggrtAdapter extends RecyclerView.Adapter{
        public MusicTriggrtAdapter() {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MusicTriggerViewHolder viewHolder = new MusicTriggerViewHolder(LayoutInflater.from(ComicReadActivity.this).inflate(R.layout.item_music_list,null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((MusicTriggerViewHolder)holder).tv_music_path.setText(musicList.get(position));
            ((MusicTriggerViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MusicTriggerViewHolder)holder).tv_music_path.setTextColor(Color.WHITE);
                    ((MusicTriggerViewHolder)holder).itemView.setBackgroundResource(R.color.white);
                    for(int i = 0 ;i<musicChose.length;i++){
                        musicChose[i] = false;
                    }
                    musicChose[position]=true;
                    notifyDataSetChanged();
                }
            });
            if(musicChose[position]) {
                ((MusicTriggerViewHolder) holder).tv_music_path.setTextColor(Color.WHITE);
                ((MusicTriggerViewHolder) holder).ll.setBackgroundResource(R.color.red_600);
            }
            else{
                ((MusicTriggerViewHolder) holder).tv_music_path.setTextColor(ComicReadActivity.this.getResources().getColor(R.color.black_alpha_192));
                ((MusicTriggerViewHolder) holder).ll.setBackgroundResource(R.color.white);
            }

        }

        @Override
        public int getItemCount() {
            return musicList.size();
        }
    }
    class MusicTriggerViewHolder extends RecyclerView.ViewHolder{
        public MusicTriggerViewHolder(View itemView) {
            super(itemView);
            tv_music_path = (TextView) itemView.findViewById(R.id.tv_music_path);
            ll  = (LinearLayout)itemView.findViewById(R.id.ll_container);
        }
        public TextView tv_music_path;
        public LinearLayout ll ;
    }
}
