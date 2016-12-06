package com.halzhang.android.verticalprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by admin on 2016/12/6.
 */

public class MyVerticalSeekBar extends View {
    private static final String TAG = MyVerticalSeekBar.class.getSimpleName();

    private int thumbColor = Color.BLACK;
    private int thumbBorderColor = Color.WHITE;

    /*Progress背景颜色*/
    private int mBackProColor=Color.BLACK;

    //坐标
    private float x, y;


    private float mRadius;

    /*当前进度*/
    private int  mProgress;

    private int mProgressSum=100;

    /*拖动条背景宽度*/
    private float mProgressBackgroundW = 6f;

    /*ProgressBackaground 拖动条背景矩形左下角和右下角坐标*/
    private float sLeft, sTop, sRight, sBottom;

    /*控件宽高*/
    private float mWidth, mHeight;

    private Paint paint = new Paint();
    protected OnStateChangeListener onStateChangeListener;


    public MyVerticalSeekBar(Context context) {
        this(context, null);
    }

    public MyVerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setColor(int thumbColor, int thumbBorderColor) {
        this.thumbColor = thumbColor;
        this.thumbBorderColor = thumbBorderColor;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = getMeasuredHeight();
        int w = getMeasuredWidth();
        mRadius = (float) w / 2;
        sLeft = (w - mProgressBackgroundW) / 2; // 背景左边缘坐标
        sRight = (w + mProgressBackgroundW) / 2;// 背景右边缘坐标
        sTop = 0;
        sBottom = h;
        mWidth = sRight - sLeft; // 背景宽度
        mHeight = sBottom - sTop; // 背景高度
        x = (float) w / 2;//圆心的x坐标
        y = (float) (1 - 0.01 * mProgress) * mHeight;//圆心y坐标
        drawBackground(canvas);
        drawCircle(canvas);
        paint.reset();
    }

    private void drawBackground(Canvas canvas) {
        RectF rectBlackBg = new RectF(sLeft, sTop, sRight, sBottom);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mBackProColor);
        canvas.drawRoundRect(rectBlackBg, mWidth / 2, mWidth / 2, paint);
    }

    private void drawCircle(Canvas canvas) {
        Paint thumbPaint = new Paint();
        y = y < mRadius ? mRadius : y;//判断thumb边界
        y = y > mHeight - mRadius ? mHeight - mRadius : y;
        thumbPaint.setAntiAlias(true);
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setColor(thumbColor);
        canvas.drawCircle(x, y, mRadius, thumbPaint);
        thumbPaint.setStyle(Paint.Style.STROKE);
        thumbPaint.setColor(thumbBorderColor);
        thumbPaint.setStrokeWidth(2);
        canvas.drawCircle(x, y, mRadius, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.y = event.getY();
        if(y<0){
            y=0;
        }
        if(y > mHeight){
            y=mHeight;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                mProgress = (int) ((mHeight - y) / mHeight * mProgressSum);
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStopTrackingTouch(this, mProgress);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mProgress = (int) ((mHeight - y) / mHeight * mProgressSum);
                if (onStateChangeListener != null) {
                    onStateChangeListener.OnStateChangeListener(this, mProgress);
                }
                Log.d(TAG, "onTouchEvent: mHeight="+ mHeight +";y="+y+";progress"+mProgress);
                setProgress(mProgress);
                this.invalidate();
                break;
        }

        return true;
    }


    public interface OnStateChangeListener {
        void OnStateChangeListener(View view, float progress);

        void onStopTrackingTouch(View view, float progress);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }
}
