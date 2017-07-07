package com.wotingfm.ui.main.view;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.BaseActivity;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.main.presenter.MainPresenter;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.mine.main.MineActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;


public class MainActivity extends TabActivity {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;
    private MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        context = this;
 /*       if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // 设置全屏，并且不会Activity的布局让出状态栏的空间
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
*/
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        InitTextView();
        mainPresenter = new MainPresenter(this);

        //  applySelectedColor();
        //    applyTextColor(false);
    }

    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
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
        // -724225
//        float[] colorHSV = new float[]{0f, 0f, 0f};
//        int c = Color.HSVToColor(colorHSV);
        int c = -1;
        int color = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));
        StatusBarUtil.setStatusBarColor(context, color, false);
//        StatusBarUtil.setStatusBarColor(context, R.color.white, false);
    }

    private void applyTextColor(boolean b) {
        if (b) {
            StatusBarUtil.StatusBarLightMode(context, false);
        } else {
            StatusBarUtil.StatusBarLightMode(context, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if ("one".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("one");
            Intent intent = null;
            if (AppManager.getAppManager().currentActivity() != null) {
                intent = new Intent(context, AppManager.getAppManager().currentActivity().getClass());
            } else {
                intent = new Intent(context, PlayerActivity.class);
            }
            intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if ("two".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("two");
            Intent intent = new Intent(context, InterPhoneActivity.class);
            intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
        } else if ("three".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("three");
            Intent intent = new Intent(context, MineActivity.class);
            intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
        }
    }

}
