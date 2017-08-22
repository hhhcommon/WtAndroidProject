package com.woting.common.service;

import android.webkit.WebView;

import com.woting.common.bean.MessageEvent;
import com.woting.common.utils.VibratorUtils;
import com.woting.ui.intercom.main.chat.presenter.ChatPresenter;

import org.greenrobot.eventbus.EventBus;

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
    public static void pushPTT(WebView view) {
        if (ChatPresenter.data != null) {
            boolean b = InterPhoneControl.beginSpeak(view, "");
            if (b) {

            } else {
                VibratorUtils.Vibrate(100);
            }
        } else {
            VibratorUtils.Vibrate(100);
        }
    }

    /**
     * 抬起语音通话
     */
    public static void releasePTT(WebView view) {
        if (ChatPresenter.data != null) {
            boolean b = InterPhoneControl.stopSpeak(view, "");
        }
    }

}
