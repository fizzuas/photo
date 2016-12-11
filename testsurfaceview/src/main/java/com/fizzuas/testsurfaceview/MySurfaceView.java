package com.fizzuas.testsurfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by fizzuas on 16/12/10.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private int degree = 0;


    private Thread mRendThread;
    private String TAG = MySurfaceView.class.getSimpleName();
    private boolean flag;
    private SurfaceHolder mSurfaceHolder;
    private Surface mSuraface;
    private Bitmap mSrc;
    private int mWidth;
    private int mHeight;
    private int min;
    private Paint mBackPaint;
    private Matrix matrix;
    private Paint mBitmapPaint;


    public MySurfaceView(Context context) {
        this(context, null);

    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        // surface到最上层，黑色背景去掉
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        // ?need
        mSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_1);


        mBackPaint = new Paint();
        mBackPaint.setColor(Color.RED);

        matrix = new Matrix();


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mWidth = specSize;
        } else {
            // 由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight()
                    + mSrc.getWidth();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mWidth = Math.min(desireByImg, specSize);
            } else

                mWidth = desireByImg;
        }

        /***
         * 设置高度
         */

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom()
                    + mSrc.getHeight();

            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, specSize);
            } else
                mHeight = desire;
        }


        min = Math.min(mHeight, mWidth);

        setMeasuredDimension(min, min);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: ");
        mRendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {


                    draw();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }


        });
        flag = true;
        mRendThread.start();
    }

    public void draw() {
        Log.d(TAG, " draw: ");
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if(canvas!=null ){

        /*在默认层绘制背景 ,该方法实在一层做操作，将圆形图片绘制封装起来*/
            canvas.drawColor(Color.BLACK);


            mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
            canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);


            //canvas.drawBitmap(mSrc,new Rect(0,0,mSrc.getWidth(),mSrc.getHeight()),new Rect(0,0,min,min),mBitmapPaint);

        }


        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
        flag = false;

    }


    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min) {
        mBitmapPaint = new Paint();

        mBitmapPaint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */

        canvas.drawCircle(min / 2, min / 2, min / 2, mBitmapPaint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */


         /*设置bitmap 变换*/
        float scale = (float) (Math.random() + 1);

        matrix.setScale((float)1.5, (float)1.5);

        matrix.setRotate(((degree+=1)%360),min/2,min/2);
        Log.d(TAG, "createCircleImage:W= " + source.getWidth() + ";H=" + source.getHeight());


       // source = Bitmap.createBitmap(source, 0, 0, min, min, matrix, false);
        Log.d(TAG, "createCircleImage: f=" + scale
        );


        canvas.drawBitmap(source, matrix, mBitmapPaint);



		/*
        绘制外圆环，stroke为6 ；canvas绘制一层layer时，draw一层一层覆盖（普通模式下，没有设置PorterDuff）
		*/

        mBitmapPaint.setStrokeWidth(6);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setColor(Color.WHITE);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(min / 2, min / 2, min / 2-3, mBitmapPaint);


        return target;
    }


}
