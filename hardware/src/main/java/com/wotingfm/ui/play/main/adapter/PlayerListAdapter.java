package com.wotingfm.ui.play.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.bean.SinglesBase;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 播放器
 * 播放列表适配器
 */
public class PlayerListAdapter extends CommonAdapter<SinglesBase> {
    private PlayerClick playerClick;

    public PlayerListAdapter(Context context, List<SinglesBase> datas) {
        super(context, R.layout.item_pop_player, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final SinglesBase s, final int position) {
        holder.setText(R.id.tvContent, s.album_title);
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tv_playList = (TextView) holder.itemView.findViewById(R.id.tv_playList);
        textViewTitle.setText(s.single_title);
        ImageView ivTop = (ImageView) holder.itemView.findViewById(R.id.ivTop);
        ImageView ivClose = (ImageView) holder.itemView.findViewById(R.id.ivClose);
        if (s.isPlay == true) {
            if (s.isAlbumList) {
                tv_playList.setVisibility(View.GONE);
            } else {
                if (s.is_radio) {
                    tv_playList.setVisibility(View.GONE);
                } else {
                    tv_playList.setVisibility(View.VISIBLE);
                }
            }
            ivTop.setVisibility(View.VISIBLE);
            ivClose.setVisibility(View.INVISIBLE);
            textViewTitle.setTextColor(Color.parseColor("#fd8548"));
        } else {
            tv_playList.setVisibility(View.GONE);
            ivTop.setVisibility(View.GONE);
            ivClose.setVisibility(View.VISIBLE);
            textViewTitle.setTextColor(Color.parseColor("#16181a"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.player(position);
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.close(position);
                }
            }
        });
        tv_playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.getList(position);
                }
            }
        });
    }

    public void setPlayerClick(PlayerClick playerClick) {
        this.playerClick = playerClick;
    }

    public interface PlayerClick {
        void player(int position);

        void getList(int position);

        void close(int position);
    }

}
