package com.wotingfm.common.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.woting.commonplat.manager.NetWorkManager;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.common.helper.CollocationHelper;

/**
 * BSApplication
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class BSApplication extends Application {
    public static android.content.SharedPreferences SharedPreferences;   // 配置信息
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        SharedPreferences = this.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
        NetWorkManager.checkNetworkStatus(this);  // 获取网络状态
        CollocationHelper.setCollocation();       // 设置配置文件
        PhoneMsgManager.getPhoneInfo(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getInstance() {
        return mContext;
    }
}


