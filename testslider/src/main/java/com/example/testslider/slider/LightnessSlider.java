package com.example.testslider.slider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;




public class LightnessSlider extends AbsCustomSlider {
    private static final String TAG = "LightnessSlider";
    //默认value是1（在父类中），默然颜色White
    private int color = Color.WHITE;
    //绘制横线bar
    private Paint barPaint = PaintBuilder.newPaint().color(color).build();
    //绘制handle内实圈
    private Paint solid = PaintBuilder.newPaint().color(Color.WHITE).build();
    //绘制Handle外透明圈
    private Paint clearingStroke = PaintBuilder.newPaint().color(0xffffffff).xPerMode(PorterDuff.Mode.CLEAR).build();


   protected onLightSelListener onLightSelListener;


    //private ColorPickerView colorPicker;
    public LightnessSlider(Context context) {
        super(context);
    }

    public LightnessSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightnessSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    画杆*/
    @Override
    protected void drawBar(Canvas barCanvas) {
        int width = barCanvas.getWidth();
        int height = barCanvas.getHeight();

        Log.d(TAG, "view canvas: viewW=" + getWidth() + "\t viewH" + this.getHeight() + "\n canvasW	=" + width + "\tcanvasH=" + height);

        //barCanvas.drawRect(0, 0, width, height, barPaint);
//给bar（也就是横线）每一个像素矩阵画矩形下面代码是形成透明度从0逐渐变为1的效果
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        Log.d(TAG, "drawBar: color=" + color);
        int l = Math.max(2, width / 256);
        for (int x = 0; x <= width; x += l) {
            hsv[2] = (float) x / (width);//hsv[2] 0->1亮度逐渐变大，0是黑色 >=1最亮
            Log.d(TAG, "drawBar: hsv[2]=" + hsv[2]);
            barPaint.setColor(Color.HSVToColor(hsv));
            barCanvas.drawRect(x, 0, x + l, height, barPaint);

        }
//        for (int y = 0; y <= height; y += l) {
//            barPaint.setColor(Color.GRAY);
//            barCanvas.drawRect(0, y, width, y+1, barPaint);
//
//        }
    }

    @Override
    protected void onValueChanged(float value) {
        //this.color=Utils2.colorAtLightness(color,value);
        Log.d(TAG, "onValueChanged: " + value);
        if (onLightSelListener != null) {
            onLightSelListener.lightChanged(value);
        }

    }

    /*
    画圆
    * */
    @Override
    protected void drawHandle(Canvas canvas, float x, float y) {
       // solid.setColor(Utils2.colorAtLightness(color, value));//value是父抽象类的value
        canvas.drawCircle(x, y, handleRadius, clearingStroke);
        canvas.drawCircle(x, y, handleRadius * 0.75f, solid);
    }


    public void setColor(int color) {
        this.color = color;
        if (bar != null) {
            updateBar();
            invalidate();
        }
    }

    public void setOnLightSelListener(onLightSelListener onLightSelListener) {
        this.onLightSelListener = onLightSelListener;
    }

    public float getValue() {
        return value;
    }

    public void release() {

    }
}