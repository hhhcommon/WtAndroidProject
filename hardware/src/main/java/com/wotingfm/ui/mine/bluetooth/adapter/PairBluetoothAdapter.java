package com.wotingfm.ui.mine.bluetooth.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.mine.bluetooth.model.BluetoothInfo;

import java.util.List;

/**
 * 已经配对多的蓝牙列表
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class PairBluetoothAdapter extends BaseAdapter {
    private Context context;
    private List<BluetoothInfo> list;
    private itemClickListener Listener;
    public PairBluetoothAdapter(Context context, List<BluetoothInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void changeData(List<BluetoothInfo> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void itemClickListener(itemClickListener l) {
        Listener = l;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_user_bluetooth_pair, parent, false);
            holder.textBluetoothName = (TextView) convertView.findViewById(R.id.text_bluetooth_name);// 设备名字
            holder.viewConn = convertView.findViewById(R.id.view_conn);
            holder.re_main = convertView.findViewById(R.id.re_main);
            holder.img_r = convertView.findViewById(R.id.img_r);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothInfo bName = list.get(position);
        if (bName.getBluetoothName() == null) {
            holder.textBluetoothName.setText(bName.getBluetoothAddress());
        } else {
            holder.textBluetoothName.setText(bName.getBluetoothName());
        }
        if(bName.isType()){
            holder.viewConn .setVisibility(View.VISIBLE);
            holder.img_r.setVisibility(View.GONE);
        }else{
            holder.viewConn .setVisibility(View.GONE);
            holder.img_r.setVisibility(View.VISIBLE);
        }
        Log.w("TAG", "bName" + bName + "position == " + position);
        holder.re_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.click(position);
            }
        });
        return convertView;
    }

    public interface itemClickListener{
        void click(int position);
    }

    class ViewHolder {
        TextView textBluetoothName;
        View viewConn,img_r,re_main;
    }
}
