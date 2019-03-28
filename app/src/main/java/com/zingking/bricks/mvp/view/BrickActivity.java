package com.zingking.bricks.mvp.view;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zingking.bricks.R;
import com.zingking.bricks.entity.MathPoint;
import com.zingking.bricks.event.BallCrashEvent;
import com.zingking.bricks.listener.IDirectionChangeListener;
import com.zingking.bricks.mvp.presenter.BrickPresenter;
import com.zingking.bricks.widget.BallView;
import com.zingking.bricks.widget.BrickView;
import com.zingking.bricks.widget.BricksBackgroundView;
import com.zingking.bricks.widget.LineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
    // README 遍历的时候用List会报ConcurrentModificationException
    private CopyOnWriteArrayList<BallView> ballViewList = new CopyOnWriteArrayList<>();
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
        BallView ballView = new BallView(this);
        BallView ballView1 = new BallView(this);
        BallView ballView2 = new BallView(this);
        BallView ballView3 = new BallView(this);
        BallView ballView4 = new BallView(this);
        BallView ballView5 = new BallView(this);
        BallView ballView6 = new BallView(this);
        BallView ballView7 = new BallView(this);
        BallView ballView8 = new BallView(this);
        flContainer.addView(ballView);
        flContainer.addView(ballView1);
        flContainer.addView(ballView2);
        flContainer.addView(ballView3);
        flContainer.addView(ballView4);
        flContainer.addView(ballView5);
        flContainer.addView(ballView6);
        flContainer.addView(ballView7);
        flContainer.addView(ballView8);

        ballViewList.add(ballView);
        ballViewList.add(ballView1);
        ballViewList.add(ballView2);
        ballViewList.add(ballView3);
        ballViewList.add(ballView4);
        ballViewList.add(ballView5);
        ballViewList.add(ballView6);
        ballViewList.add(ballView7);
        ballViewList.add(ballView8);
    }

    @Override
    public void updateLine(boolean isStart, PointF pointF) {
        if (isStart) {
            lineView.setLineStartPosition(pointF);
        } else {
            lineView.setLineStopPosition(pointF);
        }
    }

    private static final String TAG = "BrickActivity";

    @Override
    public void updateBall(PointF pointF, final double angle, final float delta) {
        Log.d(TAG, "updateBall() called with: pointF = [" + pointF + "], angle = [" + angle + "], delta = [" + delta + "]");

//        ballView.setPointPosition(pointF);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (BallView ballView : ballViewList) {
                        Thread.sleep(64);
                        ballView.startAutoMove(angle, delta, new IDirectionChangeListener() {
                            @Override
                            public boolean changeLR(boolean isRight, PointF pointF) {
                                return brickPresenter.changeLR(isRight, pointF, angle);
                            }

                            @Override
                            public boolean changeTB(boolean isDown, PointF pointF) {
                                return brickPresenter.changeTB(isDown, pointF, angle);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            public void remove(final BrickView brickView) {
                // README 不用post会报错Attempt to read from field 'int android.view.View.mViewFlags' on a null object
                // reference
                flContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        flContainer.removeView(brickView);
                    }
                });
                brickPresenter.removeBrick(brickView.getMathPoint());
            }
        });
    }
}
