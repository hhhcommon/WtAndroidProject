package com.woting.ui.play.main;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.woting.R;
import com.woting.common.application.BSApplication;
import com.woting.common.bean.MessageEvent;
import com.woting.ui.base.baseactivity.NoTitleBarBaseActivity;

import org.greenrobot.eventbus.EventBus;


public class PlayerActivity extends NoTitleBarBaseActivity {


    @Override
    public int getLayoutId() {
        //  AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        return R.layout.activity_player;
    }

//    public PlayerFragment playerFragment;

    @Override
    public void initView() {
//        playerFragment = PlayerFragment.newInstance();
//        openMain(playerFragment);
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
            if (BSApplication.IS_RESULT == true) {
                long time = System.currentTimeMillis();
                if (time - tempTime <= 2000) {
                    EventBus.getDefault().post(new MessageEvent("onDestroy"));
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    tempTime = time;
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
                }
            } else {
                BSApplication.fragmentBase = null;
                BSApplication.isIS_BACK = true;
                close();
            }
            //   backResult();
        }
    }


}
