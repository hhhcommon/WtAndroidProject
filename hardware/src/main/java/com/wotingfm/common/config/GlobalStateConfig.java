package com.wotingfm.common.config;

/**
 * GlobalConfig
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class GlobalStateConfig {

    /**
     * 版本号
     */
    public static final String appVersionName = "0.1.0.X.45";

    /**
     * 数据库名称
     */
    public static final String dbVersionName = "woting.db";

    /**
     * 数据库版本号
     */
    public static final int dbVersionCode = 1;



    /**
     * 是否活跃状态，有活跃状态才能播放声音，否则即使收到音频包也不播放
     */
    public static boolean isActive = false;

    /**
     * 是否吐司
     */
    public static boolean isToast;

    /**
     * 是不是读取配置文件
     */
    public static boolean isCollocation = true;

    /**
     * PersonClientDevice(个人客户端设备) 终端类型1=app,2=设备，3=pc
     */
    public static int PCDType;
    /**
     * 此时的界面
     */
    public static int activityType =1;
}
