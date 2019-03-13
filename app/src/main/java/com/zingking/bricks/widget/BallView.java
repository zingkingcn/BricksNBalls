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
    private PointF pointPosition = new PointF(20,20);
    private Paint paint;
    private int[] ballPosition = new int[]{0, 0};

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

    public int[] getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(int[] ballPosition) {
        this.ballPosition = ballPosition;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(ballPosition[0]).append(",").append(ballPosition[1]).append(")");
        canvas.drawCircle(pointPosition.x, pointPosition.y, 20, paint);
        // 画当前小球坐标
//        paint.setColor(Color.RED);
//        canvas.drawText(sb.toString(), pointPosition.x, pointPosition.y, paint);
//        paint.reset();
        super.onDraw(canvas);
    }
}
