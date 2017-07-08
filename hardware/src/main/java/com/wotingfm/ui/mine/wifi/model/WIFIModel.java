package com.wotingfm.ui.mine.wifi.model;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.wotingfm.common.utils.L;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class WIFIModel {

    /**
     * 得到配置好的Wifi信息
     *
     * @param wifiManager
     * @return
     */
    public List<WifiConfiguration> getConfiguration(WifiManager wifiManager) {
        List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
        return wifiConfigList;     // 已经配置好的 WiFi 信息
    }

    public List<ScanResult> getScanResultList(WifiManager wifiManager) {
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        return scanResultList;
    }

    /**
     * 获取连接的id
     *
     * @param mWifiInfo
     * @return
     */
    public String getSSID(WifiInfo mWifiInfo) {
        if (mWifiInfo != null) {
            String ssid = mWifiInfo.getSSID();
            L.e("ssid -- > > " + ssid);
            if (ssid.startsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            return ssid;
        } else {
            return null;
        }
    }

    /**
     * 去掉已经连接的数据
     *
     * @param ssid
     * @param scanResultList
     * @return
     */
    public List<ScanResult> dealList(String ssid, List<ScanResult> scanResultList) {
        if (ssid != null && scanResultList != null && scanResultList.size() > 0) {
            for (int i = 0, size = scanResultList.size(); i < size; i++) {
                if (ssid.equals(scanResultList.get(i).SSID)) {// 判断当前连接的网络
                    scanResultList.remove(i);
                    break;
                }
            }
        }
        return scanResultList;
    }

    /**
     * 判定指定 WIFI 是否已经配置好,依据 WIFI 的地址 BSSID, 返回 NetId
     *
     * @param SSID
     * @param wifiManager
     * @return
     */
    public int isConfiguration(String SSID, WifiManager wifiManager) {
        List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
        if (wifiConfigList == null || wifiConfigList.size() == 0) return -1;
        L.i("IsConfiguration", String.valueOf(wifiConfigList.size()));
        for (int i = 0; i < wifiConfigList.size(); i++) {
            String ssid = wifiConfigList.get(i).SSID;
            L.i(ssid, String.valueOf(wifiConfigList.get(i).networkId));
            L.e("ssid -- > > " + wifiConfigList.get(i).SSID);
            if (ssid.startsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            if (ssid.equals(SSID)) {// 地址相同
                return wifiConfigList.get(i).networkId;
            }
        }
        return -1;
    }

    /**
     * 连接指定 id 的 WIFI
     *
     * @param wifiId
     * @param wifiManager
     * @return
     */
    public int connectWifi(int wifiId, WifiManager wifiManager) {
        List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == wifiId) {// status: == 0 已经连接，== 1 不可连接，== 2 可以连接
                if (wifiManager.enableNetwork(wifiId, true)) {// 激活 Id，建立连接
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }
}
