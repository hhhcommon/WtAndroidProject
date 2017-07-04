package com.wotingfm.ui.intercom.alert.receive.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import com.wotingfm.R;
import com.wotingfm.common.utils.VibratorUtils;
import com.wotingfm.ui.intercom.alert.call.model.CallModel;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.alert.receive.model.ReceiveModel;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ReceivePresenter {

    private final ReceiveAlertActivity activity;
    private final ReceiveModel model;
    private SearchTalkHistoryDao dao;
    private MessageReceiver Receiver;
    private MediaPlayer musicPlayer;
    private long[] Vibrate = {400, 800, 400, 800};

    public ReceivePresenter(ReceiveAlertActivity activity) {
        this.activity = activity;
        this.model = new ReceiveModel();
        getSource();
        initDao();
        setReceiver();
        musicOpen();
        VibratorUtils.Vibrate(activity, Vibrate, true);
    }

    // 获取展示数据
    private void getSource() {
        String id = null;
        try {
            id = activity.getIntent().getStringExtra("id");
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

    // 初始化数据库
    private void initDao() {
        dao = new SearchTalkHistoryDao(activity);
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
//            filter.addAction(BroadcastConstants.PUSH_CALL);
//            filter.addAction(BroadcastConstants.PUSH_CALL_CALLALERT);
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
        VibratorUtils.cancel(activity);
        if (Receiver != null) {
            activity.unregisterReceiver(Receiver);
            Receiver = null;
        }
        if (dao != null) {
            dao = null;
        }
    }

    /*
    * 接收socket的数据进行处理
    */
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if (action.equals(BroadcastConstants.PUSH_CALL)) {
//
//
//            } else if (action.equals(BroadcastConstants.PUSH_CALL_CALLALERT)) {
//
//
//            }
        }
    }
}
