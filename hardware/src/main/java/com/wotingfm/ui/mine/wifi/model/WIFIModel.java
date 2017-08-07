package com.wotingfm.ui.mine.wifi.model;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.wotingfm.R;
import com.wotingfm.ui.mine.wifi.view.WIFIFragment;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class WIFIModel {
    private WifiManager wifiManager;
    private WIFIFragment activity;

    public WIFIModel(WIFIFragment activity) {
        this.activity = activity;
        wifiManager = (WifiManager) activity.getActivity().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 检查当前WIFI状态
     */
    public int checkState() {
        if (wifiManager.getWifiState() == 0) {
            Log.e("当前WIFI状态== ", "Wifi正在关闭");
            return 0;
        } else if (wifiManager.getWifiState() == 1) {
            Log.e("当前WIFI状态== ", "Wifi已经关闭");
            return 1;
        } else if (wifiManager.getWifiState() == 2) {
            Log.e("当前WIFI状态== ", "Wifi正在开启");
            return 2;
        } else if (wifiManager.getWifiState() == 3) {
            Log.e("当前WIFI状态== ", "Wifi已经开启");
            return 3;
        } else {
            Log.e("当前WIFI状态== ", "没有获取到WiFi状态");
            return -1;
        }
    }

    /**
     * 执行扫描
     */
    public boolean scanning() {
        boolean b = wifiManager.startScan();
        return b;
    }

    /**
     * @return
     */
    public WifiInfo getInfo() {
        WifiInfo mWifiInfo = wifiManager.getConnectionInfo();
        Log.e("已连接WIFI信息", mWifiInfo.toString());
        return mWifiInfo;
    }

    /**
     * 得到扫描的列表
     *
     * @return
     */
    public List<ScanResult> getScanResultList() {
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        if (scanResultList != null && scanResultList.size() > 0) {
            for (int i = 0; i < scanResultList.size(); i++) {
                Log.e("扫描到的数据》》》", "第" + i + "条" + scanResultList.get(i).toString());
            }
            return scanResultList;
        } else {
            return null;
        }
    }

//    //将搜索到的wifi根据信号强度从强到弱进行排序(测试有问题)
//    private void sortByLevel(List<ScanResult> resultList) {
//        for (int i = 0; i < resultList.size() - 1; i++)
//            for (int j = 1; j < resultList.size(); j++) {
//                if (resultList.get(i).level < resultList.get(j).level) {
//                    ScanResult temp = null;
//                    temp = resultList.get(i);
//                    resultList.set(i, resultList.get(j));
//                    resultList.set(j, temp);
//                }
//            }
//    }

    /**
     * 设置WIFI开关
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
     * 获取连接的id
     *
     * @param mWifiInfo
     * @return
     */
    public String getSSID(WifiInfo mWifiInfo) {
        String ssid = "";
        if (mWifiInfo != null) {
            ssid = mWifiInfo.getSSID();
            if (ssid != null && !ssid.trim().equals("")) {
                if (ssid.startsWith("\"")) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                }
                return ssid;
            } else {
                return ssid;
            }
        } else {
            return ssid;
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
     * 进行没有密码的热点的连接
     *
     * @param scan
     * @return
     */
    public boolean connectSpecificAP(ScanResult scan) {
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        boolean networkInSupplicant = false;
        boolean connectResult = false;
        // 重新连接指定AP
        wifiManager.disconnect();
        for (WifiConfiguration w : list) {
            // 将指定AP 名字转化
            // String str = convertToQuotedString(info.ssid);
            if (w.BSSID != null && w.BSSID.equals(scan.BSSID)) {
                connectResult = wifiManager.enableNetwork(w.networkId, true);
                // mWifiManager.saveConfiguration();
                networkInSupplicant = true;
                break;
            }
        }
        if (!networkInSupplicant) {
            WifiConfiguration config = CreateWifiInfo(scan, "");
            connectResult = addNetwork(config);
        }

        return connectResult;
    }

    /**
     * 然后是一个实际应用方法，只验证过没有密码的情况
     *
     * @param scan
     * @param Password
     * @return
     */

    public WifiConfiguration CreateWifiInfo(ScanResult scan, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.status = WifiConfiguration.Status.ENABLED;
        if (scan.capabilities.contains("WEP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.SSID = "\"" + scan.SSID + "\"";
            config.wepTxKeyIndex = 0;
            config.wepKeys[0] = Password;
            // config.preSharedKey = "\"" + SHARED_KEY + "\"";
        } else if (scan.capabilities.contains("PSK")) {
            //
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else if (scan.capabilities.contains("EAP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.SSID = "\"" + scan.SSID + "\"";
            // config.BSSID = info.mac;
            config.preSharedKey = null;
        }

        return config;
    }

    /**
     * 添加到网络
     *
     * @param wcg
     */
    private boolean addNetwork(WifiConfiguration wcg) {
        if (wcg == null) {
            return false;
        }
        int wcgID = wifiManager.addNetwork(wcg);
        boolean b = wifiManager.enableNetwork(wcgID, true);
        wifiManager.saveConfiguration();
        System.out.println(b);
        return b;
    }

    /**
     * 根据当前信号，获取当前信号强度
     *
     * @param level
     * @return
     */
    public int getImg(int level) {
        int imgId = R.mipmap.icon_wifi_strength_1_n;
        if (Math.abs(level) > 100) {
            imgId = R.mipmap.icon_wifi_strength_1_n;
        } else if (Math.abs(level) > 80) {
            imgId = R.mipmap.icon_wifi_strength_1_n;
        } else if (Math.abs(level) > 70) {
            imgId = R.mipmap.icon_wifi_strength_1_n;
        } else if (Math.abs(level) > 60) {
            imgId = R.mipmap.icon_wifi_strength_2_n;
        } else if (Math.abs(level) > 50) {
            imgId = R.mipmap.icon_wifi_strength_2_n;
        } else {
            imgId = R.mipmap.icon_wifi_strength_3_n;
        }
        return imgId;
    }

    /**
     * 判断是否是已经配置的连接
     *
     * @return
     */
    public int isLinked(String id) {
        int net_id = -1;
        if (id != null && !id.trim().equals("")) {
            List<WifiConfiguration> mWifiConfigurations = wifiManager.getConfiguredNetworks();
            if (mWifiConfigurations != null && mWifiConfigurations.size() > 0) {
                tosList(mWifiConfigurations);
                for (int i = 0; i < mWifiConfigurations.size(); i++) {
                    String _id = mWifiConfigurations.get(i).SSID;
                    if (_id != null && !_id.trim().equals("") && _id.equals(id)) {
                        net_id = mWifiConfigurations.get(i).networkId;
                        break;
                    }
                }
            }
        }
        return net_id;
    }

    // 打印已经配置的数据
    private void tosList(List<WifiConfiguration> List) {
        if (List != null && List.size() > 0) {
            for (int i = 0; i < List.size(); i++) {
                Log.e("已经配置的数据》》》", "第" + i + "条" + List.get(i).toString());
            }
        }
    }

    /**
     * 连接已经配置的连接
     *
     * @param Id
     */
    public boolean linkOK(int Id) {
        boolean b = wifiManager.enableNetwork(Id, true);
        return b;
    }

    /**
     * 获取加密类型
     *
     * @return
     */
    public String getEncryption(ScanResult scanResult) {
        String encryption = "";
        String descOri = scanResult.capabilities;
        if (descOri != null && !descOri.trim().equals("")) {
            if (descOri.toUpperCase().contains("WPA-PSK")) {
                encryption = encryption + "WPA-PSK/";
            }
            if (descOri.toUpperCase().contains("WPA2-PSK")) {
                encryption = encryption + "WPA2-PSK/";
            }
            if (descOri.toUpperCase().contains("WEP")) {
                encryption = encryption + "WEP/";
            }
            if (descOri.toUpperCase().contains("ESS")) {
                encryption = encryption + "ESS/";
            }
            if (encryption.length() > 0) {
                encryption = encryption.substring(0, encryption.length() - 1);
            }
        }

        return encryption;
    }

    /**
     * 关闭当前连接中的热点
     *
     * @param networkId
     * @return
     */
    public boolean remove(int networkId) {
        if (null == wifiManager) {
            Log.e("WIFIX", "WifiManager is null");
            return false;
        }
        boolean isRemoved = wifiManager.removeNetwork(networkId);
        if (!isRemoved) {
            int index = 0;
            while (!isRemoved && index < 10) {
                index++;
                isRemoved = wifiManager.removeNetwork(networkId);
            }
        }

        if (isRemoved) {
            wifiManager.saveConfiguration();
        }

        return isRemoved;
    }

}
