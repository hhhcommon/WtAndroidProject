package com.wotingfm.common.adapter.serch;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.bean.UserBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 搜索用户列表
 */

public class UsersSerchAdapter extends CommonAdapter<UserBean> {

    public UsersSerchAdapter(Context context, List<UserBean> datas, OnClick deleteClick) {
        super(context, R.layout.item_anchor, datas);
        this.deleteClick = deleteClick;
    }

    private OnClick deleteClick;

    @Override
    protected void convert(ViewHolder holder, final UserBean s, final int position) {
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        holder.setText(R.id.tvTitle, s.nick_name);
        holder.setText(R.id.tvContent, "专辑  " + s.albums_total_count + "    " + "粉丝  " + s.fans_count);
        Glide.with(BSApplication.getInstance()).load(s.avatar)// Glide
                .error(R.mipmap.oval_defut_photo)
                .placeholder(R.mipmap.oval_defut_photo).transform(new GlideCircleTransform(BSApplication.getInstance()))
                .into(ivPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null)
                    deleteClick.click(s);
            }
        });
    }

    public interface OnClick {
        void click(UserBean s);
    }
}
