package com.wotingfm.ui.mine.fm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.mine.fm.model.FMInfo;

import java.util.List;

/**
 * FM列表
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class FMListAdapter extends BaseAdapter {
    private Context context;
    private List<FMInfo> list;

    public FMListAdapter(Context context, List<FMInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void changeData(List<FMInfo> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_fm_list, parent, false);
            holder.imageSignal = (ImageView) convertView.findViewById(R.id.image_signal);
            holder.textFmFrequency = (TextView) convertView.findViewById(R.id.text_fm_frequency);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FMInfo fmInfo = list.get(position);
        String name = fmInfo.getFmName().trim();
        if (name != null && !name.equals("")) {
            holder.textFmFrequency.setText(name);// 频率
        } else {
            holder.textFmFrequency.setText("");// 频率
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageSignal;// 信号图标
        TextView textFmFrequency;// FM
    }
}
