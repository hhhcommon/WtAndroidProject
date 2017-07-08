package com.wotingfm.ui.mine.bluetooth.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.bluetooth.model.BlueToothModel;
import com.wotingfm.ui.mine.bluetooth.view.BluetoothFragment;
import com.wotingfm.ui.mine.bluetooth.model.BluetoothInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class BlueToothPresenter {

    private final BluetoothFragment activity;
    private final BlueToothModel model;
    private final BluetoothAdapter blue;
    private DeviceReceiver Receiver;
    private boolean isScan = true;             // 是否正在扫描蓝牙
    private List<BluetoothInfo> userList;      // 附近可以配对的蓝牙列表
    private List<BluetoothInfo> pairList;      // 已经配对的蓝牙列表
    private int index;                         // 配对的蓝牙在列表中的位置

    public BlueToothPresenter(BluetoothFragment activity) {
        this.activity = activity;
        this.model = new BlueToothModel();
        blue = BluetoothAdapter.getDefaultAdapter();
        getData();
        setReceiver();
        setView();
    }

    // 获取数据
    private void getData() {
        pairList = model.findAvailableDevice(blue);
        userList = model.getUserList();
    }

    // 设置界面
    private void setView() {
        // 检查蓝牙是否打开
        if (blue.isEnabled()) {
            blue.startDiscovery();
            activity.setViewForOpen();
            activity.setAdapterPair(pairList);
            activity.setAdapterUser(userList);
        } else {
            activity.setViewForClose();
            activity.setAdapterPair(pairList);
            activity.setAdapterUser(userList);
        }
    }

    // 蓝牙连接
    public void link(int position) {
        String address = pairList.get(position).getBluetoothAddress();
        BluetoothDevice mBluetoothDevice = blue.getRemoteDevice(address);
        connect(mBluetoothDevice);
    }

    /**
     * 连接
     *
     * @param d
     */
    private void connect(BluetoothDevice d) {
        try {
            UUID uuid = UUID.fromString(StringConstant.SPP_UUID);
            BluetoothSocket btSocket = d.createRfcommSocketToServiceRecord(uuid);// 连接
            L.d("TAG", "开始连接...");
            btSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 配对
     *
     * @param position
     */
    public void bond(int position) {
        if (!blue.isEnabled() || isScan || userList.size() == 0) {
            return;
        }
        String address = userList.get(position).getBluetoothAddress();
        if (address != null && !address.equals("No can be matched to use bluetooth")) {
            blue.cancelDiscovery();
            BluetoothDevice mBluetoothDevice = blue.getRemoteDevice(address);
            try {
                // 连接建立之前的先配对
                Method createBond = BluetoothDevice.class.getMethod("createBond");
                L.e("TAG", "开始配对");
                Boolean returnValue = (Boolean) createBond.invoke(mBluetoothDevice);
                if (returnValue) {
                    index = position - 1;
                    userList.get(index).setBluetoothType(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙设置
     */
    public void BluetoothSet() {
        if (blue.isEnabled()) {
            setBluetoothClose();
        } else {
            setBluetoothOpen();// 打开蓝牙
        }
    }

    // 蓝牙设置 打开蓝牙
    private void setBluetoothOpen() {
        if (blue != null) {  // 设备支持蓝牙
            blue.enable();   // 直接开启，不经过提示
            activity.setViewForOpen();
        } else {
            // 设备不支持蓝牙
            ToastUtils.show_always(activity.getActivity(), "设备不支持蓝牙!");
        }
    }

    // 蓝牙设置 关闭蓝牙
    private void setBluetoothClose() {
        blue.cancelDiscovery();// 停止蓝牙搜索
        blue.disable();// 关闭蓝牙
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
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (blue.getState() == BluetoothAdapter.STATE_OFF) {
                    activity.setViewForClose();
                    if (userList != null) userList.clear();
                    activity.setAdapterUser(userList);
                } else if (blue.getState() == BluetoothAdapter.STATE_ON) {
                    blue.startDiscovery();
                    activity.setViewForOpen();
                    pairList = model.findAvailableDevice(blue);
                    activity.setAdapterPair(pairList);
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {// 搜索到新设备
                BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {// 搜索没有配过对的蓝牙设备
                    model.deviceAdd(btd);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {// 搜索结束
                if (blue.isEnabled()) {
                    isScan = false;
                    Set<BluetoothDevice> device = model.getDevice();
                    if (device != null && device.size() > 0) { // 存在已经配对过的蓝牙设备
                        userList = model.delUserList(device);
                        activity.setAdapterUser(userList);
                    }
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        L.d("TAG", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Toast.makeText(context, "配对成功!", Toast.LENGTH_SHORT).show();
                        pairList.add(userList.get(index));
                        userList.remove(index);
                        activity.setAdapterPair(pairList);
                        activity.setAdapterUser(userList);
                        isScan = true;
                        connect(device);// 连接设备
                        break;
                    case BluetoothDevice.BOND_NONE:
                        L.d("TAG", "取消配对");
                        break;
                }
            }
        }
    }

    /**
     * 数据销毁
     */
    public void onDestroy() {
        blue.cancelDiscovery();// 停止蓝牙搜索
        if (Receiver != null) {
            activity.getActivity().unregisterReceiver(Receiver);
            Receiver = null;
        }
    }
}
