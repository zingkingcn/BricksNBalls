package com.zingking.bricks.listener;

import android.graphics.PointF;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/15
 * @description 小球移动监听
 */
public interface IBallMoveListener extends IDirectionListener{

    /**
     * 小球移动到底线，不一定是最终位置，所有小球最后都会移动到第一个到底线的位置
     * @param ballPosition 当前位置
     */
    void moveEnd(PointF ballPosition);
}
