package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.awesoman.owo2_comic.model.ComicHistoryInfo;
import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.example.awesoman.owo2_comic.storage.ComicHistoryDao;
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
        implements ReadComicViewPager.IMyViewPager, View.OnClickListener {
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


    @Bind(R.id.changeLinear)
    LinearLayout changeLinear;

    private List<String> pages = null;
    float titleStartY, titleEndY, selectStartY, selectEndY;

    private ComicReadVPAdapter comicReadVPAdapter;
    private ComicReadRVAdapter comicReadRVAdapter;
    private String chapterPath = null;
    private String chapter;
    private ComicInfo mComicInfo ;
    private boolean isInVisible;

    public static final int READ_MODE_PAGING_RIGHT = 1;
    public static final int READ_MODE_PAGING_LEFT = 2;
    public static final int READ_MODE_ROLL = 3;

    private ComicHistoryDao comicHistoryDao;

    @Override
    public int getContentViewID() {
        return R.layout.activity_comic_read;
    }

    @Override
    public void initView() {
        comicHistoryDao = new ComicHistoryDao(this);
        Bundle bundle = getIntent().getExtras();
        String path = bundle.getString("path");
        chapter = bundle.getString("chapter");
        mComicInfo = (ComicInfo)bundle.get("comic_info");

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
        rv_read.setAdapter(comicReadRVAdapter);
        ComicHistoryInfo historyInfo = comicHistoryDao.get(mComicInfo.getComicName());
        if(historyInfo!=null)
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
        changeReadMode(READ_MODE_ROLL, comicReadVPAdapter.getPageIndex());
    }

    @OnClick(R.id.saveLinear)
    public void savePict(){

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
}
