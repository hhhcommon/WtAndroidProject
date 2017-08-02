package com.wotingfm.ui.mine.myfocus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.slidingbutton.SlidingButtonView;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.mine.myfocus.model.Focus;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class MyFocusAdapter extends BaseAdapter {
    private List<Focus> list;
    private Context context;
    private OnListener onListener;

    public MyFocusAdapter(Context context, List<Focus> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Focus> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_focus, null);
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.img_view);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.tv_introduce = (TextView) convertView.findViewById(R.id.tv_introduce);
            holder.tv_focus = (TextView) convertView.findViewById(R.id.tv_focus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Focus lists = list.get(position);

        if (lists.getAvatar() != null && !lists.getAvatar().equals("")&&lists.getAvatar().startsWith("http")) {
            GlideUtils.loadImageViewRound(lists.getAvatar(), holder.img_touXiang, 60, 60);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, holder.img_touXiang, 60, 60);
        }

        if (lists.getName() == null || lists.getName().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getName());//名
        }

        if (lists.getSignature()== null || lists.getSignature().equals("")) {
            holder.tv_introduce.setText("这家伙很懒~");//简介
        } else {
            holder.tv_introduce.setText(lists.getSignature());//简介
        }

        holder.tv_focus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onListener.del(position);
            }
        });
        return convertView;
    }

    public interface OnListener {
        void del(int position);
    }

    class ViewHolder {
        public TextView tv_name,tv_introduce,tv_focus;
        public ImageView img_touXiang;
    }

}