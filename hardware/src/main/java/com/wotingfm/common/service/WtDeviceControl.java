package com.wotingfm.common.service;

import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;

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

    }

    /**
     * 点击上一首按钮
     */
    public static void pushUpButton() {

    }

    /**
     * 点击下一首按钮
     */
    public static void pushDownButton() {

    }

    /**
     * 设置当前声音
     */
    public static void setVolumn() {
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
            if (ChatPresenter.data != null) {
                InterPhoneControl.Press(ChatPresenter.data.getID());
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
            if (ChatPresenter.data != null) {
                InterPhoneControl.Loosen(ChatPresenter.data.getID());
            }
        }
    }

}
