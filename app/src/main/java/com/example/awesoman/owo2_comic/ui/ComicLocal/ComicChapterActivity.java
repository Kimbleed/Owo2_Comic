package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.bean.ComicBean;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicChapterAdapter;
import com.example.awesoman.owo2_comic.utils.SkipUtil;

import butterknife.Bind;

/**
 * Created by Awesome on 2016/11/5.
 * 漫画章节Activity
 */

public class ComicChapterActivity extends BaseActivity
        implements ComicChapterAdapter.Listener, View.OnClickListener{

    @Bind(R.id.chapter_container)
    RecyclerView chapter_container;
    @Bind(R.id.iv_comic_face)
    ImageView iv_comic_face;
    @Bind(R.id.tv_comic_name)
    TextView tv_comic_name;
    @Bind(R.id.backBtn)
    ImageView backBtn;
    @Bind(R.id.titleTxt)
    TextView titleTxt;

    FileManager comicDBManager ;
    ComicBean comicBean;
    ComicChapterAdapter adapter;

    @Override
    public void initView() {
        //获取comicEntity信息
        comicBean =(ComicBean) getIntent().getSerializableExtra("comicBean");
        //实例化ComicDBManager
        comicDBManager =FileManager.getInstance();
        tv_comic_name.setText(comicBean.getComicName());

        Bitmap bitmap =comicDBManager.getSurface(comicBean.getComicPath());
        iv_comic_face.setImageBitmap(comicDBManager.makeSurface(bitmap,getResources().getDimensionPixelSize(R.dimen.surface_comic_list_width),getResources().getDimensionPixelSize(R.dimen.surface_comic_list_height)));

        backBtn.setOnClickListener(this);
        titleTxt.setText(comicBean.getComicName());
        titleTxt.setTextColor(getResources().getColor(R.color.white));
        showChapter();
    }

    public void showChapter(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                adapter = new ComicChapterAdapter(ComicChapterActivity.this);
                adapter.setmData(comicDBManager.getChapterList(comicBean.getComicPath()));
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ComicChapterActivity.this,3);
                chapter_container.setLayoutManager(layoutManager);
                chapter_container.setAdapter(adapter);
            }
        }.execute();
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
