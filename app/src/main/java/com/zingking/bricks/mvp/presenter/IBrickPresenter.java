package com.zingking.bricks.mvp.presenter;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.listener.IDirectionListener;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/14
 * @description
 */
public interface IBrickPresenter extends IDirectionListener {
    /**
     * 创建关卡
     */
    void createLevel();

    void onBackgroundTouch(View view, MotionEvent motionEvent);

    void removeBrick(MathPoint mathPoint);

    void updateLine(boolean isStart, PointF pointF);

    void allBallEnded(boolean ended);

}
