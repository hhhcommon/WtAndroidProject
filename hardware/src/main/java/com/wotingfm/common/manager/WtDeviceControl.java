package com.wotingfm.common.manager;

import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.service.SimulationService;
import com.wotingfm.common.utils.VibratorUtils;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 控制接口的实现类
 * 作者：xinlong on 2017/7/28 16:18
 * 邮箱：645700751@qq.com
 */
public class WtDeviceControl {

    /**
     * 暂停
     */
    public static void pushCenter() {
        EventBus.getDefault().post(new MessageEvent("stop_or_star"));
    }

    /**
     * 点击上一首按钮
     */
    public static void pushUpButton() {
        EventBus.getDefault().post(new MessageEvent("step"));
    }

    /**
     * 点击下一首按钮
     */
    public static void pushDownButton() {
        EventBus.getDefault().post(new MessageEvent("next"));
    }

    /**
     * 暂停
     */
    public static void pause() {
        EventBus.getDefault().post(new MessageEvent("pause"));
    }

    /**
     * 播放
     */
    public static void start() {
        EventBus.getDefault().post(new MessageEvent("start"));
    }

    /**
     * 设置当前声音
     */
    public static void setVolumn() {
    }

    /**
     * 设置静音（呼叫成功以及对讲状态）===待定
     */
    public static void setMute() {
        EventBus.getDefault().post(new MessageEvent("pause"));
    }

    /**
     * 恢复静音前设置（呼叫成功以及对讲状态）===待定
     */
    public static void setMuteResume() {
        EventBus.getDefault().post(new MessageEvent("start"));
    }

    /**
     * 语音指令-开始
     */
    public static void pushVoiceStart() {

    }

    /**
     * 语音指令-结束
     */
    public static void releaseVoiceStop() {

    }

    /**
     * 按下语音通话
     */
    public static void pushPTT() {
        if (GlobalStateConfig.isActive) {
            SimulationService.talk();
        } else {
            // 测试代码
            if (ChatPresenter.data != null) {
                if (GlobalStateConfig.canSpeak) {
                    AVChatManager.getInstance().muteLocalAudio(false);  // 打开音频
                    sendPersonSpeakNews(true);
                } else {
                    VibratorUtils.Vibrate(100);
                }
            } else {
                VibratorUtils.Vibrate(100);
            }
        }
    }

    /**
     * 抬起语音通话
     */
    public static void releasePTT() {
        if (GlobalStateConfig.isActive) {
            SimulationService.openDevice();
        } else {
            if (ChatPresenter.data != null&&GlobalStateConfig.canSpeak) {
                AVChatManager.getInstance().muteLocalAudio(true);// 关闭音频
                sendPersonSpeakNews(false);
            }
        }
    }

    // 发送当前说话人
    private static void sendPersonSpeakNews(boolean b) {
        if (b) {
            String username = BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, "我");
            String avatar = BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, "");
            Log.e("说话人数据", "userId=" + "我" + ":" + username + ":" + avatar);
            Intent intent = new Intent(BroadcastConstants.PUSH_CHAT_OPEN);
            intent.putExtra("name", username);
            intent.putExtra("url", avatar);
            BSApplication.getInstance().sendBroadcast(intent);
        } else {
            BSApplication.getInstance().sendBroadcast(new Intent(BroadcastConstants.PUSH_CHAT_CLOSE));
        }
    }

}
