package com.wotingfm.ui.main.presenter;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.woting.commonplat.receiver.NetWorkChangeReceiver;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
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
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode> () {
                    public void onEvent(StatusCode status) {
                        Log.i("tag", "User status changed to: " + status);
                        if (status.wontAutoLogin()) {
                            RetrofitUtils.INSTANCE=null;
                            SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
                            et.putString(StringConstant.IS_LOGIN, "false");
                            et.putString(StringConstant.USER_ID, "");
                            et.putString(StringConstant.TOKEN, "");
                            et.putString(StringConstant.USER_NUM, "");
                            et.putString(StringConstant.NICK_NAME, "");
                            et.putString(StringConstant.PORTRAIT, "");
                            et.putString(StringConstant.USER_PHONE_NUMBER, "");
                            et.putString(StringConstant.GENDER, "");
                            et.putString(StringConstant.AGE, "");
                            et.putString(StringConstant.REGION, "");
                            et.putString(StringConstant.USER_SIGN, "");

                            if (!et.commit()) {
                                Log.v("commit", "数据 commit 失败!");
                            }
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                        }
                    }
                }, true);
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
                //  activity.notifyShow(true, type, title, message);
                setNewMessageNotification(type, title, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //  activity.notifyShow(false, "", "", "");
            notificationCancel();
        }
    }

    // 设置通知消息
    private void setNewMessageNotification(String type, String message, String title) {
        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder =
                new NotificationCompat.Builder(activity)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.logo)
                        .setFullScreenIntent(null, false);
        mNotificationManager.notify(1, mNotifyBuilder.build());

//        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
////        Intent pushIntent = new Intent(BroadcastConstants.PUSH_NOTIFICATION);
//////        Intent pushIntent = new Intent(mContext, NotifyNewActivity.class);
//////        PendingIntent in = PendingIntent.getActivity(mContext, 0, pushIntent, 0);
////        PendingIntent in = PendingIntent.getBroadcast(mContext, 2, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity);
//        mBuilder.setContentTitle(title)  // 设置通知栏标题
//                .setContentText(message) // 设置通知栏显示内容
//                // .setContentIntent(in) // 设置通知栏点击意图
//                .setWhen(System.currentTimeMillis())       // 通知产生时间
//                .setPriority(Notification.PRIORITY_MAX)    // 设置该通知优先级
//                .setAutoCancel(true)                       // 设置点击通知消息时通知栏的通知自动消失
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)// 通知声音、闪灯和振动方式为使用当前的用户默认设置
//                //	Notification.DEFAULT_VIBRATE  // 添加默认震动提醒 需要 VIBRATE permission
//                //	Notification.DEFAULT_SOUND    // 添加默认声音提醒
//                //	Notification.DEFAULT_LIGHTS   // 添加默认三色灯提醒
//                //	Notification.DEFAULT_ALL      // 添加默认以上3种全部提醒
//                .setFullScreenIntent(null, false)
//                .setSmallIcon(R.mipmap.logo);     // 设置通知图标
//        mNotificationManager.notify(1, mBuilder.build());
    }

    private void notificationCancel() {
        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
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
            } else if ("acceptMain".equals(event)) {
                WtDeviceControl.pause();
                activity.enterRoom(roomId);
            } else if (event.equals("exitPerson&")) {// 退出个人对讲
                activity.exitRoomPerson(null);
            } else if (event.contains("exitPerson&")) {// 退出个人对讲
                String room_id = event.split("exitPerson&")[1];
                activity.exitRoomPerson(room_id);
            } else if (messageEvent.getType() == 10) {
                roomId = messageEvent.getRoomid();
            } else if ("over".equals(event)) {

            } else if (event.equals("enterGroup&")) {
                WtDeviceControl.pause();
                activity.enterRoom(null);
            } else if (event.contains("enterGroup&")) {
                WtDeviceControl.pause();
                String room_id = event.split("enterGroup&")[1];
                activity.enterRoom(room_id);
            } else if (event.equals("exitGroup&")) {
                WtDeviceControl.start();
                activity.exitRoomGroup(roomId);// 退出房间
            } else if (event.contains("exitGroup&")) {
                WtDeviceControl.start();
                String room_id = event.split("exitGroup&")[1];
                activity.exitRoomGroup(room_id);// 退出房间
            } else if (event.equals("onDestroy")) {
                activity.destroyWebView();
            }
        } else {
            if (messageEvent != null && messageEvent.getType() == 10) {
                roomId = messageEvent.getRoomid();
            }
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
                            if (!TextUtils.isEmpty(roomid))
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
                           // activity.enterRoom(roomId);
                            break;
                        case "REFUSE":// 我的对讲邀请被拒绝（单对单）
                            EventBus.getDefault().post(new MessageEvent("refuse"));
                            WtDeviceControl.start();
                            break;
                        case "OVER":
                            WtDeviceControl.start();
                            activity.exitRoomPerson("");// 退出房间
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
