package com.wotingfm.common.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.woting.commonplat.manager.NetWorkManager;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.net.volley.VolleyRequest;
import com.wotingfm.common.helper.CollocationHelper;
import com.wotingfm.common.service.FloatingWindowService;

/**
 * BSApplication
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class BSApplication  extends Application{
    public static android.content.SharedPreferences SharedPreferences;   // 配置信息

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences = this.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
        VolleyRequest.set(Volley.newRequestQueue(this));
        NetWorkManager.checkNetworkStatus(this);  // 获取网络状态
        CollocationHelper.setCollocation();       // 设置配置文件
        PhoneMsgManager.getPhoneInfo(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}


