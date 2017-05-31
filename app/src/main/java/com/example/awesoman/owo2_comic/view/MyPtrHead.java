package com.example.awesoman.owo2_comic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.awesoman.owo2_comic.utils.MyLogger;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Awesome on 2017/5/30.
 */

public class MyPtrHead extends View implements PtrUIHandler {
    private static final String TAG = "MyPtrHead";
    private IPtrHeadListener listener;

    public MyPtrHead(Context context) {
        super(context);
    }

    public MyPtrHead(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPtrHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IPtrHeadListener getListener() {
        return listener;
    }

    public void setListener(IPtrHeadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.i("MyPtrHead","5");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.i("MyPtrHead","4");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.i("MyPtrHead","3");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.i("MyPtrHead","2");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        float currentPercent = ptrIndicator.getCurrentPercent();
        MyLogger.ddLog(TAG).i(""+currentPercent);
        Log.i("MyPtrHead","1");
        listener.onDownMoveEvent(currentPercent);
    }

     public interface IPtrHeadListener{
         void onDownMoveEvent(float percent);
     }
}
