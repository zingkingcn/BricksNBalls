package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2019/1/14
 * @description
 */

public class BallView extends View {
    private final float X = 20f;
    private final float Y = 20f;
    private final float radius = 20f;
    /**
     * 小球的圆心
     */
    private PointF pointPosition = new PointF(X,Y);
    private Paint paint;
    /**
     * 小球数学坐标点
     */
    private int[] ballCoordinate = new int[]{0, 0};

    public BallView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setColor(Color.parseColor("#00ffff"));
    }


    public PointF getPointPosition() {
        return pointPosition;
    }

    public void setPointPosition(final PointF position) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (position != null) {
                    pointPosition.set(position);
                    invalidate();
                }
            }
        });
    }

    public int[] getBallCoordinate() {
        return ballCoordinate;
    }

    public void setBallCoordinate(int[] ballCoordinate) {
        this.ballCoordinate = ballCoordinate;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(ballCoordinate[0]).append(",").append(ballCoordinate[1]).append(")");
        canvas.drawCircle(pointPosition.x, pointPosition.y, radius, paint);
        // 画当前小球坐标
//        paint.setColor(Color.RED);
//        canvas.drawText(sb.toString(), pointPosition.x, pointPosition.y, paint);
//        paint.reset();
        super.onDraw(canvas);
    }
}
