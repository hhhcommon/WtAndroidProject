package com.wotingfm.common.application;

import android.content.Context;

import com.woting.commonplat.application.WtApp;
import com.woting.commonplat.manager.NetWorkManager;
import com.wotingfm.common.helper.CollocationHelper;

/**
 * BSApplication
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class BSApplication extends WtApp {
    public static android.content.SharedPreferences SharedPreferences;   // 配置信息

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences = this.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
        NetWorkManager.checkNetworkStatus(this);  // 获取网络状态
        CollocationHelper.setCollocation();       // 设置配置文件
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
