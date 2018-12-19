package com.zingking.bricks.widget;

/**
 * Copyright (c) 2018, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2018/12/19
 * @description 自定义控件绘制结果监听
 */
public interface IDrawListener {
    /**
     * 绘制成功
     */
    void onSuccess();

    /**
     * 绘制失败
     */
    void onFailed();
}
