package com.wotingfm.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.woting.commonplat.gather.GatherData.url;

/**
 * Created by amine on 2017/6/7.
 * 播放弹出列表
 */

public class PlayerAdapter extends CommonAdapter<Player.DataBean.SinglesBean> {
    private List<Player.DataBean.SinglesBean> datas;
    private int with;
    private LinearLayout.LayoutParams layoutParams;

    public PlayerAdapter(Context context, List<Player.DataBean.SinglesBean> datas) {
        super(context, R.layout.item_player_photo, datas);
        this.datas = datas;
        with = DementionUtil.getScreenWidthInPx(context) - DementionUtil.dip2px(context, 100);
        layoutParams = new LinearLayout.LayoutParams(with, with);
    }

    @Override
    protected void convert(ViewHolder holder, Player.DataBean.SinglesBean s, int position) {
        holder.setText(R.id.tvTitle, s.single_title);
        holder.setText(R.id.tvContent, s.album_title);
        View view = holder.itemView.findViewById(R.id.view);
        View view1 = holder.itemView.findViewById(R.id.view1);
        View view2 = holder.itemView.findViewById(R.id.view2);
        view.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        view1.setVisibility(position != 0 ? View.VISIBLE : View.GONE);
        view2.setVisibility(position == datas.size() - 1 ? View.VISIBLE : View.GONE);
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        Glide.with(BSApplication.getInstance()).load(s.single_logo_url)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .override(with, with)
                .into(imageView);
        imageView.setLayoutParams(layoutParams);
    }


}
