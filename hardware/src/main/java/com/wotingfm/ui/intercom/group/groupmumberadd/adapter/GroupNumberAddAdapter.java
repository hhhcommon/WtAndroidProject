package com.wotingfm.ui.intercom.group.groupmumberadd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 组成员的适配器
 */
public class GroupNumberAddAdapter extends BaseAdapter {
    private List<Contact.user> list;
    private Context context;
    private Contact.user lists;
    private OnListener onListener;

    public GroupNumberAddAdapter(Context context, List<Contact.user> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    public void ChangeDate(List<Contact.user> list) {
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groupnumberadd, null);
            holder.img_view = (ImageView) convertView.findViewById(R.id.img_view);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        lists = list.get(position);
        String name = lists.getName();
        if (name != null && !name.trim().equals("")) {
            holder.tv_name.setText(name);
        } else {
            holder.tv_name.setText("未知");
        }

        holder.tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.apply(position);
            }
        });

        return convertView;
    }

    public interface OnListener {
        void apply(int position);
    }

    class ViewHolder {
        public ImageView img_view;
        public TextView tv_name, tv_type;
    }
}
