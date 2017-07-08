package com.wotingfm.ui.mine.bluetooth.model;

/**
 * 蓝牙信息
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class BluetoothInfo {
    private String bluetoothName;// 蓝牙名字

    private String bluetoothAddress;// 蓝牙地址

    private int bluetoothType;// 1为已配对设备 0为可以配对设备

    public int getBluetoothType() {
        return bluetoothType;
    }

    public void setBluetoothType(int bluetoothType) {
        this.bluetoothType = bluetoothType;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }
}
