package com.wotingfm.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.Reports;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static android.R.attr.data;
import static android.R.attr.type;
import static com.wotingfm.R.id.ivPhoto;

/**
 * Created by amine on 2017/6/7.
 * 主播详情页
 */

public class AnchorPersonalCenterInfoAdapter extends CommonAdapter<AnchorInfo.DataBeanXX.UserBean.DataBeanX> {
    private List<AnchorInfo.DataBeanXX.UserBean.DataBeanX> datas;
    private Context context;

    public AnchorPersonalCenterInfoAdapter(Context context, List<AnchorInfo.DataBeanXX.UserBean.DataBeanX> datas) {
        super(context, R.layout.item_anchor_personal, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, final AnchorInfo.DataBeanXX.UserBean.DataBeanX s, final int position) {
        LinearLayout linearLiveBase = (LinearLayout) holder.itemView.findViewById(R.id.linearLiveBase);
        LinearLayout linearDiscountBase = (LinearLayout) holder.itemView.findViewById(R.id.linearDiscountBase);
        LinearLayout linearSendBase = (LinearLayout) holder.itemView.findViewById(R.id.linearSendBase);
        TextView tvSendDiscountNub = (TextView) holder.itemView.findViewById(R.id.tvSendDiscountNub);
        TextView tvSendNub = (TextView) holder.itemView.findViewById(R.id.tvSendNub);
        if ("lives".equals(s.type) && !s.data.isEmpty()) {
            linearLiveBase.setVisibility(View.VISIBLE);
        } else {
            linearLiveBase.setVisibility(View.GONE);
        }
        if ("albums".equals(s.type) && !s.data.isEmpty()) {
            linearDiscountBase.setVisibility(View.VISIBLE);
            tvSendDiscountNub.setText("TA发布的专辑(" + s.total_count + ")");
        } else {
            linearDiscountBase.setVisibility(View.GONE);
        }
        if ("singles".equals(s.type) && !s.data.isEmpty()) {
            linearSendBase.setVisibility(View.VISIBLE);
            tvSendNub.setText("TA发布的节目(" + s.total_count + ")");
        } else {
            linearSendBase.setVisibility(View.GONE);
        }
        for (int i = 0, size = s.data.size(); i < size; i++) {
            if ("lives".equals(s.type)) {
                View item = LayoutInflater.from(context).inflate(R.layout.item_lives, null);
                AnchorInfo.DataBeanXX.UserBean.DataBeanX.DataBean d = s.data.get(i);
                ImageView ivPhoto = (ImageView) item.findViewById(R.id.ivPhoto);
                TextView tvTitle = (TextView) item.findViewById(R.id.tvTitle);
                TextView tvContent = (TextView) item.findViewById(R.id.tvContent);
                TextView tvTime = (TextView) item.findViewById(R.id.tvTime);
                Glide.with(BSApplication.getInstance()).load(d.cover)// Glide
                        .into(ivPhoto);
                tvTitle.setText(d.title);
                tvContent.setText("");
                tvTime.setText(d.audience_count + "人在线");
                linearLiveBase.addView(item);
            } else if ("albums".equals(s.type)) {
                View item = LayoutInflater.from(context).inflate(R.layout.item_albums, null);
                AnchorInfo.DataBeanXX.UserBean.DataBeanX.DataBean d = s.data.get(i);
                ImageView ivPhoto = (ImageView) item.findViewById(R.id.ivPhoto);
                TextView tvTitle = (TextView) item.findViewById(R.id.tvTitle);
                TextView tvContent = (TextView) item.findViewById(R.id.tvContent);
                TextView tvTime = (TextView) item.findViewById(R.id.tvTime);
                Glide.with(BSApplication.getInstance()).load(d.logo_url)// Glide
                        .into(ivPhoto);
                tvTitle.setText(d.title);
                tvContent.setText(d.lastest_news);
                tvTime.setText(d.play_count + "次播放");
                linearDiscountBase.addView(item);
            } else if ("singles".equals(s.type)) {
                View item = LayoutInflater.from(context).inflate(R.layout.item_albums, null);
                AnchorInfo.DataBeanXX.UserBean.DataBeanX.DataBean d = s.data.get(i);
                TextView tvTitle = (TextView) item.findViewById(R.id.tvTitle);
                TextView tvContent = (TextView) item.findViewById(R.id.tvContent);
                TextView tvTime = (TextView) item.findViewById(R.id.tvTime);
                tvTitle.setText(d.single_title);
                tvContent.setText(d.album_title);
                tvTime.setText(d.published_at);
                linearSendBase.addView(item);
            }
        }

    }

}
