package com.wotingfm.ui.mine.bluetooth.model;

/**
 * 蓝牙信息
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class BluetoothInfo {
    private String bluetoothName;   // 蓝牙名字
    private String bluetoothAddress;// 蓝牙地址
    private int bluetoothType;      // 蓝牙设备的类型
    private boolean Type;           // 蓝牙是否连接成功

    public boolean isType() {
        return Type;
    }

    public void setType(boolean type) {
        Type = type;
    }

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
