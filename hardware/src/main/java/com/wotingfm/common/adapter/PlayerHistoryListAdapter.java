package com.wotingfm.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.wotingfm.R.id.ivTop;

/**
 * Created by amine on 2017/6/7.
 * 播放历史
 */

public class PlayerHistoryListAdapter extends CommonAdapter<Player.DataBean.SinglesBean> {
    private PlayerHistoryClick playerHistoryClick;

    public PlayerHistoryListAdapter(Context context, List<Player.DataBean.SinglesBean> datas, PlayerHistoryClick playerHistoryClick) {
        super(context, R.layout.item_player_history, datas);
        this.playerHistoryClick = playerHistoryClick;
    }

    @Override
    protected void convert(ViewHolder holder, final Player.DataBean.SinglesBean s, final int position) {
        holder.setText(R.id.tvContent, s.album_title);
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        textViewTitle.setText(s.single_title);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        Glide.with(BSApplication.getInstance()).load(s.single_logo_url)// Glide
                .into(imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null)
                    playerHistoryClick.click(s);
            }
        });
    }


    public interface PlayerHistoryClick {
        void click(Player.DataBean.SinglesBean singlesBean);
    }

}
