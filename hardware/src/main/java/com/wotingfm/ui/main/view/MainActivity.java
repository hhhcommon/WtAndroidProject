package com.wotingfm.ui.main.view;

import android.annotation.TargetApi;
import android.app.Dialog;
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

import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
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
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


public class MainActivity extends TabActivity implements View.OnClickListener {
    public static TabHost tabHost;
    private MainPresenter mainPresenter;
    private TextView tv_4, tv_5, tv_title, tv_msg, tv_ad;
    private String type;
    private PopupWindow NDialog;
    private MainActivity context;
    private Dialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.activity_main);
        context=this;
        ButterKnife.bind(this);
        InitTextView();// 初始化视图
        dialog();// 通知消息弹出框
        initDialog();// 账户通知消息弹出框
        mainPresenter = new MainPresenter(this);
        mainPresenter.applyTextColor(false);
    }

    // 初始化视图,主页跳转的3个界面
    private void InitTextView() {
        tv_ad = (TextView) findViewById(R.id.tv_ad);// 定位
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_4.setClickable(true);
        tv_5 = (TextView) findViewById(R.id.tv_5);
//        tv_5.setOnClickListener(this);
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
        WtDeviceControl.pushPTT();
        tv_5.setBackgroundResource(R.color.app_basic);
    }

    // 松手，释放说话权
    private void jack_ptt() {
        WtDeviceControl.releasePTT();
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
            if (NDialog != null) {
                NDialog.showAsDropDown(tv_ad, 0, 40);
            }
        } else {
            if (NDialog != null && NDialog.isShowing()) {
                NDialog.dismiss();
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

    // 初始化对话框
    private void initDialog() {
        // 解散群组对话框
        View dialog1 = LayoutInflater.from(this).inflate(R.layout.dialog_talk_person_del, null);
        dialog1.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LogoActivity.class));
            }
        }); // 清空
        dialog1.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });  // 取消
        TextView textTitle = (TextView) dialog1.findViewById(R.id.tv_title);
        textTitle.setText("您的账号在其他设备上登录，是否重新登录？？");

        exitDialog = new Dialog(this, R.style.MyDialogs);
        exitDialog.setContentView(dialog1);
        exitDialog.setCanceledOnTouchOutside(false);
        exitDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_background);
    }

    public void showExitDialog(){
        exitDialog.show();
    }

    // "更多" 对话框
    private void dialog() {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_notify, null);
        LinearLayout lin_notify = (LinearLayout) dialog.findViewById(R.id.lin_notify);
        lin_notify.setOnClickListener(this);
        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
        NDialog = new PopupWindow(dialog);
        // 使其聚集
        NDialog.setFocusable(true);
        NDialog.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置允许在外点击消失
        NDialog.setOutsideTouchable(true);
        // 控制popupwindow的宽度和高度自适应
        dialog.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        NDialog.setWidth(PhoneMsgManager.ScreenWidth);
        NDialog.setHeight(dialog.getMeasuredHeight());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.stop();
        mainPresenter = null;
    }
}
