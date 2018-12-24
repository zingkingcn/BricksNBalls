package com.zingking.bricks.widget;

/**
 * Copyright (c) 2018, Z.kai All rights reserved.
 *
 * @author Z.kai
 * @date 2018/12/19
 * @description
 */
public class GameLevelUtils {
    private static final String TAG = "GameLevelUtils";

    private GameLevelUtils() {
    }

    public static int[][] getLevel() {
        int[] line0 = new int[]{0, 1, 1, 1, 1, 1, 0};
        int[] line1 = new int[]{1, 0, 1, 1, 1, 1, 1};
        int[] line2 = new int[]{1, 1, 1, 0, 1, 1, 1};
        int[] line3 = new int[]{1, 1, 1, 0, 1, 1, 1};
        int[] line4 = new int[]{0, 1, 1, 0, 1, 1, 1};
        int[] line5 = new int[]{1, 1, 1, 1, 1, 1, 1};
        int[] line6 = new int[]{1, 0, 1, 0, 1, 1, 1};
        int[] line7 = new int[]{1, 1, 1, 0, 1, 1, 1};
        int[] line8 = new int[]{1, 1, 1, 1, 1, 1, 1};
        int[] line9 = new int[]{0, 1, 1, 1, 1, 1, 0};
        int[] line10 = new int[]{0, 1, 1, 1, 1, 1, 0};
        int[][] brick = new int[9][7];
        brick[0] = line0;
        brick[1] = line1;
        brick[2] = line2;
        brick[3] = line3;
        brick[4] = line4;
        brick[5] = line5;
        brick[6] = line6;
        brick[7] = line7;
        brick[8] = line8;
//        brick[9] = line9;
//        brick[10] = line10;
        // [4][0]：表示第4行第0列，在坐标系中是(0,4)点
        return brick;
    }

}
