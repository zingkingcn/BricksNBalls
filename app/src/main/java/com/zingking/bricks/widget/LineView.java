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
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/13
 * @description
 */
public class LineView extends View {

    private PointF lineStopPosition = new PointF();
    private PointF lineStartPosition = new PointF();
    private Paint paint;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    public LineView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(lineStartPosition.x, lineStartPosition.y, lineStopPosition.x, lineStopPosition.y, paint);
    }

    public PointF getLineStartPosition() {
        return lineStartPosition;
    }

    public void setLineStartPosition(PointF lineStartPosition) {
        this.lineStartPosition = lineStartPosition;
    }

    public PointF getLineStopPosition() {
        return lineStopPosition;
    }

    public void setLineStopPosition(final PointF pointF) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                lineStopPosition = pointF;
                postInvalidate();
            }
        });
    }

    private void runOnUi(Runnable runnable){
        mainHandler.post(runnable);

    }
}
