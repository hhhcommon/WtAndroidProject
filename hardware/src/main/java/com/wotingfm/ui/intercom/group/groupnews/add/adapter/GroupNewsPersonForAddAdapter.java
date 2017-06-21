package com.wotingfm.ui.intercom.group.groupnews.add.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import java.util.List;

/**
 * 组详情界面成员的适配器,包括添加，删除
 */
public class GroupNewsPersonForAddAdapter extends BaseAdapter {
    private List<Contact.user> list;
    private Context context;
    private Contact.user lists;
    private String url;

    public GroupNewsPersonForAddAdapter(Context context, List<Contact.user> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void changeData(List<Contact.user> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groupnewspersonforadd, null);
            holder.img_view = (ImageView) convertView.findViewById(R.id.img_view);
            holder.img_add = (ImageView) convertView.findViewById(R.id.img_add);
            holder.img_del = (ImageView) convertView.findViewById(R.id.img_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        lists = list.get(position);
        if (lists.getType() == 1) {
            // 正常
            holder.img_view.setVisibility(View.VISIBLE);
            holder.img_add.setVisibility(View.GONE);
            holder.img_del.setVisibility(View.GONE);
        } else if (lists.getType() == 2) {
            // 添加
            holder.img_view.setVisibility(View.GONE);
            holder.img_add.setVisibility(View.VISIBLE);
            holder.img_del.setVisibility(View.GONE);
        } else {
            // 删除
            holder.img_view.setVisibility(View.GONE);
            holder.img_add.setVisibility(View.GONE);
            holder.img_del.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView img_view,img_add,img_del;
    }
}
