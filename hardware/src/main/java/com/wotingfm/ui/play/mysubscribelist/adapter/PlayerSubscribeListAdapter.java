package com.wotingfm.ui.play.mysubscribelist.adapter;

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
import com.wotingfm.ui.bean.AlbumsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 我的订阅
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */

public class PlayerSubscribeListAdapter extends CommonAdapter<AlbumsBean> {
    private PlayerHistoryClick playerHistoryClick;

    public PlayerSubscribeListAdapter(Context context, List<AlbumsBean> datas, PlayerHistoryClick playerHistoryClick) {
        super(context, R.layout.item_player_subscrible, datas);
        this.playerHistoryClick = playerHistoryClick;
    }

    @Override
    protected void convert(ViewHolder holder, final AlbumsBean s, final int position) {
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        LinearLayout largeLabel = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        ImageView img_play = (ImageView) holder.itemView.findViewById(R.id.img_play);

        textViewTitle.setText(s.title);
        holder.setText(R.id.tvContent, s.lastest_news);
        tvTime.setText(s.play_count + "次播放");
        if (s.logo_url != null && !s.logo_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(s.logo_url, imageView, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, imageView, 60, 60);
        }

        // 跳转到专辑
        largeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null)
                    playerHistoryClick.click(s);
            }
        });

        // 播放本专辑
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerHistoryClick != null)
                    playerHistoryClick.play(s);
            }
        });

        // 删除该订阅
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
        void play(AlbumsBean singlesBean);
        void delete(AlbumsBean singlesBean);
    }

}
