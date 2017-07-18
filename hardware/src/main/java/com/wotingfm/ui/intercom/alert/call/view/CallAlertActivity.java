package com.wotingfm.ui.intercom.alert.call.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.alert.call.presenter.CallPresenter;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 呼叫弹出框
 * author：辛龙 (xinLong)
 * 2016/12/21 18:10
 * 邮箱：645700751@qq.com
 */
public class CallAlertActivity extends Activity implements OnClickListener {
    private ImageView img_bg, img_url, img_close;
    private TextView tv_name;
    private CallPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_call);
        inItView();
        presenter = new CallPresenter(this);
    }

    // 设置界面
    private void inItView() {
        img_bg = (ImageView) findViewById(R.id.img_bg);// 高斯模糊背景
        img_url = (ImageView) findViewById(R.id.img_url);// 头像
        tv_name = (TextView) findViewById(R.id.tv_name);// 名称
        img_close = (ImageView) findViewById(R.id.img_close);// 挂断
        img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                /**
                 * 此处需要挂断电话等操作
                 */
                finish();
                break;
        }
    }

    /**
     * 设置数据
     */
    public void setViewData(String url, String name) {
        // 其中radius的取值范围是1-25，radius越大，模糊度越高。
        // 设置高斯模糊背景
        if (url != null && !url.equals("")) {
            Glide.with(this).load(url).bitmapTransform(new BlurTransformation(this, 15)).into(img_bg);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(this, R.mipmap.p);
            img_bg.setImageBitmap(bmp);
        }
        // 设置好友头像
        if (url != null && !url.equals("")) {
            GlideUtils.loadImageViewSize(this, url, 60, 60, img_url, true);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(this, R.mipmap.test);
            img_url.setImageBitmap(bmp);
        }
        // 设置好友名称
        if (name != null && !name.equals("")) {
            tv_name.setText(name);
        } else {
            tv_name.setText("好友");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            /**
             * 此处需要挂断电话等操作
             */
            presenter.musicClose();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}