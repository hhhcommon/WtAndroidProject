package com.wotingfm.ui.intercom.group.groupmumberadd.adapter;

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
 * 组成员的适配器
 */
public class GroupNumberAddAdapter extends BaseAdapter {
    private List<Contact.user> list;
    private Context context;

    public GroupNumberAddAdapter(Context context, List<Contact.user> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Contact.user> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groupnumberadd, null);
            holder.img_view = (ImageView) convertView.findViewById(R.id.img_view);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.img_choose = (ImageView) convertView.findViewById(R.id.img_choose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact.user lists = list.get(position);

        String name = lists.getName();
        if (name != null && !name.trim().equals("")) {
            holder.tv_name.setText(name);
        } else {
            holder.tv_name.setText("未知");
        }

        if (lists.getAvatar() != null && !lists.getAvatar().equals("") && lists.getAvatar().startsWith("http")) {
            GlideUtils.loadImageViewRound(lists.getAvatar(), holder.img_view, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, holder.img_view, 60, 60);
        }
        if (!lists.is_admin()) {
            holder.img_choose.setImageResource(R.mipmap.icon_select_n);
        } else {
            holder.img_choose.setImageResource(R.mipmap.icon_select_s);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView img_view,img_choose;
        public TextView tv_name;
    }
}
