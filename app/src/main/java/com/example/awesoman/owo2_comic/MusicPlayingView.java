package com.example.awesoman.owo2_comic;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.awesoman.owo2_comic.utils.LogUtil;

/**
 * Created by Awesome on 2017/3/3.
 */

public class MusicPlayingView extends View {

    private boolean isPlaying;

    private int count = 4;

    private Paint paint = null;

    private int color;

    private ValueAnimator anim1, anim2, anim3, anim4;

    private Line[] lArr = new Line[4];

    private float w, y;


    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public MusicPlayingView(Context context) {
        super(context);
    }

    public MusicPlayingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    public void init() {
        int width = getWidth() / (count + 1);
        w = width / 3;
        y = getHeight();
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                lArr[i] = new Line(width, y, y / 4, w);
            } else {
                if (i % 2 == 0) {
                    lArr[i] = new Line(width + lArr[i - 1].getX(), y, y / 4, w);
                } else {
                    lArr[i] = new Line(width + lArr[i - 1].getX(), y, y / 2, w);
                }
            }
        }
        LogUtil.i("onMeasure", w + "");
        LogUtil.i("onMeasure", y + "");
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (y < 1.0f)
            init();
        for (int i = 0; i < count; i++) {
            Line l = lArr[i];
            canvas.drawRect(l.getX(), l.getY() - l.getH(), l.getX() + l.getW(), l.getY(), paint);
        }
        canvas.restore();
    }

    public void startAnim() {
        if (!isPlaying()) {
            ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
//                    animation.getAnimatedValue()
                    invalidate();
                }
            };
            if(lArr[0]==null)
                init();
            anim1 = ObjectAnimator.ofFloat(lArr[0], "h", lArr[0].getH(), y, 0, lArr[0].getH());
            anim1.setDuration(3000);
            anim1.addUpdateListener(listener);
            anim1.setInterpolator(new LinearInterpolator());
            anim1.setRepeatMode(ValueAnimator.REVERSE);
            anim1.setRepeatCount(1000);
            anim1.start();

            anim2 = ObjectAnimator.ofFloat(lArr[1], "h", lArr[0].getH(), 0, y, lArr[0].getH());
            anim2.setDuration(2000);
            anim2.addUpdateListener(listener);
            anim2.setInterpolator(new LinearInterpolator());
            anim2.setRepeatMode(ValueAnimator.REVERSE);
            anim2.setRepeatCount(1000);
            anim2.start();

            anim3 = ObjectAnimator.ofFloat(lArr[2], "h", lArr[0].getH(), y, 0, lArr[0].getH() / 4 * 3);
            anim3.setDuration(2500);
            anim3.addUpdateListener(listener);
            anim3.setInterpolator(new LinearInterpolator());
            anim3.setRepeatMode(ValueAnimator.REVERSE);
            anim3.setRepeatCount(1000);
            anim3.start();

            anim4 = ObjectAnimator.ofFloat(lArr[3], "h", lArr[0].getH(), 0, y, lArr[0].getH() / 4 * 3);
            anim4.setDuration(1500);
            anim4.addUpdateListener(listener);
            anim4.setInterpolator(new LinearInterpolator());
            anim4.setRepeatMode(ValueAnimator.REVERSE);
            anim4.setRepeatCount(1000);
            anim4.start();
            isPlaying = true;
        }
    }

    public void stopAnim() {
        if (isPlaying()) {
            if (anim1 != null) {
                anim1.cancel();
                anim2.cancel();
                anim3.cancel();
                anim4.cancel();
            }
            isPlaying = false;
        }
    }

    class Line {
        float x, y, h, w;

        public Line(float x, float y, float h, float w) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }

        public float getW() {
            return w;
        }

        public void setW(float w) {
            this.w = w;
        }
    }
}
