package com.example.awesoman.owo2_comic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.awesoman.owo2_comic.storage.ComicEntry;

import java.io.File;

/**
 * Created by Awesome on 2017/6/3.
 */

public class DiscView  extends View {

    private int rDisc,heightScreen,widthScreen;

    private Paint mPaint;

    public DiscView(Context context) {
        super(context);
    }

    public DiscView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        heightScreen = getHeight();
        widthScreen = getWidth();
        rDisc = getWidth()/3;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeFile(ComicEntry.getComicPath()+ File.separator+"光速蒙面侠21号"+File.separator+"surface.jpg");

        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        mPaint.setFilterBitmap(false);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(widthScreen/2,heightScreen/2,rDisc,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mPaint.setColor(Color.RED);
//        canvas.drawRect(10,100,400,200,mPaint);
        Rect src = new Rect(); // 图像内的坐标
        src. left = 0;
        src. top = 0;
        src. right = widthScreen;
        src. bottom = heightScreen;

        RectF dst = new RectF(); // 图像内的坐标
        dst. left = widthScreen/2-rDisc;
        dst. top = 0;
        dst. right = widthScreen/2+rDisc;
        dst. bottom = heightScreen;
        canvas.drawBitmap(bitmap,src,dst,mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(widthScreen/2,heightScreen/2,20,mPaint);
    }
}
