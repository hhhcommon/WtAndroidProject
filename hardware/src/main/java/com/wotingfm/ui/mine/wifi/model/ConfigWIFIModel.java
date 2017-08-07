package com.wotingfm.ui.mine.wifi.model;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.mine.wifi.view.ConfigWIFIFragment;
import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ConfigWIFIModel {
    private WifiManager mWifiManager;
    private ConfigWIFIFragment activity;
    private ScanResult scanResult;

    public ConfigWIFIModel(ConfigWIFIFragment activity) {
        this.activity = activity;
        getData();
    }

    private void getData() {
        mWifiManager = (WifiManager) activity.getActivity().getSystemService(Context.WIFI_SERVICE);
        scanResult = (ScanResult) activity.getArguments().getParcelable(StringConstant.WIFI_NAME);
    }

    /**
     * Function:信号强度转换为字符串
     */
    private String singLevToStr(int level) {
        String resString = "无信号";
        if (Math.abs(level) > 100) {
        } else if (Math.abs(level) > 80) {
            resString = "弱";
        } else if (Math.abs(level) > 70) {
            resString = "强";
        } else if (Math.abs(level) > 60) {
            resString = "强";
        } else if (Math.abs(level) > 50) {
            resString = "较强";
        } else {
            resString = "极强";
        }
        return resString;
    }

    /**
     * 获取信号名
     *
     * @return
     */
    public String getSSID() {
        String wiFiName = scanResult.SSID;
        if (wiFiName == null || wiFiName.trim().equals("")) wiFiName = "连接";
        return wiFiName;
    }

    /**
     * 获取信号强度
     *
     * @return
     */
    public String getSignal() {
        String signal = singLevToStr(scanResult.level);
        return signal;
    }

    /**
     * 获取加密类型
     *
     * @return
     */
    public String getEncryption() {
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
     * 添加指定WIFI的配置信息,原列表不存在此SSID
     * @param ssid
     * @param pwd
     * @return
     */
    public int AddWifiConfig(String ssid,String pwd){
        List<ScanResult> wifiList = mWifiManager.getScanResults();
        int wifiId = -1;
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                Log.i("AddWifiConfig","equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\""+wifi.SSID+"\"";//\"转义字符，代表"
                wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                if(wifiId != -1){
                    return wifiId;
                }
            }
        }
        return wifiId;
    }
    
    //得到Wifi配置好的信息
    public List<WifiConfiguration> getConfiguration(){
        List<WifiConfiguration> wifiConfigList = mWifiManager.getConfiguredNetworks();//得到配置好的网络信息
        for(int i =0;i<wifiConfigList.size();i++){
            Log.i("getConfiguration",wifiConfigList.get(i).SSID);
            Log.i("getConfiguration",String.valueOf(wifiConfigList.get(i).networkId));
        }
        return wifiConfigList;
    }

    //连接指定Id的WIFI
    public boolean ConnectWifi(List<WifiConfiguration> wifiConfigList, int wifiId){
        for(int i = 0; i < wifiConfigList.size(); i++){
            WifiConfiguration wifi = wifiConfigList.get(i);
            if(wifi.networkId == wifiId){
                while(!(mWifiManager.enableNetwork(wifiId, true))){//激活该Id，建立连接
                    Log.i("ConnectWifi",String.valueOf(wifiConfigList.get(wifiId).status));//status:0--已经连接，1--不可连接，2--可以连接
                }
                return true;
            }
        }
        return false;
    }
}
