package com.wotingfm.ui.play.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.SinglesBase;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 搜索节目列表
 */

public class ProgramSearchAdapter extends CommonAdapter<SinglesBase> {

    public ProgramSearchAdapter(Context context, List<SinglesBase> datas, OnClick deleteClick) {
        super(context, R.layout.item_program_serch, datas);
        this.deleteClick = deleteClick;
    }

    private OnClick deleteClick;

    @Override
    protected void convert(ViewHolder holder, final SinglesBase s, final int position) {
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        LinearLayout largeLabel = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);

        if (s.album_logo_url!= null && !s.album_logo_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(s.album_logo_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
        tvTitle.setText(s.single_title + "");
        tvContent.setText(s.album_title);
        tvTime.setText(s.album_play_count + "次播放");
        largeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null)
                    deleteClick.click(s);
            }
        });
    }

    public interface OnClick {
        void click(SinglesBase s);
    }
}
