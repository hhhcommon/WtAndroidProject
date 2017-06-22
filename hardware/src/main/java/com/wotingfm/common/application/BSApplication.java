package com.wotingfm.common.application;

import android.app.Application;
import android.content.Context;
import com.woting.commonplat.manager.NetWorkManager;
import com.woting.commonplat.manager.PhoneMsgManager;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

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
        mContext = this;
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
        SharedPreferences = this.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
        NetWorkManager.checkNetworkStatus(this);  // 获取网络状态
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


