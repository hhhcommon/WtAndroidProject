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
    private String playId;

    public PlayerListAdapter(Context context, List<SinglesBase> datas) {
        super(context, R.layout.item_pop_player, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final SinglesBase s, final int position) {
        holder.setText(R.id.tvContent, s.album_title);
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        textViewTitle.setText(s.single_title);
        ImageView ivTop = (ImageView) holder.itemView.findViewById(R.id.ivTop);
        ImageView ivClose = (ImageView) holder.itemView.findViewById(R.id.ivClose);
        if (s.isPlay == true || s.id.equals(playId)) {
            ivTop.setVisibility(View.VISIBLE);
            ivClose.setVisibility(View.INVISIBLE);
            textViewTitle.setTextColor(Color.parseColor("#fd8548"));
        } else {
            ivTop.setVisibility(View.GONE);
            ivClose.setVisibility(View.VISIBLE);
            textViewTitle.setTextColor(Color.parseColor("#16181a"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.player(s, position);
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.close(s);
                }
            }
        });
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public void setPlayerClick(PlayerClick playerClick, String playId) {
        this.playId = playId;
        this.playerClick = playerClick;
    }

    public interface PlayerClick {
        void player(SinglesBase singlesBean, int postion);

        void close(SinglesBase singlesBean);
    }

}
