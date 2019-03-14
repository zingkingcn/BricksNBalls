package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.zingking.bricks.App;
import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.utils.DensityUtil;

/**
 * Copyright (c) 2018, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2018/12/14
 * @description
 */
public class BrickView extends View {
    private static final String TAG = "BrickView";
    public static final float TEXT_SIZE = 15f;
    private MathPoint mathPoint;
    private Paint paint;
    private Paint shiningPaint;
    private Paint textPaint;
    private boolean isShining = false;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private int brickNum = 10;

    public BrickView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#ff0000"));

        shiningPaint = new Paint();
        shiningPaint.setStrokeWidth(5);
        shiningPaint.setColor(Color.parseColor("#ffff00"));

        textPaint = new Paint();
        textPaint.setTextSize(DensityUtil.sp2px(App.getAppContext(), TEXT_SIZE));
        textPaint.setStrokeWidth(5);
        textPaint.setColor(Color.parseColor("#ffffff"));
    }

    public MathPoint getMathPoint() {
        return mathPoint;
    }

    public void setMathPoint(MathPoint mathPoint) {
        this.mathPoint = mathPoint;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        super.onDraw(canvas);
        if (isShining) {
            canvas.drawRect(mathPoint.getRange(), shiningPaint);
        } else {
            if (mathPoint.isAvailable()) {
                canvas.drawRect(mathPoint.getRange(), paint);
            }
        }
        String str = brickNum + "";
        Rect rect = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        RectF range = mathPoint.getRange();
        float rWidth = range.right - range.left;
        float rHeight = range.bottom - range.top;
        canvas.drawText(str, range.left + (rWidth - w) / 2, range.top + (rHeight - h) / 2 + h, textPaint);
    }

    public void shining(ShiningListener shiningListener) {
        brickNum--;
        if (shiningListener != null && brickNum == 0) {
            shiningListener.remove(this);
            return;
        }
        isShining = true;
        postInvalidate();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isShining = false;
                postInvalidate();
            }
        }, 32);
    }

    private void runOnUi(Runnable runnable, long delay) {
        if (delay > 0) {
            mainHandler.postDelayed(runnable, delay);

        } else {
            mainHandler.post(runnable);
        }
    }

    public interface ShiningListener{
        /**
         * 移除砖块
         *
         * @param brickView 需要移除的砖块
         */
        void remove(BrickView brickView);
    }
}
