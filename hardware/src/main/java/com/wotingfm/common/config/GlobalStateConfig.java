package com.wotingfm.common.config;

import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * GlobalConfig
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class GlobalStateConfig {

    /**
     * 是否可以说话（单对单）
     */
    public static  boolean canSpeak = true;
    /**
     * 来电对象
     */
    public static AVChatData avChatData;

    /**
     * 是否是测试代码
     */
    public static final boolean test = false;

    /**
     * 滚轮控件按钮动态设置宽度
     */
    public static int LoopViewW = 500;
    /**
     * 用于档位切换的参数
     * A：播放模块
     * B: 对讲模块
     * C：个人中心模块
     */
    public static int destination = 0;  // 此时所处的档位 0，第一个档位，1,第二个档位
//    public static int mineFromType = 0; // 个人中心跳转来源 0,关闭状态 ，1，第一个档位，2,第二个档位
//    public static String activityA = "A";// 第一个档位页面的展示状态
//    public static String activityB = "B";// 第二个档位页面的展示状态

    /**
     * 数据库存储数据
     */
    public static final String ok = "ok";
    /**
     * 数据库名称
     */
    public static final String dbVersionName = "wt_fmfm.db";

    /**
     * 数据库版本号
     */
    public static final int dbVersionCode = 5;

    /**
     * 硬件对讲模块是否活跃状态
     */
    public static boolean isActive = false;

    /**
     * 是否吐司
     */
    public static boolean isToast;

    /**
     * PersonClientDevice(个人客户端设备) 终端类型1=app,2=设备，3=pc
     */
    public static int PCDType;
    public static List<Contact.group> list_group;          // 通讯录中的对讲组
    public static List<Contact.user> list_person;          // 通讯录中的好友
    public static List<Contact.user> list_group_user;      // 群成员
    // 图片缓存最大容量，1000M，根据自己的需求进行修改
    public static final int GLIDE_CATCH_SIZE = 1000 * 1000 * 1000;
    // 图片缓存子目录
    public static final String GLIDE_CARCH_DIR = "image_catch";

    public static String playingId ;  // 保存定时或定量上传的数据
    public static String playingType ;  // 保存定时或定量上传的数据
    public static String listType ;  // 保存定时或定量上传的数据
    public static String currentTime ;  // 保存定时或定量上传的数据

    public static String savePath;// 华为手机拍照的图片保存路径
}
