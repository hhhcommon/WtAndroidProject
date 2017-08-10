package com.wotingfm.ui.main.presenter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.woting.commonplat.receiver.NetWorkChangeReceiver;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.service.FloatingWindowService;
import com.wotingfm.common.service.InterPhoneControl;
import com.wotingfm.common.service.NotificationService;
import com.wotingfm.common.service.WtDeviceControl;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.main.model.MainModel;
import com.wotingfm.ui.main.view.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.iflytek.cloud.resource.Resource.getText;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MainPresenter extends BasePresenter {
    private MainModel mainModel;
    private MainActivity activity;
    private Intent FloatingWindow;
    private Intent NS;
    private String roomId;

    private NetWorkChangeReceiver netWorkChangeReceiver;

    public MainPresenter(MainActivity mainActivity) {
        this.activity = mainActivity;
        this.mainModel = new MainModel(mainActivity);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, true);
        createService();
        registerReceiver();
        getVersion();
    }

    private void createService() {
        FloatingWindow = new Intent(activity, FloatingWindowService.class);//启动全局弹出框服务
        activity.startService(FloatingWindow);
        NS = new Intent(activity, NotificationService.class);//启动推送消息处理服务
        activity.startService(NS);
    }

    //注册广播
    private void registerReceiver() {
        EventBus.getDefault().register(this);
        IntentFilter m = new IntentFilter();
        m.addAction(BroadcastConstants.ACTIVITY_CHANGE);
        m.addAction(BroadcastConstants.VIEW_NOTIFY_SHOW);
        m.addAction(BroadcastConstants.VIEW_NOTIFY_CLOSE);
        activity.registerReceiver(endApplicationBroadcast, m);

        netWorkChangeReceiver = new NetWorkChangeReceiver(activity);
        IntentFilter n = new IntentFilter();
        n.addAction(NetWorkChangeReceiver.intentFilter);
        activity.registerReceiver(netWorkChangeReceiver, n);
    }

    // 顶栏颜色设置
    private void applySelectedColor() {
        // -724225
        // float[] colorHSV = new float[]{0f, 0f, 0f};
        // int c = Color.HSVToColor(colorHSV);
        int c = -1;
        int color = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));
        StatusBarUtil.setStatusBarColor(activity, color, false);
        // StatusBarUtil.setStatusBarColor(context, R.color.white, false);
    }

    // 顶栏颜色设置
    public void applyTextColor(boolean b) {
        if (b) {
            StatusBarUtil.StatusBarLightMode(activity, false);
        } else {
            StatusBarUtil.StatusBarLightMode(activity, true);
        }
    }

    // 发送注册账号请求
    private void getVersion() {
        mainModel.getVersion(new MainModel.OnLoadInterface() {
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
                } else if (viewType == 3) {
                    EventBus.getDefault().post(new MessageEvent("three"));
                    // mainActivity.changeThree();
                }else if (viewType == 4) {
                    EventBus.getDefault().post(new MessageEvent("four"));
                    // mainActivity.changeThree();
                }
            } else if (action.equals(BroadcastConstants.VIEW_NOTIFY_SHOW)) {
                String content = intent.getStringExtra("msg");  // 展示通知消息
                Log.e("推送消息", "msg" + content);
                assemblyMsg(true, content);
            } else if (action.equals(BroadcastConstants.VIEW_NOTIFY_CLOSE)) {
                assemblyMsg(false, "");// 关闭通知消息
            }
        }
    };

    /**
     * 解析通知消息并且展示
     *
     * @param b
     * @param msg
     */
    private void assemblyMsg(boolean b, String msg) {
        if (b) {
            try {
                JSONObject js = new JSONObject(msg);
                String type = js.getString("type");
                String title = js.getString("title");
                String message = js.getString("message");
                Log.e("ret", type);
                activity.notifyShow(true, type, title, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            activity.notifyShow(false, "", "", "");
        }
    }

    /**
     * 通知消息的点击事件处理
     *
     * @param type
     */
    public void jumpNotify(String type) {
        switch (type) {
            case "0":
                break;
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;
            case "10":
                break;
            case "11":
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if ("one".equals(messageEvent.getMessage())) {
            activity.setViewType(1);
        } else if ("two".equals(messageEvent.getMessage())) {
            activity.setViewType(2);
        } else if ("three".equals(messageEvent.getMessage())) {
            activity.setViewType(3);
        } else if ("four".equals(messageEvent.getMessage())) {
            activity.setViewType(4);
        } else if ("acceptMain".equals(messageEvent.getMessage())) {
            WtDeviceControl.pause();
            activity.enterRoom(roomId);
        } else if (messageEvent.getMessage().contains("create&Rommid")) {
            roomId = messageEvent.getMessage().split("create&Rommid")[1];
        } else if ("over".equals(messageEvent.getMessage())) {
        } else if (messageEvent.getMessage().contains("enterGroup&")) {
            WtDeviceControl.pause();
            String room_id = messageEvent.getMessage().split("enterGroup&")[1];
            activity.enterRoom(room_id);
        } else if (messageEvent.getMessage().contains("exitGroup&")) {
            WtDeviceControl.start();
            activity.exitRoom();// 退出房间
        } else if (messageEvent.getMessage().equals("onDestroy")) {
            activity.destroyWebView();
        }
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            final IMMessage im = messages.get(messages.size() - 1);
            if (im != null) {
                Map<String, Object> map = im.getPushPayload();
                String type = map.get("type") + "";
                String userId = map.get("userId") + "";
                String roomid = map.get("roomid") + "";

                if (type != null && !type.trim().equals("")) {
                    switch (type) {
                        case "LAUNCH":// 收到别人邀请我对讲（单对单）
                            roomId = roomid;
                            EventBus.getDefault().post(new MessageEvent("two"));
                            ReceiveAlertActivity.start(activity, im.getFromAccount(), userId);
                            WtDeviceControl.pause();
                            break;
                        case "CANCEL":// 取消呼叫别人
                            EventBus.getDefault().post(new MessageEvent("cancel"));
                            WtDeviceControl.start();
                            break;
                        case "ACCEPT": // 我的对讲邀请被接受（单对单）
                            EventBus.getDefault().post(new MessageEvent("accept"));
                            WtDeviceControl.pause();
                            activity.enterRoom(roomId);
                            break;
                        case "REFUSE":// 我的对讲邀请被拒绝（单对单）
                            EventBus.getDefault().post(new MessageEvent("refuse"));
                            WtDeviceControl.start();
                            break;
                        case "OVER":
                            WtDeviceControl.start();
                            activity.exitRoom();// 退出房间
                            break;
                    }
                }
            }
        }
    };

    /**
     * app退出时执行该操作
     */
    public void stop() {
        mainModel = null;
        activity.stopService(FloatingWindow);
        activity.stopService(NS);
        activity.unregisterReceiver(netWorkChangeReceiver);
        activity.unregisterReceiver(endApplicationBroadcast);
        Thread.setDefaultUncaughtExceptionHandler(null);
        EventBus.getDefault().unregister(this);
        Log.e("app退出", "app退出");
    }
}
