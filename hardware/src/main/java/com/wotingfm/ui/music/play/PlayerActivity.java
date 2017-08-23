package com.wotingfm.ui.music.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.music.play.view.PlayerFragment;

import org.greenrobot.eventbus.EventBus;


public class PlayerActivity extends BaseFragmentActivity {
    public static InterPhoneActivity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(R.layout.fragment_main);
        AppManager.getAppManager().addActivity(this);
        openMain(PlayerFragment.newInstance());
    }

    public void openMain(Fragment frg) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    public void close() {
        getSupportFragmentManager().popBackStackImmediate();
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
            if (GlobalStateConfig.IS_RESULT == true) {
                long time = System.currentTimeMillis();
                if (time - tempTime <= 2000) {
                    EventBus.getDefault().post(new MessageEvent("onDestroy"));
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    tempTime = time;
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
                }
            } else {
                GlobalStateConfig.fragmentBase = null;
                GlobalStateConfig.isIS_BACK = true;
                close();
            }
        }
    }

}
