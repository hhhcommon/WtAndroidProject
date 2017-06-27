package com.wotingfm.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.common.utils.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 我的订阅
 */

public class PlayerSubscribleListAdapter extends CommonAdapter<AlbumsBean> {
    private PlayerHistoryClick playerHistoryClick;

    public PlayerSubscribleListAdapter(Context context, List<AlbumsBean> datas, PlayerHistoryClick playerHistoryClick) {
        super(context, R.layout.item_player_subscrible, datas);
        this.playerHistoryClick = playerHistoryClick;
    }

    @Override
    protected void convert(ViewHolder holder, final AlbumsBean s, final int position) {
        holder.setText(R.id.tvContent, s.lastest_news);
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        textViewTitle.setText(s.title);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        Glide.with(BSApplication.getInstance()).load(s.logo_url)// Glide
                .into(imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null)
                    playerHistoryClick.click(s);
            }
        });
        tvTime.setText(s.play_count + "次播放");
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
        void click(AlbumsBean singlesBean);

        void delete(AlbumsBean singlesBean);
    }

}
