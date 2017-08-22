package com.woting.ui.intercom.group.groupnews.noadd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.woting.R;
import com.woting.common.utils.GlideUtils;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 组详情界面成员的适配器
 */
public class GroupNewsPersonForNoAddAdapter extends BaseAdapter {
    private List<Contact.user> list;
    private Context context;

    public GroupNewsPersonForNoAddAdapter(Context context, List<Contact.user> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void changeData(List<Contact.user> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groupnewsperson, null);
            holder.img_view = (ImageView) convertView.findViewById(R.id.img_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact.user lists = list.get(position);
        if (lists.getAvatar() != null && !lists.getAvatar().equals("") && lists.getAvatar().startsWith("http:")) {
            GlideUtils.loadImageViewRound(lists.getAvatar(), holder.img_view, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, holder.img_view, 60, 60);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView img_view;
    }
}
