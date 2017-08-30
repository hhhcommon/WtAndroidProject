package com.wotingfm.ui.play.radio.fragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.bean.RadioInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 电台详情，明天
 */

public class RadioTomorrowAdapter extends CommonAdapter<RadioInfo.DataBean.TomorrowBean> {
    private TomorrowBeanClick playerClick;

    public RadioTomorrowAdapter(Context context, List<RadioInfo.DataBean.TomorrowBean> datas, TomorrowBeanClick radioClick) {
        super(context, R.layout.item_tomorrow, datas);
        this.playerClick = radioClick;
    }

    @Override
    protected void convert(ViewHolder holder, final RadioInfo.DataBean.TomorrowBean s, final int position) {
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvTodayYu = (TextView) holder.itemView.findViewById(R.id.tvTodayYu);
        tvTitle.setText(s.title);
        if (s.had_subscribed == true) {
            tvTodayYu.setText("已预约");
            tvTodayYu.setTextColor(Color.parseColor("#cccccd"));
        } else {
            tvTodayYu.setText("预约");
            tvTodayYu.setTextColor(Color.parseColor("#16181a"));
        }
        tvTodayYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null)
                    playerClick.follow(s, position);
            }
        });
        holder.setText(R.id.tvTime, s.start_time + "-" + s.end_time);
    }


    public interface TomorrowBeanClick {
        void follow(RadioInfo.DataBean.TomorrowBean singlesBean, int position);
    }

}
