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
import com.wotingfm.common.service.InterPhoneControl;
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
    public WebView mWebView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tv_4, tv_5, tv_title, tv_msg;
    private LinearLayout lin_notify, largeLabel;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.activity_main);
        InitTextView();
        mainPresenter = new MainPresenter(this);
        mainPresenter.applyTextColor(false);
    }

    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
        largeLabel = (LinearLayout) findViewById(R.id.largeLabel);
        lin_notify = (LinearLayout) findViewById(R.id.lin_notify);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        mWebView = (WebView) findViewById(R.id.mWebView);
        webViewSet(mWebView);// 设置webView的参数
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_4.setClickable(true);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_5.setClickable(true);
        tvTouchSet();
        tabHost = extracted();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("one")
                .setContent(new Intent(this, PlayerActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("two")
                .setContent(new Intent(this, InterPhoneActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("three").setIndicator("three")
                .setContent(new Intent(this, MineActivity.class)));
//        tabHost.addTab(tabHost.newTabSpec("four").setIndicator("four")
//                .setContent(new Intent(this, LookListActivity.class)));
    }

    // 设置webView的参数
    private void webViewSet(WebView mWebView) {
        mWebView.getSettings().setJavaScriptEnabled(true);// 启用javascript
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
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                // 上一首
                WtDeviceControl.pushUpButton();
                break;
            case R.id.tv_2:
                // 暂停，继续
                WtDeviceControl.pushCenter();
                break;
            case R.id.tv_3:
                // 下一首
                WtDeviceControl.pushDownButton();
                break;
            case R.id.lin_notify:
                // 通知消息的点击事件处理
                mainPresenter.jumpNotify(type);
                break;
        }
    }

    private void tvTouchSet() {
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

    }

    // 对讲请求
    private void press_ptt() {
        EventBus.getDefault().post(new MessageEvent("pause"));
        WtDeviceControl.pushPTT(mWebView);
        tv_5.setBackgroundResource(R.color.app_basic);
    }

    // 松手，释放说话权
    private void jack_ptt() {
        WtDeviceControl.releasePTT(mWebView);
        tv_5.setBackgroundColor(Color.parseColor("#16181a"));
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

    /**
     * 退出房间
     */
    public void exitRoom(){
        InterPhoneControl.quitRoom(mWebView,"");
    }

    /**
     * 进入房间
     * @param roomId
     */
    public void enterRoom(String roomId){
        InterPhoneControl.enterRoom(mWebView,roomId);
    }

    /**
     * 模拟通知消息的展示
     *
     * @param b
     * @param type
     * @param title
     * @param msg
     */
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

    private TabHost extracted() {
        return getTabHost();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (mWebView != null) {
//            mWebView.loadUrl("javascript:exitRoom()");
//            mWebView.destroy();
//            mWebView = null;
//        }
//    }

    /**
     * 设置界面类型
     * @param type
     */
    public void setViewType(int type) {
        switch (type) {
            case 1:
                tabHost.setCurrentTabByTag("one");
                break;
            case 2:
                tabHost.setCurrentTabByTag("two");
                break;
            case 3:
                tabHost.setCurrentTabByTag("three");
                break;
            case 4:
                tabHost.setCurrentTabByTag("four");
                break;
            case 5:
                break;
        }
    }

    /**
     * 销毁传输对象
     */
    public void destroyWebView(){
        if (mWebView != null) {
            exitRoom();// 退出房间
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
        mainPresenter.stop();
        mainPresenter = null;
    }
}
