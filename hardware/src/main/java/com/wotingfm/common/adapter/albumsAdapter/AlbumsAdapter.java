package com.wotingfm.common.adapter.albumsAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Subscrible;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by amine on 2017/6/7.
 */

public class AlbumsAdapter extends CommonAdapter<AlbumsBean> {
    private PlayerClick playerClick;

    public AlbumsAdapter(Context context, List<AlbumsBean> datas) {
        super(context, R.layout.item_albums, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final AlbumsBean s, final int position) {
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        Glide.with(BSApplication.getInstance()).load(s.logo_url)// Glide
                .error(R.mipmap.oval_defut_other)
                .placeholder(R.mipmap.oval_defut_other)
                .into(ivPhoto);
        tvTitle.setText(s.title + "");
        tvContent.setText(s.lastest_news + "");
        tvTime.setText(s.play_count + "次播放");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null)
                    playerClick.clickAlbums(s);
            }
        });
    }

    public void setPlayerClick(PlayerClick playerClick) {
        this.playerClick = playerClick;
    }

    public interface PlayerClick {
        void clickAlbums(AlbumsBean singlesBean);
    }

}
