package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.zingking.bricks.App;
import com.zingking.bricks.listener.IBallMoveListener;
import com.zingking.bricks.utils.DensityUtil;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2019/1/14
 * @description
 */

public class BallView extends View {
    private static final String TAG = "BallView";
    private final float X = DensityUtil.dip2px(App.getAppContext(), 20f);
    private final float Y = DensityUtil.dip2px(App.getAppContext(), 20f);
    private final float radius = DensityUtil.dip2px(App.getAppContext(), 20f);
    /**
     * 小球的圆心
     */
    private PointF pointPosition = new PointF(X, Y);
    /**
     * 小球停止时的位置
     */
    private PointF endPointF = null;
    private Paint paint;
    /**
     * 小球数学坐标点
     */
    private int[] ballCoordinate = new int[]{0, 0};
    private double moveY = 0;
    private double moveX = 0;
    private boolean isRight = true;
    private boolean isDown = true;
    private double angle = -1;
    private float delta = -1;
    private IBallMoveListener changeListener;

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

    /**
     * 设置小球圆心位置，用来实现小球的移动，现改用{@link BallView#startAutoMove(double, float, IBallMoveListener)}
     *
     * @param position 设置小球圆心位置
     */
    @Deprecated
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
        startAutoMove();
        move2End();
        super.onDraw(canvas);
    }

    private void startAutoMove() {
        if (angle == -1 /*|| delta == -1*/) {
            return;
        }
        final PointF pointF = pointPosition;
        boolean changeLR = changeListener.changeLR(isRight, pointF, angle);
        boolean changeTB = changeListener.changeTB(isDown, pointF, angle);
        if (isRight) {
            if (changeLR) {
                isRight = false;
            } else {
                pointF.x += moveX;
            }
        } else {
            if (changeLR) {
                isRight = true;
            } else {
                pointF.x -= moveX;
            }
        }
        if (isDown) {
            if (changeTB) {
                isDown = false;
            } else {
                pointF.y += moveY;
            }
        } else {
            if (changeTB) {
                isDown = true;
            } else {
                pointF.y -= moveY;
            }
        }
        pointPosition.set(pointF);
        Log.i(TAG, "run: pointF =  " + pointF.toString());
        if (pointF.y - radius < 0) {
            pointF.y = radius;
            angle = -1;
//            delta = -1;
            changeListener.moveEnd(pointF);
        } else {
            postInvalidate();
        }
    }

    private void move2End() {
        if (endPointF == null) {
            return;
        }
        final PointF pointF = pointPosition;
        if (pointF.x < endPointF.x) {
            pointF.x += delta;
            if (pointF.x > endPointF.x) {
                pointF.x = endPointF.x;
            }
        } else if (pointF.x > endPointF.x) {
            pointF.x -= delta;
            if (pointF.x < endPointF.x) {
                pointF.x = endPointF.x;
            }
        } else {
            return;
        }
        Log.i(TAG, "move2End: " + this + pointF);
        pointPosition.set(pointF);
        postInvalidate();
        if (pointF.x == endPointF.x) {
            endPointF = null;
        }
    }

    public void startAutoMove(double angle, float delta, IBallMoveListener changeListener) {
        this.angle = angle;
        this.delta = delta;
        this.changeListener = changeListener;
        moveY = Math.sin(angle * Math.PI / 180) * delta;
        moveX = Math.cos(angle * Math.PI / 180) * delta;
        isRight = angle > 0;
        isDown = true;
        moveY = Math.abs(moveY);
        moveX = Math.abs(moveX);
        startAutoMove();
    }

    public void moveEndPosition(PointF targetPoint) {
        endPointF = targetPoint;
        move2End();
    }
}
