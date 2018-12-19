package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 * Copyright (c) 2018, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2018/12/14
 * @description
 */
public class BrickView extends View {
    private static final String TAG = "BrickView";
    private Paint paint;
    private RectF positionRectF = new RectF(-1, -1, -1, -1);
    /**
     * 左上坐标点
     */
    private float[] ltPosition = new float[2];
    /**
     * 右下坐标点
     */
    private float[] rbPosition = new float[2];

    public BrickView(Context context) {
        super(context);
        init();
    }
    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#ff0000"));
    }

    public RectF getPositionRectF(){
        return positionRectF;
    }

    public void setLtPosition(float[] ltPosition) {
        this.ltPosition = ltPosition;
        positionRectF.left = ltPosition[0];
        positionRectF.top = ltPosition[1];
    }

    public void setRbPosition(float[] rbPosition) {
        this.rbPosition = rbPosition;
        positionRectF.right = rbPosition[0];
        positionRectF.bottom = rbPosition[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        super.onDraw(canvas);
        if (isValidRectF()) {
            canvas.drawRect(positionRectF, paint);
        }
    }

    private boolean isValidRectF(){
        return positionRectF.left >= 0 && positionRectF.top >= 0 && positionRectF.right >= 0 && positionRectF.bottom >= 0;
    }




}
