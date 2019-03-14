package com.zingking.bricks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zingking.bricks.entity.BackgroundPosition;
import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.mvp.view.BrickActivity;
import com.zingking.bricks.widget.BallView;
import com.zingking.bricks.widget.BrickView;
import com.zingking.bricks.widget.BricksBackgroundView;
import com.zingking.bricks.utils.GameLevelUtils;
import com.zingking.bricks.widget.LineView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static final int HORIZONTAL_NUM = 7;
    public static final int VERTICAL_NUM = 10;
    public static final int PADDING = 60;

    private FrameLayout flContainer;
    private BricksBackgroundView backgroundView;
    private List<Integer> pointXList = new ArrayList<>();
    private List<Integer> pointYList = new ArrayList<>();

    private int[][] brickPosition;
    private float currTouchX = 0;
    private float currTouchY = 0;
    private float lastTouchX = 0;
    private float lastTouchY = 0;
    private Thread thread;
    private BallView ballView;
    private LineView lineView;
    private List<MathPoint> mathPointList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        ballView = new BallView(this);
        backgroundView = new BricksBackgroundView(this);
        backgroundView.setDrawListener(new BricksBackgroundView.IDrawListener() {
            @Override
            public void onSuccess(BackgroundPosition position) {
                if (brickPosition == null) {
                    createBrickPosition();
                }
                drawBrickByLevel();
            }

            @Override
            public void onFailed() {

            }
        });
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d(TAG, "dispatchTouchEvent() called with: event = [" + event + "]");
                if (isDrawing) {
                    return true;
                }
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        currTouchX = 0;
                        currTouchY = 0;
                        float x = event.getX();
                        float y = event.getY();
                        if (x >= 0 && x <= view.getWidth()){
                            currTouchX = x;
                            lastTouchX = currTouchX;
                        }else {
                            currTouchX = lastTouchX;
                        }
                        if (y >= 0 && y <= view.getHeight()) {
                            currTouchY = y;
                            lastTouchY = currTouchY;
                        }else {
                            currTouchY = lastTouchY;
                        }
                        lineView.setLineStopPosition(new PointF(x, y));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDrawing) {
                            break;
                        }
                        x = event.getX();
                        y = event.getY();
                        if (x >= 0 && x <= view.getWidth()){
                            currTouchX = x;
                            lastTouchX = currTouchX;
                        }else {
                            currTouchX = lastTouchX;
                        }
                        if (y >= 0 && y <= view.getHeight()) {
                            currTouchY = y;
                            lastTouchY = currTouchY;
                        }else {
                            currTouchY = lastTouchY;
                        }
                        lineView.setLineStopPosition(new PointF(x, y));
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        Log.i(TAG, "dispatchTouchEvent: 超界");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch: currTouchX = " + currTouchX);
                        Log.i(TAG, "onTouch: currTouchY = " + currTouchY);
                        if (currTouchX > 0 && currTouchY > 0) {
                            PointF lineStartPosition = lineView.getLineStartPosition();
                            PointF lineStopPosition = lineView.getLineStopPosition();
                            angle = Math.atan((lineStopPosition.y - lineStartPosition.y) / ((double) (lineStopPosition
                                    .x - lineStartPosition.x))) / Math.PI *180;
                            startAutoMove();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        backgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
         backgroundView.setHorizontalNum(HORIZONTAL_NUM);
//        backgroundView.setVerticalNum(VERTICAL_NUM);
        backgroundView.setPadding(PADDING);
        lineView = new LineView(this);
        lineView.setLineStartPosition(new PointF(ballRadius, ballRadius));
        flContainer.addView(backgroundView);
        flContainer.addView(ballView);
        flContainer.addView(lineView);
    }

    private final Object object = new Object();
    boolean isDrawing = false;
    private boolean isRun = false;
    private double angle = 0;
    double moveY = 0;
    double moveX = 0;
    private boolean isRight = true;
    private boolean isDown = true;
    float delta = 10f;
    float ballRadius = 20f;

    private void startAutoMove() {
        moveY = Math.sin(angle * Math.PI / 180) * delta;
        moveX = Math.cos(angle * Math.PI / 180) * delta;
        if (isDrawing) {
            return;
        }
        isRight = angle > 0;
        isDown = true;
        moveY = Math.abs(moveY);
        moveX = Math.abs(moveX);
        final PointF pointF = new PointF(ballRadius, ballRadius);
        synchronized (object) {
            object.notify();
        }
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Log.i(TAG, "run: pointF =  " + pointF.toString());
                            if (pointF.y - ballRadius < 0) {
                                synchronized (object) {
                                    isDrawing = false;
                                    lineView.setLineStartPosition(pointF);
                                    pointF.y = ballRadius;
                                    object.wait();
                                }
                            }
                            Thread.sleep(2);
                            if (isRight){
                                if (!changeLR(pointF)) {
                                    pointF.x += moveX;
                                }else {
                                    isRight = false;
                                }
                            } else {
                                boolean b = changeLR(pointF);
                                if (!b) {
                                    pointF.x -= moveX;
                                } else {
                                    isRight = true;
                                }
                            }
                            if (isDown) {
                                if (!changeTB(pointF)) {
                                    pointF.y += moveY;
                                } else {
                                    isDown = false;
                                }
                            } else {
                                if (!changeTB(pointF)) {
                                    pointF.y -= moveY;
                                } else {
                                    isDown = true;
                                }
                            }
                            int[] ballPosition = getBallPosition(pointF);
                            ballView.setPointPosition(pointF);
                            ballView.setBallCoordinate(ballPosition);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (!isRun) {
            thread.start();
            isRun = true;
        }
        isDrawing = true;
    }

    private void createBrickPosition() {
        brickPosition = GameLevelUtils.getLevel();
    }

    private void drawBrickByLevel() {
        final float[] xPositions = backgroundView.getXPositions();// x轴点的数量和 列位置参数(数组长度为数量，值为位置参数)
        final float[] yPositions = backgroundView.getYPositions();// y轴点的数量和 行位置参数
        final Context context = this;
        int yNum = yPositions.length;// y轴砖块数据点的数量
        int xNum = xPositions.length;// x轴砖块数据点的数量
        // 遍历砖块样式
        for (int yCoordinate = 0, yLen = brickPosition.length; yCoordinate < yLen; yCoordinate++) {
            if (yNum - 1 != yLen) {
                Log.e(TAG, "drawBrickByLevel 砖块有 " + yLen + " 行，砖块数据有 " + yNum + "组.");
                continue;
            }
            int xLen = brickPosition[yCoordinate].length;
            if (xNum - 1 != xLen) {
                Log.e(TAG, "drawBrickByLevel 砖块有 " + xLen + " 列，砖块数据有 " + xNum + "组.");
                continue;
            }
            for (int xCoordinate = 0; xCoordinate < xLen; xCoordinate++) {
                if (brickPosition[yCoordinate][xCoordinate] == 1) {
                    MathPoint mathPoint = new MathPoint(xCoordinate, yCoordinate);
                    mathPoint.setRange(xPositions[xCoordinate] + 3f, yPositions[yCoordinate] + 3f,
                            xPositions[xCoordinate + 1] - 3f, yPositions[yCoordinate + 1] - 3f);
                    BrickView brickView = new BrickView(context);
                    brickView.setMathPoint(mathPoint);
                    flContainer.addView(brickView);
                    mathPointList.add(mathPoint);
                }
            }
        }
        ballView.bringToFront();
        lineView.bringToFront();
    }

        // 遍历数据，竖着画(先遍历xPositions)
//        for (int xCoordinate = 0, xLen = xPositions.length - 1; xCoordinate < xLen; xCoordinate++) {
//            for (int yCoordinate = 0, yLen = yPositions.length - 1; yCoordinate < yLen; yCoordinate++) {
//                // 第 yCoordinate 行，第 xCoordinate 列
//                if (brickPosition[yCoordinate][xCoordinate] == 1) {
//                    BrickView ballView = new BrickView(context);
//                    ballView.setLtPosition(new float[]{xPositions[xCoordinate] + 3f, yPositions[yCoordinate] + 3f});
//                    ballView.setRbPosition(new float[]{xPositions[xCoordinate + 1] - 3f, yPositions[yCoordinate + 1]
//                            - 3f});
//                    flContainer.addView(ballView);
//                }
//            }
//        }

    private void drawBrickByRandom(){
        pointXList.clear();
        pointYList.clear();
        flContainer.removeAllViews();
        Random random = new Random();
        // 设置两个变量可以调整横向砖块的密度
        for (int i = 0; i < 30; i++) {// 这可以限制横向砖块个数
            random.setSeed(System.nanoTime());
            int pointX = random.nextInt(10);// 这里可以限制最大列数
            pointXList.add(pointX);
        }
        Random randomY = new Random();
        for (int i = 0; i < 50; i++) {// 这可以限制纵向砖块个数
            randomY.setSeed(System.nanoTime());
            int pointX = randomY.nextInt(10);// 这里可以限制最大行数
            pointYList.add(pointX);
        }
        float[] xPositons = backgroundView.getXPositions();// x轴点的数量和位置参数(数组长度为数量，值为位置参数)
        float[] yPositons = backgroundView.getYPositions();// y轴点的数量和位置参数
        for (int i = 0, yLen = yPositons.length - 1; i < yLen; i++) {
            for (int j = 0, xLen = xPositons.length - 1; j < xLen; j++) {
                if (pointXList.contains(i) && pointYList.contains(j)) {
                    pointXList.remove(Integer.valueOf(i));
                    pointYList.remove(Integer.valueOf(j));
                    MathPoint mathPoint = new MathPoint(i, j);
                    mathPoint.setRange(xPositons[j] + 3f, yPositons[i] + 3f,
                            xPositons[j + 1] - 3f, yPositons[i + 1] - 3f);
                    BrickView ballView = new BrickView(this);
                    ballView.setMathPoint(mathPoint);
//                    ballView.setLtPosition(new float[]{xPositons[j] + 3f, yPositons[i] + 3f});
//                    ballView.setRbPosition(new float[]{xPositons[j + 1] - 3f, yPositons[i + 1] - 3f});
                    flContainer.addView(ballView);
                }
            }
        }
    }

    public void btnClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_change:
                drawBrickByRandom();
                break;
            case R.id.btn_mvp:
                startActivity(new Intent(this, BrickActivity.class));
                break;
            default:
        }
    }


    /**
     * 左右方向是否改变
     */
    private boolean changeLR(PointF pointF) {
        int[] ballPosition = getBallPosition(pointF);
        boolean result = false;
        for (MathPoint mathPoint : mathPointList) {
            RectF range = mathPoint.getRange();
            if (isRight) {
                if (ballPosition[0] + 1 == mathPoint.getX()&& ballPosition[1] == mathPoint.getY()) {
                    // 如果小球向右移动，当小球x坐标大于方块左侧，则表示需要改为向左移动
                    result = pointF.x + ballRadius >= range.left;
                    break;
                }
            } else {
                if (ballPosition[0] - 1 == mathPoint.getX() && ballPosition[1] == mathPoint.getY()) {
                    // 如果小球向左移动，当小球x坐标小于方块右侧，则表示需要改为向向右移动
                    result = pointF.x - ballRadius <= range.right && pointF.x - ballRadius >= range.left;
                    break;
                }
            }
        }
        // 如果未发生碰撞则检测边缘
        if (!result) {
            if(isRight){
                result = pointF.x + ballRadius >= backgroundView.getWidth();
            }else {
                result = pointF.x - ballRadius < 0;
            }
        }
        return result;
    }

    /**
     * 上下方向是否改变
     */
    private boolean changeTB(PointF pointF) {
        boolean result = false;
        int[] ballPosition = getBallPosition(pointF);
        for (MathPoint mathPoint : mathPointList) {
            RectF range = mathPoint.getRange();
            if (isDown) {
                if (ballPosition[1] + 1 == mathPoint.getY()&& ballPosition[0] == mathPoint.getX()) {
                    // 如果小球向下移动，当小球y坐标大于方块上侧，则表示需要改为向上移动
                    result = pointF.y + ballRadius >= range.top;
                }
            } else {
                if (ballPosition[1] - 1 == mathPoint.getY() && ballPosition[0] == mathPoint.getX()) {
                    // 如果小球向上移动，当小球y坐标小于方块下侧，则表示需要改为向向下移动
                    result = pointF.y - ballRadius <= range.bottom && pointF.y - ballRadius >= range.top;
                }
            }
        }
        // 如果未发生碰撞则检测边缘
        if (!result) {
            if(isDown){
                result = pointF.y + ballRadius >= backgroundView.getHeight();
            }else {
                result = pointF.y - ballRadius < 0;
            }
        }
        return result;
    }

    private int[] getBallPosition(PointF pointF) {
        final float[] xPositions = backgroundView.getXPositions();// x轴点的数量和 列位置参数(数组长度为数量，值为位置参数)
        final float[] yPositions = backgroundView.getYPositions();// y轴点的数量和 行位置参数
        int xCoordinate = -1;
        int yCoordinate = -1;
        if (pointF.x<xPositions[0]){
            xCoordinate = -1;
        } else if (pointF.x > xPositions[xPositions.length - 1]) {
            xCoordinate = xPositions.length -1;
        }

        if (pointF.y<yPositions[0]){
            yCoordinate = -1;
        } else if (pointF.y > yPositions[yPositions.length - 1]) {
            yCoordinate = yPositions.length -1;
        }

        for (int i = 0, len = xPositions.length - 1; i < len; i++) {
            if (pointF.x > xPositions[i] && pointF.x < xPositions[i + 1]) {
                xCoordinate = i;
                break;
            }
        }
        for (int i = 0, len = yPositions.length - 1; i < len; i++) {
            if (pointF.y > yPositions[i] && pointF.y < yPositions[i + 1]) {
                yCoordinate = i;
                break;
            }
        }
        return new int[]{xCoordinate, yCoordinate};
    }
}
