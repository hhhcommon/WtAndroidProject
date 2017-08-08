package com.wotingfm.ui.mine.bluetooth.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.bluetooth.model.BlueToothModel;
import com.wotingfm.ui.mine.bluetooth.view.BluetoothFragment;
import com.wotingfm.ui.mine.bluetooth.model.BluetoothInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class BlueToothPresenter {

    private BluetoothFragment activity;
    private BlueToothModel model;
    private DeviceReceiver Receiver;

    public BlueToothPresenter(BluetoothFragment activity) {
        this.activity = activity;
        this.model = new BlueToothModel();
        setView();
        setData();
        setReceiver();
    }

    /**
     * 获取此时的状态
     * STATE_OFF = 10;
     * STATE_TURNING_ON = 11;
     * STATE_ON = 12;
     * STATE_TURNING_OFF = 13;
     */
    private void setView() {
        int type = model.getState();
        switch (type) {
            case -1:
                activity.setViewForBluetoothClose();
                Log.e("当前蓝牙状态== ", "蓝牙已经关闭");
                getNoData();
                break;
            case 10:
                activity.setViewForBluetoothClose();
                Log.e("当前蓝牙状态== ", "蓝牙已经关闭");
                getNoData();
                break;
            case 11:
                activity.setViewForBluetoothOpenIng();
                Log.e("当前蓝牙状态== ", "蓝牙打开中。。。");
                break;
            case 12:
                activity.setViewForBluetoothOpen();
                getData();
                Log.e("当前蓝牙状态== ", "蓝牙已经打开");
                model.scan();
                Log.e("当前蓝牙状态== ", "蓝牙开始扫描");
                break;
            case 13:
                activity.setViewForBluetoothCloseIng();
                Log.e("当前蓝牙状态== ", "蓝牙关闭中。。。");
                break;
        }
    }

    // 设置默认数据
    private void setData() {
        if(model.isOpen()){
            activity.setViewFordDetectionClose(true);
        }else{
            activity.setViewFordDetectionClose(false);
        }

        String name = model.getName();
        if (name != null && !name.trim().equals("")) {
            activity.setPhoneName(name);
        } else {
            activity.setPhoneName("未知");
        }
    }

    // 获取数据
    private void getData() {
        List<BluetoothInfo> pairList = model.findAvailableDevice();// 已经配对的蓝牙列表
        List<BluetoothInfo> userList = model.getUserList();// 附近可以配对的蓝牙列表
        setView(userList, pairList);
    }

    private void getNoData(){
        List<BluetoothInfo> pairList = new ArrayList<>();// 已经配对的蓝牙列表
        List<BluetoothInfo> userList =  new ArrayList<>();// 附近可以配对的蓝牙列表
        setView(userList, pairList);
    }

    // 设置界面
    private void setView(List<BluetoothInfo> userList, List<BluetoothInfo> pairList) {
        activity.setAdapterPair(pairList);
        activity.setAdapterUser(userList);
    }

    /**
     * 蓝牙连接
     *
     * @param pairList
     * @param position
     */
    public void link(List<BluetoothInfo> pairList, int position) {
        String address = pairList.get(position).getBluetoothAddress();
        model.link(address);
    }

    /**
     * 配对
     *
     * @param position
     */
    public void bond(List<BluetoothInfo> userList, int position) {
        String address = userList.get(position).getBluetoothAddress();
        model.bond(address);
    }

    /**
     * 蓝牙设置
     */
    public void BluetoothSet() {
        model.BluetoothSet();
    }

    public void setDetection() {
        if(model.isOpen()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            activity.getActivity().startActivity(intent);
        }
    }

    // 注册蓝牙接收广播
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new DeviceReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            activity.getActivity().registerReceiver(Receiver, filter);
        }
    }

    // 蓝牙搜索状态广播监听
    private class DeviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED://蓝牙设备状态改变
                    setView();
                    break;
                case BluetoothDevice.ACTION_FOUND://发现设备
                    BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {// 搜索没有配过对的蓝牙设备
                        model.deviceAdd(btd);
                        Set<BluetoothDevice> device = model.getDevice();
                        if (device != null && device.size() > 0) { // 存在已经配对过的蓝牙设备
                            List<BluetoothInfo> userList = model.delUserList(device);
                            activity.setAdapterUser(userList);
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Set<BluetoothDevice> device = model.getDevice();
                    if (device != null && device.size() > 0) { // 存在已经配对过的蓝牙设备
                        List<BluetoothInfo> userList = model.delUserList(device);
                        activity.setAdapterUser(userList);
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED://设备连接状态改变
                    BluetoothDevice bDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (bDevice.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            L.d("TAG", "正在配对......");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Toast.makeText(context, "配对成功!", Toast.LENGTH_SHORT).show();
                            getData();
                            model.connect(bDevice);// 连接设备
                            break;
                        case BluetoothDevice.BOND_NONE:
                            L.d("TAG", "取消配对");
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model.destroy();
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
        model = null;
    }
}
