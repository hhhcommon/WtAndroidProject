package com.wotingfm.ui.intercom.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.WindowManager;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 对讲模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class InterPhoneActivity extends BaseFragmentActivity {
    public static InterPhoneActivity context;
    private long tempTime;
    private MessageReceiver Receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.fragment_main);
        context = this;
        openOne(new InterPhoneFragment());
        setReceiver();
    }

    // 打开新的 Fragment
    private void openOne(Fragment frg) {
        hintKbTwo();
        context.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    /**
     * 打开新的 Fragment
     *
     * @param frg
     */
    public static void open(Fragment frg) {
        hintKbTwo();
        context.getSupportFragmentManager().beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setCustomAnimations(
                        R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out)
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    /**
     * 关闭已经打开的 Fragment
     */
    public static void close() {
        context.getSupportFragmentManager().popBackStackImmediate();// 立即删除回退栈中的数据
        hintKbTwo();
    }

    /**
     * 关闭当前activity的fragment，只保留一个
     */
    public static void closeAll() {
        // 获取当前回退栈中的Fragment个数
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        // 回退栈中至少有多个fragment,栈底部是首页
        if (backStackEntryCount > 1) {
            // 如果回退栈中Fragment个数大于一.一直退出
            while (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStackImmediate();
            }
        }
        hintKbTwo();
    }



    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.VIEW_INTER_PHONE_CLOSE_ALL);
            registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.VIEW_INTER_PHONE_CLOSE_ALL)) {
                closeAll();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent in = new Intent(BroadcastConstants.IMAGE_UPLOAD);
        in.putExtra("resultCode", resultCode);
        try {
            in.putExtra("uri", data.getData().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            in.putExtra("path", data.getStringExtra("return"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendBroadcast(in);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            long time = System.currentTimeMillis();
            if (time - tempTime <= 2000) {
                EventBus.getDefault().post(new MessageEvent("onDestroy"));
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                tempTime = time;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            }
        } else {
            close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Receiver != null) {
            unregisterReceiver(Receiver);
            Receiver = null;
        }
    }
}
