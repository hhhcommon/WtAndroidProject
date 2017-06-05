package com.wotingfm.ui.intercom.main.view;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;

/**
 * 对讲模块主页
 * 作者：xinLong on 2017/6/4 23:20
 * 邮箱：645700751@qq.com
 */

public class InterPhoneActivity extends FragmentActivity  {
    private static InterPhoneActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercom);
        context = this;
        open(new InterPhoneFragment());
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
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    // 关闭已经打开的 Fragment
    public static void close() {
        context.getSupportFragmentManager().popBackStackImmediate();// 立即删除回退栈中的数据
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
