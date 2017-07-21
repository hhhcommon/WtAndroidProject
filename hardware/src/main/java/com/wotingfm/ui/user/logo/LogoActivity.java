package com.wotingfm.ui.user.logo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.intercom.main.view.InterPhoneFragment;
import com.wotingfm.ui.test.PlayerActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 个人模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class LogoActivity extends FragmentActivity {
    private static LogoActivity context;

    public static void start(Context activity) {
        Intent intent = new Intent(activity, LogoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        context = this;
        applyTextColor(false);
        open(new LogoFragment());
    }

    // 打开新的 Fragment
    public static void open(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setCustomAnimations(
                        R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out)
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
        hintKbTwo();
    }

    // 关闭已经打开的 Fragment
    public static void close() {
        context.getSupportFragmentManager().popBackStackImmediate();// 立即删除回退栈中的数据
        hintKbTwo();
    }

    // 关闭activity
    public static void closeActivity() {
        context.finish();
    }

    /**
     * 关闭当前activity的fragment，只保留一个
     */
    public void closeAll() {
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

    //此方法只是关闭软键盘
    private static void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && context.getCurrentFocus() != null) {
            if (context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //此方法，如果显示则隐藏，如果隐藏则显示
    private static void hintKbOne() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm.isActive()) {//如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
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

    private void applyTextColor(boolean b) {
        if (b) {
            StatusBarUtil.StatusBarLightMode(context, false);
        } else {
            StatusBarUtil.StatusBarLightMode(context, true);
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


    private long tempTime;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            long time = System.currentTimeMillis();
            if (time - tempTime <= 2000) {
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                tempTime = time;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            }
        } else {
            close();
        }
    }
}
