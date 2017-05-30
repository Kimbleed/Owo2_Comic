package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.example.awesoman.owo2_comic.utils.FastBlur;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.ComicBean;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicChapterAdapter;
import com.example.awesoman.owo2_comic.utils.SkipUtil;
import com.example.awesoman.owo2_comic.view.CustomTitlebar;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Awesome on 2016/11/5.
 * 漫画章节Activity
 */

public class ComicChapterActivity extends BaseActivity
        implements ComicChapterAdapter.IComicChapterListener{

    @Bind(R.id.iv_bg)
    ImageView iv_bg;
    @Bind(R.id.chapter_container)
    RecyclerView chapterContainer;
    @Bind(R.id.ptr)
    PtrClassicFrameLayout ptrLayout;

    FileManager comicDBManager ;
    ComicBean comicBean;
    ComicChapterAdapter adapter;

    @Override
    public void initView() {
        //获取comicEntity信息
        comicBean =(ComicBean) getIntent().getSerializableExtra("comicBean");
        //实例化ComicDBManager
        comicDBManager =FileManager.getInstance();
        showChapter();
        initPtr();
        setBg();
    }

    public void showChapter(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                adapter = new ComicChapterAdapter(ComicChapterActivity.this,ComicChapterActivity.this);
                adapter.setmData(comicDBManager.getChapterList(comicBean.getComicPath()));
                adapter.setTitle(comicBean.getComicName());
                adapter.setSurPath(comicBean.getComicPath());
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                GridLayoutManager layoutManager = new GridLayoutManager(ComicChapterActivity.this,3);
                GridLayoutManager.SpanSizeLookup bbb = new GridLayoutManager.SpanSizeLookup(){
                    @Override
                    public int getSpanSize(int position) {
                        return adapter.getItemViewType(position);
                    }
                };
                layoutManager.setSpanSizeLookup(bbb);
                chapterContainer.setLayoutManager(layoutManager);
                chapterContainer.setAdapter(adapter);
            }
        }.execute();
    }

    public void setBg(){
        //获取图片
        Bitmap bitmap =comicDBManager.getSurface(comicBean.getComicPath());
        //截取图片
        bitmap = comicDBManager.makeSurface(bitmap,CTX.getResources().getDimensionPixelSize(R.dimen.surface_comic_list_width),CTX.getResources().getDimensionPixelSize(R.dimen.surface_comic_list_height));
        //模糊处理
        iv_bg.setImageBitmap(FastBlur.doBlur(bitmap,50,false));
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_chapter;
    }

    @Override
    public void onChapterClick(String chapter) {
        Intent intent = new Intent(this,ComicReadActivity.class);
        intent.putExtra("chapter",chapter);
        intent.putExtra("path", comicBean.getComicPath());
        SkipUtil.skip(this,intent,false);
    }

    @Override
    public void onSurClick() {
        Intent intent = new Intent();
        intent.setClass(this,ChooseChapterActivity.class);
        intent.putExtra("comicBean",comicBean);
        SkipUtil.skip(this, intent,false);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
    }

    public void initPtr(){
        ptrLayout.setLastUpdateTimeRelateObject(this);
//        ptrLayout.setBackgroundResource((R.color.white));
        ptrLayout.setDurationToCloseHeader(1000);
        ImageView head = new ImageView(this);
        ptrLayout.setHeaderView(head);
        ImageView foot = new ImageView(this);
        ptrLayout.setFooterView(foot);
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                Log.i("onLoadMoreBegin","here");
//                getVideos(cloudAlbumAdapter.mData.size()/10+1);
//                ptrLayout.refreshComplete();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.i("onRefreshBegin","here");
//                getVideos(1);
//                ptrLayout.refreshComplete();
            }
        });

    }
}
