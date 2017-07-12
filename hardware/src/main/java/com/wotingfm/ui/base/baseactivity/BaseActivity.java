package com.wotingfm.ui.base.baseactivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import static com.wotingfm.ui.intercom.main.view.InterPhoneActivity.context;

/**
 * App
 * Created by Administrator on 9/6/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    // 设置android app 的字体大小不受系统字体大小改变的影响
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity(this);
    }
}
