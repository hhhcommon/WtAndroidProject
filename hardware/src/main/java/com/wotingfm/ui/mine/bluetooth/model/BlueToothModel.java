package com.wotingfm.ui.mine.bluetooth.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.upLoadImage;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.ToastUtils;

import java.io.IOException;
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
    /**
     * 配对成功后的蓝牙套接字
     */
    private BluetoothSocket mBluetoothSocket;

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
     * 获取当前扫描模式
     * SCAN_MODE_NONE = 20
     * SCAN_MODE_CONNECTABLE = 21
     * SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23
     */
    public int getScanMode() {
        if (checkBtIsAvailable()) {
            return blue.getScanMode();
        } else {
            return -1;
        }
    }

    /**
     * 获取手机昵称
     *
     * @return
     */
    public String getName() {
        return blue.getName();
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
    public void destroyScan() {
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
     * 尝试打开蓝牙可见性
     */
    public void setDetection(int timeout) {
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(blue, timeout);
            setScanMode.invoke(blue, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试关闭蓝牙可见性
     * 可见：adapter.setScanMode(BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
     * 不可见：adapter.setScanMode(BluetoothAdapter.SCAN_MODE_CONNECTABLE);
     * 设置可见性超时时间： adapter.setDiscoverableTimeout(BLUETOOTH_DSCOVERABLE_TIME);
     */
    public void closeBluetoothDiscoverable() {
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(blue, 1);
            setScanMode.invoke(blue, BluetoothAdapter.SCAN_MODE_CONNECTABLE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 尝试配对和连接
     */
    public void link(String address, Handler handler) {
        BluetoothDevice mBluetoothDevice = blue.getRemoteDevice(address);
        if (mBluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
            //如果这个设备取消了配对，则尝试配对
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mBluetoothDevice.createBond();
            }
        } else if (mBluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            //如果这个设备已经配对完成，则尝试连接
            connect(mBluetoothDevice, handler);
        }
    }

    /**
     * 尝试连接一个设备，子线程中完成，因为会线程阻塞
     *
     * @param btDev   蓝牙设备对象
     * @param handler 结果回调事件
     * @return
     */
    private void connect(BluetoothDevice btDev, Handler handler) {
        try {
            //通过和服务器协商的uuid来进行连接
            mBluetoothSocket = btDev.createRfcommSocketToServiceRecord(UUID.fromString(SequenceUUID.getUUID()));
            if (mBluetoothSocket != null)
                //全局只有一个bluetooth，所以我们可以将这个socket对象保存在appliaction中
//                BltAppliaction.bluetoothSocket = mBluetoothSocket;
                //通过反射得到bltSocket对象，与uuid进行连接得到的结果一样，但这里不提倡用反射的方法
                //mBluetoothSocket = (BluetoothSocket) btDev.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(btDev, 1);
                Log.d("blueTooth", "开始连接...");
            //在建立之前调用
            if (blue.isDiscovering())
                //停止搜索
                blue.cancelDiscovery();
            //如果当前socket处于非连接状态则调用连接
            if (!getmBluetoothSocket().isConnected()) {
                //你应当确保在调用connect()时设备没有执行搜索设备的操作。
                // 如果搜索设备也在同时进行，那么将会显著地降低连接速率，并很大程度上会连接失败。
                getmBluetoothSocket().connect();
            }
            Log.d("blueTooth", "已经链接");
            if (handler == null) return;
            //结果回调
            Message message = new Message();
            message.what = 4;
            message.obj = btDev;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.e("blueTooth", "...链接失败");
            try {
                getmBluetoothSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public BluetoothSocket getmBluetoothSocket() {
        return mBluetoothSocket;
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
     * 蓝牙连接
     */
    public void link(String address) {
        BluetoothDevice mBluetoothDevice = blue.getRemoteDevice(address);
        connect(mBluetoothDevice);
    }

    /**
     * 连接
     *
     * @param d
     */
    public void connect(BluetoothDevice d) {
        try {
            UUID uuid = UUID.fromString(SequenceUUID.getUUID());
            BluetoothSocket btSocket = d.createRfcommSocketToServiceRecord(uuid);// 连接
            L.e("蓝牙连接", "开始连接...");
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
                bName.setBluetoothType(btd.getBluetoothClass().getDeviceClass());
                bName.setType(false);
                pairList.add(bName);
                Log.e("蓝牙连接", "已经配对蓝牙设备" + new GsonBuilder().serializeNulls().create().toJson(btd));
            }
        }
        Log.e("蓝牙连接", "已经配对的数目---<<" + pairList.size() + ">>");
        if (pairList.size() > 0) {
            return assemblyData(pairList);
        } else {
            return pairList;
        }
    }

    // 组装数据
    private List<BluetoothInfo> assemblyData(List<BluetoothInfo> pairList) {
        if (pairList != null && pairList.size() > 0) {
            List<BluetoothInfo> List = getLinked();
            if (List != null && List.size() > 0) {
                for (int i = 0; i < List.size(); i++) {
                    String address = List.get(i).getBluetoothAddress().trim();
                    if (address != null && !address.trim().equals("")) {
                        for (int j = 0; j < pairList.size(); j++) {
                            String _address = pairList.get(j).getBluetoothAddress().trim();
                            if (_address != null && _address.equals(address)) {
                                pairList.get(j).setType(true);
                            }
                        }
                    }
                }
            }
        }
        return pairList;
    }


    /**
     * 组装数据
     *
     * @param device
     * @return
     */
    public List<BluetoothInfo> delUserList(Set<BluetoothDevice> device) {
        List<BluetoothInfo> userList = new ArrayList<>();
        Iterator<BluetoothDevice> it = device.iterator();
        while (it.hasNext()) {
            BluetoothDevice btd = it.next();
            BluetoothInfo bName = new BluetoothInfo();
            bName.setBluetoothName(btd.getName());
            bName.setBluetoothAddress(btd.getAddress());
            bName.setBluetoothType(btd.getBluetoothClass().getDeviceClass());
            bName.setType(false);
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
        Log.e("蓝牙连接", "搜索到蓝牙设备" + new GsonBuilder().serializeNulls().create().toJson(btd));
        device.add(btd);
    }

    /**
     * 得到已经连接成功的连接
     */
    public List<BluetoothInfo> getLinked() {
        List<BluetoothInfo> List = new ArrayList<>();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);//得到连接状态的方法
            method.setAccessible(true);//打开权限
            int state = (int) method.invoke(adapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                Log.e("蓝牙连接", "已经连接成功的设备数量：" + devices.size());
                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) {
                        BluetoothInfo bName = new BluetoothInfo();
                        bName.setBluetoothName(device.getName());
                        bName.setBluetoothAddress(device.getAddress());
                        bName.setBluetoothType(device.getBluetoothClass().getDeviceClass());
                        List.add(bName);
                        Log.e("蓝牙连接", "已经连接成功的设备" + device.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List;
    }


}
