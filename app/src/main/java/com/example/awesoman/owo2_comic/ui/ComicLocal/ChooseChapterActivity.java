package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.ComicBean;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ChooseChapterAdapter;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.utils.SkipUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Awesome on 2017/3/30.
 * 选取封面来自章节Activity
 */

public class ChooseChapterActivity extends BaseActivity
        implements View.OnClickListener ,ChooseChapterAdapter.IChooseChapter{


    @Bind(R.id.titleTxt)
    TextView titleTxt;
    @Bind(R.id.lv_chapter)
    ListView chapterLV;

    private ComicBean comicBean;
    private ChooseChapterAdapter adapter;
    private FileManager comicDBManager;
    private List<String> chapterList ;


    @Override
    public int getContentViewID() {
        return R.layout.activity_choose_chapter;
    }

    @Override
    public void initView() {
        chapterLV = (ListView) findViewById(R.id.lv_chapter);
        titleTxt.setText("选择章节文件夹");
        Bundle bundle = getIntent().getExtras();
        comicDBManager = FileManager.getInstance();

        //获取comicEntity信息
        comicBean =(ComicBean) getIntent().getSerializableExtra("comicBean");
        showChapter();
    }

    public void showChapter(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                adapter = new ChooseChapterAdapter(ChooseChapterActivity.this);
                chapterList = comicDBManager.getChapterList(comicBean.getComicPath());
                adapter.setmData(chapterList);
                adapter.setListener(ChooseChapterActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                chapterLV.setAdapter(adapter);
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.backBtn:
                finish();
                break;
        }
    }

    @Override
    public void chooseChapter(int position) {
        Intent intent = new Intent();
        intent.setClass(this,ChooseSurfaceActivity.class);
        //传入漫画下章节的地址路径
        intent.putExtra("path",comicBean.getComicPath());
        intent.putExtra("chapter",chapterList.get(position));
        SkipUtil.skip(this,intent,false);
    }
}
