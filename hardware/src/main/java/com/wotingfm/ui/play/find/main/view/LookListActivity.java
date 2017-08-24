package com.wotingfm.ui.play.find.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by amine on 2017/6/21.
 * 发现列表
 */

public class LookListActivity extends BaseFragmentActivity {

    public static LookListActivity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_look_list);
        AppManager.getAppManager().addActivity(this);
        context = this;
        openOne(new LookListFragment());
    }
    public static void openOne(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commit();
        hintKbTwo();
    }

    /**
     * 打开新的 Fragment
     */
    public static void open(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out)
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commit();
        hintKbTwo();
    }

    /**
     * 关闭已经打开的 Fragment
     */
    public static void close() {
        if (context != null && context.getSupportFragmentManager() != null) {
            context.getSupportFragmentManager().popBackStackImmediate();
        }
        hintKbTwo();
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
            long time = System.currentTimeMillis();
            if (time - tempTime <= 2000) {
                EventBus.getDefault().post(new MessageEvent("onDestroy"));
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                tempTime = time;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            }
        }
    }

}
