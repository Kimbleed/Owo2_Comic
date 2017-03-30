package com.example.awesoman.owo2_comic.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Awesome on 2016/11/22.
 */

public class ReadComicViewPager extends ViewPager {

    private float vpWidth,xTouchStart,yTouchStart ;
    private long touchStartTime;//第一次点击时间
    private IMyViewPager listener;

    public void setListener(IMyViewPager listener) {
        this.listener = listener;
    }

    public ReadComicViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadComicViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vpWidth = getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i("onTouchEvent",this.getClass().toString()+(super.onTouchEvent(ev)?"true":"false"));
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i("dispatchTouchEvent",this.getClass().toString()+(super.dispatchTouchEvent(ev)?"true":"false"));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xTouchStart = ev.getX();
                yTouchStart = ev.getY();
                touchStartTime = System.currentTimeMillis();
            break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(ev.getX()-xTouchStart)<10
                        && Math.abs(ev.getY()-yTouchStart)<10
                        ){
                    int part = -1;
                    if(listener!=null) {
                        if (xTouchStart < vpWidth / 3) {
                            listener.clickOnViewPager(0);
                        } else if (xTouchStart < vpWidth / 3 * 2 && xTouchStart > vpWidth / 3) {
                            listener.clickOnViewPager(1);
                        } else {
                            listener.clickOnViewPager(2);
                        }
                    }
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @part 左中右部分  0 左  1中  2右
     */
    public interface IMyViewPager{
        void clickOnViewPager(int part);
    }
}
