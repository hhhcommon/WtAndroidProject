package com.wotingfm.common.adapter.albumsAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.bean.Subscrible;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 */

public class AlbumsInfoProgramAdapter extends CommonAdapter<Player.DataBean.SinglesBean> {
    private AlbumsInfoClick playerClick;

    public AlbumsInfoProgramAdapter(Context context, List<Player.DataBean.SinglesBean> datas) {
        super(context, R.layout.item_albums_info_program, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final Player.DataBean.SinglesBean s, final int position) {
        holder.setText(R.id.tvTitle, s.single_title);
        holder.setText(R.id.tvTime, s.single_seconds);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.player(s, position);
                }
            }
        });
    }


    public void setPlayerClick(AlbumsInfoClick playerClick) {
        this.playerClick = playerClick;
    }

    public interface AlbumsInfoClick {
        void player(Player.DataBean.SinglesBean albumsBean, int postion);

    }

}
