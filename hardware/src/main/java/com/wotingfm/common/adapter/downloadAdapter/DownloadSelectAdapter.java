package com.wotingfm.common.adapter.downloadAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.common.bean.Player;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.wotingfm.R.mipmap.p;

/**
 * Created by amine on 2017/6/7.
 */

public class DownloadSelectAdapter extends CommonAdapter<Player.DataBean.SinglesBean> {
    private AlbumsInfoClick playerClick;
    private List<Player.DataBean.SinglesBean> datas;

    public DownloadSelectAdapter(Context context, List<Player.DataBean.SinglesBean> datas) {
        super(context, R.layout.item_albums_select, datas);
        this.datas = datas;
    }

    private boolean isSelect = false;

    public void setMore(boolean isSelect) {
        this.isSelect = isSelect;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, final Player.DataBean.SinglesBean s, final int position) {
        holder.setText(R.id.tvTitle, s.single_title);
        holder.setText(R.id.tvTime, s.single_seconds);
        ImageView ivSelect = (ImageView) holder.itemView.findViewById(R.id.ivSelect);
        ivSelect.setImageResource((s.isSelect == true || isSelect == true) ? R.mipmap.icon_select_s : R.mipmap.icon_select_n);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null) {
                    playerClick.player(s, s.isSelect, position);
                }
                s.isSelect = !s.isSelect;
                datas.set(position, s);
                notifyDataSetChanged();
            }
        });
    }


    public void setPlayerClick(AlbumsInfoClick playerClick) {
        this.playerClick = playerClick;
    }

    public interface AlbumsInfoClick {
        void player(Player.DataBean.SinglesBean albumsBean, boolean isSelect, int postion);

    }

}
