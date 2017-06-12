package com.wotingfm.common.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;

/**
 * 悬浮窗服务----在主页中启动
 * 作者：xinlong on 2016/9/5 11:37
 * 邮箱：645700751@qq.com
 */
public class FloatingWindowService extends Service {

    public static final String OPERATION = "operation";
    public static final int OPERATION_SHOW = 100;
    public static final int OPERATION_HIDE = 101;
    private static final int HANDLE_CHECK_ACTIVITY = 200;

    private boolean isAdded = false; // 是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private View floatView;
    private LinearLayout lin_d;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        // 注册广播监听是否打开个人中心页面
        IntentFilter m = new IntentFilter();
        m.addAction(BroadcastConstants.MINE_ACTIVITY_CHANGE);
        registerReceiver(broadCast, m);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);
        switch (operation) {
            case OPERATION_SHOW:
                mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
                mHandler.sendEmptyMessage(HANDLE_CHECK_ACTIVITY);
                break;
            case OPERATION_HIDE:
                mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_CHECK_ACTIVITY:
                    if (!isAdded) {
                        wm.addView(floatView, params);
                        isAdded = true;
                    }
                    mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                    break;
            }
        }
    };

    /**
     * 创建悬浮窗
     */
    private void createFloatView() {
        floatView = LayoutInflater.from(this).inflate(R.layout.dialog_float, null);
        LinearLayout lin_a = (LinearLayout) floatView.findViewById(R.id.lin_a);
        LinearLayout lin_b = (LinearLayout) floatView.findViewById(R.id.lin_b);
        LinearLayout lin_c = (LinearLayout) floatView.findViewById(R.id.lin_c);
        lin_d = (LinearLayout) floatView.findViewById(R.id.lin_d);
        floatView.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.test_aa));
        //btn_floatView.setText("我听科技");
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        //设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
         * 那么优先级会降低一些, 即拉下通知栏不可见
         */
        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */
//		params.gravity= Gravity.BOTTOM|Gravity.RIGHT;
//		floatView.setPadding(0,0,20,20);
        // 设置悬浮窗的长得宽
        params.width = PhoneMsgManager.ScreenWidth / 6;
        params.height = PhoneMsgManager.ScreenWidth / 6;
        // 设置悬浮窗的Touch监听
        floatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                //startActivity(intent);

                if (GlobalStateConfig.destination == 0) {
                    GlobalStateConfig.destination = 1;
                    setView(1);
                } else if (GlobalStateConfig.destination == 1) {
                    GlobalStateConfig.destination = 0;
                    setView(0);
                }
            }
        });

        floatView.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        //startActivity(intent);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        Log.e("悬浮窗", params.y + "=======" + params.x);
                        // 更新悬浮窗位置
                        wm.updateViewLayout(floatView, params);
                        break;
                }
                return false;
            }
        });
        wm.addView(floatView, params);
        isAdded = true;
    }

    /// type 0 第一档位  1 第二档位
    private void setView(int type) {
        int viewType = 1;
        String activityA = GlobalStateConfig.activityA;
        String activityB = GlobalStateConfig.activityB;

        if (type == 0) {
            if (activityA.equals("A")) {
                viewType = 1;
            } else if (activityA.equals("B")) {
                viewType = 2;
            } else if (activityA.equals("C")) {
                viewType = 3;
            }
        } else {
            if (activityB.equals("A")) {
                viewType = 1;
            } else if (activityB.equals("B")) {
                viewType = 2;
            } else if (activityB.equals("C")) {
                viewType = 3;
            }
        }
        sendBroadCast(viewType);
        if (viewType == 1) {
            lin_d.setBackgroundResource(R.mipmap.test_bb);
        } else if (viewType == 2) {
            lin_d.setBackgroundResource(R.mipmap.test_aa);
        } else if (viewType == 3) {
            lin_d.setBackgroundResource(R.mipmap.test_cc);
        }
    }

    // 发送广播
    private void sendBroadCast(int type) {
        Intent push = new Intent(BroadcastConstants.ACTIVITY_CHANGE);
        Bundle bundle = new Bundle();
        bundle.putInt("viewType", type);
        push.putExtras(bundle);
        sendBroadcast(push);
    }

    //接收定时服务发送过来的广播  用于界面更改
    private BroadcastReceiver broadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.MINE_ACTIVITY_CHANGE)) {
                // 按钮切换-----档位切换广播
                int viewType = intent.getIntExtra("viewType",1);
                if (viewType == 1) {
                    lin_d.setBackgroundResource(R.mipmap.test_bb);
                } else if (viewType == 2) {
                    lin_d.setBackgroundResource(R.mipmap.test_aa);
                } else if (viewType == 3) {
                    lin_d.setBackgroundResource(R.mipmap.test_cc);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCast);
    }
}
