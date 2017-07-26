package com.wotingfm.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wotingfm.common.constant.BroadcastConstants;

import org.json.JSONObject;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 通知消息模块
 * 作者：xinlong on 2017/7/13 23:57
 * 邮箱：645700751@qq.com
 */
public class NotificationService extends Service {
    private static ArrayBlockingQueue<String> MsgQueue = new ArrayBlockingQueue<String>(128);  // 需要处理的已经组装好的消息队列
    private static NotificationService context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        DealMessage delM = new DealMessage();
        delM.start();
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
                        dealM(type);
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
                if (type.equals("friend_apply")) {// 0.好友申请
                    return 0;
                } else if (type.equals("APPROVE_APPLY")) {// 1.好友同意(重新获取好友)
                    return 1;
                } else if (type.equals("DENY_APPLY")) {// 2.好友拒绝
                    return 2;
                } else if (type.equals("DELETE_FRIEND")) {// 3.删除好友(重新获取好友)
                    return 3;
                } else if (type.equals("Friend_update")) {// 4.好友信息修改（头像，昵称）(重新获取好友)
                    return 4;
                } else if (type.equals("")) {// 5.有人申请加群 （通知群主与管理员）
                    return 5;
                } else if (type.equals("")) {// 6.同意加入群组(重新获取群组)
                    return 6;
                } else if (type.equals("")) {// 7.拒绝加入群组
                    return 7;
                } else if (type.equals("")) {// 8.删除群成员（被删除）(重新获取群组)
                    return 8;
                } else if (type.equals("")) {// 9.群消息修改（头像，名称等）(重新获取群组)
                    return 9;
                } else if (type.equals("")) {// 10.下线提醒
                    return 10;
                }  else if (type.equals("")) {// 11.通知消息（待定）
                    return 11;
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
    private void dealM(int type) {
        switch (type) {
            case 0:// 0.好友申请
                break;
            case 1:// 1.好友同意(重新获取好友)
                context.sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
                break;
            case 2:// 2.好友拒绝
                break;
            case 3:// 3.删除好友(重新获取好友)
                context.sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
                break;
            case 4:// 4.好友信息修改（头像，昵称）(重新获取好友)
                context.sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
                break;
            case 5:// 5.有人申请加群 （通知群主与管理员）
                break;
            case 6:// 6.同意加入群组(重新获取群组)
                context.sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                break;
            case 7:// 7.拒绝加入群组
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
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
