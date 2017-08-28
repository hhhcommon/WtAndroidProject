package com.wotingfm.ui.adapter.findHome;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.AlbumsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 每日听单，编辑精选列表页
 */

public class ItemSelected1Adapter extends CommonAdapter<AlbumsBean> {
    private SelectedClick tagClick;
    private int with = 0;
    private LinearLayout.LayoutParams  layoutParams2;
    private RelativeLayout.LayoutParams layoutParams1;

    public ItemSelected1Adapter(Context context, List<AlbumsBean> datas, SelectedClick tagClick) {
        super(context, R.layout.item_item1_selected_home, datas);
        this.tagClick = tagClick;
        with = (DementionUtil.getScreenWidthInPx(context) - DementionUtil.dip2px(context, 54)) / 3;
        layoutParams1 = new RelativeLayout.LayoutParams(with, with);
        layoutParams2 = new LinearLayout.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void convert(ViewHolder holder, final AlbumsBean dataBean, int position) {
        TextView textView = (TextView) holder.itemView.findViewById(R.id.tvContent);
        ImageView ivClass = (ImageView) holder.itemView.findViewById(R.id.ivClass);

        if (dataBean.logo_url!= null && !dataBean.logo_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(dataBean.logo_url, ivClass, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivClass, 60, 60);
        }
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
        void click(AlbumsBean dataBean);
    }

}