package com.wotingfm.ui.mine.main.view;

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

import com.woting.commonplat.utils.JsonEncloseUtils;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.mine.bluetooth.view.BluetoothFragment;
import com.wotingfm.ui.mine.fm.view.FMSetFragment;
import com.wotingfm.ui.mine.message.notify.view.MsgNotifyFragment;
import com.wotingfm.ui.mine.personinfo.view.PersonalInfoFragment;
import com.wotingfm.ui.mine.qrcodes.EWMShowFragment;
import com.wotingfm.ui.mine.set.view.SettingFragment;
import com.wotingfm.ui.mine.wifi.view.WIFIFragment;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.main.presenter.MinePresenter;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * 个人中心主界面
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private FragmentActivity context;
    private MinePresenter presenter;
    private ImageView image_head, img_bg;
    private TextView text_user_name, text_user_number, text_wifi_name,tv_bg_clear;

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
        img_bg = (ImageView) rootView.findViewById(R.id.img_bg);//
        tv_bg_clear = (TextView) rootView.findViewById(R.id.tv_bg_clear);// 背景遮罩
        image_head = (ImageView) rootView.findViewById(R.id.image_head);// 头像
        image_head.setOnClickListener(this);
        text_user_name = (TextView) rootView.findViewById(R.id.text_user_name);// 昵称
        text_user_number = (TextView) rootView.findViewById(R.id.text_user_number);// id号
        text_wifi_name = (TextView) rootView.findViewById(R.id.text_wifi_name);// wifi是否连接
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
        if (url != null && !url.trim().equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewSrc(url, img_bg, true, 8);
        } else {
            GlideUtils.loadImageViewSrc(R.mipmap.p, img_bg, true, 8);
        }
        tv_bg_clear.setVisibility(View.VISIBLE);
        if (url != null && !url.trim().equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewRound(url, image_head, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, image_head, 70, 70);
        }
        text_user_name.setText(name);
        text_user_number.setText("我听号：" + num);
    }

    /**
     * 设置界面数据(未登录)
     */
    public void setView() {
        image_head.setImageResource(R.mipmap.icon_avatar_d);
        GlideUtils.loadImageViewSrc(R.mipmap.p, img_bg, false, 8);
        tv_bg_clear.setVisibility(View.GONE);
        text_user_name.setText("点击登录");
        text_user_number.setText("登录后可享受更多服务");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                if (GlobalStateConfig.mineFromType == 1) {
                    GlobalStateConfig.mineFromType = 0;
                    GlobalStateConfig.activityA = "A";
                    //  MainActivity.changeOne();
                    EventBus.getDefault().post(new MessageEvent("one"));
                    Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("viewType", 1);
                    push.putExtras(bundle);
                    context.sendBroadcast(push);
                } else if (GlobalStateConfig.mineFromType == 2) {
                    GlobalStateConfig.mineFromType = 0;
                    GlobalStateConfig.activityB = "B";
                    //  MainActivity.changeTwo();
                    EventBus.getDefault().post(new MessageEvent("two"));
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
                WIFIFragment f = new WIFIFragment();
                MineActivity.open(f);
                f.setResultListener(new WIFIFragment.ResultListener() {
                    @Override
                    public void resultListener(boolean type) {
                        if (type) {
                            presenter.wifiSet();
                        }
                    }
                });
                break;
            case R.id.flow_set:// 流量管理

                break;
            case R.id.image_info:// 消息中心
                if (CommonUtils.isLogin()) MineActivity.open(new MsgNotifyFragment());
                break;
            case R.id.image_qr_code:// 二维码
                if (CommonUtils.isLogin()) {
                    EWMShowFragment fragment = new EWMShowFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "person");// 路径来源
                    bundle.putString("image", BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, ""));// 头像
                    bundle.putString("news", BSApplication.SharedPreferences.getString(StringConstant.USER_SIGN, ""));// 简介
                    bundle.putString("name", BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, ""));// 姓名

                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "person");
                    map.put("id", BSApplication.SharedPreferences.getString(StringConstant.USER_ID, ""));
                    String url = JsonEncloseUtils.jsonEnclose(map).toString();

                    bundle.putString("uri", url);// 内容路径
                    fragment.setArguments(bundle);
                    MineActivity.open(fragment);
                }
                break;
            case R.id.image_head:// 登录
                if (!CommonUtils.isLogin()) {
                    startActivity(new Intent(this.getActivity(), LogoActivity.class));
                } else {
                    MineActivity.open(new PersonalInfoFragment());
                }
                break;
        }
    }

    /**
     * 设置WiFi是否连接
     * @param b
     */
    public void wifiSet(boolean b) {
        if (b) {
            text_wifi_name.setVisibility(View.VISIBLE);
        } else {
            text_wifi_name.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
