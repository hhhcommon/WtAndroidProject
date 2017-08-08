package com.wotingfm.ui.mine.wifi.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.wifi.model.WIFIModel;
import com.wotingfm.ui.mine.wifi.view.ConfigWIFIFragment;
import com.wotingfm.ui.mine.wifi.view.WIFIFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class WIFIPresenter {

    private WIFIFragment activity;
    private WIFIModel model;
    protected boolean isUpdate = true;
    private Handler mHandler = new MyHandler();
    private static final int REFRESH_CONN = 100;

    public WIFIPresenter(WIFIFragment activity) {
        this.activity = activity;
        this.model = new WIFIModel(activity);
        setView();
        setReceiver();            // 硬件广播接收
        refreshWifiStatusOnTime();// 定时扫描线程
    }

    // 设置界面
    private void setView() {
        int t = model.checkState();// 检查当前WIFI状态
        if (t == 0) {
            Log.e("当前WIFI状态== ", "Wifi正在关闭");
            activity.setViewCloseIng();
        } else if (t == 1) {
            Log.e("当前WIFI状态== ", "Wifi已经关闭");
            activity.setViewClose();
        } else if (t == 2) {
            Log.e("当前WIFI状态== ", "Wifi正在开启");
            activity.setViewOpenIng();
        } else if (t == 3) {
            Log.e("当前WIFI状态== ", "Wifi已经开启");
            activity.setViewOpen();
            List<ScanResult> list = new ArrayList<>();
            activity.setData(list);
            // 开始扫描
            model.scanning();
            activity.setScanView(true);
        } else if (t == -1) {
            Log.e("当前WIFI状态== ", "没有获取到WiFi状态");
            activity.setViewClose();
        }
    }

    // 设置数据
    private void getData() {
        WifiInfo mWifiInfo = model.getInfo();// 得到已经连接的信息
        List<ScanResult> scanResultList = model.getScanResultList();// 获取扫描列表
        String id = model.getSSID(mWifiInfo); // 获取当前连接数据
        Log.e("已经连接的网络名称", id + "");
        // 设置当前的连接信息
        if (id != null && !id.equals("") && !id.equals("<unknown ssid>")) {
            activity.setViewID(id, model.getImg(mWifiInfo.getRssi()));
        } else {
            mHandler.sendEmptyMessage(REFRESH_CONN);
        }
        // 去掉自己的数据后进行适配
        if (scanResultList != null && scanResultList.size() > 0) {
            scanResultList = model.dealList(id, scanResultList);
        }
        if (scanResultList != null && scanResultList.size() > 0) {
            activity.setData(scanResultList);
        } else {
            List<ScanResult> list = new ArrayList<>();
            activity.setData(list);
        }
    }

    /**
     * wifi设置按钮
     */
    public void WIFISet() {
        model.WIFISet();
    }

    /**
     * 连接
     *
     * @param position
     */
    public void link(List<ScanResult> scanResultList, int position) {
        // 关闭当前连接中的
        WifiInfo mWifiInfo = model.getInfo();// 得到已经连接的信息
        String _id = model.getSSID(mWifiInfo); // 获取当前连接数据
        int id=model.isLinked(_id);
        if(id!=-1) model.remove(id);
        ScanResult scanResult = scanResultList.get(position);
        // 判断是否需要密码
        String desc = model.getEncryption(scanResult);
        if (desc.equals("")) {
            isConnectSelf(scanResult);
        } else {
            isConnect(scanResult);
        }
    }

    /**
     * 有密码验证连接
     *
     * @param scanResult
     */
    private void isConnect(final ScanResult scanResult) {
        if (model.isLinked(scanResult.SSID) != -1) {
            // 连接已经配置好的网络
            activity.setLinking(scanResult.SSID, model.getImg(scanResult.level));
            model.linkOK(model.isLinked(scanResult.SSID));
        } else {
            // 未连接显示连接输入对话框
            ConfigWIFIFragment fg = new ConfigWIFIFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(StringConstant.WIFI_NAME, scanResult);
            fg.setArguments(bundle);
            MineActivity.open(fg);
            fg.setResultListener(new ConfigWIFIFragment.ResultListener() {
                @Override
                public void resultListener(boolean type) {
                    if (type) {
                        activity.setLinking(scanResult.SSID, model.getImg(scanResult.level));
//                        setAddCardResult(true);
                    }
                }
            });
        }
    }

    /**
     * 无密码直连
     *
     * @param scanResult
     */
    private void isConnectSelf(ScanResult scanResult) {
        activity.setLinking(scanResult.SSID, model.getImg(scanResult.level));
        boolean isWifi = model.connectSpecificAP(scanResult);
    }

    /**
     * 处理接收结果的逻辑,暂不使用
     */
    private void setAddCardResult(boolean b) {
        if (b) {
            mHandler.sendEmptyMessage(REFRESH_CONN);
        }
    }

    // 注册广播 监听 WiFi 的状态
    private void setReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        activity.getActivity().registerReceiver(mBroadcastReceiver, filter);
    }

    // 广播接收器  用于更新 WiFi 列表
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:// wifi连接上与否
                    Log.e("", "网络已经改变");
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        Log.e("WIFI", "wifi已经断开");
//                        activity.setLinkClose(model.getImg(100));
                    } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                        Log.e("WIFI", "正在连接...");
                        activity.setLinking("",model.getImg(100));
                    } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        Log.e("WIFI", "连接成功...");
                       getData();
                        activity.setScanView(false);
                    }
                    break;
                case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                    int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                    if (error == WifiManager.ERROR_AUTHENTICATING) {
                        Log.e("WIFI", "密码认证错误Code为：" + error);
                        ToastUtils.show_always(activity.getActivity(), "wifi密码认证错误！");
                    }
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    Log.e("状态", "wifiState" + wifiState);
                    if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                        Log.e("WIFI", "wifi正在关闭中。");
                        activity.setViewCloseIng();
                        List<ScanResult> list = new ArrayList<>();
                        activity.setData(list);
                    } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                        Log.e("WIFI", "Wi-Fi已经关闭。");
                        activity.setViewClose();
                        List<ScanResult> list = new ArrayList<>();
                        activity.setData(list);
                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
                        Log.e("WIFI", "Wi-Fi正在打开中。");
                        activity.setViewOpenIng();
                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                        Log.e("WIFI", "Wi-Fi已经打开。");
                        activity.setViewOpen();
                        // 开始扫描
                        model.scanning();
                        activity.setScanView(true);
                    } else if (wifiState == WifiManager.WIFI_STATE_UNKNOWN) {
                        Log.e("WIFI", "Wi-Fi未知状态。");
                        activity.setViewClose();
                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:// 扫描结果返回数据
                    boolean isScanned = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, true);
                    if(isScanned){
                        getData();
                        activity.setScanView(false);
                    }
                    break;
            }
        }
    };

    /**
     * 定时刷新Wifi列表信息
     */
    private void refreshWifiStatusOnTime() {
        new Thread() {
            public void run() {
                while (isUpdate) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(REFRESH_CONN);
                }
            }
        }.start();
    }

    // 进行扫描后数据更改
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_CONN:
                    try {
                        int t = model.checkState();
                        if (t == 3) {
                            // 开始扫描
                            model.scanning();
                            activity.setScanView(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 取消广播
     */
    public void destroy() {
        isUpdate = false;
        if (mBroadcastReceiver != null) {
            activity.getActivity().unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        model = null;
    }
}
