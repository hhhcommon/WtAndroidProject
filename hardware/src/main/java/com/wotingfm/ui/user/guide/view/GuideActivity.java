package com.wotingfm.ui.user.guide.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.woting.commonplat.net.volley.VolleyRequest;
import com.wotingfm.ui.user.guide.presenter.GuidePresenter;

import java.lang.ref.WeakReference;

/**
 * 引导页面，第二个activity
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class GuideActivity extends Activity {
    private final InnerHandler mHandler = new InnerHandler(this);
    private GuidePresenter guidePresenter;
    private String tag = "SPLASH_VOLLEY_REQUEST_CANCEL_TAG";
    private boolean isCancelRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guidePresenter =new GuidePresenter(this);
        mHandler.sendEmptyMessageDelayed(0, 3000); // 延时启动
    }

    // 防止内存泄漏
    private static class InnerHandler extends Handler {
        private final WeakReference<GuideActivity> mActivity;

        public InnerHandler(GuideActivity activity) {
            mActivity = new WeakReference<GuideActivity>(activity);
        }

        public void handleMessage(Message msg) {
            GuideActivity activity = mActivity.get();
            if (activity == null) {
                return;
            }
            activity.todo();
        }
    }

    // 延时操作
    public void todo() {
        guidePresenter.todo();
    }

    /**
     * 获取当前页面标签
     *
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     * 获取当前页面的加载状态
     *
     * @return
     */
    public boolean getCancelRequest() {
        return isCancelRequest;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCancelRequest = VolleyRequest.cancelRequest(tag);
        mHandler.removeCallbacksAndMessages(null);
    }
}
