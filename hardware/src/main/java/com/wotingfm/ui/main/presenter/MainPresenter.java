package com.wotingfm.ui.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.woting.commonplat.receiver.NetWorkChangeReceiver;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.manager.InterPhoneControl;
import com.wotingfm.common.service.FloatingWindowService;
import com.wotingfm.common.service.NotificationService;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.main.model.MainModel;
import com.wotingfm.ui.main.view.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.Map;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MainPresenter extends BasePresenter implements AVChatStateObserver {
    private MainModel mainModel;
    private MainActivity activity;
    private Intent FloatingWindow;
    private Intent NS;

    private NetWorkChangeReceiver netWorkChangeReceiver;

    public MainPresenter(MainActivity mainActivity) {
        this.activity = mainActivity;
        this.mainModel = new MainModel(mainActivity);
        createService();
        registerReceiver();
        getVersion();
        AVChatManager.getInstance().observeAVChatState(this, true);
        registerAVChatIncomingCallObserver(true);// 注册网络通话来电
        registerHangUpCallObserver(true);// 主叫方挂断的监听
        registerCalleeAckObserver(true);// 被叫方的监听（接听、拒绝、忙）
        registerControlObserver(true);// 单对单是否可以说话的监听
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
            } else if (event.contains("enterGroup&")) {
                String room_id = event.split("enterGroup&")[1];
                enterRoom(room_id);// 进入对讲房间
                toggleSpeaker(true);
            } else if (event.equals("onDestroy")) {
                exitRoom();
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
                if (CommonUtils.isLogin()) {
                    mainModel.unRegisterLogin();
                    // 发送注销登录广播通知所有界面
                    activity.sendBroadcast(new Intent(BroadcastConstants.CANCEL));
                    showQuitPerson();// 展示账号被顶替的弹出框
                }
            }
        }
    };

    // 展示账号被顶替的弹出框
    private void showQuitPerson() {
        activity.showExitDialog();
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                GlobalStateConfig.avChatData = data;
                Log.e("来电话数据", new GsonBuilder().serializeNulls().create().toJson(data));
                ToastUtils.show_always(BSApplication.getInstance(), "来电话了");
                try {
                    String s = new GsonBuilder().serializeNulls().create().toJson(data);
                    JSONObject js = new JSONObject(s);
                    String acc_id = js.getString("b");
                    String user_id = acc_id.substring(6);
                    Log.e("user_id", "" + user_id);
                    ReceiveAlertActivity.start(activity, acc_id, user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, register);
    }

    private void registerHangUpCallObserver(boolean register) {
        AVChatManager.getInstance().observeHangUpNotification(new Observer<AVChatCommonEvent>() {
            @Override
            public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
                Log.e("对方挂断电话数据", new GsonBuilder().serializeNulls().create().toJson(avChatHangUpInfo));
                EventBus.getDefault().post(new MessageEvent("cancel"));
                activity.sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
                AVChatManager.getInstance().disableRtc();

            }
        }, register);
    }

    private void registerCalleeAckObserver(boolean register) {
        AVChatManager.getInstance().observeCalleeAckNotification(new Observer<AVChatCalleeAckEvent>() {
            @Override
            public void onEvent(AVChatCalleeAckEvent ackInfo) {
                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    // 对方忙
                    EventBus.getDefault().post(new MessageEvent("cancel"));
                    AVChatManager.getInstance().disableRtc();
                    ToastUtils.show_always(activity, "对方忙");
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    // 对方拒绝
                    EventBus.getDefault().post(new MessageEvent("cancel"));
                    AVChatManager.getInstance().disableRtc();
                    ToastUtils.show_always(activity, "对方拒绝");
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    // 对方接受
                    EventBus.getDefault().post(new MessageEvent("accept"));
                    toggleSpeaker(true);
                    ToastUtils.show_always(activity, "对方接受");
                }
            }
        }, register);
    }

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    private void registerControlObserver(boolean register) {
        AVChatManager.getInstance().observeControlNotification(new Observer<AVChatControlEvent>() {
            @Override
            public void onEvent(AVChatControlEvent netCallControlNotification) {
                Log.e("对方发来指令值", new GsonBuilder().serializeNulls().create().toJson(netCallControlNotification));
                if (netCallControlNotification.getControlCommand() == 1) {
                    toggleSpeaker(true);
                    GlobalStateConfig.canSpeak = false;
                    sendPersonSpeakNews(true, netCallControlNotification);
                } else {
                    GlobalStateConfig.canSpeak = true;
                    sendPersonSpeakNews(false, null);
                }
                Toast.makeText(activity, "对方发来指令值：" + netCallControlNotification.getControlCommand(), Toast.LENGTH_SHORT).show();
            }
        }, register);
    }

    // 发送当前说话人
    private void sendPersonSpeakNews(boolean b, AVChatControlEvent netCallControlNotification) {
        if (b) {
            if (netCallControlNotification != null) {
                try {
                    String s = new GsonBuilder().serializeNulls().create().toJson(netCallControlNotification);
                    JSONObject js = new JSONObject(s);
                    String msg = js.getString("data");
                    JSONTokener jsonParser = new JSONTokener(msg);
                    JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                    String acc_id = arg1.getString("b");
                    String user_id = acc_id.substring(6);

                    Contact.user u = getUser(user_id);
                    if (u != null) {
                        String username = u.getName();
                        String avatar = u.getAvatar();
                        Log.e("说话人数据", "userId=" + user_id + ":" + username + ":" + avatar);
                        Intent intent = new Intent(BroadcastConstants.PUSH_CHAT_OPEN);
                        intent.putExtra("name", username);
                        intent.putExtra("url", avatar);
                        activity.sendBroadcast(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            activity.sendBroadcast(new Intent(BroadcastConstants.PUSH_CHAT_CLOSE));
        }
    }

    private Contact.user getUser(String id) {
        if (id != null && !id.trim().equals("")) {
            if (ChatPresenter.data != null) {
                // 此时有对讲状态
                String _t = ChatPresenter.data.getTyPe().trim();
                if (_t != null && !_t.equals("") && _t.equals("person")) {
                    // 此时的对讲状态是单对单
                    List<Contact.user> list = GlobalStateConfig.list_person;
                    if (list != null && list.size() > 0) {
                        Contact.user u = getU(list, id);
                        return u;
                    } else {
                        return null;
                    }
                } else if (_t != null && !_t.equals("") && _t.equals("group")) {
                    // 此时的对讲状态是群组
                    List<Contact.user> list = GlobalStateConfig.list_group_user;
                    if (list != null && list.size() > 0) {
                        Contact.user u = getU(list, id);
                        return u;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 判断是否有好友数据
    private Contact.user getU(List<Contact.user> list, String id) {
        boolean t = false;
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            String _id = list.get(i).getId();
            if (_id != null && !_id.equals("")) {
                if (_id.equals(id)) {
                    t = true;
                    j = i;
                }
            }
        }
        if (t) {
            return list.get(j);
        } else {
            return null;
        }
    }

    /**
     * 设置扬声器的开关
     *
     * @param b
     */
    public void toggleSpeaker(boolean b) {
        ToastUtils.show_always(activity, "此时状态" + b);
        boolean type = AVChatManager.getInstance().speakerEnabled();
        ToastUtils.show_always(activity, "扬声器的开关" + type);
        if (b) {
            if (!type) {
                AVChatManager.getInstance().setSpeaker(!AVChatManager.getInstance().speakerEnabled());// 设置扬声器是否开启
            }
        }
    }

    private void set() {
//        mManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * 退出房间=群组
     */
    public void exitRoomGroup(String id) {
        InterPhoneControl.quitRoomGroup(id, new InterPhoneControl.Listener() {
            @Override
            public void type(boolean b) {
                if (b) {
                    Log.e("退出组对讲", "退出组成功");
                } else {
                    Log.e("退出组对讲", "退出组失败");
                }

            }
        });
    }

    /**
     * 退出房间=个人
     */
    public void exitRoomPerson(String id) {
        InterPhoneControl.quitRoomPerson();
    }

    /**
     * 进入房间
     *
     * @param roomId
     */
    public void enterRoom(String roomId) {
        InterPhoneControl.enterRoom(roomId, new InterPhoneControl.Listener() {
            @Override
            public void type(boolean b) {
                if (b) {
                    AVChatManager.getInstance().muteLocalAudio(true);// 关闭音频
                    toggleSpeaker(true);
                }
            }
        });
    }

    /**
     * 退出房间
     */
    private void exitRoom() {
        if (ChatPresenter.data != null) {
            // 此时有对讲状态
            String _t = ChatPresenter.data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {// 此时的对讲状态是单对单
                exitRoomPerson(ChatPresenter.data.getACC_ID());
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {// 此时的对讲状态是群组
                exitRoomGroup(ChatPresenter.data.getACC_ID());
            }
        }
    }

    /**
     * app退出时执行该操作
     */
    public void stop() {
        exitRoom();
        mainModel = null;
        activity.stopService(FloatingWindow);
        activity.stopService(NS);
        activity.unregisterReceiver(netWorkChangeReceiver);
        activity.unregisterReceiver(endApplicationBroadcast);
        Thread.setDefaultUncaughtExceptionHandler(null);
        EventBus.getDefault().unregister(this);
        Log.e("app退出", "app退出");
    }

    /**
     * Java方法，人数监听
     */
    @JavascriptInterface
    public void roomNumberListen(final String roomNumber) {
        Log.e("群成员变化", "人数： " + roomNumber);
        Intent intent = new Intent(BroadcastConstants.PUSH_CHAT_GROUP_NUM);
        try {
            intent.putExtra("num", roomNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.sendBroadcast(intent);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onJoinedChannel(int code, String filePath, String fileName, int elapsed) {
        // code 返回加入频道是否成功
        Log.e("服务器连接是否成功的回调 ", "code:" + code);
    }

    @Override
    public void onUserJoined(String account) {
        // 其他用户音视频服务器连接成功后 可以获取当前通话的用户帐号。
        Log.e("其他用户音视频服务器连接成功 ", "account:" + account);
    }

    @Override
    public void onUserLeave(String account, int event) {
        // 通话过程中，若有用户离开
        //  event   －1,用户超时离开  0,正常退出
        Log.e("通话过程中，有用户离开 ", "account:" + account + "   event:" + event);
    }

    @Override
    public void onLeaveChannel() {
        // 自己成功离开频道回调
        Log.e("自己成功离开频道回调 ", "自己成功离开频道回调");
    }

    @Override
    public void onProtocolIncompatible(int status) {
        // @param status 0 自己版本过低  1 对方版本过低
        Log.e("通话双方软件版本不兼容 ", "status:" + status);
    }

    @Override
    public void onDisconnectServer() {
        // 通话过程中，从服务器断开连接, 当自己断网超时后，会回调 onDisconnectServer
        Log.e("服务器断开连接 ", "自己断网超时");
    }

    @Override
    public void onNetworkQuality(String account, int value, AVChatNetworkStats stats) {
        // @param value 0~3 ,the less the better; 0 : best; 3 : worst
        Log.e("通话网络状况 ", "account:" + account + "   value:" + value);
    }

    @Override
    public void onCallEstablished() {
        // 音视频连接成功建立
        Log.e("音视频连接成功建立 ", "音视频连接成功建立");
    }

    @Override
    public void onDeviceEvent(int code, String desc) {
        // 音视频设备状态发生改变
        Log.e("音视频设备状态发生改变 ", "code:" + code + "     desc:" + desc);
    }

    @Override
    public void onTakeSnapshotResult(String s, boolean b, String s1) {
        // 执行截图
        Log.e("执行截图 ", "执行截图");
    }

    @Override
    public void onConnectionTypeChanged(int netType) {
        // 本地网络类型发生改变
        Log.e("本地网络类型发生改变 ", "netType:" + netType);
    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {
        // 当用户录制音视频结束时回调，会通知录制的用户id和录制文件路径。
    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {
        // 当用户录制语音结束时回调，会通知录制文件路径。
    }

    @Override
    public void onLowStorageSpaceWarning(long l) {
        // 存储空间不足时的警告
    }

    @Override
    public void onFirstVideoFrameAvailable(String s) {
        // 当用户第一帧视频画面绘制前通知
    }

    @Override
    public void onFirstVideoFrameRendered(String s) {
        // 当用户视频画面的分辨率改变时通知
    }

    @Override
    public void onVideoFrameResolutionChanged(String s, int i, int i1, int i2) {
        // 用户视频画面的分辨率改变时
    }

    @Override
    public void onVideoFpsReported(String s, int i) {
        // 实时汇报用户的视频绘制帧率
    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame avChatVideoFrame, boolean b) {
        // 当用户开始外部视频处理后,采集到的视频数据通过次回调通知。
        // 用户可以对视频数据做相应的美颜等不同的处理。 需要通过setParameters开启视频数据处理。
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {
        // 当用户开始外部语音处理后,采集到的语音数据通过次回调通知。
        // 用户可以对语音数据做相应的变声等不同的处理。需要通过setParameters开启语音数据处理。
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int device) {
        // 当用户切换扬声器或者耳机的插拔等操作时, 语音的播放设备都会发生变化通知
        Log.e("语音的播放设备 ", "device:" + device);
    }

    @Override
    public void onReportSpeaker(Map<String, Integer> map, int i) {
        // 正在说话用户的语音强度回调，包括自己和其他用户的声音强度。
        // 如果一个用户没有说话,或者说话声音小没有被参加到混音,那么这个用户的信息不会在回调中出现。
        Log.e("语音强度 ", "map:" + new GsonBuilder().serializeNulls().create().toJson(map));

    }

    @Override
    public void onAudioMixingEvent(int i) {
        // 当伴音出错或者结束时，通过此回调进行通知
    }

    @Override
    public void onSessionStats(AVChatSessionStats avChatSessionStats) {
        // 通知实时统计信息
        Log.e("通知实时统计信息 ", "avChatSessionStats:" + new GsonBuilder().serializeNulls().create().toJson(avChatSessionStats));
    }

    @Override
    public void onLiveEvent(int liveEvent) {
        // 通知互动直播事件
        Log.e("通知互动直播事件 ", "liveEvent:" + liveEvent);
    }
}
