package com.wotingfm.ui.play.album.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.AlbumsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
/**
 * 专辑列表
 */

public class AlbumsAdapter extends CommonAdapter<AlbumsBean> {
    private PlayerClick playerClick;

    public AlbumsAdapter(Context context, List<AlbumsBean> datas) {
        super(context, R.layout.item_albums, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final AlbumsBean s, final int position) {
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        ImageView img_play = (ImageView) holder.itemView.findViewById(R.id.img_play);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);

        if (s.logo_url != null && !s.logo_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(s.logo_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
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
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null)
                    playerClick.play(s);
            }
        });
    }

    public void setPlayerClick(PlayerClick playerClick) {
        this.playerClick = playerClick;
    }

    public interface PlayerClick {
        void clickAlbums(AlbumsBean singlesBean);
        void play(AlbumsBean singlesBean);
    }

}
