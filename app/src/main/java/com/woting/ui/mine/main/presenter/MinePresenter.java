package com.woting.ui.mine.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.woting.common.application.BSApplication;
import com.woting.common.constant.BroadcastConstants;
import com.woting.common.constant.StringConstant;
import com.woting.common.utils.CommonUtils;
import com.woting.ui.mine.main.view.MineFragment;

/**
 * 个人中心界面处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class MinePresenter {

    private final MineFragment activity;
    private MessageReceiver Receiver;
    private WifiManager wifiManager;

    public MinePresenter(MineFragment activity) {
        this.activity = activity;
        wifiManager = (WifiManager) activity.getActivity().getSystemService(Context.WIFI_SERVICE);
        setReceiver();// 设置广播接收器
    }

    /**
     * 设置界面数据
     */
    public void getData() {
        // 设置登录界面
        if (CommonUtils.isLogin()) {
            String url = BSApplication.SharedPreferences.getString(StringConstant.PORTRAIT, "");// 头像
            String name = BSApplication.SharedPreferences.getString(StringConstant.NICK_NAME, "我听");// 昵称
            String num = BSApplication.SharedPreferences.getString(StringConstant.USER_NUM, "000");// id
            activity.setViewForLogin(url, name, num);
        }else{
            // 设置未登录界面
            activity.setView();
        }
    }

    // 设置广播接收器
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CANCEL);
            filter.addAction(BroadcastConstants.LOGIN);
            filter.addAction(BroadcastConstants.MINE_CHANGE);
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.CANCEL)) {
                activity.setView();// 设置未登录界面
            }else if(action.equals(BroadcastConstants.LOGIN)){
                getData();
            }else if(action.equals(BroadcastConstants.MINE_CHANGE)){
                getData();// 个人中心界面数据更改
            }
        }
    }

}
