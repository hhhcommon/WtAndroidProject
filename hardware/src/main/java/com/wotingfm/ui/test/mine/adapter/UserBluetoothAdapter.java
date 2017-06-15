package com.wotingfm.ui.test.mine.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.test.mine.model.BluetoothInfo;

import java.util.List;

/**
 * 可用蓝牙列表
 * Created by Administrator on 6/14/2017.
 */
public class UserBluetoothAdapter extends BaseAdapter {
    private Context context;
    private List<BluetoothInfo> list;

    public UserBluetoothAdapter(Context context, List<BluetoothInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<BluetoothInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BluetoothInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_user_bluetooth, parent, false);
            holder.textBluetoothName = (TextView) convertView.findViewById(R.id.text_bluetooth_name);// 设备名称
            holder.textPairDevice = (TextView) convertView.findViewById(R.id.text_pair_device);// "选取可用设备"
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothInfo bName = list.get(position);
        if (position > 0) {
            if (list.get(position).getBluetoothType() == (list.get(position - 1).getBluetoothType())) {
                holder.textPairDevice.setVisibility(View.GONE);
            }
        }
        if (bName.getBluetoothName() == null) {
            holder.textBluetoothName.setText(bName.getBluetoothAddress());
        } else {
            holder.textBluetoothName.setText(bName.getBluetoothName());
        }
        Log.w("TAG", "bName" + bName.getBluetoothName() + "\t" + bName.getBluetoothAddress());

        return convertView;
    }

    class ViewHolder {
        TextView textBluetoothName;// 设备名称
        TextView textPairDevice;// "选取可用设备"
    }
}
