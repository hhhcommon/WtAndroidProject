package com.wotingfm.ui.intercom.alert.receive.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.manager.InterPhoneControl;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.base.baseactivity.BaseFragmentActivity;
import com.wotingfm.ui.intercom.alert.receive.presenter.ReceivePresenter;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

/**
 * 来电话弹出框
 *
 * @author 辛龙
 *         2016年3月7日
 */
public class ReceiveAlertActivity extends BaseFragmentActivity implements OnClickListener {
    private ImageView img_bg, img_url, img_close, img_ok;
    private TextView tv_name;
    private ReceivePresenter presenter;

    public static void start(Context context, String roomId, String userId) {
        Intent intent = new Intent(context, ReceiveAlertActivity.class);
        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("accId", roomId);
        intent.putExtra("id", userId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏
        setContentView(R.layout.activity_receive);
        inItView();
        presenter = new ReceivePresenter(this);
    }

    // 设置界面
    private void inItView() {
        img_bg = (ImageView) findViewById(R.id.img_bg);       // 高斯模糊背景
        img_url = (ImageView) findViewById(R.id.img_url);     // 头像
        tv_name = (TextView) findViewById(R.id.tv_name);      // 名称
        img_close = (ImageView) findViewById(R.id.img_close); // 挂断
        img_close.setOnClickListener(this);
        img_ok = (ImageView) findViewById(R.id.img_ok);       // 接听
        img_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                /**
                 * 此处需要挂断电话等操作
                 */
                InterPhoneControl.refuse( new InterPhoneControl.Listener() {
                    @Override
                    public void type(boolean b) {
                        if(b){
                            presenter.setCallType(0);
                            finish();
                        }
                    }
                });

                break;
            case R.id.img_ok:
                /**
                 * 此处需要接收电话等操作
                 */
                if (ChatPresenter.data != null) {
                    String type = ChatPresenter.data.getTyPe().trim();
                    if (type != null && !type.equals("") && type.equals("person")) {
                        sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));// 好友界面关闭
                    } else {
                        sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE)); // 群组界面关闭
                    }
                }
                InterPhoneControl.accept( new InterPhoneControl.Listener() {
                    @Override
                    public void type(boolean b) {
                        if(b){
                            presenter.setCallType(1);
                            finish();
                        }
                    }
                });

                break;
        }
    }

    /**
     * 设置数据
     */
    public void setViewData(String url, String name) {
        // 其中radius的取值范围是1-25，radius越大，模糊度越高。
        // 不设置高斯模糊背景
        if (url != null && !url.equals("")) {
            GlideUtils.loadImageViewSrc(url, img_bg, false, 20);
        } else {
            GlideUtils.loadImageViewSrc(R.mipmap.p, img_bg, false, 20);
        }
        // 设置好友头像
        if (url != null && !url.equals("")) {
            GlideUtils.loadImageViewRound(url, img_url, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, img_url, 60, 60);
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
            InterPhoneControl.refuse( new InterPhoneControl.Listener() {
                @Override
                public void type(boolean b) {
                    if(b){
                        presenter.setCallType(0);
                        finish();
                    }
                }
            });

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
