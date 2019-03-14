package com.zingking.bricks.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.zingking.bricks.entity.BackgroundPosition;

/**
 * Copyright © 2018 www.zingking.cn All Rights Reserved.
 *
 * @author Z.kai
 * @date 2018/12/13
 * @description 辅助绘制砖块的控件，只需要设置 横向 的砖块个数，大小自动计算
 */

public class BricksBackgroundView extends View {
    private static final String TAG = "BricksBackgroundView";
    /**
     * 高和个数不能整除时对齐的偏移量
     */
    float deltaY = 0F;
    /**
     * 宽和个数不能整除时对齐的偏移量
     */
    float deltaX = 0F;
    private Paint paint;
    private Paint txtPaint;
    private Canvas canvas;
    private int padding = 60;
    private int width;
    private int height;
    private BackgroundPosition position = new BackgroundPosition();
    /**
     *
     */
    private float[] xPositions;
    private float[] yPositions;
    private IDrawListener iDrawListener;
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
        paint.setStrokeWidth(6);
        paint.setColor(Color.parseColor("#00ffff"));

        txtPaint = new Paint();
        txtPaint.setStrokeWidth(6);
        txtPaint.setTextSize(30f);
        txtPaint.setColor(Color.parseColor("#ff0000"));
    }

    public void setPadding(int value) {
        padding = value;
    }

    public int getHorizontalNum() {
        return horizontalNum;
    }

    public void setHorizontalNum(int value) {
        this.horizontalNum = value;
    }

    public int getVerticalNum() {
        return verticalNum;
    }

    public void setVerticalNum(int value) {
        this.verticalNum = value;
    }

    public void setContentGravity() {
        // TODO: 2018/12/19 完成 deltaX，deltaY的逻辑 by Z.kai
    }

    public void setDrawListener(IDrawListener iDrawListener) {
        this.iDrawListener = iDrawListener;
    }

    public float[] getXPositions() {
        return xPositions;
    }

    public float[] getYPositions() {
        return yPositions;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        super.onDraw(canvas);
        this.canvas = canvas;
        width = getWidth();
        height = getHeight();
        if (horizontalNum != 0) {
            drawByHorizontalNum();
            if (iDrawListener != null) {
                updatePosition();
                iDrawListener.onSuccess(position);
            }
        } else if (verticalNum != 0) {
            drawByVerticalNum();
            if (iDrawListener != null) {
                updatePosition();
                iDrawListener.onSuccess(position);
            }
        } else {
            if (iDrawListener != null) {
                iDrawListener.onFailed();
            }
        }
    }

    @Deprecated
    @Override
    public void setPadding(int left, int top, int right, int bottom) {

    }

    private void drawByHorizontalNum() {
        float everyWidth = (width - padding) / horizontalNum;
        int horizontalLineNum = horizontalNum + 1;
        float lastX = horizontalNum * everyWidth;
//        deltaX = width - lastX - padding; // if right
//        deltaX = deltaX / 2;// if center
        xPositions = new float[horizontalLineNum];
        // 根据横向砖块画竖线
        for (int i = 0; i < horizontalLineNum; i++) {
            drawVerticalLine(i, everyWidth);
        }

        float everyHeight = everyWidth;
        int verticalNum = (int) ((height - padding) / everyHeight);
        setVerticalNum(verticalNum);
        int verticalLineNum = verticalNum + 1;
        float lastY = verticalNum * everyHeight;
//        deltaY = height - lastY - padding;// if bottom
//        deltaY = deltaY / 2; // if center
        yPositions = new float[verticalLineNum];
        // 根据竖向砖块画横线
        for (int i = 0; i < verticalLineNum; i++) {
            drawHorizontalLine(i, everyHeight);
        }

        // 画方块坐标
//        for (int i = 0, len = xPositions.length; i < len; i++) {
//            for (int j = 0, len2 = yPositions.length; j < len2; j++) {
//                canvas.drawText(i + "," + j, xPositions[i]+20, yPositions[j]+40, txtPaint);
//            }
//        }
    }

    private void updatePosition() {
        position.setWidth(getWidth());
        position.setHeight(getHeight());
        position.setXPositions(xPositions);
        position.setYPositions(yPositions);
    }

    private void drawByVerticalNum() {
        float everyHeight = (height - padding) / verticalNum;
        int verticalLineNum = verticalNum + 1;
        float lastY = verticalNum * everyHeight;
//        deltaY = height - lastY - padding; // if bottom
//        deltaY = deltaY / 2;// if center
        yPositions = new float[verticalLineNum];
        // 根据竖向砖块画横线
        for (int i = 0; i < verticalLineNum; i++) {
            drawHorizontalLine(i, everyHeight);
        }

        float everyWidth = everyHeight;
        int horizontalNum = (int) ((width - padding) / everyWidth);
        setHorizontalNum(horizontalNum);
        int horizontalLineNum = horizontalNum + 1;
        float lastX = horizontalNum * everyWidth;
//        deltaX = width - lastX - padding; // if right
//        deltaX = deltaX / 2;// if center
        xPositions = new float[horizontalLineNum];
        // 根据横向砖块画竖线
        for (int i = 0; i < horizontalLineNum; i++) {
            drawVerticalLine(i, everyWidth);
        }
    }

    private void drawVerticalLine(int index, float everyWidth) {
        float x = index * everyWidth;
        // 画辅助线
//        canvas.drawLine(x + padding / 2 + deltaX, 0, x + padding / 2 + deltaX, getHeight(), paint);
        xPositions[index] = x + padding / 2 + deltaX;
    }

    private void drawHorizontalLine(int index, float everyHeight) {
        float y = index * everyHeight;
        // 画辅助线
//        canvas.drawLine(0, y + padding / 2 + deltaY, getWidth(), y + padding / 2 + deltaY, paint);
        yPositions[index] = y + padding / 2 + deltaY;
    }

    public interface IDrawListener {
        /**
         * 绘制成功
         *
         * @param position 背景位置相关参数
         */
        void onSuccess(BackgroundPosition position);

        /**
         * 绘制失败
         */
        void onFailed();
    }

}
