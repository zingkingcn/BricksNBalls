package com.zingking.bricks.entity;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2019/3/14
 * @description
 */
public class BackgroundPosition {
    /**
     * x轴点的数量和 列位置参数(数组长度为数量，值为位置参数)
     */
    private float[] xPositions;
    /**
     * y轴点的数量和 行位置参数
     */
    private float[] yPositions;
    private int width;
    private int height;

    public BackgroundPosition() {
    }

    public BackgroundPosition(float[] xPositions, float[] yPositions, int width, int height) {
        this.xPositions = xPositions;
        this.yPositions = yPositions;
        this.width = width;
        this.height = height;
    }

    public float[] getXPositions() {
        return xPositions;
    }

    public void setXPositions(float[] xPositions) {
        this.xPositions = xPositions;
    }

    public float[] getYPositions() {
        return yPositions;
    }

    public void setYPositions(float[] yPositions) {
        this.yPositions = yPositions;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
