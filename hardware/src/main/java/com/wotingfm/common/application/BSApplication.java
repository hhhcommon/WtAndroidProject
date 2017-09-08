package com.wotingfm.common.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.iflytek.cloud.SpeechUtility;
import com.woting.commonplat.manager.NetWorkManager;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.common.manager.NIMManager;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.service.PlayerService;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.jpush.android.api.JPushInterface;

/**
 * BSApplication
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class BSApplication extends MultiDexApplication {
    public static SharedPreferences SharedPreferences;   // 配置信息
    public static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = this;
        SharedPreferences = this.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
        RetrofitUtils.INSTANCE = null;

        //SDK初始化(启动后台服务，若已经存在用户登录信息，SDK将自动登录)
        SpeechUtility.createUtility(this, "appid=56275014");// 初始化讯飞

        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());

        NetWorkManager.checkNetworkStatus(this);  // 获取网络状态

        PhoneMsgManager.getPhoneInfo(this);       // 获取手机信息
        Intent playerService = new Intent(mContext, PlayerService.class);// 启动播放服务
        mContext.startService(playerService);
        JPushInterface.setDebugMode(true);        // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);                // 初始化 JPush
        JPushInterface.getRegistrationID(this);
        // JPushInterface.resumePush(this);
        // JPushInterface.stopPush(this);

        NIMManager.NIMSet();// 直播配置

    }

    public static Context getInstance() {
        return mContext;
    }
}
