package com.wotingfm.ui.mine.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Toast;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.mine.main.view.MineFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：xinLong on 2017/6/2 12:15
 * 邮箱：645700751@qq.com
 */
public class MineActivity extends BaseFragmentActivity {

    private static MineActivity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        context = this;
        openOne(new MineFragment());
    }

    /**
     * 打开新的 Fragment
     */
    private void openOne(Fragment frg) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent in = new Intent(BroadcastConstants.IMAGE_UPLOAD);
        in.putExtra("resultCode", resultCode);
        if(data!=null){
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
        }else{
            try {
                in.putExtra("uri", GlobalStateConfig.savePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                in.putExtra("path", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sendBroadcast(in);
    }

    private long tempTime;

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

}
