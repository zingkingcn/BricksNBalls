package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2018/12/13
 * @description 辅助绘制砖块的控件，只需要设置 横向 的砖块个数，大小自动计算
 */

public class BricksBackgroundView extends View {
    private static final String TAG = "BricksBackgroundView";

    private Paint paint;
    private Canvas canvas;
    private int padding = 60;
    private int width;
    private int height;

    /**
     * 高和个数不能整除时对齐的偏移量
     */
    float deltaY = 0F;
    /**
     * 宽和个数不能整除时对齐的偏移量
     */
    float deltaX = 0F;
    /**
     * 横向砖块个数
     */
    private int horizontalNum = 0;
    /**
     * 纵向砖块个数
     */
    private int verticalNum = 0;

    public BricksBackgroundView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#00ffff"));
    }

    @Deprecated
    @Override
    public void setPadding(int left, int top, int right, int bottom) {

    }

    public void setPadding(int value) {
        padding = value;
    }

    public void setHorizontalNum(int value) {
        this.horizontalNum = value;
    }

    public void setVerticalNum(int value) {
        this.verticalNum = value;
    }

    public void setContentGravity(){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        width = getWidth();
        height = getHeight();
        if (horizontalNum != 0) {
            drawByHorizontalNum();
        } else {
            drawByVerticalNum();
        }
    }

    private void drawByVerticalNum() {
        float everyHeight = (height - padding) / verticalNum;
        int verticalLineNum = verticalNum + 1;
        float lastY = verticalNum * everyHeight;
//        deltaY = height - lastY - padding; // if bottom
//        deltaY = deltaY / 2;// if center
        for (int i = 0; i < verticalLineNum; i++) {
            drawHorizontalLine(i, everyHeight);
        }

        float everyWidth = everyHeight;
        int horizontalNum = (int) ((width - padding) / everyWidth);
        int horizontalLineNum = horizontalNum + 1;
        float lastX = horizontalNum * everyWidth;
//        deltaX = width - lastX - padding; // if right
//        deltaX = deltaX / 2;// if center
        for (int i = 0; i < horizontalLineNum; i++) {
            drawVerticalLine(i, everyWidth);
        }
    }

    private void drawByHorizontalNum() {
        float everyWidth = (width - padding) / horizontalNum;
        int horizontalLineNum = horizontalNum + 1;
        float lastX = horizontalNum * everyWidth;
//        deltaX = width - lastX - padding; // if right
//        deltaX = deltaX / 2;// if center
        for (int i = 0; i < horizontalLineNum; i++) {
            drawVerticalLine(i, everyWidth);
        }

        float everyHeight = everyWidth;
        int verticalNum = (int) ((height - padding) / everyHeight);
        int verticalLineNum = verticalNum + 1;
        float lastY = verticalNum * everyHeight;
//        deltaY = height - lastY - padding;// if bottom
//        deltaY = deltaY / 2; // if center
        for (int i = 0; i < verticalLineNum; i++) {
            drawHorizontalLine(i, everyHeight);
        }
    }

    private void drawHorizontalLine(int index, float everyHeight) {
        float y = index * everyHeight;
        canvas.drawLine(0, y + padding / 2 + deltaY, getWidth(), y + padding / 2 + deltaY, paint);
    }

    private void drawVerticalLine(int index, float everyWidth) {
        float x = index * everyWidth;
        canvas.drawLine(x + padding / 2 + deltaX, 0, x + padding / 2 + deltaX, getHeight(), paint);
    }
}
