package com.wotingfm.common.adapter.findHome;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Selected;
import com.wotingfm.common.bean.SelectedMore;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/22.
 */

public class ItemSelected1Adapter extends CommonAdapter<SelectedMore.DataBean.AlbumsBean> {
    private SelectedClick tagClick;
    private int with = 0;
    private LinearLayout.LayoutParams layoutParams1, layoutParams2;

    public ItemSelected1Adapter(Context context, List<SelectedMore.DataBean.AlbumsBean> datas, SelectedClick tagClick) {
        super(context, R.layout.item_item1_selected_home, datas);
        this.tagClick = tagClick;
        with = (DementionUtil.getScreenWidthInPx(context) - DementionUtil.dip2px(context, 54)) / 3;
        layoutParams1 = new LinearLayout.LayoutParams(with, with);
        layoutParams2 = new LinearLayout.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void convert(ViewHolder holder, final SelectedMore.DataBean.AlbumsBean dataBean, int position) {
        TextView textView = (TextView) holder.itemView.findViewById(R.id.tvContent);
        ImageView ivClass = (ImageView) holder.itemView.findViewById(R.id.ivClass);
        Glide.with(BSApplication.getInstance()).load(dataBean.logo_url)// Glide
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .override(with, with)
                .into(ivClass);
        ivClass.setLayoutParams(layoutParams1);
        textView.setLayoutParams(layoutParams2);
        textView.setText(dataBean.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagClick != null)
                    tagClick.click(dataBean);
            }
        });
    }

    public interface SelectedClick {
        void click(SelectedMore.DataBean.AlbumsBean dataBean);
    }

}