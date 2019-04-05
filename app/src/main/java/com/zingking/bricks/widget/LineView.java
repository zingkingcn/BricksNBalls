package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
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
    private Path path;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public LineView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.WHITE);
        path = new Path();
        path.addCircle(0, 0, 3, Path.Direction.CW);
        paint.setPathEffect(new PathDashPathEffect(path, 15, 0, PathDashPathEffect.Style.ROTATE));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(lineStartPosition.x, lineStartPosition.y);
        path.lineTo(lineStopPosition.x, lineStopPosition.y);
        canvas.drawPath(path, paint);
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

    private void runOnUi(Runnable runnable) {
        mainHandler.post(runnable);
    }
}
