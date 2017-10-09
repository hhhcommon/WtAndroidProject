package com.wotingfm.ui.base.baseactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.wotingfm.common.manager.WtDeviceControl;
import com.wotingfm.ui.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：xinlong on 2016/10/25 21:18
 * 邮箱：645700751@qq.com
 */
public abstract class BaseFragmentActivity extends AppCompatActivity {
    protected static FragmentActivity context;
    boolean isTalk = false;
    boolean isTTS = false;
    private MyBroadCastReceiver myBroadCastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //电源键监听
//        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(mBatInfoReceiver, filter);
        myBroadCastReceiver = new MyBroadCastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(myBroadCastReceiver, intentFilter);
    }

    // 设置android app 的字体大小不受系统字体大小改变的影响
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    //此方法只是关闭软键盘
    public static void hintKbTwo() {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive() && context.getCurrentFocus() != null) {
//            if (context.getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    //此方法，如果显示则隐藏，如果隐藏则显示
    public static void hintKbOne() {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
//        if (imm.isActive()) {//如果开启
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//        }
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (event.getRepeatCount() == 0) {//识别长按短按的代码
                    if (isTalk) {
                        WtDeviceControl.releasePTT();
                        isTalk = false;
                    } else {
                        EventBus.getDefault().post(new MessageEvent("pause"));
                        WtDeviceControl.pushPTT();
                        isTalk = true;
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (event.getRepeatCount() == 0) {//识别长按短按的代码
                    if (isTTS) {
                        WtDeviceControl.releaseVoiceStop();
                        isTTS = false;
                    } else {
                        WtDeviceControl.pushVoiceStart();
                        isTTS = true;
                    }
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//            final String action = intent.getAction();
//            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                Log.e("电源键监听", "电源键监听");
//            }
//        }
//    };

    private class MyBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //你自己先把 reasons == homekey 和 长按homekey 排除，剩下的做下面的处理
            String reason = intent.getStringExtra("reason");
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                System.out.println("Intent.ACTION_CLOSE_SYSTEM_DIALOGS : " + intent.getStringExtra("reason"));
                if (intent.getExtras()!=null && intent.getExtras().getBoolean("myReason")){
                    myBroadCastReceiver.abortBroadcast();
                }else if (reason != null){
                    if (reason.equalsIgnoreCase("globalactions")){
                        //屏蔽电源长按键的方法：
                        Intent myIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                        myIntent.putExtra("myReason", true);
                        context.sendOrderedBroadcast(myIntent, null);
                        Log.e("电源","电源 键被长按");
                    }else if (reason.equalsIgnoreCase("homekey")){
                        //屏蔽Home键的方法
                        //在这里做一些你自己想要的操作,比如重新打开自己的锁屏程序界面，这样子就不会消失了
                        Log.e("Home","Home 键被触发");
                    }else if (reason.equalsIgnoreCase("recentapps")){
                        //屏蔽Home键长按的方法
                        Log.e("Home","Home 键被长按");
                    }
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCastReceiver);
    }

}