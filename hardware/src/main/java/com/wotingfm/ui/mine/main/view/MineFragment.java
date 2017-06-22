package com.wotingfm.ui.mine.main.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.mine.fragment.BluetoothFragment;
import com.wotingfm.ui.mine.fragment.FMSetFragment;
import com.wotingfm.ui.mine.set.view.SettingFragment;
import com.wotingfm.ui.mine.fragment.WIFIFragment;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.main.presenter.MinePresenter;
import com.wotingfm.ui.user.logo.LogoActivity;

/**
 * 个人中心主界面
 * Created by Administrator on 2017/6/7.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    public static BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

    private View rootView;
    private FragmentActivity context;
    private MinePresenter presenter;
    private ImageView image_head;
    private TextView text_user_name, text_user_number;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            rootView.setOnClickListener(this);
            initView();
            initEvent();
            presenter = new MinePresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        image_head = (ImageView) rootView.findViewById(R.id.image_head);// 头像
        image_head.setOnClickListener(this);
        text_user_name = (TextView) rootView.findViewById(R.id.text_user_name);// 昵称
        text_user_number = (TextView) rootView.findViewById(R.id.text_user_number);// id号
    }

    // 初始化点击事件
    private void initEvent() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回按钮
        rootView.findViewById(R.id.fm_set).setOnClickListener(this);// FM 设置
        rootView.findViewById(R.id.setting).setOnClickListener(this);// 设置
        rootView.findViewById(R.id.bluetooth_set).setOnClickListener(this);// 蓝牙设置
        rootView.findViewById(R.id.wifi_set).setOnClickListener(this);// 无线局域网
        rootView.findViewById(R.id.flow_set).setOnClickListener(this);// 流量管理
        rootView.findViewById(R.id.image_info).setOnClickListener(this);// 消息中心
        rootView.findViewById(R.id.image_qr_code).setOnClickListener(this);// 二维码
    }

    /**
     * 设置界面数据(登录)
     *
     * @param url
     * @param name
     * @param num
     */
    public void setViewForLogin(String url, String name, String num) {
        if (url != null && !url.trim().equals("")) {
            GlideUtils.loadImageViewSize(this.getActivity(), url, 70, 70, image_head, true);
        } else {
            image_head.setImageResource(R.mipmap.icon_avatar_d);
        }
        text_user_name.setText(name);
        text_user_number.setText("我听号：" + num);
    }

    /**
     * 设置界面数据(未登录)
     */
    public void setView() {
        image_head.setImageResource(R.mipmap.icon_avatar_d);
        text_user_name.setText("点击登录");
        text_user_number.setText("登录后可享受更多服务");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                if (GlobalStateConfig.mineFromType == 1) {
                    GlobalStateConfig.mineFromType = 0;
                    GlobalStateConfig.activityA = "A";
                    MainActivity.changeOne();
                    Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("viewType", 1);
                    push.putExtras(bundle);
                    context.sendBroadcast(push);
                } else if (GlobalStateConfig.mineFromType == 2) {
                    GlobalStateConfig.mineFromType = 0;
                    GlobalStateConfig.activityB = "B";
                    MainActivity.changeTwo();
                    Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("viewType", 2);
                    push.putExtras(bundle);
                    context.sendBroadcast(push);
                }
                break;
            case R.id.fm_set:// FM 设置
                MineActivity.open(new FMSetFragment());
                break;
            case R.id.setting:// 设置
                MineActivity.open(new SettingFragment());
                break;
            case R.id.bluetooth_set:// 蓝牙设置
                MineActivity.open(new BluetoothFragment());
                break;
            case R.id.wifi_set:// 无线局域网
                MineActivity.open(new WIFIFragment());
                break;
            case R.id.flow_set:// 流量管理

                break;
            case R.id.image_info:// 消息中心

                break;
            case R.id.image_qr_code:// 二维码

                break;
            case R.id.image_head:// 登录
                startActivity(new Intent(this.getActivity(), LogoActivity.class));
                break;


        }
    }
}
