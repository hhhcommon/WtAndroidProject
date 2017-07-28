package com.wotingfm.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.test.PlayerActivity;

import org.appspot.apprtc.CallActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.R.attr.type;
import static com.iflytek.cloud.resource.Resource.getText;

/**
 * Created by amine on 2017/7/25.
 */

public class IMManger {
    public static IMManger INSTANCE;


    public static IMManger getInstance() {
        if (INSTANCE == null) {
            synchronized (IMManger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IMManger();
                }
            }
        }
        return INSTANCE;
    }

    /*
            type =1 是发送
            type =2 是接受
            type =1 是拒绝
     */
    public void sendMsg(String sessionId, String chatRoom,String userId) {
        IMMessage msg = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, null);
        // Map<String, Object> data = new HashMap<>();
        //// data.put("chatRoom", chatRoom);
        msg.setContent(chatRoom);
        msg.setPushContent(userId);
        // msg.setRemoteExtension(data); // 设置服务器扩展字段
        NIMClient.getService(MsgService.class).sendMessage(msg, false);
    }
}
