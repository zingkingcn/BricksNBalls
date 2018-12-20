package com.zingking.bricks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zingking.bricks.widget.BrickView;
import com.zingking.bricks.widget.BricksBackgroundView;
import com.zingking.bricks.widget.GameLevelUtils;
import com.zingking.bricks.widget.IDrawListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        backgroundView = new BricksBackgroundView(this);
        backgroundView.setDrawListener(new IDrawListener() {
            @Override
            public void onSuccess() {
                if (brickPosition == null) {
                    createBrickPosition();
                }
                drawBrickByLevel();
            }

            @Override
            public void onFailed() {

            }
        });
        backgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        flContainer.addView(backgroundView);
        // backgroundView.setHorizontalNum(HORIZONTAL_NUM);
        backgroundView.setVerticalNum(VERTICAL_NUM);
        backgroundView.setPadding(PADDING);
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
                    BrickView ballView = new BrickView(context);
                    ballView.setLtPosition(new float[]{xPositions[xCoordinate] + 3f, yPositions[yCoordinate] + 3f});
                    ballView.setRbPosition(new float[]{xPositions[xCoordinate + 1] - 3f, yPositions[yCoordinate + 1] - 3f});
                    flContainer.addView(ballView);
                }
            }
        }
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
                    BrickView ballView = new BrickView(this);
                    ballView.setLtPosition(new float[]{xPositons[j] + 3f, yPositons[i] + 3f});
                    ballView.setRbPosition(new float[]{xPositons[j + 1] - 3f, yPositons[i + 1] - 3f});
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
            default:
        }
    }
}
