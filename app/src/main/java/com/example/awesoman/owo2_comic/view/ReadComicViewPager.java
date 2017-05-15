package com.example.awesoman.owo2_comic.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Awesome on 2016/11/22.
 */

public class ReadComicViewPager extends ViewPager {

    private float vpWidth, xTouchStart, yTouchStart;
    private boolean noScroll = false;
    private IMyViewPager listener;
    public static final int HANDLER_FIRST_CLICK = 100;
    public static final int HANDLER_SECOND_CLICK = 200;
    public static final int HADNLER_CLEAR_UP_TIME = 300;
    private boolean hasFirstClick = false;

    public Handler mClickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_FIRST_CLICK:
                    Log.i("ViewPagerOnTouch", "HADNLER:FIRST_CLICK");
                    listener.clickOnViewPager((int) msg.obj);
                    break;
                case HANDLER_SECOND_CLICK:
                    Log.i("ViewPagerOnTouch", "HADNLER:SECOND_CLICK");
                    removeMessages(HANDLER_FIRST_CLICK);
                    break;
                case HADNLER_CLEAR_UP_TIME:
                    Log.i("ViewPagerOnTouch", "HADNLER:CLEAR_UP");
                    hasFirstClick = false;
                    break;
            }
        }
    };

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    public boolean isNoScroll() {
        return noScroll;
    }

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xTouchStart = ev.getX();
                yTouchStart = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(ev.getX() - xTouchStart) < 10 && Math.abs(ev.getY() - yTouchStart) < 10) {
                    int part = -1;
                    if (listener != null) {
                        Message message = new Message();
                        message.what = HANDLER_FIRST_CLICK;

                        if (xTouchStart < vpWidth / 3) {
                            message.obj = 0;
                        } else if (xTouchStart < vpWidth / 3 * 2 && xTouchStart > vpWidth / 3) {
                            message.obj = 1;
                        } else {
                            message.obj = 2;
                        }

                            //第一次点击
                            Log.i("ViewPagerOnTouch", "FirstClick");
                            hasFirstClick = true;
                            if (!noScroll) {
                                mClickHandler.sendMessageDelayed(message, 500);
                            }
                            mClickHandler.sendEmptyMessageDelayed(HADNLER_CLEAR_UP_TIME, 400);
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
    public interface IMyViewPager {
        void clickOnViewPager(int part);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
}
