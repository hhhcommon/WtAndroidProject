package com.wotingfm.ui.intercom.alert.receive.presenter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.wotingfm.R;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.VibratorUtils;
import com.wotingfm.ui.intercom.alert.receive.model.ReceiveModel;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

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
    private MediaPlayer musicPlayer;
    private long[] Vibrate = {400, 800, 400, 800};
    private String id = null;
    private String roomId = null;

    public ReceivePresenter(ReceiveAlertActivity activity) {
        this.activity = activity;
        this.model = new ReceiveModel(activity);
        getSource();
        setReceiver();
        musicOpen();
        VibratorUtils.Vibrate(activity, Vibrate, true);
    }

    // 获取展示数据
    private void getSource() {
        try {
            id = activity.getIntent().getStringExtra("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            roomId = activity.getIntent().getStringExtra("roomId");
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
     * 获取roomId
     * @return
     */
    public String getRoomId(){
        return roomId;
    }

    /**
     * 获取用户Id
     * @return
     */
    public String getId(){
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
        musicPlayer = MediaPlayer.create(activity, R.raw.ringback);
        if (musicPlayer == null) {
            musicPlayer = MediaPlayer.create(activity, R.raw.talkno);
        }
        //  musicPlayer = MediaPlayer.create(instance, getSystemDefaultRingtoneUri());
        if (musicPlayer != null) {
            musicPlayer.start();
            // 监听音频播放完的代码，实现音频的自动循环播放
            musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer arg0) {
                    if (musicPlayer != null) {
                        musicPlayer.start();
                        musicPlayer.setLooping(true);
                    }
                }
            });
        } else {
            // 播放器初始化失败
        }
    }

    /**
     * 铃声关闭
     */
    private void musicClose() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
    }

    //获取系统默认铃声的Uri
    private Uri getSystemDefaultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_RINGTONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if(msg!=null&&!msg.trim().equals("")) {
            Log.e("呼叫流程", "返回数据" + msg.toString());
            if ( "cancel".equals(msg)) {
                activity.finish();
            }
        }
    }

    /**
     * 接受后操作
     */
    public void pushCallOk() {
        model.del(id);// 删除跟本次id相关的数据
        model.add(model.assemblyData(model.getUser(id), GlobalStateConfig.ok, ""));// 把本次数据添加的数据库
        activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
        InterPhoneActivity.closeAll();
//        activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
    }

    /**
     * 注销的操作
     */
    public void destroy() {
        musicClose();
        VibratorUtils.cancel(activity);
        EventBus.getDefault().unregister(this);
        model = null;
    }

}
