package com.wotingfm.ui.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.play.look.activity.LookListFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class PlayerActivity extends NoTitleBarBaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    private PlayerFragment playerFragment;

    @Override
    public void initView() {
        AppManager.getAppManager().addActivity(this);
        playerFragment = PlayerFragment.newInstance();
        openMain(playerFragment);
    }

    public void open(Fragment frg) {
        getSupportFragmentManager().beginTransaction().remove(frg).commit();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in, 0,
                        R.anim.slide_left_in, 0)
                .add(R.id.fragment, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }


    public void openMain(Fragment frg) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, frg)
                .addToBackStack(SequenceUUID.getUUID())
                .commitAllowingStateLoss();
    }

    public void openNoAnim(Fragment frg) {
        getSupportFragmentManager().beginTransaction().remove(frg).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, frg)
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
