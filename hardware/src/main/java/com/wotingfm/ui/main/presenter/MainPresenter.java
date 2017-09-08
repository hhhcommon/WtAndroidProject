package com.wotingfm.ui.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.woting.commonplat.receiver.NetWorkChangeReceiver;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.manager.WtDeviceControl;
import com.wotingfm.common.service.FloatingWindowService;
import com.wotingfm.common.service.NotificationService;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.main.model.MainModel;
import com.wotingfm.ui.main.view.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MainPresenter extends BasePresenter {
    private MainModel mainModel;
    private MainActivity activity;
    private Intent FloatingWindow;
    private Intent NS;

    private NetWorkChangeReceiver netWorkChangeReceiver;
    private String roomId;

    public MainPresenter(MainActivity mainActivity) {
        this.activity = mainActivity;
        this.mainModel = new MainModel(mainActivity);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, true);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(outObserver, true);
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
                } else if (viewType == 4) {
                    EventBus.getDefault().post(new MessageEvent("four"));
                    // mainActivity.changeThree();
                }
            } else if (action.equals(BroadcastConstants.VIEW_NOTIFY_SHOW)) {
                String content = intent.getStringExtra("msg");  // 展示通知消息
                Log.e("推送消息", "msg" + content);
                assemblyMsg(true, content);
            } else if (action.equals(BroadcastConstants.VIEW_NOTIFY_CLOSE)) {
                assemblyMsg(false, "");// 关闭通知消息
                Log.e("推送消息", "关闭消息");
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
//                setNewMessageNotification(type, title, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            activity.notifyShow(false, "", "", "");
//            notificationCancel();
        }
    }

//    // 设置通知消息
//    private void setNewMessageNotification(String type, String message, String title) {
//        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mNotifyBuilder =
//                new NotificationCompat.Builder(activity)
//                        .setContentTitle(title)
//                        .setContentText(message)
//                        .setSmallIcon(R.mipmap.logo)
//                        .setFullScreenIntent(null, false);
//        mNotificationManager.notify(1, mNotifyBuilder.build());
//
////        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//////        Intent pushIntent = new Intent(BroadcastConstants.PUSH_NOTIFICATION);
////////        Intent pushIntent = new Intent(mContext, NotifyNewActivity.class);
////////        PendingIntent in = PendingIntent.getActivity(mContext, 0, pushIntent, 0);
//////        PendingIntent in = PendingIntent.getBroadcast(mContext, 2, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity);
////        mBuilder.setContentTitle(title)  // 设置通知栏标题
////                .setContentText(message) // 设置通知栏显示内容
////                // .setContentIntent(in) // 设置通知栏点击意图
////                .setWhen(System.currentTimeMillis())       // 通知产生时间
////                .setPriority(Notification.PRIORITY_MAX)    // 设置该通知优先级
////                .setAutoCancel(true)                       // 设置点击通知消息时通知栏的通知自动消失
////                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)// 通知声音、闪灯和振动方式为使用当前的用户默认设置
////                //	Notification.DEFAULT_VIBRATE  // 添加默认震动提醒 需要 VIBRATE permission
////                //	Notification.DEFAULT_SOUND    // 添加默认声音提醒
////                //	Notification.DEFAULT_LIGHTS   // 添加默认三色灯提醒
////                //	Notification.DEFAULT_ALL      // 添加默认以上3种全部提醒
////                .setFullScreenIntent(null, false)
////                .setSmallIcon(R.mipmap.logo);     // 设置通知图标
////        mNotificationManager.notify(1, mBuilder.build());
//    }
//
//    private void notificationCancel() {
//        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancelAll();
//    }

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
        String event = messageEvent.getMessage();
        if (!TextUtils.isEmpty(event)) {
            if ("one".equals(event)) {
                activity.setViewType(1);
            } else if ("two".equals(event)) {
                activity.setViewType(2);
            } else if ("three".equals(event)) {
                activity.setViewType(3);
            } else if ("four".equals(event)) {
                activity.setViewType(4);
            } else if (messageEvent.getType() == 10) {
                roomId = messageEvent.getRoomid();
                Log.e("roomId_0000000", roomId);
            } else if (event.equals("enterPersonRoom")) {
                WtDeviceControl.setMute();// 设置静音
                Log.e("roomId_2222222", roomId);
                activity.enterRoom(roomId);// 进入对讲房间
            } else if (event.equals("exitPerson")) {
                Log.e("roomId_3333333", roomId);
                activity.exitRoomPerson(roomId);// 退出对讲房间
            } else if (event.contains("enterGroup&")) {
                WtDeviceControl.setMute();// 设置静音
                String room_id = event.split("enterGroup&")[1];
                activity.enterRoom(room_id);// 进入对讲房间
            } else if (event.contains("exitGroup&")) {
                WtDeviceControl.setMuteResume();// 设置静音恢复
                String room_id = event.split("exitGroup&")[1];
                activity.exitRoomGroup(room_id);// 退出对讲房间
            } else if (event.equals("onDestroy")) {
                activity.destroyWebView();
            }
        } else {
            if (messageEvent != null && messageEvent.getType() == 10) {
                roomId = messageEvent.getRoomid();
                Log.e("roomId_5555555", roomId);
            }
        }
    }

    /**
     * 消息接收观察者
     * 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
     */
    Observer<StatusCode> outObserver = new Observer<StatusCode>() {
        public void onEvent(StatusCode status) {
            Log.i("tag", "User status changed to: " + status);
            if (status.wontAutoLogin()) {
                mainModel.unRegisterLogin();
                // 发送注销登录广播通知所有界面
                activity.sendBroadcast(new Intent(BroadcastConstants.CANCEL));
            }
        }
    };

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
                    Log.e("单对单对讲收到的数据", type);
                    switch (type) {
                        case "LAUNCH":// 收到别人邀请我对讲（单对单）
                            if (!TextUtils.isEmpty(roomid)) roomId = roomid;
                            Log.e("roomId_1111111", roomId);
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
                            break;
                        case "REFUSE":// 我的对讲邀请被拒绝（单对单）
                            EventBus.getDefault().post(new MessageEvent("refuse"));
                            WtDeviceControl.start();
                            break;
                        case "OVER":
                            WtDeviceControl.start();
                            activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));// 关闭好友聊天界面（退出房间）
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
