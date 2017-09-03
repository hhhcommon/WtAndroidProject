package com.wotingfm.ui.play.album.view.download.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.bean.Player;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 专辑下载适配器
 */

public class DownloadSelectAdapter extends CommonAdapter<Player.DataBean.SinglesBean> {
    private AlbumsInfoClick playerClick;

    public DownloadSelectAdapter(Context context, List<Player.DataBean.SinglesBean> datas) {
        super(context, R.layout.item_albums_select, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final Player.DataBean.SinglesBean s, final int position) {
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        TextView tv_odl = (TextView) holder.itemView.findViewById(R.id.tv_odl);
        ImageView ivSelect = (ImageView) holder.itemView.findViewById(R.id.ivSelect);

        tvTitle.setText(s.single_title);
        tvTime.setText(s.single_seconds);
        if (s.isDownloadOver) {
            tvTitle.setTextColor(Color.parseColor("#cccccd"));
            tvTime.setTextColor(Color.parseColor("#cccccd"));
            tv_odl.setVisibility(View.VISIBLE);
            ivSelect.setImageResource(R.mipmap.icon_select_n);
        } else {
            if (s.isSelect) {
                tvTitle.setTextColor(Color.parseColor("#16181a"));
                tvTime.setTextColor(Color.parseColor("#959698"));
                tv_odl.setVisibility(View.GONE);
                ivSelect.setImageResource(R.mipmap.icon_select_s);
            } else {
                tvTitle.setTextColor(Color.parseColor("#16181a"));
                tvTime.setTextColor(Color.parseColor("#959698"));
                tv_odl.setVisibility(View.GONE);
                ivSelect.setImageResource(R.mipmap.icon_select_n);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.itemClick( position);
                }
            }
        });
    }


    public void setPlayerClick(AlbumsInfoClick playerClick) {
        this.playerClick = playerClick;
    }

    public interface AlbumsInfoClick {
        void itemClick( int position);

    }

}
