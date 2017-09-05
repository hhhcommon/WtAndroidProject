package com.wotingfm.ui.main.view;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.manager.InterPhoneControl;
import com.wotingfm.common.manager.WtDeviceControl;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.main.presenter.MainPresenter;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;
import com.wotingfm.ui.play.main.PlayerActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


public class MainActivity extends TabActivity implements View.OnClickListener {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;
    public WebView mWebView;
    private TextView tv_4, tv_5, tv_title, tv_msg, tv_ad;
    private String type;
    private PopupWindow Ndialog;
    private DownloadClient downloadClient;
    private MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.activity_main);
        context=this;
        ButterKnife.bind(this);
//        if(Build.VERSION.SDK_INT>=23){
//            //①checkSelfPermission 检查当前应用的权限
//            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)== PermissionChecker.PERMISSION_DENIED){
//                //②PERMISSION_DENIED说明没有权限需要手动申请
////                requestPermissions 请求权限的方法
//                //第一个参数 activity
//                //第二个参数 需要请求的权限的 权限String数组
//                //第三个参数 请求码 用来区分不同的权限请求
//                //需要注意 最后一个参数 requestCode需要>0
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
//                return;
//            }
//        }
        InitTextView();// 初始化视图
        dialog();// 通知消息弹出框
        mainPresenter = new MainPresenter(this);
        mainPresenter.applyTextColor(false);
        downloadClient = new DownloadClient(this);
    }

    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
        tv_ad = (TextView) findViewById(R.id.tv_ad);// 定位
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
        tabHost.addTab(tabHost.newTabSpec("four").setIndicator("four")
                .setContent(new Intent(this, LookListActivity.class)));
    }

    // 设置webView的参数
    private void webViewSet(WebView mWebView) {
        setUpWebViewDefaults(mWebView);
        // mWebView.loadUrl("https://rtcmulticonnection.herokuapp.com/demos/Audio-Conferencing.html?roomid=123456789");
        mWebView.loadUrl("https://apprtc.wotingfm.com/demos/Audio-Conferencing.html?simple=true");
        mWebView.addJavascriptInterface(this, "android");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                context.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if(request.getOrigin().toString().equals("https://apprtc.wotingfm.com/demos/Audio-Conferencing.html?simple=true")) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null)
            mWebView.onPause();
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
//                NotificationService.test();
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
    public void exitRoomGroup(String id) {
        InterPhoneControl.quitRoomGroup(mWebView, id);
    }

    public void exitRoomPerson(String id) {
        InterPhoneControl.quitRoomPerson(mWebView, id);
    }

    /**
     * 进入房间
     *
     * @param roomId
     */
    public void enterRoom(String roomId) {
        InterPhoneControl.enterRoom(mWebView, roomId);
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
            tv_title.setText(title);
            tv_msg.setText(msg);
            if (Ndialog != null) {
                Ndialog.showAsDropDown(tv_ad, 0, 40);
            }
        } else {
            if (Ndialog != null && Ndialog.isShowing()) {
                Ndialog.dismiss();
            }
        }
    }

    private TabHost extracted() {
        return getTabHost();
    }

    /**
     * 设置界面类型
     *
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

    // "更多" 对话框
    private void dialog() {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_notify, null);
        LinearLayout lin_notify = (LinearLayout) dialog.findViewById(R.id.lin_notify);
        lin_notify.setOnClickListener(this);
        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
        Ndialog = new PopupWindow(dialog);
        // 使其聚集
        Ndialog.setFocusable(true);
        Ndialog.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置允许在外点击消失
        Ndialog.setOutsideTouchable(true);
        // 控制popupwindow的宽度和高度自适应
        dialog.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Ndialog.setWidth(PhoneMsgManager.ScreenWidth);
        Ndialog.setHeight(dialog.getMeasuredHeight());
    }

    /**
     * 销毁传输对象
     */
    public void destroyWebView() {
        if (mWebView != null) {
            exitRoom();// 退出房间
            mWebView.destroy();
            mWebView = null;
        }
    }

    private Handler handler = new Handler(Looper.myLooper());

    /**
     * Java方法，谁在说话
     */
    @JavascriptInterface
    public void beginSpeakCallBack(final String userId, final String username, final String useravatar, final String roomNumber) {
        if (handler != null)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("说话人数据", "userId=" + userId + ":" + username + ":" + useravatar + ":" + roomNumber);
                    Intent intent = new Intent(BroadcastConstants.PUSH_CHAT_OPEN);
                    try {
                        intent.putExtra("name", username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        intent.putExtra("url", useravatar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        intent.putExtra("num", roomNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendBroadcast(intent);
                }
            });
    }

    /**
     * Java方法，房间没有人在说话的时候
     */
    @JavascriptInterface
    public void noPeopleSpoke() {
        if (handler != null)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("说话结束", "没有人在说话");
                    sendBroadcast(new Intent(BroadcastConstants.PUSH_CHAT_CLOSE));
                }
            });
    }

    /**
     * Java方法，人数监听
     */
    @JavascriptInterface
    public void roomNumberListen(final String roomNumber) {
        if (handler != null)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("群成员变化", "人数： " + roomNumber);
                    Intent intent = new Intent(BroadcastConstants.PUSH_CHAT_GROUP_NUM);
                    try {
                        intent.putExtra("num", roomNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendBroadcast(intent);

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
     * Convenience method to set some generic defaults for a
     * given WebView
     *
     * @param webView
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
//
//        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                callPhone();
//            } else{
//                // Permission Denied
//                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
        downloadClient.unregister();
        mainPresenter.stop();
        mainPresenter = null;
    }
}
