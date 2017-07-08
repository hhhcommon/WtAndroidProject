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
import android.widget.ListView;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.wifi.adapter.WiFiListAdapter;
import com.wotingfm.ui.mine.wifi.presenter.WIFIPresenter;
import java.util.List;

/**
 *  WIFI 界面
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class WIFIFragment extends Fragment implements View.OnClickListener {
    private WiFiListAdapter adapter;

    private ListView wifiListView;
    private ImageView imageWiFiSet;
    private TextView textUserWiFi;
    private View rootView;
    private View viewConnSuccess;
    private TextView textWifiName;
    private WIFIPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wifi, container, false);
            rootView.setOnClickListener(this);
            init();
            presenter=new WIFIPresenter(this);
            presenter.setView();
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

        viewConnSuccess = headView.findViewById(R.id.view_conn_success);// 显示连接的网络
        textWifiName = (TextView) headView.findViewById(R.id.text_wifi_name);// 连接的网络SSID
        textUserWiFi = (TextView) headView.findViewById(R.id.user_wifi_list);   // 提示文字  可用 WiFi
        headView.findViewById(R.id.wifi_set).setOnClickListener(this);          // WiFi设置
        imageWiFiSet = (ImageView) headView.findViewById(R.id.image_wifi_set);
    }

    /**
     * WiFi 打开
     */
    public void setViewOpen(){
        textUserWiFi.setVisibility(View.VISIBLE);
        imageWiFiSet.setImageResource(R.mipmap.on_switch);
    }

    /**
     *  WiFi 关闭
     */
    public void setViewClose(){
        textUserWiFi.setVisibility(View.GONE);
        imageWiFiSet.setImageResource(R.mipmap.close_switch);
        viewConnSuccess.setVisibility(View.GONE);
    }

    /**
     * 设置连接的id
     * @param ssid
     */
    public void setViewID(String ssid){
        viewConnSuccess.setVisibility(View.VISIBLE);
        textWifiName.setText(ssid);
    }

    /**
     * 设置list数据
     * @param scanResultList
     */
    public void setData(List<ScanResult> scanResultList){
        if(adapter==null){
            adapter = new WiFiListAdapter(this.getActivity(), scanResultList);
            wifiListView.setAdapter(adapter);
        }else{
            adapter .notifyDataSetChanged();
        }
        setItemListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
            case R.id.wifi_set:     // WiFi 开关
              presenter.WIFISet();
                break;
        }
    }

    // ListView 子条目点击事件  连接 WiFi
    private void setItemListener() {
        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position - 1 >= 0) {
                   presenter.link(position-1);
                }
            }
        });
    }

    // 处理接收结果的逻辑
    protected void setAddCardResult(String result) {
        presenter.setAddCardResult(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}