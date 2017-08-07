package com.wotingfm.ui.mine.wifi.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class WiFiListAdapter extends BaseAdapter {
    private Context context;
    private List<ScanResult> list;

    public WiFiListAdapter(Context context, List<ScanResult> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<ScanResult> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_wifi_list, parent, false);
            holder.textWiFiName = (TextView) convertView.findViewById(R.id.text_wifi_name);
            holder.image_wifi_lock = (ImageView) convertView.findViewById(R.id.image_wifi_lock);
            holder.image_wifi = (ImageView) convertView.findViewById(R.id.image_wifi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult result = list.get(position);

        if (result.SSID != null && !result.SSID.equals("")) {
            holder.textWiFiName.setText(result.SSID);
        } else {
            holder.textWiFiName.setText("");
        }

        holder.image_wifi.setImageResource(getImg(result.level));

        if (TextUtils.isEmpty(getEncryption(result))) {
            holder.image_wifi_lock.setVisibility(View.INVISIBLE);
        } else {
            holder.image_wifi_lock.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private String getEncryption(ScanResult scanResult) {
        String encryption = "";
        String descOri = scanResult.capabilities;
        if (descOri != null && !descOri.trim().equals("")) {
            if (descOri.toUpperCase().contains("WPA-PSK")) {
                encryption = encryption + "WPA-PSK/";
            }
            if (descOri.toUpperCase().contains("WPA2-PSK")) {
                encryption = encryption + "WPA2-PSK/";
            }
            if (descOri.toUpperCase().contains("WEP")) {
                encryption = encryption + "WEP/";
            }
            if (descOri.toUpperCase().contains("ESS")) {
                encryption = encryption + "ESS/";
            }
            if (encryption.length() > 0) {
                encryption = encryption.substring(0, encryption.length() - 1);
            }
        }

        return encryption;
    }

    // 获取当前信号强度
    private int getImg(int level) {
        int imgId = R.mipmap.icon_wifi_strength_1_n;
        if (Math.abs(level) > 100) {
            imgId = R.mipmap.icon_wifi_strength_1_n;
        } else if (Math.abs(level) > 80) {
            imgId = R.mipmap.icon_wifi_strength_1_n;
        } else if (Math.abs(level) > 70) {
            imgId = R.mipmap.icon_wifi_strength_1_n;
        } else if (Math.abs(level) > 60) {
            imgId = R.mipmap.icon_wifi_strength_2_n;
        } else if (Math.abs(level) > 50) {
            imgId = R.mipmap.icon_wifi_strength_2_n;
        } else {
            imgId = R.mipmap.icon_wifi_strength_3_n;
        }
        return imgId;
    }

    class ViewHolder {
        TextView textWiFiName;// WiFi Name
        ImageView image_wifi_lock, image_wifi;
    }
}
