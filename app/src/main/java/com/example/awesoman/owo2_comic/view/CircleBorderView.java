package com.example.awesoman.owo2_comic.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.utils.LogUtil;

/**
 * Created by Awesome on 2017/2/21.
 */

public class CircleBorderView extends View {

    private float widthContainer,heightContainer,//容器宽高
            xCircle,yCircle,//圆心
            radiusCircle = -1,//圆半径
            radiusStart,radiusEnd;

    private int duration;

//    private ShapeDrawable shapeDrawable = null;

    private Paint paint = null;

    private boolean flag = true;

    public CircleBorderView(Context context) {
        super(context);
    }

    public CircleBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        init();
}

    private void init(){
        widthContainer = this.getWidth();
        heightContainer = this.getHeight();
        xCircle = widthContainer/8;
        yCircle = heightContainer/2;
        radiusCircle = widthContainer/6;
        radiusStart = radiusCircle;
        radiusEnd = widthContainer*4/5;
        duration = 300;

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.red));
        paint.setAntiAlias(true);

        LogUtil.i("cenjunhuiInit","onMeasure");
        LogUtil.i("cenjunhuiInit",getWidth()+"");
        LogUtil.i("cenjunhuiInit",getHeight()+"");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        LogUtil.i("cenjunhuiOnDraw","onDraw");
        LogUtil.i("cenjunhuiOnDraw",xCircle+"");
        LogUtil.i("cenjunhuiOnDraw",yCircle+"");
        LogUtil.i("cenjunhuiOnDraw",radiusCircle+"");
        LogUtil.i("cenjunhuiOnDraw",paint.getColor()+"");
        if(radiusCircle <1.0f)
            init();
        canvas.drawCircle(xCircle,yCircle,radiusCircle,paint);
        canvas.restore();
    }

    public void clickAnimation(){
        if(flag) {
            ValueAnimator bgAni = ObjectAnimator.ofFloat(this, "radiusCircle", radiusStart, radiusEnd);
            bgAni.setInterpolator(new AccelerateInterpolator());
            bgAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    radiusCircle = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            bgAni.setDuration(duration);
            bgAni.setEvaluator(new FloatEvaluator());

            ValueAnimator colorAni = ObjectAnimator.ofInt(paint, "color", getResources().getColor(R.color.red), getResources().getColor(R.color.redUndeep));
            colorAni.setInterpolator(new AccelerateInterpolator());
            colorAni.setDuration(duration);
            colorAni.setEvaluator(new ArgbEvaluator());

            AnimatorSet set = new AnimatorSet();
            set.play(bgAni).with(colorAni);
            set.start();
        }
        else{
            ValueAnimator bgAni = ObjectAnimator.ofFloat(this, "radiusCircle", radiusEnd , radiusStart);
            bgAni.setInterpolator(new AccelerateInterpolator());
            bgAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    radiusCircle = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            bgAni.setDuration(duration);
            bgAni.setEvaluator(new FloatEvaluator());

            ValueAnimator colorAni = ObjectAnimator.ofInt(paint, "color", getResources().getColor(R.color.redUndeep),getResources().getColor(R.color.red));
            colorAni.setInterpolator(new AccelerateInterpolator());
            colorAni.setDuration(duration);
            colorAni.setEvaluator(new ArgbEvaluator());

            AnimatorSet set = new AnimatorSet();
            set.play(bgAni).with(colorAni);
            set.start();
        }
        flag = !flag;
    }

    public float getRadiusCircle() {
        return radiusCircle;
    }

    public void setRadiusCircle(int radiusCircle) {
        this.radiusCircle = radiusCircle;
    }

}
