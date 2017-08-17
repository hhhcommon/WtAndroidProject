package com.wotingfm.common.service;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.IMManger;

/**
 * 对讲控制
 * author：辛龙 (xinLong)
 * 2017/7/28 11:21
 * 邮箱：645700751@qq.com
 */
public class InterPhoneControl {

    /**
     * 呼叫方==呼叫
     *
     * @param id
     * @return
     */
    public static boolean call(String id) {
        boolean b = IMManger.getInstance().sendMsg(id, "LAUNCH", CommonUtils.getUserId());
        return b;
    }

    /**
     * 呼叫方==挂断电话
     *
     * @param roomId
     * @param userId
     * @return
     */
    public static boolean hangUp(String roomId, String userId) {
        boolean b = IMManger.getInstance().sendMsg(roomId, "CANCEL", userId);
        return b;
    }

    /**
     * 被叫方==接受
     *
     * @param roomId
     * @param userId
     * @return
     */
    public static boolean accept(String roomId, String userId) {
        boolean b = IMManger.getInstance().sendMsg(roomId, "ACCEPT", userId);
        return b;
    }

    /**
     * 被叫方==拒绝接受
     *
     * @param roomId
     * @param userId
     * @return
     */
    public static boolean refuse(String roomId, String userId) {
        boolean b = IMManger.getInstance().sendMsg(roomId, "REFUSE", userId);
        return b;
    }

    /**
     * 退出房間==个人
     *
     * @param room_id
     * @return
     */
    public static boolean quitRoomPerson(WebView view, String room_id) {
        view.loadUrl("javascript:exitRoom()");
        return true;
    }

    /**
     * 退出房間==群组
     *
     * @param room_id
     * @return
     */
    public static boolean quitRoomGroup(WebView view, String room_id) {
        view.loadUrl("javascript:exitRoom()");
        return true;
    }

    /**
     * 進入房間
     *
     * @param room_id
     * @return
     */
    public static boolean enterRoom(WebView view, String room_id) {
        Log.d("mingku","room_id="+room_id);
        String userId = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");// 头像
        String userName = BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, "我听");// 昵称
        String userAvatar = BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, "000");// id
        view.loadUrl("javascript:joinRoom('" + room_id + "','" + userId + "','" + userName + "','" + userAvatar + "')");
        return true;
    }

    /**
     * 開始說話
     *
     * @param room_id
     * @return
     */
    public static boolean beginSpeak(WebView view, String room_id) {
        view.loadUrl("javascript:beginSpeak()");
        return true;
    }

    /**
     * 結束說話
     *
     * @param room_id
     * @return
     */
    public static boolean stopSpeak(WebView view, String room_id) {
        view.loadUrl("javascript:stopSpeak()");
        return true;
    }


    /**
     * 結束单对单說話
     * @param room_id
     * @return
     */
    public static boolean over( String room_id){
        IMManger.getInstance().sendMsg(room_id, "OVER", "");
        return true;
    }

}