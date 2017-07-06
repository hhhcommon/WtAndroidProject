package com.wotingfm.common.adapter.radioAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.Radio;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 */

public class RadioAdapter extends CommonAdapter<Radio.DataBean.ChannelsBean> {
    private RadioClick playerClick;

    public RadioAdapter(Context context, List<Radio.DataBean.ChannelsBean> datas, RadioClick radioClick) {
        super(context, R.layout.item_radio, datas);
        this.playerClick = radioClick;
    }

    @Override
    protected void convert(ViewHolder holder, final Radio.DataBean.ChannelsBean s, final int position) {
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        Glide.with(BSApplication.getInstance()).load(s.image_url)// Glide
                .error(R.mipmap.oval_defut_other)
                .placeholder(R.mipmap.oval_defut_other)
                .into(ivPhoto);
        tvTitle.setText(s.title + "");
        tvContent.setText(s.desc + "");
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
        void clickAlbums(Radio.DataBean.ChannelsBean singlesBean);
    }

}
