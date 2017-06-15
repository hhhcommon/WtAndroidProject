package com.wotingfm.ui.mine.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.R;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.L;
import com.wotingfm.ui.mine.MineActivity;
import com.wotingfm.ui.mine.adapter.WiFiListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * WIFI 界面
 */
public class WIFIFragment extends Fragment implements View.OnClickListener {
    private WiFiListAdapter adapter;
    private ScanResult wiFiName;
    private WifiInfo mWifiInfo;

    private ListView wifiListView;
    private ImageView imageWiFiSet;
    private TextView textUserWiFi;

    private List<WifiConfiguration> wifiConfigList;     // 已经配置好的 WiFi 信息
    private List<ScanResult> scanResultList;            // 扫描得到的附近的 WiFi 列表
    private WifiManager wifiManager;
    private FragmentActivity context;
    private View rootView;
    private WIFIFragment ct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
            rootView.setOnClickListener(this);
            context = getActivity();
            ct = this;
            init();
        }
        return rootView;
    }

    private void init() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);

        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.wlan));

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 注册广播 监听 WiFi 的状态
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConstants.UPDATE_WIFI_LIST);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(mBroadcastReceiver, filter);

        wifiListView = (ListView) rootView.findViewById(R.id.wifi_list_view);
        View headView = LayoutInflater.from(context).inflate(R.layout.head_view_wifi, null);
        wifiListView.addHeaderView(headView);
        wifiListView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        textUserWiFi = (TextView) headView.findViewById(R.id.user_wifi_list);   // 提示文字  可用 WiFi

        headView.findViewById(R.id.wifi_set).setOnClickListener(this);          // WiFi设置

        imageWiFiSet = (ImageView) headView.findViewById(R.id.image_wifi_set);
        if (wifiManager.isWifiEnabled()) {// WiFi 打开
            textUserWiFi.setVisibility(View.VISIBLE);
            imageWiFiSet.setImageResource(R.mipmap.on_switch);
            getConfiguration();
            mWifiInfo = wifiManager.getConnectionInfo();
            L.i("TAG", mWifiInfo.toString());
        } else {    // WiFi 关闭
            textUserWiFi.setVisibility(View.GONE);
            imageWiFiSet.setImageResource(R.mipmap.close_switch);
        }
        scanResultList = wifiManager.getScanResults();
        if (scanResultList != null && scanResultList.size() > 0) {// 判断附近是否有可用 WiFi
            wifiListView.setAdapter(adapter = new WiFiListAdapter(context, scanResultList));
        } else {
            wifiListView.setAdapter(adapter = new WiFiListAdapter(context, scanResultList = new ArrayList<>()));
        }
        setItemListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.wifi_set:         // WiFi 开关
                if (wifiManager.isWifiEnabled()) {// 如果是打开状态则关闭 WiFi
                    wifiManager.setWifiEnabled(false);
                } else {                // 否则打开 WiFi
                    wifiManager.setWifiEnabled(true);
                    scanResultList = wifiManager.getScanResults();
                }
                break;
        }
    }

    // ListView 子条目点击事件  连接 WiFi
    private void setItemListener() {
        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position - 1 >= 0) {
                    String ssid = mWifiInfo.getSSID();
                    L.e("ssid -- > > " + ssid);
                    if (ssid.startsWith("\"")) {
                        ssid = ssid.substring(1, ssid.length() - 1);
                    }
                    if (ssid.equals(scanResultList.get(position - 1).SSID)) {// 点击的是已经连接的网络
                        return;
                    }
                    final int wifiId = isConfiguration(scanResultList.get(position - 1).SSID);
                    L.e("wifiId -- > > " + wifiId);
                    if (wifiId != -1) {// 点击的是已经配置过的网络
                        connectWifi(wifiId);
                    } else {// 点击的是没有配置过的网络  需要进行配置然后连接
                        ConfigWiFiFragment fg = new ConfigWiFiFragment();
                        wiFiName = scanResultList.get(position - 1);
                        Bundle bundle = new Bundle();
                        bundle.putString(StringConstant.WIFI_NAME, wiFiName.SSID);
                        fg.setArguments(bundle);
                        fg.setTargetFragment(ct, 0);
                        context.getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_content, fg)
                                .addToBackStack(SequenceUUID.getUUID())
                                .commit();

                    }
                }
            }
        });
    }

    protected void setAddCardResult(String result) {
        // 处理接收结果的逻辑
        if (result != null && !result.trim().equals("")) {
            L.i("设置的WiFi密码 -- > " + result);
            int id = addWifiConfig(wiFiName, result);
            if (id != -1) connectWifi(id);
        }
    }

    // 得到配置好的Wifi信息
    private void getConfiguration() {
        if (wifiConfigList != null) wifiConfigList.clear();
        wifiConfigList = wifiManager.getConfiguredNetworks();
    }

    // 判定指定 WIFI 是否已经配置好,依据 WIFI 的地址 BSSID, 返回 NetId
    private int isConfiguration(String SSID) {
        getConfiguration();
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

    // 连接指定 id 的 WIFI
    private boolean connectWifi(int wifiId) {
        getConfiguration();
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == wifiId) {// status: == 0 已经连接，== 1 不可连接，== 2 可以连接
                if (wifiManager.enableNetwork(wifiId, true)) {// 激活 Id，建立连接
                    L.i("TAG", "已成功连接网络");
                    Intent pushWifi = new Intent(BroadcastConstants.UPDATE_WIFI_LIST);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("wifiName", wifiManager.getConnectionInfo().getSSID());
                    pushWifi.putExtras(bundle1);
                    context.sendBroadcast(pushWifi);
                }
                return true;
            }
        }
        return false;
    }

    // 广播接收器  用于更新 WiFi 列表
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastConstants.UPDATE_WIFI_LIST)) {// 更新 WiFi 列表
                L.i("扫描WiFi");
                getConfiguration();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWifiInfo = wifiManager.getConnectionInfo();// 已连接 WiFi 信息
                        adapter.setList(scanResultList = wifiManager.getScanResults());
                        L.i("scanResultList.size() --- > > " + scanResultList.size());
                        L.i("TAG", mWifiInfo.toString());
                    }
                }, 1200L);
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听 wifi 的打开与关闭，与连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                L.e("H3c", "wifiState : " + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:// WiFi 关闭
                        imageWiFiSet.setImageResource(R.mipmap.close_switch);
                        textUserWiFi.setVisibility(View.GONE);
                        scanResultList.clear();
                        adapter.notifyDataSetChanged();
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:// WiFi 打开
                        imageWiFiSet.setImageResource(R.mipmap.on_switch);
                        textUserWiFi.setVisibility(View.VISIBLE);
                        context.sendBroadcast(new Intent(BroadcastConstants.UPDATE_WIFI_LIST));
                        break;
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(mBroadcastReceiver);
    }
}