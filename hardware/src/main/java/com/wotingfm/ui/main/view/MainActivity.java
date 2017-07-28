package com.wotingfm.ui.main.view;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.service.WtDeviceControl;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.main.presenter.MainPresenter;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.mine.main.MineActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends TabActivity implements View.OnClickListener {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;
    private TextView tv_4, tv_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航
        //  applySelectedColor();
        applyTextColor(false);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        InitTextView();
        mainPresenter = new MainPresenter(this);
    }

    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_4.setClickable(true);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_5.setClickable(true);
        tv_4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("按钮操作", "按下");
                        //按下状态
                        press();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("按钮操作", "松手");
                        //抬起手后的操作
                        jack();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e("按钮操作", "取消");
                        //抬起手后的操作
                        jack();
                        break;
                }
                return false;
            }
        });

        tv_5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("按钮操作", "按下");
                        //按下状态
                        press_ptt();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("按钮操作", "松手");
                        //抬起手后的操作
                        jack_ptt();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e("按钮操作", "取消");
                        //抬起手后的操作
                        jack_ptt();
                        break;
                }
                return false;
            }
        });

        tabHost = extracted();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("one")
                .setContent(new Intent(this, PlayerActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("two")
                .setContent(new Intent(this, InterPhoneActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("three").setIndicator("three")
                .setContent(new Intent(this, MineActivity.class)));
    }

    private TabHost extracted() {
        return getTabHost();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                // 上一首
                WtDeviceControl.pushUpButton();
                break;
            case R.id.tv_2:
                // 暂停，继续
                WtDeviceControl.pushCenter();
                break;
            case R.id.tv_3:
                // 下一首
                WtDeviceControl.pushDownButton();
                break;
        }
    }

    // 对讲请求
    private void press_ptt() {
        WtDeviceControl.pushPTT();
        tv_5.setBackgroundResource(R.color.app_basic);
    }

    // 松手，释放说话权
    private void jack_ptt() {
        WtDeviceControl.releasePTT();
        tv_5.setBackgroundResource(R.color.white);
    }

    // 开始语音识别
    private void press() {
        WtDeviceControl.pushVoiceStart();
        tv_4.setBackgroundResource(R.color.app_basic);
    }

    // 松手，结束语音识别
    private void jack() {
        WtDeviceControl.releaseVoiceStop();
        tv_4.setBackgroundResource(R.color.white);
    }

    /**
     * 对外提供的方法
     */
   /* public void changeOne() {
        tabHost.setCurrentTabByTag("one");
        Intent intent = new Intent(context,  PlayerActivity.class);
        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    public void changeTwo() {
        tabHost.setCurrentTabByTag("two");
        Intent intent = new Intent(context, InterPhoneActivity.class);
        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    public void changeThree() {
        tabHost.setCurrentTabByTag("three");
        Intent intent = new Intent(context, MineActivity.class);
        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
*/
    // app退出时执行该操作
    private void stop() {
        mainPresenter.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
        EventBus.getDefault().unregister(this);
    }

    private void applySelectedColor() {
        //  -724225
        //  float[] colorHSV = new float[]{0f, 0f, 0f};
        //  int c = Color.HSVToColor(colorHSV);
        int c = -1;
        int color = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));
        StatusBarUtil.setStatusBarColor(this, color, false);
        // StatusBarUtil.setStatusBarColor(context, R.color.white, false);
    }

    private void applyTextColor(boolean b) {
        if (b) {
            StatusBarUtil.StatusBarLightMode(this, false);
        } else {
            StatusBarUtil.StatusBarLightMode(this, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if ("one".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("one");
        } else if ("two".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("two");
        } else if ("three".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("three");
        }
    }


}
