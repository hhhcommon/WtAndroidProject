package com.wotingfm.ui.mine.bluetooth.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.wotingfm.ui.mine.bluetooth.model.BlueToothModel;
import com.wotingfm.ui.mine.bluetooth.model.BluetoothInfo;
import com.wotingfm.ui.mine.bluetooth.view.BluetoothFragment;
import com.wotingfm.ui.mine.bluetooth.view.EditBlueToothNameFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class BlueToothPresenter {

    private BluetoothFragment activity;
    private BlueToothModel model;
    private DeviceReceiver Receiver;
    private boolean disCover = false;// 蓝牙是否可被发现
    private String address;

    public BlueToothPresenter(BluetoothFragment activity) {
        this.activity = activity;
        this.model = new BlueToothModel();
        getNoData();// 列表适配空数据
        setView();
        setData();
        setReceiver();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

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
                setDiscoverView(false);// 修改检测界面
                break;
            case 10:
                activity.setViewForBluetoothClose();
                Log.e("当前蓝牙状态== ", "蓝牙已经关闭");
                getNoData();// 列表适配空数据
                setDiscoverView(false);// 修改检测界面
                break;
            case 11:
                activity.setViewForBluetoothOpenIng();
                Log.e("当前蓝牙状态== ", "蓝牙打开中。。。");
                break;
            case 12:
                activity.setViewForBluetoothOpen();
                getData();
                Log.e("当前蓝牙状态== ", "蓝牙已经打开");
                setDiscoverView(false);// 修改检测界面
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
        setScanMode();
        setName();
    }

    // 设置昵称
    private void setName() {
        String name = model.getName();
        if (name != null && !name.trim().equals("")) {
            activity.setPhoneName(name);
        } else {
            activity.setPhoneName("未知");
        }
    }

    // 设置扫描模式
    private void setScanMode() {
        // 获取扫描模式
        if (model.getScanMode() == 23) {
            setDiscoverView(true);
            disCover = true;
        } else {
            setDiscoverView(false);
            disCover = false;
        }
    }

    // 设置蓝牙检测界面样式
    private void setDiscoverView(boolean b) {
        disCover = b;
        if (model.getState() == 12) {
            // 蓝牙打开状态
            if (b) {
                activity.setViewForDetectionOpen();
            } else {
                activity.setViewForDetectionClose(true);
            }
        } else {
            // 蓝牙关闭状态
            activity.setViewForDetectionClose(false);
        }
    }

    // 获取数据
    private void getData() {
        List<BluetoothInfo> pairList = model.findAvailableDevice();// 已经配对的蓝牙列表
        activity.setAdapterPair(pairList);
    }

    //
    private void getNoData() {
        List<BluetoothInfo> pairList = new ArrayList<>();// 已经配对的蓝牙列表
        List<BluetoothInfo> userList = new ArrayList<>();// 附近可以配对的蓝牙列表
        setView(userList, pairList);
    }

    // 设置界面
    private void setView(List<BluetoothInfo> userList, List<BluetoothInfo> pairList) {
        activity.setAdapterPair(pairList);
        activity.setAdapterUser(userList);
    }

    /**
     * 修改蓝牙昵称
     */
    public void upDataName() {
        EditBlueToothNameFragment fragment = new EditBlueToothNameFragment();
        activity.openFragment(fragment);
        fragment.setResultListener(new EditBlueToothNameFragment.ResultListener() {
            @Override
            public void resultListener(boolean type, String name) {
                if (type) {
                    if (name != null & !name.equals("")) {
                        activity.setPhoneName(name);
                    }
                }
            }
        });
    }

    /**
     * 蓝牙连接
     *
     * @param pairList
     * @param position
     */
    public void link(List<BluetoothInfo> pairList, int position) {
        model.destroyScan();
        address = pairList.get(position).getBluetoothAddress();
        //链接的操作应该在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.link(address,handler);
            }
        }).start();
        
    }

    /**
     * 配对
     *
     * @param position
     */
    public void bond(List<BluetoothInfo> userList, int position) {
        model.destroyScan();
        String address = userList.get(position).getBluetoothAddress();
        model.bond(address);
    }

    /**
     * 蓝牙设置
     */
    public void BluetoothSet() {
        model.BluetoothSet();
    }

    /**
     * 蓝牙可见的操作功能
     */
    public void setDetection() {
        if (model.getState() == 12) {
            if (disCover) {
                /*
                 * 此时蓝牙可见
                 * 执行：关闭蓝牙可见，设置蓝牙不可见见面
                 */
                model.closeBluetoothDiscoverable();
                setDiscoverView(false);
                disCover = false;
            } else {
                /*
                 * 此时蓝牙不可见
                 * 执行：打开蓝牙可见，设置蓝牙可见见面
                 */
                model.setDetection(300);
                setDiscoverView(true);
                disCover = true;
            }
        }
    }

    // 注册蓝牙接收广播
    private void setReceiver() {
        if (Receiver == null) {
            Receiver = new DeviceReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
            filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
            filter.setPriority(Integer.MAX_VALUE);
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
                case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED://行动扫描模式改变了
                    setScanMode();
                    break;
                case BluetoothDevice.ACTION_FOUND://发现设备
                    BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {// 搜索没有配过对的蓝牙设备
                        model.deviceAdd(btd);
                        Set<BluetoothDevice> device = model.getDevice();
                        if (device != null && device.size() > 0) {
                            List<BluetoothInfo> userList = model.delUserList(device);
                            activity.setAdapterUser(userList);
                        }
                    }
                    activity.setViewForScan(true);
                    Log.e("蓝牙连接", "扫描中。。。");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Set<BluetoothDevice> device = model.getDevice();
                    if (device != null && device.size() > 0) {
                        List<BluetoothInfo> userList = model.delUserList(device);
                        activity.setAdapterUser(userList);
                    }
                    activity.setViewForScan(false);
                    getData();
                    Log.e("蓝牙连接", "扫描完成");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED://设备连接状态改变
                    BluetoothDevice bDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (bDevice.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            Log.e("蓝牙连接", "正在配对......");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Toast.makeText(context, "配对成功!", Toast.LENGTH_SHORT).show();
                            getData();
                            model.scan();
                            model.connect(bDevice);// 连接设备
                            break;
                        case BluetoothDevice.BOND_NONE:
                            Log.e("蓝牙连接", "取消配对");
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED://设备连接成功
                    BluetoothDevice Device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e("蓝牙连接", "设备连接成功" + new GsonBuilder().serializeNulls().create().toJson(Device));
                    getData();
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED://设备断开连接
                    BluetoothDevice disDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e("蓝牙连接", "设备断开连接" + new GsonBuilder().serializeNulls().create().toJson(disDevice));
                    getData();
                    break;
                case BluetoothDevice.ACTION_PAIRING_REQUEST://
                    break;
            }
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model.destroyScan();
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
        model = null;
    }
}
