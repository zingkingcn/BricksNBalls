package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.TestLooperManager;
import android.view.View;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2019/1/14
 * @description
 */

public class BallView extends View {
    private PointF pointPosition = new PointF();
    private Paint paint;

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

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(pointPosition.x, pointPosition.y, 50, paint);
        super.onDraw(canvas);
    }
}
