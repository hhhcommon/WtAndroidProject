package com.wotingfm.ui.mine.wifi.presenter;

import android.net.wifi.WifiConfiguration;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.wifi.model.ConfigWIFIModel;
import com.wotingfm.ui.mine.wifi.view.ConfigWIFIFragment;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ConfigWIFIPresenter {
    private ConfigWIFIModel model;
    private ConfigWIFIFragment activity;

    public ConfigWIFIPresenter(ConfigWIFIFragment activity) {
        this.activity = activity;
        this.model = new ConfigWIFIModel(activity);
        getData();
    }

    /**
     * 获取数据进行界面展示
     */
    private void getData() {
        String wiFiName = model.getSSID();
        String signal = model.getSignal();
        String encryption = model.getEncryption();
        activity.setView(wiFiName, signal, encryption);
    }

    /**
     * 进行连接
     *
     * @param password
     */
    public void link(String password) {
//        if (password != null && !password.trim().equals("")) {
//            int netId = model.AddWifiConfig(model.getSSID(), password);
//            if (netId != -1) {
//                List<WifiConfiguration> list = model.getConfiguration();//添加了配置信息，要重新得到配置信息
//                if (model.ConnectWifi(list, netId)) {
//                    activity.setResult(true);
//                    MineActivity.close();
//                }
//            } else {
//                ToastUtils.show_always(activity.getActivity(), "连接失败，请稍后再试！");
//            }
//        } else {
//            ToastUtils.show_always(activity.getActivity(), "密码不能为空");
//        }
        if (password != null && !password.trim().equals("")) {
//            WifiConfiguration config = model.CreateWifiInfo(model.getScan(), password);
            WifiConfiguration config = model.createWifiConfig(model.getSSID(), password, model.getCreateType());
            boolean connectResult = model.ConnectWifi(config);
            if (connectResult) {
                activity.setResult(true);
                MineActivity.close();
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "密码不能为空");
        }

    }

}
