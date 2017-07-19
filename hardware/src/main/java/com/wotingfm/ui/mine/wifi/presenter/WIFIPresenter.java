package com.wotingfm.ui.mine.wifi.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.L;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.wifi.model.WIFIModel;
import com.wotingfm.ui.mine.wifi.view.ConfigWiFiFragment;
import com.wotingfm.ui.mine.wifi.view.WIFIFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class WIFIPresenter {

    private final WIFIFragment activity;
    private final WIFIModel model;
    private WifiManager wifiManager;

    private ScanResult wiFiName;

    public WIFIPresenter(WIFIFragment activity) {
        this.activity = activity;
        this.model = new WIFIModel();
        wifiManager = (WifiManager) activity.getActivity().getSystemService(Context.WIFI_SERVICE);
        setView();
        setReceiver();
    }

    // 设置界面
    private void setView() {
        if (wifiManager.isWifiEnabled()) {
            activity.setViewOpen();// WiFi 打开
            getData();// 设置数据
        } else {
            activity.setViewClose();// WiFi 关闭
        }
    }

    // 设置数据
    private void getData() {
        WifiInfo mWifiInfo = wifiManager.getConnectionInfo();
        L.i("TAG", mWifiInfo.toString());
        // 获取扫描列表
         List<ScanResult> scanResultList = model.getScanResultList(wifiManager);
        // 获取当前连接数据
        String id = model.getSSID(mWifiInfo);
        // 设置当前的连接信息
        if (id != null && !id.equals("")) {
            activity.setViewID(id);
        } else {
            activity.setViewID("00000000");
        }
        // 去掉自己的数据后进行适配
        if (id != null && !id.equals("") && scanResultList != null && scanResultList.size() > 0) {
            scanResultList = model.dealList(id, scanResultList);
        }
        if (scanResultList != null && scanResultList.size() > 0) {
            activity.setData(scanResultList);
        } else {
            List<ScanResult> list=new ArrayList<>();
            activity.setData(list);
        }
    }

    /**
     * wifi设置按钮
     */
    public void WIFISet() {
        if (wifiManager.isWifiEnabled()) {
            // 如果是打开状态则关闭 WiFi
            wifiManager.setWifiEnabled(false);
        } else {
            // 否则打开 WiFi
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 连接
     *
     * @param position
     */
    public void link(List<ScanResult> scanResultList, int position) {
        WifiInfo mWifiInfo = wifiManager.getConnectionInfo();
        String ID = model.getSSID(mWifiInfo);
        String _ID = scanResultList.get(position).SSID;
        if (ID.equals(_ID)) {// 点击的是已经连接的网络
            return;
        }
        final int wifiId = model.isConfiguration(_ID, wifiManager);
        L.e("wifiId -- > > " + wifiId);
        if (wifiId != -1) {
            // 点击的是已经配置过的网络
            int type = model.connectWifi(wifiId, wifiManager);
            if (type == 2) {
                L.i("TAG", "已成功连接网络");
                Intent pushWifi = new Intent(BroadcastConstants.UPDATE_WIFI_LIST);
                Bundle bundle1 = new Bundle();
                bundle1.putString("wifiName", wifiManager.getConnectionInfo().getSSID());
                pushWifi.putExtras(bundle1);
                activity.getActivity().sendBroadcast(pushWifi);
            }
        } else {
            // 点击的是没有配置过的网络  需要进行配置然后连接
            ConfigWiFiFragment fg = new ConfigWiFiFragment();
            wiFiName = scanResultList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString(StringConstant.WIFI_NAME, wiFiName.SSID);
            fg.setArguments(bundle);
            MineActivity.open(fg);
        }
    }

    // 注册广播 监听 WiFi 的状态
    private void setReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConstants.UPDATE_WIFI_LIST);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        activity.getActivity().registerReceiver(mBroadcastReceiver, filter);
    }

    /**
     * 处理接收结果的逻辑
     *
     * @param result
     */
    public void setAddCardResult(String result) {
        if (result != null && !result.trim().equals("")) {
            L.i("设置的WiFi密码 -- > " + result);
            int id = addWifiConfig(wiFiName, result);
            if (id != -1) model.connectWifi(id, wifiManager);
        }
    }

    // 添加指定 WIFI 的配置信息,原列表不存在此 SSID
    private int addWifiConfig(ScanResult wifi, String pwd) {
        WifiConfiguration wifiCong = new WifiConfiguration();
        wifiCong.SSID = "\"" + wifi.SSID + "\"";
        wifiCong.preSharedKey = "\"" + pwd + "\"";// WPA-PSK 密码
        wifiCong.hiddenSSID = false;
        wifiCong.status = WifiConfiguration.Status.ENABLED;
        // 将配置好的特定 WIFI 密码信息添加,添加完成后默认是不激活状态,成功返回 ID,否则为 -1
        return wifiManager.addNetwork(wifiCong);
    }

    // 广播接收器  用于更新 WiFi 列表
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastConstants.UPDATE_WIFI_LIST)) {// 更新 WiFi 列表
                Log.e("扫描WiFi", "扫描WiFi ");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1200L);
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听 wifi 的打开与关闭，与连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.e("WIFI状态", "wifiState : " + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:// WiFi 关闭
                        activity.setViewClose();
                        List<ScanResult> list=new ArrayList<>();
                        activity.setData(list);
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:// WiFi 打开
                        activity.setViewOpen();
                        context.sendBroadcast(new Intent(BroadcastConstants.UPDATE_WIFI_LIST));
                        break;
                }
            }
        }
    };


    /**
     * 取消广播
     */
    public void destroy() {
        if (mBroadcastReceiver != null) {
            activity.getActivity().unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }
}
