package com.woting.ui.main.view;

import android.app.TabActivity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost;

import com.woting.R;
import com.woting.common.bean.MessageEvent;
import com.woting.common.constant.BroadcastConstants;
import com.woting.common.service.InterPhoneControl;
import com.woting.common.service.WtDeviceControl;
import com.woting.ui.intercom.main.chat.presenter.ChatPresenter;
import com.woting.ui.intercom.main.view.InterPhoneActivity;
import com.woting.ui.main.presenter.MainPresenter;
import com.woting.ui.play.look.activity.LookListActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * 主页
 */
public class MainActivity extends TabActivity  {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;
    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InitTextView();// 初始化视图
        mainPresenter = new MainPresenter(this);
        mainPresenter.applyTextColor(false);
    }

    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
        mWebView = (WebView) findViewById(R.id.mWebView);
        webViewSet(mWebView);// 设置webView的参数
        tabHost = extracted();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("one")
                .setContent(new Intent(this, LookListActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("two")
                .setContent(new Intent(this, InterPhoneActivity.class)));
    }

    // 设置webView的参数
    private void webViewSet(WebView mWebView) {
        mWebView.getSettings().setJavaScriptEnabled(true);// 启用javascript
        // 从assets目录下面的加载html
        // mWebView.loadUrl("https://rtcmulticonnection.herokuapp.com/demos/Audio-Conferencing.html?roomid=123456789");
        mWebView.loadUrl("https://apprtc.woting.com/demos/Audio-Conferencing.html?simple=true");
        mWebView.addJavascriptInterface(this, "android");
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

//    tv_5.setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.e("按钮操作", "按下");
//                    //按下状态
//                    press_ptt();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    Log.e("按钮操作", "松手");
//                    //抬起手后的操作
//                    jack_ptt();
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    Log.e("按钮操作", "取消");
//                    //抬起手后的操作
//                    jack_ptt();
//                    break;
//            }
//            return false;
//        }
//    });

    // 对讲请求
    private void press_ptt() {
        EventBus.getDefault().post(new MessageEvent("pause"));
        WtDeviceControl.pushPTT(mWebView);
    }

    // 松手，释放说话权
    private void jack_ptt() {
        WtDeviceControl.releasePTT(mWebView);
    }

    // 开始语音识别
    private void press() {
        WtDeviceControl.pushVoiceStart();
    }

    // 松手，结束语音识别
    private void jack() {
        WtDeviceControl.releaseVoiceStop();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
        mainPresenter.stop();
        mainPresenter = null;
    }
}
