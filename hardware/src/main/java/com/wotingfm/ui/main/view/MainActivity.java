package com.wotingfm.ui.main.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.InterPhoneActivity;
import com.wotingfm.ui.main.presenter.MainPresenter;
import com.wotingfm.ui.test.DuiJiangActivity;
import com.wotingfm.ui.test.MineActivity;
import com.wotingfm.ui.test.PlayerActivity;


public class MainActivity extends TabActivity {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        InitTextView();
        mainPresenter = new MainPresenter(this);
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
    public static void changeOne() {
        tabHost.setCurrentTabByTag("one");
    }

    public static void changeTwo() {
        tabHost.setCurrentTabByTag("two");
    }

    public static void changeThree() {
        tabHost.setCurrentTabByTag("three");
    }

    // app退出时执行该操作
    private void stop() {
        mainPresenter.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }
}
