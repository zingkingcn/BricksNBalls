package com.zingking.bricks.listener;

import android.graphics.PointF;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2019/3/29
 * @description
 */

public interface IDirectionListener {

    /**
     * 判断小球是否需要改变左右方向，即判断小球是否和砖块发生碰撞，发生碰撞了则需要改变方向
     *
     * @param isRight 小球右移动
     * @param pointF  小球圆心真实坐标点
     * @param angle   发射线角度，在43-48范围内需要判断对角方块位置是否发生碰撞
     * @return true:方向发生改变<p>false:方向未发生改变</p>
     */
    boolean changeLR(boolean isRight, PointF pointF, double angle);

    boolean changeTB(boolean isDown, PointF pointF, double angle);
}
