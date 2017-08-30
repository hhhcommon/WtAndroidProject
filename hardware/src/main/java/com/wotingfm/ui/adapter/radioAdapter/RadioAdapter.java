package com.wotingfm.ui.adapter.radioAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.ChannelsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 */

public class RadioAdapter extends CommonAdapter<ChannelsBean> {
    private RadioClick playerClick;

    public RadioAdapter(Context context, List<ChannelsBean> datas, RadioClick radioClick) {
        super(context, R.layout.item_radio, datas);
        this.playerClick = radioClick;
    }

    @Override
    protected void convert(ViewHolder holder, final ChannelsBean s, final int position) {
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);

        if (s.image_url!= null && !s.image_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(s.image_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
        tvTitle.setText(s.title.trim() + "");
        tvContent.setText(s.desc.trim() + "");
        tvTime.setText(s.listen_count + "人听过");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null)
                    playerClick.clickAlbums(s);
            }
        });
    }


    public interface RadioClick {
        void clickAlbums(ChannelsBean singlesBean);
    }

}
