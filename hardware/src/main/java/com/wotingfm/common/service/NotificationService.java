package com.wotingfm.common.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.woting.commonplat.utils.JsonEncloseUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.IntegerConstant;
import com.wotingfm.common.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 通知消息模块
 * 作者：xinlong on 2017/7/13 23:57
 * 邮箱：645700751@qq.com
 */
public class NotificationService extends Service {
    private static ArrayBlockingQueue<String> MsgQueue = new ArrayBlockingQueue<String>(128);      // 需要处理的已经组装好的消息队列
    private static ArrayBlockingQueue<String> ShowMsgQueue = new ArrayBlockingQueue<String>(128);  // 需要展示的消息队列
    private static NotificationService context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        DealMessage delM = new DealMessage();
        delM.start();
        ShowMessage showM = new ShowMessage();
        showM.start();
    }

    /**
     * 说明：解析推送数据，处理，转成特定数据
     * 0.通知消息（待定）
     * 1.好友同意
     * 2.好友拒绝
     * 3.删除好友
     * 4.好友信息修改（头像，昵称）
     * 5.有人申请加群 （通知群主与管理员）
     * 6.同意加入群组
     * 7.拒绝加入群组
     * 8.删除群成员（被删除）
     * 9.群消息修改（头像，名称等）
     * 10.下线提醒
     */
    public static void saveM(String s) {
        MsgQueue.add(s);
    }

    public static void  test(){
        ShowMsgQueue.add(assemblyData("1", "样式测试消息", "这是一条测试消息"));
    }

    /**
     * 采用存储转发机制
     */
    private class DealMessage extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = MsgQueue.take();
                    if (msg != null && msg.trim().length() > 0) {
                        int type = parsingM(msg);
                        JSONObject js = new JSONObject(msg);
                        String message = js.getString("message");
                        dealM(type, message);
                    }
                } catch (Exception e) {
                    Log.e("DealReceive处理线程:::", e.toString());
                }
            }
        }
    }

    /**
     * 解析数据
     *
     * @param msg
     * @return
     */
    private int parsingM(String msg) {
        try {
            JSONObject js = new JSONObject(msg);
            String type = js.getString("type");
            if (type != null && !type.equals("")) {
                if (type.equals("FRIEND_APPLY")) {// 0.好友申请
                    return 0;
                } else if (type.equals("FRIEND_APPROVE")) {// 1.好友同意(重新获取好友)
                    return 1;
                } else if (type.equals("FRIEND_DENY")) {// 2.好友拒绝
                    return 2;
                } else if (type.equals("FRIEND_DELETE")) {// 3.删除好友(重新获取好友)
                    return 3;
                } else if (type.equals("FRIEND_UPDATE")) {// 4.好友信息修改（头像，昵称）(重新获取好友)
                    return 4;
                } else if (type.equals("GROUP_APPLY")) {// 5.有人申请加群 （通知群主与管理员）
                    return 5;
                } else if (type.equals("CHAT_GROUP_APPROVE")) {// 6.同意加入群组(重新获取群组)
                    return 6;
                } else if (type.equals("CHAT_GROUP_DENY")) {// 7.拒绝加入群组
                    return 7;
                } else if (type.equals("GROUP_REMOVE")) {// 8.删除群成员（被删除）(重新获取群组)
                    return 8;
                } else if (type.equals("UPDATE_GROUP_DETAIL")) {// 9.群消息修改（头像，名称等）(重新获取群组)
                    return 9;
                } else if (type.equals("")) {// 10.下线提醒
                    return 10;
                } else if (type.equals("")) {// 11.通知消息（待定）
                    return 11;
                } else if (type.equals("GROUP_ADD")) {// 12.有人邀请我入群
                    return 12;
                } else if (type.equals("ADD_MEMBER")) {// 13.群组有新的成员加入，给群内所有成员发送推送
                    return 13;
                }else {
                    return 999;
                }
            } else {
                return 999;
            }
        } catch (Exception e) {
            Log.e("DealReceive处理线程:::", e.toString());
            return 999;
        }
    }

    /**
     * 最终处理消息
     *
     * @param type
     */
    private void dealM(int type, String msg) {
        switch (type) {
            case 0:// 0.好友申请
                assemblyData();
                ShowMsgQueue.add(assemblyData("1", "好友消息", msg));
                break;
            case 1:// 1.好友同意(重新获取好友)
                context.sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
                ShowMsgQueue.add(assemblyData("1", "好友消息", msg));
                break;
            case 2:// 2.好友拒绝
                ShowMsgQueue.add(assemblyData("2", "好友消息", msg));
                break;
            case 3:// 3.删除好友(重新获取好友)
                context.sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
                break;
            case 4:// 4.好友信息修改（头像，昵称）(重新获取好友)
                context.sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
                break;
            case 5:// 5.有人申请加群 （通知群主与管理员）
                ShowMsgQueue.add(assemblyData("5", "群组消息", msg));
                break;
            case 6:// 6.同意加入群组(重新获取群组)
                context.sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                break;
            case 7:// 7.拒绝加入群组
                ToastUtils.show_always(BSApplication.mContext, "拒绝加入群组");
                break;
            case 8:// 8.删除群成员（被删除）(重新获取群组)
                context.sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                break;
            case 9:// 9.群消息修改（头像，名称等）(重新获取群组)
                context.sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                break;
            case 10:// 10.下线提醒
                break;
            case 11:// 11.通知消息（待定）
                ShowMsgQueue.add(assemblyData("11", "通知消息", msg));
                break;
            case 12:// 12.有人邀请我入群
                ShowMsgQueue.add(assemblyData("12", "群组消息", msg));
                break;
            case 13:// 13.群组有新的成员加入，给群内所有成员发送推送
                ShowMsgQueue.add(assemblyData("13", "群组消息", msg));
                context.sendBroadcast(new Intent(BroadcastConstants.GROUP_USER_CHANGE));
                break;
        }
    }

    /**
     * 组装好友消息小红点数据
     */
    private void assemblyData() {
        int num = BSApplication.SharedPreferences.getInt(IntegerConstant.RED_POINT_PERSON, 0);
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putInt(IntegerConstant.RED_POINT_PERSON, num + 1);
        if (!et.commit()) {
            Log.e("commit", "数据 commit 失败!");
        }
        sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_POINT_CHANGE));
    }

    /**
     * 消息展示
     */
    private class ShowMessage extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = ShowMsgQueue.take();
                    if (msg != null && msg.trim().length() > 0) {
                        Intent it = new Intent(BroadcastConstants.VIEW_NOTIFY_SHOW);
                        it.putExtra("msg", msg);
                        sendBroadcast(it);
                        sleep(4000);
                        sendBroadcast(new Intent(BroadcastConstants.VIEW_NOTIFY_CLOSE));
                    }
                } catch (Exception e) {
                    Log.e("ShowMessage处理线程:::", e.toString());
                }
            }
        }
    }

    /**
     * 组装展示数据
     *
     * @param type
     * @param title
     * @param message
     * @return
     */
    private static String assemblyData(String type, String title, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("title", title);
        map.put("message", message);
        String s = JsonEncloseUtils.jsonEnclose(map).toString();
        return s;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
