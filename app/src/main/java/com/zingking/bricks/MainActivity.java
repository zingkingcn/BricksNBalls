package com.zingking.bricks;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zingking.bricks.widget.BricksBackgroundView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static final int HORIZONTAL_NUM = 10;
    public static final int VERTICAL_NUM = 10;
    public static final int PADDING = 60;

    private FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        BricksBackgroundView backgroundView = new BricksBackgroundView(this);
        backgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        flContainer.addView(backgroundView);
        backgroundView.setHorizontalNum(HORIZONTAL_NUM);
//        backgroundView.setVerticalNum(VERTICAL_NUM);
        backgroundView.setPadding(PADDING);

//        ViewGroup.LayoutParams layoutParams = flContainer.getLayoutParams();
//        int width = layoutParams.width;
//        float everyWidth = (float) width / HORIZONTAL_NUM;
//        layoutParams.height = (int) (VERTICAL_NUM * everyWidth) + (PADDING / 2);
//        Log.i(TAG, "kai ---- initView layoutParams.toString() ----> " + layoutParams.width + "  " + layoutParams.height);
//        flContainer.setLayoutParams(layoutParams);

    }
}
