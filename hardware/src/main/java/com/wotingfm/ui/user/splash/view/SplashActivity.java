package com.wotingfm.ui.user.splash.view;

import android.app.Activity;
import android.os.Bundle;

import com.wotingfm.ui.user.splash.presenter.SplashPresenter;

/**
 * 启动页面，第一个activity
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class SplashActivity extends Activity {
    // private final InnerHandler mHandler = new InnerHandler(this);
    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter =new SplashPresenter(this);
        todo();
        // mHandler.sendEmptyMessageDelayed(0, 1000); // 延时启动
    }

    // 防止内存泄漏
//    private static class InnerHandler extends Handler {
//        private final WeakReference<SplashActivity> mActivity;
//
//        public InnerHandler(SplashActivity activity) {
//            mActivity = new WeakReference<SplashActivity>(activity);
//        }
//
//        public void handleMessage(Message msg) {
//            SplashActivity activity = mActivity.get();
//            if (activity == null) {
//                return;
//            }
//            activity.todo();
//
//        }
//    }

    // 跳转到引导页
    private void todo() {
       presenter.todo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mHandler.removeCallbacksAndMessages(null);
        presenter.destroy();
        presenter=null;
    }
}
