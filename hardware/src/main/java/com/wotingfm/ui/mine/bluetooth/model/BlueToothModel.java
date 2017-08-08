package com.wotingfm.ui.mine.bluetooth.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.bluetooth.view.BluetoothFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class BlueToothModel {
    private BluetoothAdapter blue;
    private Set<BluetoothDevice> device;                    // 搜索到新的蓝牙设备列表

    public BlueToothModel() {
        blue = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 参数 无
     * 返回值 true 表示可以用嘛，否则不可以
     * 异常 无
     * 描述：这个方法用于检查蓝牙是否可用
     */
    public boolean checkBtIsAvailable() {
        if (blue == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 蓝牙是否打开
     *
     * @return
     */
    public boolean isOpen() {
        if (checkBtIsAvailable()) {
            return blue.isEnabled();
        } else {
            return false;
        }
    }

    /**
     * 获取此时的状态
     * STATE_OFF = 10;
     * STATE_TURNING_ON = 11;
     * STATE_ON = 12;
     * STATE_TURNING_OFF = 13;
     */
    public int getState() {
        if (checkBtIsAvailable()) {
            return blue.getState();
        } else {
            return -1;
        }
    }

    /**
     * 获取手机昵称
     * @return
     */
    public String getName(){
        return  blue.getName();
    }

    /**
     * 开始扫描
     *
     * @return
     */
    public boolean scan() {
        return blue.startDiscovery();
    }

    /**
     * 停止蓝牙搜索
     */
    public void destroy() {
        blue.cancelDiscovery();
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

    // 蓝牙设置 关闭蓝牙
    private void setBluetoothClose() {
        blue.cancelDiscovery();// 停止蓝牙搜索
        blue.disable();// 关闭蓝牙
    }

    // 蓝牙设置 打开蓝牙
    private void setBluetoothOpen() {
        if (blue != null) {  // 设备支持蓝牙
            blue.enable();   // 直接开启，不经过提示
        } else {
            // 设备不支持蓝牙
        }
    }

    /**
     * 蓝牙连接
     */
    public void link(String address) {
        BluetoothDevice mBluetoothDevice = blue.getRemoteDevice(address);
        connect(mBluetoothDevice);
    }

    /**
     * 配对
     */
    public void bond(String address) {
        if (address != null && !address.equals("No can be matched to use bluetooth")) {
            blue.cancelDiscovery();
            BluetoothDevice mBluetoothDevice = blue.getRemoteDevice(address);
            try {
                // 连接建立之前的先配对
                Method createBond = BluetoothDevice.class.getMethod("createBond");
                L.e("TAG", "开始配对");
                Boolean returnValue = (Boolean) createBond.invoke(mBluetoothDevice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接
     *
     * @param d
     */
    public void connect(BluetoothDevice d) {
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
     * 获取已经配对的蓝牙设备
     *
     * @return
     */
    public List<BluetoothInfo> findAvailableDevice() {
        List<BluetoothInfo> pairList = new ArrayList<>();
        // 获取可配对蓝牙设备
        Set<BluetoothDevice> device = blue.getBondedDevices();
        if (device.size() > 0) { // 存在已经配对过的蓝牙设备
            Iterator<BluetoothDevice> it = device.iterator();
            while (it.hasNext()) {
                BluetoothDevice btd = it.next();
                BluetoothInfo bName = new BluetoothInfo();
                bName.setBluetoothName(btd.getName());
                bName.setBluetoothAddress(btd.getAddress());
                bName.setBluetoothType(1);
                pairList.add(bName);
            }
            for (int i = 0; i < pairList.size(); i++) {
                L.i(pairList.get(i).getBluetoothName());
            }
        }
        Log.e("已经配对的数目", "--- > > " + pairList.size());
        return pairList;
    }

    public List<BluetoothInfo> getUserList() {
        List<BluetoothInfo> userList = new ArrayList<>();// 附近可以配对的蓝牙列表
        return userList;
    }

    public List<BluetoothInfo> delUserList(Set<BluetoothDevice> device) {
        List<BluetoothInfo> userList = new ArrayList<>();
        Iterator<BluetoothDevice> it = device.iterator();
        while (it.hasNext()) {
            BluetoothDevice btd = it.next();
            BluetoothInfo bName = new BluetoothInfo();
            bName.setBluetoothName(btd.getName());
            bName.setBluetoothAddress(btd.getAddress());
            bName.setBluetoothType(0);

            userList.add(bName);
        }
        return userList;
    }

    public Set<BluetoothDevice> getDevice() {
        return device;
    }

    public void deviceAdd(BluetoothDevice btd) {
        if (device == null) {
            device = new HashSet<>();
        }
        L.e("搜索到蓝牙设备", "--->" + btd.toString());
        device.add(btd);
    }

}
