package com.wotingfm.application;

import android.app.Application;
import android.content.Context;
import com.wotingfm.common.NetWorkManager;

/**
 * BSApplication
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class BSApplication extends Application {
    public static android.content.SharedPreferences SharedPreferences;   // 配置信息

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences = this.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
        NetWorkManager.checkNetworkStatus(this);// 获取网络状态
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
