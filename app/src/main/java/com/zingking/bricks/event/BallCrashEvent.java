package com.zingking.bricks.event;

import com.zingking.bricks.entity.MathPoint;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/14
 * @description EventBus 小球碰撞事件
 */
public class BallCrashEvent {
    /**
     * 发生碰撞的砖块的数学坐标点
     */
    private MathPoint mathPoint;

    public BallCrashEvent(MathPoint mathPoint) {
        this.mathPoint = mathPoint;
    }

    public MathPoint getMathPoint() {
        return mathPoint;
    }
}
