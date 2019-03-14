package com.zingking.bricks.mvp.view;


import android.graphics.PointF;

import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.widget.BricksBackgroundView;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/14
 * @description
 */
public interface IBrickView {
    void addBackgroundView(BricksBackgroundView.IDrawListener iDrawListener);

    void addBrickView(MathPoint mathPoint);

    void addLineView();

    void addBallView();

    /**
     * 更新线的两个坐标
     *
     * @param isStart true:线的起点坐标<P>false:线的终点坐标</P>
     * @param pointF  坐标点值
     */
    void updateLine(boolean isStart, PointF pointF);

    /**
     * 更新圆心
     *
     * @param pointF 圆心坐标
     */
    void updateBall(PointF pointF);

    /**
     * 更新圆的数学坐标
     *
     * @param coordinate 数学坐标
     */
    void updateBallCoordinate(int[] coordinate);
}
