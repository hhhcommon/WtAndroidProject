package com.woting.common.adapter.radioAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.R;
import com.woting.common.bean.RadioInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 电台详情，今天
 */

public class RadioTodayAdapter extends CommonAdapter<RadioInfo.DataBean.TodayBean> {
    private TodayBeanClick playerClick;

    public RadioTodayAdapter(Context context, List<RadioInfo.DataBean.TodayBean> datas, TodayBeanClick radioClick) {
        super(context, R.layout.item_today, datas);
        this.playerClick = radioClick;
    }

    @Override
    protected void convert(ViewHolder holder, final RadioInfo.DataBean.TodayBean s, final int position) {
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        ImageView ivPlayer = (ImageView) holder.itemView.findViewById(R.id.ivPlayer);
        TextView tvTodayYu = (TextView) holder.itemView.findViewById(R.id.tvTodayYu);
        if (s.is_playing == true) {
            tvContent.setText("正在直播");
            ivPlayer.setVisibility(View.VISIBLE);
            tvTitle.setTextColor(Color.parseColor("#fd8548"));
        } else {
            tvContent.setText("回听");
            ivPlayer.setVisibility(View.GONE);
            tvTitle.setTextColor(Color.parseColor("#959698"));
        }
        tvTitle.setText(s.title);
        if (s.can_reserved == true) {
            tvTodayYu.setVisibility(View.VISIBLE);
            if (s.had_subscribed == true) {
                tvTodayYu.setText("已预约");
            } else {
                tvTodayYu.setText("预约");
            }
            tvTodayYu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerClick != null)
                        playerClick.follow(s, position);
                }
            });
        } else {
            tvTodayYu.setVisibility(View.GONE);
        }
        holder.setText(R.id.tvTime, s.start_time + "-" + s.end_time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null && s.is_playing == true)
                    playerClick.clickAlbums(s);
            }
        });
    }


    public interface TodayBeanClick {
        void clickAlbums(RadioInfo.DataBean.TodayBean singlesBean);

        void follow(RadioInfo.DataBean.TodayBean singlesBean, int position);
    }

}
