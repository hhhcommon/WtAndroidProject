package com.woting.ui.intercom.main.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.woting.R;
import com.woting.common.utils.GlideUtils;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 联系人适配器
 * 作者：xinLong on 2017/6/8 14:36
 * 邮箱：645700751@qq.com
 */
public class ContactsAdapter extends BaseAdapter implements SectionIndexer {
    private List<Contact.user> list;
    private Context context;
    private OnListener onListener;

    public ContactsAdapter(Context context, List<Contact.user> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Contact.user> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_contacts, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.img_view);
            holder.img_chat = (ImageView) convertView.findViewById(R.id.img_chat);

            holder.indexLayout = (LinearLayout) convertView.findViewById(R.id.index);
            holder.indexTv = (TextView) convertView.findViewById(R.id.indexTv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact.user lists = list.get(position);

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.indexLayout.setVisibility(View.VISIBLE);
            holder.indexTv.setText(list.get(position).getSortLetters());
        } else {
            holder.indexLayout.setVisibility(View.GONE);
        }

        String nickName = "名称";
        try {
            String name = lists.getName();
            if (name != null && !name.equals("")) {
                nickName = name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String name = lists.getAlias_name();
            if (name != null && !name.equals("")) {
                nickName = name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_name.setText(nickName);//名


        if (lists.getAvatar() != null && !lists.getAvatar().equals("") && lists.getAvatar().startsWith("http")) {
            GlideUtils.loadImageViewRound(lists.getAvatar(), holder.img_touXiang, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, holder.img_touXiang, 60, 60);
        }

        holder.img_chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onListener.add(position);
            }
        });
        return convertView;
    }

    public interface OnListener {
        void add(int position);
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView img_touXiang;
        public ImageView img_chat;

        public LinearLayout indexLayout;
        public TextView indexTv;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
