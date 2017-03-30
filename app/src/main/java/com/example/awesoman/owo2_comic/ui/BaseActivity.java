package com.example.awesoman.owo2_comic.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import butterknife.ButterKnife;


/**
 * Created by Awesome on 2017/2/20.
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context CTX;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CTX = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewID());

        ButterKnife.bind(this);

        initView();
    }



    /**
     * 设置R.layout布局
     * @return
     */
    public abstract int getContentViewID();

    /**
     * 初始化view
     * @return
     */
    public abstract void initView();
}
