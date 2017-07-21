package com.wotingfm.ui.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.utils.StatusBarUtil;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;


public class PlayerActivity extends NoTitleBarBaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public void initView() {
        AppManager.getAppManager().addActivity(this);
        openMain(PlayerFragment.newInstance());
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
        getSupportFragmentManager().popBackStack();
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
