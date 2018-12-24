package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.zingking.bricks.entity.MathPoint;

/**
 * Copyright (c) 2018, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2018/12/14
 * @description
 */
public class BrickView extends View {
    private static final String TAG = "BrickView";
    private MathPoint mathPoint;
    private Paint paint;

    public BrickView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#ff0000"));
    }

    public MathPoint getMathPoint() {
        return mathPoint;
    }

    public void setMathPoint(MathPoint mathPoint) {
        this.mathPoint = mathPoint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        super.onDraw(canvas);
        if (mathPoint.isAvailable()) {
            canvas.drawRect(mathPoint.getRange(), paint);
        }
    }

}
