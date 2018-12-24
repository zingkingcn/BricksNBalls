package com.zingking.bricks.entity;

import android.graphics.RectF;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2018/12/24
 * @description 数学坐标系中的点的表示，例如  (0,1) (-2,-2)d等
 * <p>这里表示方块的位置，左上角为 (0,0)，往右为正，往下为重
 * </p>
 */

public class MathPoint {
    private int x = Integer.MAX_VALUE;
    private int y = Integer.MAX_VALUE;
    private RectF range = new RectF(0, 0, 0, 0);

    public MathPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setRange(float left, float top, float right, float bottom) {
        this.range.set(left, top, right, bottom);
    }

    public RectF getRange() {
        return range;
    }

    public void setRange(RectF range) {
        this.range = range;
    }

    public boolean isAvailable() {
        return isPointAvailable() && isRangeAvailable();
    }

    public boolean isPointAvailable() {
        return x != Integer.MAX_VALUE && y != Integer.MAX_VALUE;
    }

    public boolean isRangeAvailable() {
        return range.right > range.left && range.bottom > range.top;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MathPoint mathPoint = (MathPoint) o;
        return x == mathPoint.x && y == mathPoint.y;
    }
}
