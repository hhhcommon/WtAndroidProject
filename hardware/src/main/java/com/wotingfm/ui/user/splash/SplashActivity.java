package com.wotingfm.ui.user.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.user.guide.view.GuideActivity;
import java.lang.ref.WeakReference;

/**
 * 启动页面，第一个activity
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class SplashActivity extends Activity {
    private final InnerHandler mHandler = new InnerHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(0, 1000); // 延时启动
    }

    // 防止内存泄漏
    private static class InnerHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        public InnerHandler(SplashActivity activity) {
            mActivity = new WeakReference<SplashActivity>(activity);
        }

        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            if (activity == null) {
                return;
            }
            activity.todo();

        }
    }

    // 跳转到引导页
    private void todo(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
