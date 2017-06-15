package com.wotingfm.ui.intercom.group.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.ui.intercom.group.groupchat.model.GroupChat;

import java.util.List;

/**
 * 适配器
 */
public class GroupChatAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GroupChat> group;

    public GroupChatAdapter(Context context, List<GroupChat> group) {
        this.context = context;
        this.group = group;
    }

    public void changeData(List<GroupChat> group) {
        this.group = group;
        notifyDataSetChanged();
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
            holder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(groupPosition==0){
            holder.tv_line.setVisibility(View.GONE);
        }else{
            holder.tv_line.setVisibility(View.VISIBLE);
        }

        final GroupChat lists = group.get(groupPosition);
        int num=1;
        try {
            num = group.get(groupPosition).getPerson().size();
        } catch (Exception e) {
            e.printStackTrace();
            num=1;
        }
        if (lists.getName() == null || lists.getName().equals("")) {
            holder.tv_name.setText("我创建的群");
        } else {
            holder.tv_name.setText(lists.getName());
        }

        holder.tv_number.setText("("+num+")");
        return convertView;
    }

    /**
     * 显示：child
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groups, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.image);
            holder.img_chat = (ImageView) convertView.findViewById(R.id.img_chat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupChat.news lists = group.get(groupPosition).getPerson().get(childPosition);


        if (lists.getName() == null || lists.getName().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getName());//名
        }
        return convertView;

    }

    class ViewHolder {
        public ImageView image;
        public TextView tv_name;
        public ImageView img_touXiang;
        public ImageView img_chat;
        public TextView tv_number;
        public TextView tv_line;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}