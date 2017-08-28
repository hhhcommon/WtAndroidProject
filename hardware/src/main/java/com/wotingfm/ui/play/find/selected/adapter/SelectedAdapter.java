package com.wotingfm.ui.play.find.selected.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.Selected;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 发现=精选列表
 */

public class SelectedAdapter extends CommonAdapter<Selected.DataBeanX.DataBean> {
    private SelectedClick selectedClick;

    public SelectedAdapter(Context context, List<Selected.DataBeanX.DataBean> datas, SelectedClick selectedClick) {
        super(context, R.layout.item_radiostation, datas);
        this.selectedClick = selectedClick;
    }

    @Override
    protected void convert(ViewHolder holder, final Selected.DataBeanX.DataBean dataBean, int position) {
        try {
            holder.setText(R.id.tvTitle, dataBean.single_title);
        } catch (Exception e) {
            holder.setText(R.id.tvTitle, "");
        }
        try {
            holder.setText(R.id.tvContent, dataBean.album_title);
        } catch (Exception e) {
            holder.setText(R.id.tvContent, "");
        }
        try {
            holder.setText(R.id.tvTime, dataBean.single_play_count + "次播放");
        } catch (Exception e) {
            holder.setText(R.id.tvTime, "0次播放");
        }
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);

        if (dataBean.single_logo_url != null && !dataBean.single_logo_url.equals("")) {
            GlideUtils.loadImageViewRoundCornersMusic(dataBean.single_logo_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedClick != null)
                    selectedClick.click(dataBean);
            }
        });
    }

    public interface SelectedClick {
        void click(Selected.DataBeanX.DataBean dataBean);
    }

}
