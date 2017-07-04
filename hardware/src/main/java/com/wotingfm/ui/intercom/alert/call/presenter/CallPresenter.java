package com.wotingfm.ui.intercom.alert.call.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.add.search.local.model.SearchContactsForLocalModel;
import com.wotingfm.ui.intercom.add.search.local.view.SearchContactsForLocalFragment;
import com.wotingfm.ui.intercom.alert.call.model.CallModel;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class CallPresenter {

    private final CallAlertActivity activity;
    private final CallModel model;
    private MessageReceiver Receiver;
    private MediaPlayer musicPlayer;
    private Contact.user user;

    public CallPresenter(CallAlertActivity activity) {
        this.activity = activity;
        this.model = new CallModel(activity);
        getSource();
        setReceiver();
        musicOpen();
    }

    // 获取展示数据
    private void getSource() {
        String id = activity.getIntent().getStringExtra("id");
        if (id != null && !id.equals("")) {
            user = model.getUser(id);
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

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.PUSH_CALL_REC);
            activity.registerReceiver(Receiver, filter);
        }
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

    /**
     * 注销的操作
     */
    public void destroy() {
        musicClose();
        model.destroy();
        if (GlobalStateConfig.test) {
            dealPushCall();
        }
        if (Receiver != null) {
            activity.unregisterReceiver(Receiver);
            Receiver = null;
        }
    }

    // 接收推送的数据进行处理
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.PUSH_CALL_REC)) {
                dealPushCall();// 处理呼叫成功返回的数据
            }
        }
    }

    // 处理呼叫成功返回的数据
    private void dealPushCall() {
        if (user != null) {
            DBTalkHistory l = model.assemblyData(user);
            String id = l.getID().trim();
            if (id != null && !id.equals("")) {
                model.add(l);
                activity.sendBroadcast(new Intent(BroadcastConstants.PUSH_CALL_SEND));
                activity.finish();
            }
        }
    }
}
