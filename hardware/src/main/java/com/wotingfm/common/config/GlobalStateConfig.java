package com.wotingfm.common.config;

/**
 * GlobalConfig
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class GlobalStateConfig {
    /**
     * 是否是测试代码
     */
    public static final boolean test = true;
    /**
     * 用于档位切换的参数
     * A：播放模块
     * B: 对讲模块
     * C：个人中心模块
     */
    public static int destination=0 ;  // 此时所处的档位 0，第一个档位，1,第二个档位
    public static int mineFromType=0 ; // 个人中心跳转来源 0,关闭状态 ，1，第一个档位，2,第二个档位
    public static String activityA="A";// 第一个档位页面的展示状态
    public static String activityB="B";// 第二个档位页面的展示状态
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

}
