package com.wotingfm.ui.adapter.findHome;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.ChannelsBean;
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
        holder.setText(R.id.tvTitle, dataBean.title.trim());
        holder.setText(R.id.tvContent, dataBean.desc.trim());
        holder.setText(R.id.tvTime, dataBean.listen_count + "人听过");
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);

        if (dataBean.image_url!= null && !dataBean.image_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(dataBean.image_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
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
