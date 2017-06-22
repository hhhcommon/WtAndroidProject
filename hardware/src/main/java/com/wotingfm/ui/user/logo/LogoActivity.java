package com.wotingfm.ui.user.logo;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.view.InterPhoneFragment;

/**
 * 个人模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class LogoActivity extends FragmentActivity  {
    private static LogoActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        context = this;
        open(new LogoFragment());
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

    // 打开新的 Fragment
    public static void open(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    // 关闭已经打开的 Fragment
    public static void close() {
        context.getSupportFragmentManager().popBackStackImmediate();// 立即删除回退栈中的数据
    }

    // 关闭activity
    public static void closeActivity() {
        context.finish();
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
