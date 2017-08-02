package com.wotingfm.ui.intercom.group.groupchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChat;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 适配器
 */
public class GroupChatAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GroupChat> group;
    private clickListener clickListener;

    public GroupChatAdapter(Context context, List<GroupChat> group) {
        this.context = context;
        this.group = group;
    }

    public void changeData(List<GroupChat> group) {
        this.group = group;
        notifyDataSetChanged();
    }

    public void setClickListener(clickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return group.get(groupPosition).getPerson().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return group.get(groupPosition).getPerson().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 显示：group
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_group_chat_lin, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.img_group_type = (ImageView) convertView.findViewById(R.id.img_group_type);

            holder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 是否展开
        if (isExpanded) {
            holder.img_group_type.setImageResource(R.mipmap.icon_triangle_black);
        } else {
            holder.img_group_type.setImageResource(R.mipmap.icon_triangle_right_black);
        }

        if (groupPosition == 0) {
            holder.tv_line.setVisibility(View.GONE);
        } else {
            holder.tv_line.setVisibility(View.VISIBLE);
        }

        final GroupChat lists = group.get(groupPosition);
        int num = 1;
        try {
            num = group.get(groupPosition).getPerson().size();
        } catch (Exception e) {
            e.printStackTrace();
            num = 1;
        }
        if (lists.getName() == null || lists.getName().equals("")) {
            holder.tv_name.setText("我的群");
        } else {
            holder.tv_name.setText(lists.getName());
        }

        holder.tv_number.setText("(" + num + ")");
        return convertView;
    }

    /**
     * 显示：child
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groups, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.tv_lin = (TextView) convertView.findViewById(R.id.tv_lin);//名
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.img_view);
            holder.img_chat = (ImageView) convertView.findViewById(R.id.img_chat);
            holder.tv_lin.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact.group lists = group.get(groupPosition).getPerson().get(childPosition);

        if (lists.getTitle() == null || lists.getTitle().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getTitle());//名
        }

        if (lists.getLogo_url() != null && !lists.getLogo_url().equals("") && lists.getLogo_url().startsWith("http")) {
            GlideUtils.loadImageViewRoundCorners(lists.getLogo_url(), holder.img_touXiang, 60, 60);
        } else {
            GlideUtils.loadImageViewRoundCorners(R.mipmap.icon_avatar_d, holder.img_touXiang, 60, 60);
        }

        holder.img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(groupPosition, childPosition);
            }
        });

        return convertView;
    }

    public interface clickListener {
        void onItemClick(int groupPosition, int childPosition);
    }

    class ViewHolder {
        public ImageView img_chat, img_group_type, img_touXiang, image;
        public TextView tv_number, tv_name, tv_line, tv_lin;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
