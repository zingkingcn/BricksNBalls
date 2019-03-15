package com.zingking.bricks.mvp.model;

import android.graphics.PointF;
import android.graphics.RectF;

import com.zingking.bricks.entity.BackgroundPosition;
import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.event.BallCrashEvent;
import com.zingking.bricks.utils.GameLevelUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/14
 * @description
 */
public class BrickModel {
    float ballRadius = 20f;
    /**
     * 整个关卡砖块布局
     */
    private int[][] brickPosition;
    /**
     * 砖块的数学坐标点集合
     */
    // README 用list会报ConcurrentModificationException
    private CopyOnWriteArrayList<MathPoint> mathPointList = new CopyOnWriteArrayList<>();
    /**
     * 发射线的起点位置
     */
    private PointF lineStartPF = new PointF(0, 0);
    /**
     * 发射线的终点位置
     */
    private PointF lineStopPF = new PointF(0, 0);
    /**
     * 发射线角度
     */
    private double angle = 0;
    /**
     * 这个背景的位置信息
     */
    private BackgroundPosition backgroundPosition;

    public void createLevel() {
        if (brickPosition == null) {
            brickPosition = GameLevelUtils.getLevel();
        }
    }

    public void createLevel(int index) {

    }

    public double calcAngle(PointF start, PointF stop) {
        return Math.atan((stop.y - start.y) / ((double) (stop.x - start.x))) / Math.PI * 180;
    }

    public int[][] getBrickPosition() {
        return brickPosition;
    }

    public void addMathPoint(MathPoint mathPoint) {
        mathPointList.add(mathPoint);
    }

    public void removeMathPoint(MathPoint mathPoint) {
        mathPointList.remove(mathPoint);
    }

    public void setLineStartPF(PointF lineStartPF) {
        this.lineStartPF = lineStartPF;
    }

    public void setLineStopPF(PointF lineStopPF) {
        this.lineStopPF = lineStopPF;
    }

    public double getAngle() {
        return Math.atan((lineStopPF.y - lineStartPF.y) / ((double) (lineStopPF.x - lineStartPF.x))) / Math.PI * 180;
    }

    public void setBackgroundPosition(BackgroundPosition backgroundPosition) {
        this.backgroundPosition = backgroundPosition;
    }

    /**
     * 左右方向是否改变
     */
    public boolean changeLR(boolean isRight, PointF pointF) {
        int[] ballPosition = getBallPosition(pointF);
        boolean result = false;
        for (MathPoint mathPoint : mathPointList) {
            RectF range = mathPoint.getRange();
            if (isRight) {
                if (ballPosition[0] + 1 == mathPoint.getX() && ballPosition[1] == mathPoint.getY()) {
                    // 如果小球向右移动，当小球x坐标大于方块左侧，则表示需要改为向左移动
                    result = pointF.x + ballRadius >= range.left;
                    if (result) {
                        EventBus.getDefault().post(new BallCrashEvent(mathPoint));
                    }
                    break;
                }
            } else {
                if (ballPosition[0] - 1 == mathPoint.getX() && ballPosition[1] == mathPoint.getY()) {
                    // 如果小球向左移动，当小球x坐标小于方块右侧，则表示需要改为向向右移动
                    result = pointF.x - ballRadius <= range.right && pointF.x - ballRadius >= range.left;
                    if (result) {
                        EventBus.getDefault().post(new BallCrashEvent(mathPoint));
                    }
                    break;
                }
            }
        }
        // 如果未发生碰撞则检测边缘
        if (!result) {
            if (isRight) {
                result = pointF.x + ballRadius >= backgroundPosition.getWidth();
            } else {
                result = pointF.x - ballRadius < 0;
            }
        }
        return result;
    }

    public int[] getBallPosition(PointF pointF) {
        final float[] xPositions = backgroundPosition.getXPositions();// x轴点的数量和 列位置参数(数组长度为数量，值为位置参数)
        final float[] yPositions = backgroundPosition.getYPositions();// y轴点的数量和 行位置参数
        int xCoordinate = -1;
        int yCoordinate = -1;
        if (pointF.x < xPositions[0]) {
            xCoordinate = -1;
        } else if (pointF.x > xPositions[xPositions.length - 1]) {
            xCoordinate = xPositions.length - 1;
        }

        if (pointF.y < yPositions[0]) {
            yCoordinate = -1;
        } else if (pointF.y > yPositions[yPositions.length - 1]) {
            yCoordinate = yPositions.length - 1;
        }

        for (int i = 0, len = xPositions.length - 1; i < len; i++) {
            if (pointF.x > xPositions[i] && pointF.x < xPositions[i + 1]) {
                xCoordinate = i;
                break;
            }
        }
        for (int i = 0, len = yPositions.length - 1; i < len; i++) {
            if (pointF.y > yPositions[i] && pointF.y < yPositions[i + 1]) {
                yCoordinate = i;
                break;
            }
        }
        return new int[]{xCoordinate, yCoordinate};
    }

    /**
     * 上下方向是否改变
     */
    public boolean changeTB(boolean isDown, PointF pointF) {
        boolean result = false;
        int[] ballPosition = getBallPosition(pointF);
        for (MathPoint mathPoint : mathPointList) {
            RectF range = mathPoint.getRange();
            if (isDown) {
                if (ballPosition[1] + 1 == mathPoint.getY() && ballPosition[0] == mathPoint.getX()) {
                    // 如果小球向下移动，当小球y坐标大于方块上侧，则表示需要改为向上移动
                    result = pointF.y + ballRadius >= range.top;
                    if (result) {
                        EventBus.getDefault().post(new BallCrashEvent(mathPoint));
                    }
                }
            } else {
                if (ballPosition[1] - 1 == mathPoint.getY() && ballPosition[0] == mathPoint.getX()) {
                    // 如果小球向上移动，当小球y坐标小于方块下侧，则表示需要改为向向下移动
                    result = pointF.y - ballRadius <= range.bottom && pointF.y - ballRadius >= range.top;
                    if (result) {
                        EventBus.getDefault().post(new BallCrashEvent(mathPoint));
                    }
                }
            }
        }
        // 如果未发生碰撞则检测边缘
        if (!result) {
            if (isDown) {
                result = pointF.y + ballRadius >= backgroundPosition.getHeight();
            } else {
                result = pointF.y - ballRadius < 0;
            }
        }
        return result;
    }

}
