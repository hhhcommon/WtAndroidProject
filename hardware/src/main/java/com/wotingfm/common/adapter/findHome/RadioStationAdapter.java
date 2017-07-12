package com.wotingfm.common.adapter.findHome;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.bean.Radio;
import com.wotingfm.common.bean.Radiostation;
import com.wotingfm.common.bean.Selected;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/21.
 * 发现，电台列表
 */

public class RadioStationAdapter extends CommonAdapter<ChannelsBean> {
    private RadioStationClick radioStationClick;

    public RadioStationAdapter(Context context, List<ChannelsBean> datas, RadioStationClick radioStationClick) {
        super(context, R.layout.item_radiostation, datas);
        this.radioStationClick = radioStationClick;

    }

    @Override
    protected void convert(ViewHolder holder, final ChannelsBean dataBean, int position) {
        holder.setText(R.id.tvTitle, dataBean.title);
        holder.setText(R.id.tvContent, dataBean.desc);
        holder.setText(R.id.tvTime, dataBean.listen_count + "人听过");
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        Glide.with(BSApplication.getInstance()).load(dataBean.image_url)// Glide
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .into(ivPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioStationClick != null)
                    radioStationClick.click(dataBean);
            }
        });
    }

    public interface RadioStationClick {
        void click(ChannelsBean dataBean);
    }


}
