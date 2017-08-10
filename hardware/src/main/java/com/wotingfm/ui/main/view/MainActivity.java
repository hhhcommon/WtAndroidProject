package com.wotingfm.ui.main.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.woting.commonplat.utils.KeyboardChangeListener;
import com.wotingfm.R;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.bean.Room;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.service.WtDeviceControl;
import com.wotingfm.common.utils.AndroidBug5497Workaround;
import com.wotingfm.common.utils.IMManger;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.NetUtils;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.main.presenter.MainPresenter;
import com.wotingfm.ui.play.live.LiveRoomActivity;
import com.wotingfm.ui.play.look.activity.serch.SerchFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.mine.main.MineActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.id;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.wotingfm.R.id.mWebView;
import static com.wotingfm.R.id.relatLable;
import static com.wotingfm.R.id.tvContent;
import static com.wotingfm.R.mipmap.disconnect;


public class MainActivity extends TabActivity implements View.OnClickListener {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;
    private MainActivity context;
    public WebView mWebView;
    private LinearLayout largeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        largeLabel = (LinearLayout) findViewById(R.id.largeLabel);
        context = this;
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
        mWebView = (WebView) findViewById(R.id.mWebView);
        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 从assets目录下面的加载html
        // mWebView.loadUrl("https://rtcmulticonnection.herokuapp.com/demos/Audio-Conferencing.html?roomid=123456789");
        mWebView.loadUrl("https://apprtc.wotingfm.com/demos/Audio-Conferencing.html?simple=true");
        mWebView.addJavascriptInterface(MainActivity.this, "android");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onPermissionRequest(PermissionRequest request) {
                                            request.grant(request.getResources());
                                        }
                                    }
        );
        // 设置允许JS弹窗
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // applySelectedColor();
        applyTextColor(false);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, true);
        InitTextView();
        mainPresenter = new MainPresenter(this);
    }


    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
        lin_notify = (LinearLayout) findViewById(R.id.lin_notify);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_msg = (TextView) findViewById(R.id.tv_msg);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_4.setClickable(true);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_5.setClickable(true);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_6.setClickable(true);
        tv_4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("按钮操作", "按下");
                        //按下状态
                        press();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("按钮操作", "松手");
                        //抬起手后的操作
                        jack();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e("按钮操作", "取消");
                        //抬起手后的操作
                        jack();
                        break;
                }
                return false;
            }
        });

        tv_5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("按钮操作", "按下");
                        //按下状态
                        press_ptt();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("按钮操作", "松手");
                        //抬起手后的操作
                        jack_ptt();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e("按钮操作", "取消");
                        //抬起手后的操作
                        jack_ptt();
                        break;
                }
                return false;
            }
        });

        tv_6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("按钮操作", "按下");
                        //按下状态
                        press_pttGroup();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("按钮操作", "松手");
                        //抬起手后的操作
                        jack_pttGroup();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e("按钮操作", "取消");
                        //抬起手后的操作
                        jack_pttGroup();
                        break;
                }
                return false;
            }
        });
        tabHost = extracted();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("one")
                .setContent(new Intent(this, PlayerActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("two")
                .setContent(new Intent(this, InterPhoneActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("three").setIndicator("three")
                .setContent(new Intent(this, MineActivity.class)));
    }

    private TabHost extracted() {
        return getTabHost();
    }

    private boolean isStar = true;

    // 对讲请求
    private void press_ptt() {
        WtDeviceControl.pushPTT();
        EventBus.getDefault().post(new MessageEvent("pause"));
        tv_5.setBackgroundResource(R.color.app_basic);
        if (isStar == true) {
            mWebView.loadUrl("javascript:beginSpeak()");
            isStar = false;
        }

    }

    // 松手，释放说话权
    private void jack_ptt() {
        WtDeviceControl.releasePTT();
        mWebView.loadUrl("javascript:stopSpeak()");
        isStar = true;
        tv_5.setBackgroundColor(Color.parseColor("#16181a"));
    }

    // 对讲请求
    private void press_pttGroup() {
        WtDeviceControl.pushPTT();
        EventBus.getDefault().post(new MessageEvent("pause"));
        tv_6.setBackgroundResource(R.color.app_basic);
        if (isStarGroup == true) {
            mWebView.loadUrl("javascript:beginSpeak()");
            isStarGroup = false;
        }

    }

    private boolean isStarGroup = true;

    // 松手，释放说话权
    private void jack_pttGroup() {
        WtDeviceControl.releasePTT();
        mWebView.loadUrl("javascript:stopSpeak()");
        isStarGroup = true;
        tv_6.setBackgroundColor(Color.parseColor("#16181a"));
    }

    // 开始语音识别
    private void press() {
        WtDeviceControl.pushVoiceStart();
        tv_4.setBackgroundResource(R.color.app_basic);
    }

    // 松手，结束语音识别
    private void jack() {
        WtDeviceControl.releaseVoiceStop();
        tv_4.setBackgroundColor(Color.parseColor("#16181a"));
    }

    private TextView tv_4, tv_5, tv_6, tv_title, tv_msg;
    private LinearLayout lin_notify;
    private String type;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                // 上一首
                EventBus.getDefault().post(new MessageEvent("step"));
                WtDeviceControl.pushUpButton();
                break;
            case R.id.tv_2:
                // 暂停，继续
                EventBus.getDefault().post(new MessageEvent("stop_or_star"));
                WtDeviceControl.pushCenter();
                break;
            case R.id.tv_3:
                // 下一首
                EventBus.getDefault().post(new MessageEvent("next"));
                WtDeviceControl.pushDownButton();
                break;
            case R.id.lin_notify:
                // 通知消息的点击事件处理
                mainPresenter.jumpNotify(type);
                break;
        }
    }

    public void notifyShow(boolean b, String type, String title, String msg) {
        if (b) {
            this.type = type;
            lin_notify.setVisibility(View.VISIBLE);
            tv_title.setText(title);
            tv_msg.setText(msg);
        } else {
            lin_notify.setVisibility(View.GONE);
        }
    }

    // app退出时执行该操作
    private void stop() {
        mainPresenter.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWebView != null) {
            mWebView.loadUrl("javascript:exitRoom()");
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
        if (mWebView != null) {
            mWebView.loadUrl("javascript:exitRoom()");
            mWebView.destroy();
            mWebView = null;
        }
        Thread.setDefaultUncaughtExceptionHandler(null);
        EventBus.getDefault().unregister(this);
    }


    private void applySelectedColor() {
        // -724225
//        float[] colorHSV = new float[]{0f, 0f, 0f};
//        int c = Color.HSVToColor(colorHSV);
        int c = -1;
        int color = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));
        StatusBarUtil.setStatusBarColor(context, color, false);
//        StatusBarUtil.setStatusBarColor(context, R.color.white, false);
    }

    private void applyTextColor(boolean b) {
        if (b) {
            StatusBarUtil.StatusBarLightMode(context, false);
        } else {
            StatusBarUtil.StatusBarLightMode(context, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if ("one".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("one");
        } else if ("two".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("two");
        } else if ("three".equals(messageEvent.getMessage())) {
            tabHost.setCurrentTabByTag("three");
        } else if ("acceptMain".equals(messageEvent.getMessage())) {
            EventBus.getDefault().post(new MessageEvent("pause"));
         /*   disconnect();
            initPlayer();
            mainPresenter.connectToRoom(roomId, false, false, false, 0);
            activityRunning = true;
            // Video is not paused for screencapture. See onPause.
            if (peerConnectionClient != null && !screencaptureEnabled) {
                peerConnectionClient.startVideoSource();
            }
            onToggleMicBase(false);*/
            mWebView.loadUrl("javascript:joinRoom('" + roomId + "')");
            tv_5.setVisibility(View.VISIBLE);
            largeLabel.setVisibility(View.VISIBLE);
            tv_6.setVisibility(View.GONE);
        } else if (messageEvent.getMessage().contains("create&Rommid")) {
            roomId = messageEvent.getMessage().split("create&Rommid")[1];
        } else if ("over".equals(messageEvent.getMessage())) {
            tv_5.setVisibility(View.GONE);
            largeLabel.setVisibility(View.GONE);
        } else if (messageEvent.getMessage().contains("enterGroup&")) {
            tv_5.setVisibility(View.GONE);
            EventBus.getDefault().post(new MessageEvent("pause"));
            String roomid = messageEvent.getMessage().split("enterGroup&")[1];
            mWebView.loadUrl("javascript:joinRoom('" + roomid + "')");
            tv_6.setVisibility(View.VISIBLE);
            largeLabel.setVisibility(View.VISIBLE);
        } else if (messageEvent.getMessage().contains("exitGroup&")) {
            tv_5.setVisibility(View.GONE);
            EventBus.getDefault().post(new MessageEvent("start"));
            mWebView.loadUrl("javascript:exitRoom()");
            tv_6.setVisibility(View.GONE);
            largeLabel.setVisibility(View.GONE);
        } else if (messageEvent.getMessage().equals("onDestroy")) {
            if (mWebView != null) {
                mWebView.loadUrl("javascript:exitRoom()");
                mWebView.destroy();
                mWebView = null;
            }
        }
    }

    private String roomId;
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
                //收到发起对讲
                if ("LAUNCH".equals(type)) {
                  /*  disconnect();
                    initPlayer();*/
                    roomId = roomid;
                    EventBus.getDefault().post(new MessageEvent("two"));
                    ReceiveAlertActivity.start(MainActivity.this, im.getFromAccount(), userId);
                    EventBus.getDefault().post(new MessageEvent("pause"));
                }
                //取消
                if ("CANCEL".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("cancel"));
                    EventBus.getDefault().post(new MessageEvent("start"));
                }
                //拒绝对讲
                else if ("REFUSE".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("refuse"));
                    EventBus.getDefault().post(new MessageEvent("start"));
                } else if ("OVER".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("start"));
                    mWebView.loadUrl("javascript:exitRoom()");
                    tv_5.setVisibility(View.GONE);
                    largeLabel.setVisibility(View.GONE);
                }
                //接受对讲
                else if ("ACCEPT".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("accept"));
                    EventBus.getDefault().post(new MessageEvent("pause"));
                    mWebView.loadUrl("javascript:joinRoom('" + roomId + "')");
                    tv_5.setVisibility(View.VISIBLE);
                    largeLabel.setVisibility(View.VISIBLE);
                  /*  activityRunning = true;
                    disconnect();
                    initPlayer();
                    mainPresenter.connectToRoom(roomId, false, false, false, 0);
                    // Video is not paused for screencapture. See onPause.
                    if (peerConnectionClient != null && !screencaptureEnabled) {
                        peerConnectionClient.startVideoSource();
                    }
                    onToggleMicBase(false);*/
                }
            }
        }
    };


    private static final String TAG = MainActivity.class.getSimpleName();
}
