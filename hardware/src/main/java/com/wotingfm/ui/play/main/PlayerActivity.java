package com.wotingfm.ui.play.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.main.view.InterPhoneFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * 播放主页
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class PlayerActivity extends BaseFragmentActivity {

    public static PlayerActivity context;
    private long tempTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.fragment_main);
        AppManager.getAppManager().addActivity(this);
        context=this;
        openMain(PlayerFragment.newInstance());
    }

    public static void open(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out)
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }


    public void openMain(Fragment frg) {
        context.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    public static void close() {
        context.getSupportFragmentManager().popBackStackImmediate();
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

//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            long time = System.currentTimeMillis();
//            if (time - tempTime <= 2000) {
//                android.os.Process.killProcess(android.os.Process.myPid());
//            } else {
//                tempTime = time;
//                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            if (GlobalStateConfig.IS_RESULT == true) {
//                long time = System.currentTimeMillis();
//                if (time - tempTime <= 2000) {
//                    EventBus.getDefault().post(new MessageEvent("onDestroy"));
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                } else {
//                    tempTime = time;
//                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                GlobalStateConfig.fragmentBase = null;
//                GlobalStateConfig.isIS_BACK = true;
//                close();
//            }
//            //   backResult();
//        }
//    }


}
