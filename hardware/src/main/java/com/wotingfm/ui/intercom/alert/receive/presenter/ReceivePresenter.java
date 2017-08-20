package com.wotingfm.ui.intercom.alert.receive.presenter;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.wotingfm.R;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.service.AudioService;
import com.wotingfm.common.utils.VibratorUtils;
import com.wotingfm.ui.intercom.alert.receive.model.ReceiveModel;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ReceivePresenter {

    private ReceiveAlertActivity activity;
    private ReceiveModel model;
    private long[] Vibrate = {400, 800, 400, 800};
    private String id = null;
    private int callType = 0;
    private String accId;

    public ReceivePresenter(ReceiveAlertActivity activity) {
        this.activity = activity;
        this.model = new ReceiveModel(activity);
        musicOpen();
        VibratorUtils.Vibrate(activity, Vibrate, true);
        getSource();
        setReceiver();
    }

    // 获取展示数据
    private void getSource() {
        try {
            id = activity.getIntent().getStringExtra("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            accId = activity.getIntent().getStringExtra("accId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id != null && !id.equals("")) {
            Contact.user user = model.getUser(id);
            if (user != null) {
                String url = "";
                try {
                    url = user.getAvatar();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String name = "";
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
     * 获取AccId
     *
     * @return
     */
    public String getAccId() {
        return accId;
    }

    /**
     * 设置呼叫类型
     *
     * @param type
     */
    public void setCallType(int type) {
        callType = type;
    }

    /**
     * 获取用户Id
     *
     * @return
     */
    public String getId() {
        return id;
    }

    // 设置广播接收器
    private void setReceiver() {
        EventBus.getDefault().register(this);
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
    private void musicClose() {
        Intent intent = new Intent(activity,AudioService.class);
        activity.stopService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if (msg != null && !msg.trim().equals("")) {
            Log.e("呼叫流程", "返回数据" + msg.toString());
            if ("cancel".equals(msg)) {
                callType = 0;
                activity.finish();
            }
        }
    }

    /**
     * 接受后操作
     */
    private void pushCallOk() {
        model.del(id);// 删除跟本次id相关的数据
        model.add(model.assemblyData(model.getUser(id), GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
        activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
        activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CLOSE_ALL));// 关闭所有对讲模块界面
    }

    /**
     * 注销的操作
     */
    public void destroy() {
        musicClose();
        VibratorUtils.cancel(activity);
        if (callType == 1) pushCallOk();
        EventBus.getDefault().unregister(this);
        model = null;
    }

}
