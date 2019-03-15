package com.zingking.bricks.listener;

import android.graphics.PointF;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/15
 * @description
 */
public interface IDirectionChangeListener {
    boolean changeLR(boolean isRight, PointF pointF);

    boolean changeTB(boolean isDown, PointF pointF);
}
