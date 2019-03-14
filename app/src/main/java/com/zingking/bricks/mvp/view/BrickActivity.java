package com.zingking.bricks.mvp.view;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zingking.bricks.R;
import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.event.BallCrashEvent;
import com.zingking.bricks.mvp.presenter.BrickPresenter;
import com.zingking.bricks.widget.BallView;
import com.zingking.bricks.widget.BrickView;
import com.zingking.bricks.widget.BricksBackgroundView;
import com.zingking.bricks.widget.LineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class BrickActivity extends Activity implements IBrickView {

    public static final int HORIZONTAL_NUM = 7;
    public static final int VERTICAL_NUM = 10;
    public static final int PADDING = 60;
    float ballRadius = 20f;
    private BrickPresenter brickPresenter;
    private FrameLayout flContainer;
    private BricksBackgroundView bricksBackgroundView;
    private LineView lineView;
    private BallView ballView;
    private HashMap<MathPoint, BrickView> pointViewHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brick);
        EventBus.getDefault().register(this);
        initView();
        brickPresenter = new BrickPresenter(this);
        brickPresenter.createLevel();
    }

    private void initView() {
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void addBackgroundView(BricksBackgroundView.IDrawListener iDrawListener) {
        bricksBackgroundView = new BricksBackgroundView(this);
        bricksBackgroundView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        bricksBackgroundView.setHorizontalNum(HORIZONTAL_NUM);
//        backgroundView.setVerticalNum(VERTICAL_NUM);
        bricksBackgroundView.setPadding(PADDING);
        bricksBackgroundView.setDrawListener(iDrawListener);
        bricksBackgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                brickPresenter.onBackgroundTouch(view, event);
                if (false) {
                    view.performClick();
                }
                return true;
            }
        });
        flContainer.addView(bricksBackgroundView);
    }

    @Override
    public void addBrickView(MathPoint mathPoint) {
        BrickView brickView = new BrickView(this);
        brickView.setMathPoint(mathPoint);
        flContainer.addView(brickView);
        pointViewHashMap.put(mathPoint, brickView);
    }

    @Override
    public void addLineView() {
        lineView = new LineView(this);
        lineView.setLineStartPosition(new PointF(ballRadius, ballRadius));
        flContainer.addView(lineView);
    }

    @Override
    public void addBallView() {
        ballView = new BallView(this);
        flContainer.addView(ballView);
    }

    @Override
    public void updateLine(boolean isStart, PointF pointF) {
        if (isStart) {
            lineView.setLineStartPosition(pointF);
        } else {
            lineView.setLineStopPosition(pointF);
        }
    }

    @Override
    public void updateBall(PointF pointF) {
        ballView.setPointPosition(pointF);
    }

    @Override
    public void updateBallCoordinate(int[] coordinate) {
        ballView.setBallCoordinate(coordinate);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ballCrash(final BallCrashEvent ballCrashEvent) {
        final BrickView brickView = pointViewHashMap.get(ballCrashEvent.getMathPoint());
        brickView.shining(new BrickView.ShiningListener() {
            @Override
            public void remove(BrickView brickView) {
                flContainer.removeView(brickView);
                brickPresenter.removeBrick(brickView.getMathPoint());
            }
        });
    }
}
