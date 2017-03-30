package com.example.awesoman.owo2_comic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.utils.LogUtil;

/**
 * Created by Awesome on 2017/2/23.
 */

public class DragProgressBar extends View {

    private float xPointStart, xPointEnd,//播放点的x起点,x终点
            lengthStartToEnd,//起点到终点的长度
            xPoint, yPoint,//当前播放点的坐标, xPoint变动,yPoint不变
            xTouchStart, yTouchStart,//初点击的坐标
            radius = 8,//播放点的半径
            progressCurrent//当前进度 百分比
                    ;

    public void setXPoint(float progressCurrent) {
        this.xPoint = progressCurrent* lengthStartToEnd + xPointStart;
        invalidate();
    }

    private Paint paintHasPlay = null;

    private Paint paintNoPlay = null;

    private boolean isTouched = false;

    private int color = 0;

    private IPlayerProgressBar listener;

    public void setListener(IPlayerProgressBar listener) {
        this.listener = listener;
    }

    public DragProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public DragProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public void initPaint() {
        paintHasPlay = new Paint();
        paintHasPlay.setColor(getResources().getColor(R.color.colorAccent));
        paintNoPlay = new Paint();
        paintNoPlay.setColor(getResources().getColor(R.color.gray));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(xPoint<31.0)
            initMeasure();
    }


    public void initMeasure(){
        xPointStart = 30;
        xPointEnd = getWidth() - 30;
        xPoint = xPointStart;
        yPoint = getHeight() / 2;
        lengthStartToEnd = xPointEnd - xPointStart;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.i("onDraw","xPoint:"+xPoint);
        canvas.save();
        if(xPoint<31.0)
            initMeasure();
        canvas.drawRect(xPoint, yPoint - 3, xPointEnd, yPoint + 3, paintNoPlay);
        canvas.drawRect(xPointStart, yPoint - 3, xPoint, yPoint + 3, paintHasPlay);
        canvas.drawCircle(xPoint, yPoint, radius, paintHasPlay);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("dispatch", "ACTION_DOWN");
                xTouchStart = event.getX();
                yTouchStart = event.getY();
//                LogUtil.i("xTouchStart",""+xTouchStart);
//                LogUtil.i("xPoint-radius",""+(xPoint-radius));
//                LogUtil.i("xPoint+radius",""+(xPoint+radius));
//                LogUtil.i("yTouchStart",""+yTouchStart);
//                LogUtil.i("yPoint-10",""+(yPoint-10));
//                LogUtil.i("yPoint+10",""+(yPoint+10));
                if (xTouchStart <= xPoint + radius && xTouchStart >= xPoint - radius
                        && yTouchStart <= yPoint + 10 && yTouchStart >= yPoint - 10) {
//                    LogUtil.i("ACTION_DOWN","if");
                    isTouched = true;
                }
                if (listener != null)
                    listener.onDown(progressCurrent);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("dispatch", "ACTION_MOVE");
                if (isTouched) {
                    xPoint = event.getX();
//                    LogUtil.i("ACTION_DOWN","ifIsTouched");
//                    LogUtil.i("xPoint",xPoint+"");
//                    LogUtil.i("xPointEnd",xPointEnd+"");
//                    LogUtil.i("xPointStart",xPointStart+"");
                    if (xPoint <= xPointEnd && xPoint >= xPointStart) {
//                        LogUtil.i("ACTION_DOWN","invalidate");
                        invalidate();
                        progressCurrent = (xPoint - xPointStart) / (lengthStartToEnd);
                    }
                    if (listener != null)
                        listener.onMove(progressCurrent);
                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.i("dispatch", "ACTION_UP");
                if (isTouched) {
                    isTouched = false;
                    if(event.getX()<xPointStart)
                        xPoint = xPointStart;
                    else if(event.getX() > xPointEnd)
                        xPoint = xPointEnd;
                    if (listener != null)
                        listener.onUp(progressCurrent);
                }
                break;
        }
        return true;
    }

    public interface IPlayerProgressBar {
        void onDown(float progressCurrent);

        void onMove(float progressCurrent);

        void onUp(float progressCurrent);
    }

}
