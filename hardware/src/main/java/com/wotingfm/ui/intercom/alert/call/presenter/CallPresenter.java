package com.wotingfm.ui.intercom.alert.call.presenter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.wotingfm.R;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.ui.intercom.alert.call.model.CallModel;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

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
    private MediaPlayer musicPlayer;
    private Contact.user user;
    private String id = null;
    private String roomId = null;

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
    public void musicClose() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
    }

    //获取系统默认铃声的Uri
    private Uri getSystemDefaultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_RINGTONE);
    }

    // 设置广播接收器
    private void setReceiver() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if(msg!=null&&!msg.trim().equals("")){
            Log.e("呼叫流程","返回数据"+msg.toString());
            if ("refuse".equals(msg) ) {
                /**
                 * 此处需要进行延时挂断操作（未实现）
                 */
                EventBus.getDefault().post(new MessageEvent("over"));
            } else if ("cancel".equals(msg)) {
                EventBus.getDefault().post(new MessageEvent("over"));
            }else if("accept".equals(msg)){
                EventBus.getDefault().post(new MessageEvent("over"));
                dealPushCall();// 处理呼叫成功返回的数据
            }
        }
    }

    // 处理呼叫成功返回的数据（此处有问题）
    private void dealPushCall() {
        if (user != null) {
            activity.sendBroadcast(new Intent(BroadcastConstants.PUSH_CALL_SEND));
            activity.finish();
        }
    }

    /**
     * 注销的操作
     */
    public void destroy() {
        musicClose();
        EventBus.getDefault().unregister(this);
        model = null;
    }
}
