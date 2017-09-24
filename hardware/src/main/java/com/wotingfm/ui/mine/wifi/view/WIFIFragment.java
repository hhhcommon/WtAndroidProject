package com.wotingfm.ui.mine.wifi.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.intercom.main.contacts.adapter.NoAdapter;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.wifi.adapter.WiFiListAdapter;
import com.wotingfm.ui.mine.wifi.presenter.WIFIPresenter;

import java.util.List;

/**
 * WIFI 界面
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class WIFIFragment extends BaseFragment implements View.OnClickListener {
    private WiFiListAdapter adapter;

    private ListView wifiListView;
    private ImageView imageWiFiSet, image_wifi, image_wifi_conn;
    private TextView text_wifi_set, text_wifi_news, tv_news;
    private View rootView;
    private View viewConnSuccess;
    private TextView textWifiName;
    private WIFIPresenter presenter;
    private ResultListener Listener;
    private ProgressBar progressBar, progressBar_scan;
    private LinearLayout user_wifi_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
            rootView.setOnClickListener(this);
            init();
            presenter = new WIFIPresenter(this);
        }
        return rootView;
    }

    private void init() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.wlan));
        wifiListView = (ListView) rootView.findViewById(R.id.wifi_list_view);

        View headView = LayoutInflater.from(this.getActivity()).inflate(R.layout.head_view_wifi, null);
        wifiListView.addHeaderView(headView);
        wifiListView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        headView.findViewById(R.id.wifi_set).setOnClickListener(this);           // WiFi设置
        imageWiFiSet = (ImageView) headView.findViewById(R.id.image_wifi_set);
        text_wifi_set = (TextView) headView.findViewById(R.id.text_wifi_set);    // 提示文字
        text_wifi_news = (TextView) headView.findViewById(R.id.text_wifi_news);  // 提示文字

        viewConnSuccess = headView.findViewById(R.id.view_conn_success);         // 显示连接的网络
        image_wifi = (ImageView) headView.findViewById(R.id.image_wifi);         // 已经连接的热点的信号强度
        textWifiName = (TextView) headView.findViewById(R.id.text_wifi_name);    // 连接的网络SSID
        tv_news = (TextView) headView.findViewById(R.id.tv_news);                // 提示文字：连接中，连接成功
        image_wifi_conn = (ImageView) headView.findViewById(R.id.image_wifi_conn);
        progressBar = (ProgressBar) headView.findViewById(R.id.progressBar);     // 连接等待提示

        user_wifi_list = (LinearLayout) headView.findViewById(R.id.user_wifi_list);       // 提示文字：选取网络
        progressBar_scan = (ProgressBar) headView.findViewById(R.id.progressBar_scan);    // 扫描等待提示

//        View footView = LayoutInflater.from(this.getActivity()).inflate(R.layout.foot_view_wifi, null);
//        other = (LinearLayout) footView.findViewById(R.id.lin_other);       // 其它隐藏网络
//        other.setOnClickListener(this);
//        tv_line= (TextView) footView.findViewById(R.id.tv_line);            // 背景线
//        wifiListView.addFooterView(footView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                closeFragment();
                break;
            case R.id.wifi_set:     // WiFi 开关
                presenter.WIFISet();
                break;
//            case R.id.lin_other:     // 其它隐藏网络
//                ToastUtils.show_always(this.getActivity(),"其它隐藏网络");
//                break;

        }
    }

    /**
     * WiFi 打开
     */
    public void setViewOpen() {
        text_wifi_set.setText("开启WLAN");
        text_wifi_news.setVisibility(View.GONE);
        imageWiFiSet.setImageResource(R.mipmap.on_switch);
        viewConnSuccess.setVisibility(View.GONE);
        user_wifi_list.setVisibility(View.VISIBLE);
    }

    /**
     * WiFi 打开中
     */
    public void setViewOpenIng() {
        text_wifi_set.setText("开启WLAN");
        text_wifi_news.setText("正在打开 WLAN...");
        text_wifi_news.setVisibility(View.VISIBLE);
        imageWiFiSet.setImageResource(R.mipmap.on_switch);
        viewConnSuccess.setVisibility(View.GONE);
        user_wifi_list.setVisibility(View.VISIBLE);
    }

    /**
     * WiFi 关闭
     */
    public void setViewClose() {
        text_wifi_set.setText("开启WLAN");
        text_wifi_news.setText("要查看可用网络,请打开WLAN.");
        text_wifi_news.setVisibility(View.VISIBLE);
        imageWiFiSet.setImageResource(R.mipmap.off_switch);
        viewConnSuccess.setVisibility(View.GONE);
        user_wifi_list.setVisibility(View.GONE);
    }

    /**
     * WiFi 关闭中
     */
    public void setViewCloseIng() {
        text_wifi_set.setText("开启WLAN");
        text_wifi_news.setText("正在关闭 WLAN...");
        text_wifi_news.setVisibility(View.VISIBLE);
        imageWiFiSet.setImageResource(R.mipmap.off_switch);
        viewConnSuccess.setVisibility(View.GONE);
        user_wifi_list.setVisibility(View.GONE);
    }

    /**
     * 设置连接的id
     *
     * @param ssid
     */
    public void setViewID(String ssid, int id) {
        viewConnSuccess.setVisibility(View.VISIBLE);
        tv_news.setText("连接成功");
        image_wifi_conn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        image_wifi.setImageResource(id);
        textWifiName.setText(ssid);
    }

    /**
     * 设置连接中状态界面
     */
    public void setLinking(String ssid, int id) {
        viewConnSuccess.setVisibility(View.VISIBLE);
        tv_news.setText("连接中...");
        image_wifi_conn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        image_wifi.setImageResource(id);
        if (ssid != null && !ssid.trim().equals("")) {
            textWifiName.setText(ssid);
        }
    }

    /**
     * 设置连接中状态界面
     */
    public void setLinkClose(int id) {
        viewConnSuccess.setVisibility(View.VISIBLE);
        tv_news.setText("已断开...");
        image_wifi_conn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        image_wifi.setImageResource(id);
        textWifiName.setText("未连接");

    }

    /**
     * 设置扫描等待提示是否展示
     *
     * @param b
     */
    public void setScanView(boolean b) {
        if (b) {
            progressBar_scan.setVisibility(View.VISIBLE);
        } else {
            progressBar_scan.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置list数据
     *
     * @param scanResultList
     */
    public void setData(List<ScanResult> scanResultList) {
//        if(scanResultList!=null&&scanResultList.size()>0){
//            other.setVisibility(View.VISIBLE);
//            tv_line.setVisibility(View.VISIBLE);
//        }else{
//            other.setVisibility(View.GONE);
//            tv_line.setVisibility(View.GONE);
//        }
        if (adapter == null) {
            adapter = new WiFiListAdapter(this.getActivity(), scanResultList);
            wifiListView.setAdapter(adapter);
        } else {
            adapter.setList(scanResultList);
        }
        setItemListener(scanResultList);
    }


    // ListView 子条目点击事件  连接 WiFi
    private void setItemListener(final List<ScanResult> scanResultList) {
        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position - 1 >= 0) {
                    presenter.link(scanResultList, position - 1);
                }
            }
        });
    }

    /**
     * 返回值设置
     *
     * @param type
     */
    public void setResult(boolean type) {
        Listener.resultListener(type);
    }

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setResult(true);
        presenter.destroy();
        presenter = null;
    }
}