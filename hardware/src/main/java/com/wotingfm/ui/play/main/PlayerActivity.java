package com.wotingfm.ui.play.main;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;


public class PlayerActivity extends NoTitleBarBaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    public PlayerFragment playerFragment;

    @Override
    public void initView() {
        playerFragment = PlayerFragment.newInstance();
        openMain(playerFragment);
    }


    public void open(Fragment frg) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in, 0,
                        R.anim.slide_left_in, 0)
                .add(R.id.fragment_content, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
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
            //   backResult();
        }
    }


}
