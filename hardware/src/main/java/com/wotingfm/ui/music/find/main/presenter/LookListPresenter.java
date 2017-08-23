package com.wotingfm.ui.music.find.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneModel;
import com.wotingfm.ui.music.find.main.view.LookListFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 获取好友以及群组列表
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class LookListPresenter {

    private final LookListFragment activity;
    private final InterPhoneModel model;

    public LookListPresenter(LookListFragment activity) {
        this.activity = activity;
        this.model = new InterPhoneModel();
    }


    /**
     * 界面销毁,注销广播
     */
    public void destroy() {

    }
}
