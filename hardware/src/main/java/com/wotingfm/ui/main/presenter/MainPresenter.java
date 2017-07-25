package com.wotingfm.ui.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.iflytek.cloud.SpeechUtility;
import com.woting.commonplat.receiver.NetWorkChangeReceiver;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.service.FloatingWindowService;
import com.wotingfm.common.service.NotificationService;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.ui.main.model.MainModel;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.user.register.model.RegisterModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MainPresenter extends BasePresenter {
    private final MainModel mainModel;
    private MainActivity mainActivity;
    private Intent FloatingWindow;
    private Intent NS;

    private NetWorkChangeReceiver netWorkChangeReceiver;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mainModel = new MainModel(mainActivity);
        getVersion();
        createService();
        registerReceiver();
    }

    private void createService() {
        FloatingWindow = new Intent(mainActivity, FloatingWindowService.class);//启动全局弹出框服务
        mainActivity.startService(FloatingWindow);
        NS = new Intent(mainActivity, NotificationService.class);//启动推送消息处理服务
        mainActivity.startService(NS);
    }


    //注册广播
    private void registerReceiver() {
        IntentFilter m = new IntentFilter();
        m.addAction(BroadcastConstants.ACTIVITY_CHANGE);
        mainActivity.registerReceiver(endApplicationBroadcast, m);

        netWorkChangeReceiver = new NetWorkChangeReceiver(mainActivity);
        IntentFilter n = new IntentFilter();
        n.addAction(NetWorkChangeReceiver.intentFilter);
        mainActivity.registerReceiver(netWorkChangeReceiver, n);
    }

    //接收定时服务发送过来的广播  用于界面更改
    private BroadcastReceiver endApplicationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.ACTIVITY_CHANGE)) {
                // 按钮切换-----档位切换广播
                int viewType = intent.getIntExtra("viewType", 1);
                Log.e("界面显示状态", viewType + "");
                if (viewType == 1) {
                    EventBus.getDefault().post(new MessageEvent("one"));
                    //  mainActivity.changeOne();
                } else if (viewType == 2) {
                    EventBus.getDefault().post(new MessageEvent("two"));
                    // mainActivity.changeTwo();
                } else {
                    EventBus.getDefault().post(new MessageEvent("three"));
                    // mainActivity.changeThree();
                }
            }
        }
    };

    // 发送注册账号请求
    private void getVersion() {
        mainModel.getVersion( new MainModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                dealVersionSuccess(o);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    // 处理注册返回数据
    private void dealVersionSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************对外接口************************/

    /**
     * app退出时执行该操作
     */
    public void stop() {
        mainActivity.stopService(FloatingWindow);
        mainActivity.stopService(NS);
        mainActivity.unregisterReceiver(netWorkChangeReceiver);
        mainActivity.unregisterReceiver(endApplicationBroadcast);
        Log.e("app退出", "app退出");
    }

}
