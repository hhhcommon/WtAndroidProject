package com.woting.commonplat.config;

import android.os.Environment;

/**
 * 保存公共属性
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class GlobalUrlConfig {

    /**
     * apk下载默认路径
     */
    public static String apkUrl = "http://www.wotingfm.com/download/WoTing.apk";
    // 缓存路径
    public static String playCacheDirI = Environment.getRootDirectory() + "";                  // 获取手机根目录
    public static String playCacheDirO = Environment.getExternalStorageDirectory().getAbsolutePath();// 获取SD卡根目录
    public static String ksyPlayCache = "/WTFM/playCache/";                                    // 金山云缓存地址
    public static String upLoadCache = "/WTFM/APP/";                                           // app更新下载地址
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/woting/";
    public static final String DOWNLOAD_PATH1 = Environment.getExternalStorageDirectory() + "/woting/download/";
}
