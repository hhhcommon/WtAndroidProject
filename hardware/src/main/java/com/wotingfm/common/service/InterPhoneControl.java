package com.wotingfm.common.service;

import android.content.Context;

/**
 * 对讲机数据组装
 * author：辛龙 (xinLong)
 * 2017/7/28 11:21
 * 邮箱：645700751@qq.com
 */
public class InterPhoneControl {

    /**
     * 退出小组
     *
     * @param groupId
     */
    public static void Quit(String groupId) {

    }

    /**
     * 进入小组
     *
     * @param groupId
     */
    public static void Enter( String groupId) {

    }

    /**
     * 取消说话
     *
     * @param groupId
     */
    public static void Loosen( String groupId) {

    }

    /**
     * 请求说话
     *
     * @param groupId
     */
    public static void Press( String groupId) {

    }

    /**
     * 个人请求通话-----呼叫
     *
     * @param id
     */
    public static void PersonTalkPress( String id) {

    }

    /**
     * 个人请求通话----------呼叫传递(Server->被叫App)-------应答消息
     *
     * @param callId
     * @param reMsgId
     * @param callerId
     */
    public static void PersonTalkHJCDYD( String callId, String reMsgId, String callerId) {

    }

    /**
     * 个人开始通话------（获取说话权限）查看组内是否有人说话
     *
     * @param context
     */
    public static void PersonTalkPressStart(Context context) {

    }

    /**
     * 个人结束通话------释放说话权限
     *
     * @param context
     */
    public static void PersonTalkPressStop(Context context) {

    }

    /**
     * 个人请求通话----------接收应答
     *
     * @param context
     * @param callId
     * @param callerId
     */
    public static void PersonTalkAllow(Context context, String callId, String callerId) {

    }

    /**
     * 个人请求通话----------拒绝应答
     *
     * @param context
     * @param callId
     * @param callerId
     */
    public static void PersonTalkOver(Context context, String callId, String callerId) {

    }

    /**
     * 个人请求通话----------应答超时
     *
     * @param context
     * @param callId
     * @param callerId
     */
    public static void PersonTalkTimeOver(Context context, String callId, String callerId) {

    }

    /**
     * 个人请求通话----------挂断电话
     *
     * @param context
     * @param callId
     */
    public static void PersonTalkHangUp(Context context, String callId) {

    }

    /**
     * socket 重连后发送的数据register
     *
     * @param context
     */
    public static void sendEntryMessage(Context context) {

    }

}