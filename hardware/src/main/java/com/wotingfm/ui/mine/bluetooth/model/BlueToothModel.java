package com.wotingfm.ui.mine.bluetooth.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.wotingfm.common.utils.L;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class BlueToothModel {
    private Set<BluetoothDevice> device;                    // 搜索到新的蓝牙设备列表

    /**
     * 获取已经配对的蓝牙设备
     *
     * @return
     */
    public List<BluetoothInfo> findAvailableDevice(BluetoothAdapter blue) {
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
            Log.w("TAG", "--- > > " + pairList.size());
            for (int i = 0; i < pairList.size(); i++) {
                L.i(pairList.get(i).getBluetoothName());
            }
        }
        return pairList;
    }

    public List<BluetoothInfo> getUserList() {
        List<BluetoothInfo> userList = new ArrayList<>();// 附近可以配对的蓝牙列表
        return userList;
    }

    public List<BluetoothInfo> delUserList(Set<BluetoothDevice> device){
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

    public Set<BluetoothDevice> getDevice(){
        return device;
    }

    public void deviceAdd(BluetoothDevice btd){
        if (device == null) {
            device = new HashSet<>();
        }
        L.i("TAG", "搜索到蓝牙设备" + btd.getName());
        device.add(btd);
    }

}
