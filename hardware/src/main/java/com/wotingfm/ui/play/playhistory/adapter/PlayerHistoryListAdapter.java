package com.wotingfm.ui.play.playhistory.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.common.utils.TimeUtil;
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
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        LinearLayout labelContent = (LinearLayout) holder.itemView.findViewById(R.id.labelContent);
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);

        if(s.is_radio){
            textViewTitle.setText(s.album_title);
            holder.setText(R.id.tvContent, "电台");
            tvTime.setText(TimeUtil.stampToDate(String.valueOf(s.play_time)) + "");

        }else{
            textViewTitle.setText(s.single_title);
            holder.setText(R.id.tvContent, s.album_title);
            tvTime.setText(TimeUtil.stampToDate(String.valueOf(s.play_time)) + "");
        }

        if (s.single_logo_url != null && !s.single_logo_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(s.single_logo_url, imageView, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, imageView, 60, 60);
        }

        labelContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null)
                    playerHistoryClick.click(s);
            }
        });

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
