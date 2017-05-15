package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.utils.MyImageLoader;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.view.ReadComicViewPager;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicReadAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Awesome on 2016/11/11.
 * 漫画阅读Activity
 */

public class ComicReadActivity extends BaseActivity
        implements ReadComicViewPager.IMyViewPager, View.OnClickListener {
    @Bind(R.id.vp_read)
    ReadComicViewPager vp_read;
    @Bind(R.id.moreSelectLinear)
    LinearLayout moreSelectLinear;
    @Bind(R.id.titleLinear)
    LinearLayout titleLinear;
    @Bind(R.id.backBtn)
    ImageView backBtn;
    @Bind(R.id.titleTxt)
    TextView titleTxt;


    private List<String> pages = null;
    private List<String> pagesPath = null;
    float titleStartY, titleEndY, selectStartY, selectEndY;

    ComicReadAdapter comicReadAdapter;
    private MyImageLoader imageLoader;
    String chapterPath = null;
    private boolean isInVisible;

    @Override
    public int getContentViewID() {
        return R.layout.activity_comic_read;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        String path = bundle.getString("path");
        String chapter = bundle.getString("chapter");
        comicReadAdapter = new ComicReadAdapter(this);

        chapterPath = path + File.separator + chapter;
        comicReadAdapter.setChapterPath(chapterPath);

        pages = FileManager.getInstance().getComicPhotoList(path, chapter);
        comicReadAdapter.setPages(pages);

        comicReadAdapter.setListener(new ComicReadAdapter.IDoubleClick() {
            @Override
            public void doubleClick() {
//                Log.i("ComicReadActivity","IDoubleClick");
//                vp_read.setNoScroll(!vp_read.isNoScroll());
            }
        });



        pagesPath = new ArrayList<>();
        for (String page : pages) {
            pagesPath.add(chapterPath + File.separator + page);
        }
        imageLoader = MyImageLoader.getInstance();
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                imageLoader.addBitmapsToCache(pagesPath);
                return null;
            }
        }.execute();
        vp_read.setAdapter(comicReadAdapter);
        vp_read.setListener(this);
        backBtn.setOnClickListener(this);
        titleTxt.setText(chapter);

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

    public void showOrHideMoreSelect() {
        //title框
        ValueAnimator showTitleAni = null;
        //select框
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
        showTitleAni.setDuration(500);
        showTitleAni.setInterpolator(new LinearInterpolator());

        //select框动画设置
        showSelectAni.setDuration(500);
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
}
