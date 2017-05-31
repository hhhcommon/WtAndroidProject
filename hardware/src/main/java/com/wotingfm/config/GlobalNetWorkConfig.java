package com.wotingfm.config;

/**
 * 保存公共属性
 * 作者：xinlong on 2016/8/23 21:18
 * 邮箱：645700751@qq.com
 */
public class GlobalNetWorkConfig {
    // 网络情况 1为成功WiFi已连接，2为cmnet，3为cmwap，4为ctwap， -1为网络未连接
    public static final int NETWORK_STATE_IDLE = -1;
    public static final int NETWORK_STATE_WIFI = 1;
    public static final int NETWORK_STATE_CMNET = 2;
    public static final int NETWORK_STATE_CMWAP = 3;
    public static final int NETWORK_STATE_CTWAP = 4;
    public static int CURRENT_NETWORK_STATE_TYPE = NETWORK_STATE_IDLE;
}
