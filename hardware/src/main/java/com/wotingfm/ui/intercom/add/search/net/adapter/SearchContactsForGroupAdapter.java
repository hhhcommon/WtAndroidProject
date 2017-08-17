package com.wotingfm.ui.intercom.add.search.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/8 14:36
 * 邮箱：645700751@qq.com
 */
public class SearchContactsForGroupAdapter extends BaseAdapter {
    private List<Contact.group> list;
    private Context context;

    public SearchContactsForGroupAdapter(Context context, List<Contact.group> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Contact.group> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groups, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.img_view);
            holder.img_chat = (ImageView) convertView.findViewById(R.id.img_chat);
            holder.img_chat.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact.group lists = list.get(position);

        if (lists.getTitle() == null || lists.getTitle().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getTitle());//名
        }

        if (lists.getLogo_url() != null && !lists.getLogo_url().equals("") && lists.getLogo_url().startsWith("http:")) {
            GlideUtils.loadImageViewRoundCorners(lists.getLogo_url(), holder.img_touXiang, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCorners(R.mipmap.icon_avatar_d, holder.img_touXiang, 60, 60);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView img_touXiang;
        public ImageView img_chat;
    }


}
