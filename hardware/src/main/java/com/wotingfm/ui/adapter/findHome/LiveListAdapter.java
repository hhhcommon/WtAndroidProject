package com.wotingfm.ui.adapter.findHome;

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
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.LiveBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

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
        holder.setText(R.id.tvTitle, dataBean.title);
        holder.setText(R.id.tvNumber, dataBean.audience_count + "人");
        TextView tvType = (TextView) holder.itemView.findViewById(R.id.tvType);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        ImageView ivPhotoBg = (ImageView) holder.itemView.findViewById(R.id.ivPhotoBg);
        tvContent.setLayoutParams(layoutParams2);
        tvContent.setText(dataBean.owner.name);
        if ("living".equals(dataBean.type)) {
            tvType.setText("直播中");
            tvType.setBackgroundResource(R.drawable.live_bg);
        } else if ("prepare".equals(dataBean.type)) {
            tvType.setText("预告");
            tvType.setBackgroundResource(R.drawable.trailer_bg);
        }

        if (dataBean.cover!= null && !dataBean.cover.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(dataBean.cover, ivPhotoBg, 300, 300);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhotoBg, 300, 300);
        }
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
