package com.wotingfm.common.adapter.findHome;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.LiveBean;
import com.wotingfm.common.bean.Radiostation;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static android.R.attr.padding;
import static android.R.attr.x;
import static com.wotingfm.R.id.fragmentTop;
import static com.wotingfm.R.id.ivPhoto;
import static com.wotingfm.R.id.ivPhotoBg;
import static com.wotingfm.R.id.tvContent;
import static com.wotingfm.R.id.tvType;
import static com.wotingfm.R.mipmap.p;

/**
 * Created by amine on 2017/6/21.
 * 发现==直播列表
 */

public class LiveListAdapter extends CommonAdapter<LiveBean.DataBean> {
    private LiveListClick radioStationClick;
    private LinearLayout.LayoutParams layoutParams2;
    private FrameLayout.LayoutParams layoutParams1;
    private int with;

    public LiveListAdapter(Context context, List<LiveBean.DataBean> datas, LiveListClick radioStationClick) {
        super(context, R.layout.item_live_list, datas);
        this.radioStationClick = radioStationClick;
        with = (DementionUtil.getScreenWidthInPx(context) - DementionUtil.dip2px(context, 24)) / 2;
        layoutParams1 = new FrameLayout.LayoutParams(with, with);
        layoutParams2 = new LinearLayout.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void convert(ViewHolder holder, final LiveBean.DataBean dataBean, int position) {
        holder.setText(R.id.tvTitle, dataBean.title1);
        holder.setText(R.id.tvNumber, dataBean.number + "人");
        TextView tvType = (TextView) holder.itemView.findViewById(R.id.tvType);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        ImageView ivPhotoBg = (ImageView) holder.itemView.findViewById(R.id.ivPhotoBg);
        tvContent.setLayoutParams(layoutParams2);
        tvContent.setText(dataBean.title2);
        if (dataBean.type == 1) {
            tvType.setText("直播中");
            tvType.setBackgroundResource(R.drawable.live_bg);
        } else if (dataBean.type == 2) {
            tvType.setText("预告");
            tvType.setBackgroundResource(R.drawable.trailer_bg);
        }
        Glide.with(BSApplication.getInstance()).load(dataBean.logo)// Glide
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .override(with, with)
                .into(ivPhotoBg);
        ivPhotoBg.setLayoutParams(layoutParams1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioStationClick != null)
                    radioStationClick.click(dataBean);
            }
        });
    }


    public interface LiveListClick {
        void click(LiveBean.DataBean dataBean);
    }


}
