package com.wotingfm.ui.mine.bluetooth.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.mine.bluetooth.presenter.BlueToothPresenter;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.bluetooth.adapter.PairBluetoothAdapter;
import com.wotingfm.ui.mine.bluetooth.adapter.UserBluetoothAdapter;
import com.wotingfm.ui.mine.bluetooth.model.BluetoothInfo;
import java.util.List;

/**
 * 蓝牙设置
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class BluetoothFragment extends Fragment implements View.OnClickListener {
    private UserBluetoothAdapter userAdapter;
    private PairBluetoothAdapter pairAdapter;
    private ListView pairBluetoothList;                     // 已经配对过的
    private ListView userBluetoothList;                     // 可用蓝牙设备列表
    private TextView textPairDevice;                        // "已经配对过的设备"
    private ImageView imageBluetoothSet;                    // 蓝牙设置开关
    private FragmentActivity context;
    private View rootView;
    private BlueToothPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
            rootView.setOnClickListener(this);
            context = getActivity();
            initView();
            presenter = new BlueToothPresenter(this);
        }
        return rootView;
    }

    // 初始化视图
    private void initView() {
        TextView textTitle = (TextView) rootView.findViewById(R.id.tv_center);// 标题
        textTitle.setText(getString(R.string.bluetooth_settings));// 设置标题
        ImageView leftImage = (ImageView) rootView.findViewById(R.id.head_left_btn);// 返回
        leftImage.setOnClickListener(this);

        userBluetoothList = (ListView) rootView.findViewById(R.id.list_user_bluetooth);// 搜索到的可用可配对设备列表
        userBluetoothList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        View headView = LayoutInflater.from(context).inflate(R.layout.head_view_bluetooth, null);
        userBluetoothList.addHeaderView(headView);
        headView.findViewById(R.id.bluetooth_set).setOnClickListener(this);// 开启蓝牙按钮
        pairBluetoothList = (ListView) headView.findViewById(R.id.list_pair_bluetooth);// 已经配对过的列表
        textPairDevice = (TextView) headView.findViewById(R.id.text_pair_device);// "已经配对过的设备"
        imageBluetoothSet = (ImageView) rootView.findViewById(R.id.image_bluetooth_set);// 蓝牙显示图片
    }

    /**
     * 设置蓝牙打开界面
     */
    public void setViewForOpen() {
        textPairDevice.setVisibility(View.VISIBLE);
        imageBluetoothSet.setImageResource(R.mipmap.on_switch);
        userBluetoothList.setDividerHeight(1);
    }

    /**
     * 设置蓝牙关闭界面
     */
    public void setViewForClose() {
        textPairDevice.setVisibility(View.GONE);
        imageBluetoothSet.setImageResource(R.mipmap.close_switch);
        userBluetoothList.setDividerHeight(0);
    }

    public void setAdapterPair(List<BluetoothInfo> pairList ){
        if (pairAdapter == null) {
            pairAdapter = new PairBluetoothAdapter(context, pairList);
            pairBluetoothList.setAdapter(pairAdapter);// 配对过的列表
        } else {
            pairAdapter.changeData(pairList);
        }
        setItemForPair();
    }

    public void setAdapterUser(List<BluetoothInfo> userList){
        if (userAdapter == null) {
            userAdapter = new UserBluetoothAdapter(context, userList);
            userBluetoothList.setAdapter(userAdapter);// 可用的列表
        } else {
            userAdapter.changeData(userList);
        }
        setItemForUser();
    }

    // 子条目点击事件  发送配对请求
    private void setItemForPair() {
        // 没有配对的设备点进行配对
        userBluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0) {
                    presenter.bond(position - 1);
                }
            }
        });
    }

    // 子条目点击事件  发送配对请求
    private void setItemForUser() {
        // 已经配对过的设备点击则直接连接
        pairBluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.link(position);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_set:// 蓝牙开关
                presenter.BluetoothSet();
                break;
            case R.id.head_left_btn:// 返回
                MineActivity.close();
                break;
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

}
