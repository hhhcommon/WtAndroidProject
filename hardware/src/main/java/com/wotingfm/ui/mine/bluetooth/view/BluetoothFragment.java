package com.wotingfm.ui.mine.bluetooth.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.woting.commonplat.widget.HeightListView;
import com.wotingfm.R;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.mine.bluetooth.adapter.PairBluetoothAdapter;
import com.wotingfm.ui.mine.bluetooth.adapter.UserBluetoothAdapter;
import com.wotingfm.ui.mine.bluetooth.model.BluetoothInfo;
import com.wotingfm.ui.mine.bluetooth.presenter.BlueToothPresenter;

import java.util.List;

/**
 * 蓝牙设置
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class BluetoothFragment extends BaseFragment implements View.OnClickListener {
    private UserBluetoothAdapter userAdapter;
    private PairBluetoothAdapter pairAdapter;
    private ListView pairBluetoothList;                     // 已经配对过的
    private ListView userBluetoothList;                     // 可用蓝牙设备列表
    private TextView textPairDevice, text_bluetooth_set, text_bluetooth_news, text_bluetooth_detection, text_bluetooth_detection_news, text_use_device, tv_phone_name;
    private ImageView imageBluetoothSet, image_bluetooth_detection_set;
    private View rootView;
    private BlueToothPresenter presenter;
    private ProgressBar progressBar_scan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
            rootView.setOnClickListener(this);
            initView();
            presenter = new BlueToothPresenter(this);
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        ImageView leftImage = (ImageView) rootView.findViewById(R.id.head_left_btn);// 返回
        leftImage.setOnClickListener(this);
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.bluetooth_settings));            // 设置标题

        View headView = LayoutInflater.from(this.getActivity()).inflate(R.layout.head_view_bluetooth, null);

        headView.findViewById(R.id.bluetooth_set).setOnClickListener(this);   // 开启蓝牙按钮
        text_bluetooth_set = (TextView) headView.findViewById(R.id.text_bluetooth_set);  // 提示内容：开启蓝牙
        text_bluetooth_news = (TextView) headView.findViewById(R.id.text_bluetooth_news);// 提示内容：蓝牙开启中
        imageBluetoothSet = (ImageView) headView.findViewById(R.id.image_bluetooth_set); // 蓝牙开启显示图片

        headView.findViewById(R.id.bluetooth_detection).setOnClickListener(this);        // 开放检测按钮
        text_bluetooth_detection = (TextView) headView.findViewById(R.id.text_bluetooth_detection);            // 提示内容：开放检测
        text_bluetooth_detection_news = (TextView) headView.findViewById(R.id.text_bluetooth_detection_news);  // 提示内容：已关闭，仅允许配对设备检测到
        image_bluetooth_detection_set = (ImageView) headView.findViewById(R.id.image_bluetooth_detection_set); // 开放检测显示图片

        headView.findViewById(R.id.lin_phone_name).setOnClickListener(this);  // 手机名称
        tv_phone_name = (TextView) headView.findViewById(R.id.tv_phone_name); // 提示内容：手机名称

        headView.findViewById(R.id.lin_base).setOnClickListener(this);        // 设置底座

        textPairDevice = (TextView) headView.findViewById(R.id.text_pair_device);      // "已经配对过的设备"
        pairBluetoothList = (ListView) headView.findViewById(R.id.list_pair_bluetooth);// 已经配对过的列表
        textPairDevice = (TextView) headView.findViewById(R.id.text_pair_device);      // "已经配对过的设备"

        text_use_device = (TextView) headView.findViewById(R.id.text_use_device);      // "可用的设备"
        progressBar_scan = (ProgressBar) headView.findViewById(R.id.progressBar_scan); // 扫描等待提示

        userBluetoothList = (ListView) rootView.findViewById(R.id.list_user_bluetooth);// 搜索到的可用可配对设备列表
        userBluetoothList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        userBluetoothList.addHeaderView(headView);
    }

    /**
     * 设置蓝牙打开界面
     */
    public void setViewForBluetoothOpen() {
        text_bluetooth_set.setText("开启蓝牙");
        text_bluetooth_set.setVisibility(View.VISIBLE);// 提示内容：开启蓝牙
        text_bluetooth_news.setVisibility(View.GONE);// 提示内容：蓝牙开启中
        imageBluetoothSet.setImageResource(R.mipmap.on_switch);// 蓝牙开启显示图片
    }

    /**
     * 设置蓝牙打开中界面
     */
    public void setViewForBluetoothOpenIng() {
        text_bluetooth_set.setText("开启蓝牙");
        text_bluetooth_set.setVisibility(View.VISIBLE);// 提示内容：开启蓝牙
        text_bluetooth_news.setText("蓝牙开启中...");
        text_bluetooth_news.setVisibility(View.VISIBLE);// 提示内容：蓝牙开启中
        imageBluetoothSet.setImageResource(R.mipmap.on_switch);// 蓝牙开启显示图片
    }

    /**
     * 设置蓝牙关闭中界面
     */
    public void setViewForBluetoothCloseIng() {
        text_bluetooth_set.setText("开启蓝牙");
        text_bluetooth_set.setVisibility(View.VISIBLE);// 提示内容：开启蓝牙
        text_bluetooth_news.setText("蓝牙关闭中...");
        text_bluetooth_news.setVisibility(View.VISIBLE);// 提示内容：蓝牙开启中
        imageBluetoothSet.setImageResource(R.mipmap.off_switch);// 蓝牙开启显示图片
    }

    /**
     * 设置蓝牙关闭界面
     */
    public void setViewForBluetoothClose() {
        text_bluetooth_set.setText("开启蓝牙");
        text_bluetooth_set.setVisibility(View.VISIBLE);// 提示内容：开启蓝牙
        text_bluetooth_news.setVisibility(View.GONE);// 提示内容：蓝牙开启中
        imageBluetoothSet.setImageResource(R.mipmap.off_switch);// 蓝牙开启显示图片
    }

    /**
     * 设置检测打开界面
     */
    public void setViewForDetectionOpen() {
        text_bluetooth_detection.setText("开放检测");// 提示内容：开放检测
        text_bluetooth_detection.setTextColor(Color.parseColor("#16181a"));
        text_bluetooth_detection_news.setText("已开启，允许周围设备检测到");// 提示内容：已开启，允许周围设备检测到
        text_bluetooth_detection_news.setTextColor(Color.parseColor("#959698"));
        image_bluetooth_detection_set.setImageResource(R.mipmap.on_switch);// 开放检测显示图片
    }

    /**
     * 设置检测关闭界面
     *
     * @param b 是否已经开启蓝牙
     */
    public void setViewForDetectionClose(boolean b) {
        if (b) {
            text_bluetooth_detection.setText("开放检测");// 提示内容：开放检测
            text_bluetooth_detection.setTextColor(Color.parseColor("#16181a"));
            text_bluetooth_detection_news.setText("已关闭，仅允许配对设备检测到");// 提示内容：已关闭，仅允许配对设备检测到
            text_bluetooth_detection_news.setTextColor(Color.parseColor("#959698"));
            image_bluetooth_detection_set.setImageResource(R.mipmap.off_switch);// 开放检测显示图片
        } else {
            text_bluetooth_detection.setText("开放检测");// 提示内容：开放检测
            text_bluetooth_detection.setTextColor(Color.parseColor("#959698"));
            text_bluetooth_detection_news.setText("已关闭，仅允许配对设备检测到");// 提示内容：已关闭，仅允许配对设备检测到
            text_bluetooth_detection_news.setTextColor(Color.parseColor("#959698"));
            image_bluetooth_detection_set.setImageResource(R.mipmap.off_switch);// 开放检测显示图片
        }
    }

    /**
     * 设置本机名称
     *
     * @param name
     */
    public void setPhoneName(String name) {
        tv_phone_name.setText(name);
    }

    /**
     * 扫描等待提示的展示
     *
     * @param b
     */
    public void setViewForScan(boolean b) {
        if (b) {
            progressBar_scan.setVisibility(View.VISIBLE);
        } else {
            progressBar_scan.setVisibility(View.GONE);
        }
    }

    /**
     * 适配配对过的数据
     *
     * @param pairList
     */
    public void setAdapterPair(List<BluetoothInfo> pairList) {
        if (pairList != null && pairList.size() > 0) {
            textPairDevice.setVisibility(View.VISIBLE);
        } else {
            textPairDevice.setVisibility(View.GONE);
        }
        if (pairAdapter == null) {
            pairAdapter = new PairBluetoothAdapter(this.getActivity(), pairList);
            pairBluetoothList.setAdapter(pairAdapter);// 配对过的列表
        } else {
            pairAdapter.changeData(pairList);
        }
        new HeightListView(this.getActivity()).setListViewHeightBasedOnChildren(pairBluetoothList);
        setItemForPair(pairList);
    }

    /**
     * 适配可以用的连接
     *
     * @param userList
     */
    public void setAdapterUser(List<BluetoothInfo> userList) {
        if (userList != null && userList.size() > 0) {
            text_use_device.setVisibility(View.VISIBLE);
        } else {
            text_use_device.setVisibility(View.GONE);
        }
        if (userAdapter == null) {
            userAdapter = new UserBluetoothAdapter(this.getActivity(), userList);
            userBluetoothList.setAdapter(userAdapter);// 可用的列表
        } else {
            userAdapter.changeData(userList);
        }
        setItemForUser(userList);
    }

    // 子条目点击事件  发送配对请求
    private void setItemForPair(final List<BluetoothInfo> pairList) {
        // 已经配对过的设备点击则直接连接
        pairAdapter.itemClickListener(new PairBluetoothAdapter.itemClickListener() {
            @Override
            public void click(int position) {
                presenter.link(pairList, position);
            }
        });
//        pairBluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                presenter.link(pairList, position);
//
//            }
//        });

    }

    // 子条目点击事件  发送配对请求
    private void setItemForUser(final List<BluetoothInfo> userList) {
        // 没有配对的设备点进行配对
        userBluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0) {
                    presenter.bond(userList, position - 1);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                closeFragment();
                break;
            case R.id.bluetooth_set:// 蓝牙开关
                presenter.BluetoothSet();
                break;
            case R.id.bluetooth_detection:// 开放检测开关
                presenter.setDetection();
                break;
            case R.id.lin_phone_name:// 修改手机昵称按钮
                presenter.upDataName();
                break;
            case R.id.lin_base:// 一键连接底座
                ToastUtils.show_always(this.getActivity(),"一键连接底座");
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }

}
