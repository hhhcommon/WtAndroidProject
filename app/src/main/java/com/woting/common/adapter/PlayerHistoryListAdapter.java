package com.woting.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.woting.R;
import com.woting.common.application.BSApplication;
import com.woting.common.bean.Player;
import com.woting.common.utils.TimeUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

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
        LinearLayout labelContent = (LinearLayout) holder.itemView.findViewById(R.id.labelContent);
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        textViewTitle.setText(s.single_title);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        Glide.with(BSApplication.getInstance()).load(s.single_logo_url)// Glide
                .error(R.mipmap.oval_defut_other)
                .placeholder(R.mipmap.oval_defut_other)
                .into(imageView);
        labelContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null)
                    playerHistoryClick.click(s);
            }
        });
        tvTime.setText(TimeUtil.stampToDate(s.play_time + ""));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null) {
                    swipeable_container.quickClose();
                    playerHistoryClick.delete(s);
                }
            }
        });
    }


    public interface PlayerHistoryClick {
        void click(Player.DataBean.SinglesBean singlesBean);

        void delete(Player.DataBean.SinglesBean singlesBean);
    }

}
