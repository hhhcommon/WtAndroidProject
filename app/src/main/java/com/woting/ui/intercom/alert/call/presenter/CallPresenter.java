package com.woting.ui.intercom.alert.call.presenter;

import android.content.Intent;
import android.util.Log;

import com.woting.common.bean.MessageEvent;
import com.woting.common.constant.BroadcastConstants;
import com.woting.common.service.AudioService;
import com.woting.ui.intercom.alert.call.model.CallModel;
import com.woting.ui.intercom.alert.call.view.CallAlertActivity;
import com.woting.ui.intercom.main.contacts.model.Contact;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class CallPresenter {

    private CallAlertActivity activity;
    private CallModel model;
    private Contact.user user;
    private String id = null;
    private String roomId = null;
    private String fromType = "";// 界面来源
    private int callType = 0;

    public CallPresenter(CallAlertActivity activity) {
        this.activity = activity;
        this.model = new CallModel(activity);
        getSource();
        setReceiver();
        musicOpen();
    }

    // 获取展示数据
    private void getSource() {
        try {
            id = activity.getIntent().getStringExtra("id").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fromType = activity.getIntent().getStringExtra("fromType").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            roomId = activity.getIntent().getStringExtra("roomId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != null && !id.equals("")) {
            user = model.getUser(id);
            if (user != null) {
                String url = "";
                try {
                    url = user.getAvatar();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String name = "好友";
                try {
                    name = user.getName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activity.setViewData(url, name);
            } else {
                activity.finish();
            }
        } else {
            activity.finish();
        }
    }

    /**
     * 获取roomId
     *
     * @return
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * 获取用户Id
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 铃声开启
     */
    public void musicOpen() {
        Intent intent = new Intent(activity,AudioService.class);
        activity.startService(intent);
    }

    /**
     * 铃声关闭
     */
    public void musicClose() {
        Intent intent = new Intent(activity,AudioService.class);
        activity.stopService(intent);
    }

    // 设置广播接收器
    private void setReceiver() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if (msg != null && !msg.trim().equals("")) {
            Log.e("呼叫流程", "返回数据" + msg.toString());

            if ("refuse".equals(msg)) {
                /**
                 * 此处需要进行延时挂断操作（未实现）
                 */
                activity.finish();
            } else if ("cancel".equals(msg)) {
                activity.finish();
            } else if ("accept".equals(msg)) {
                EventBus.getDefault().post(new MessageEvent("enterPersonRoom"));
                callType = 1;
                activity.finish();
            }
        }
    }

    // 处理呼叫成功返回的数据
    private void dealPushCall() {
        Intent intent = new Intent(BroadcastConstants.PUSH_CALL_SEND);
        intent.putExtra("fromType", fromType);
        activity.sendBroadcast(intent);
    }

    /**
     * 注销的操作
     */
    public void destroy() {
        musicClose();
        if (callType == 1) dealPushCall();
        EventBus.getDefault().unregister(this);
        model = null;
    }
}
