package com.wotingfm.ui.base.baseactivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.woting.commonplat.widget.LoadingDialog;
import com.wotingfm.common.utils.ProgressDialogUtils;

import butterknife.ButterKnife;

/**
 * App
 * Created by Administrator on 9/6/2016.
 */
public abstract class NoTitleBarBaseActivity extends BaseActivity {
    protected Context context;


    // 设置android app 的字体大小不受系统字体大小改变的影响
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void hideSoftKeyboard() {
        if (inputMethodManager == null)
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private InputMethodManager inputMethodManager;

    /*    @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            //注：回调 3
            Bugtags.onDispatchTouchEvent(this, event);
            return super.dispatchTouchEvent(event);
        }*/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);    // 透明导航栏
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        this.initView();
    }

    private LoadingDialog mLdDialog;

    public void showLodingDialog() {
        mLdDialog = ProgressDialogUtils.instance(this).getLoadingDialog();
        if (!mLdDialog.isShowing()) {
            mLdDialog.show();
        }
    }

    public void dissmisDialog() {
        if (mLdDialog != null)
            mLdDialog.dismiss();
    }

    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView();
}
