package com.wotingfm.common.adapter.findHome;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Classification;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Selected;
import com.wotingfm.common.view.GridSpacingItemDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.wotingfm.R.id.ivClass;
import static com.wotingfm.ui.intercom.main.view.InterPhoneActivity.context;

/**
 * Created by amine on 2017/6/7.
 * <p>
 * 发现=精选列表
 */

public class SelectedAdapter extends CommonAdapter<Selected.DataBeanX> {
    private Context context;
    private SelectedClickBase selectedClickBase;
    private int with = 0;
    private LinearLayout.LayoutParams layoutParams1, layoutParams2;

    public SelectedAdapter(Context context, List<Selected.DataBeanX> datas, SelectedClickBase selectedClickBase) {
        super(context, R.layout.item_selected_home, datas);
        this.context = context;
        with = (DementionUtil.getScreenWidthInPx(context) - DementionUtil.dip2px(context, 54)) / 3;
        this.selectedClickBase = selectedClickBase;
        layoutParams1 = new LinearLayout.LayoutParams(with, with);
        layoutParams2 = new LinearLayout.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void convert(ViewHolder holder, final Selected.DataBeanX s, final int position) {
        TextView textView = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        textView.setText(s.title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedClickBase != null)
                    selectedClickBase.clickMore(s);
            }
        });
        RecyclerView recyclerView = (RecyclerView) holder.itemView.findViewById(R.id.mRecyclerView);
        if ("DAILY_LISTENINGS".equals(s.type) || "EDITOR_SELECTIONS".equals(s.type)) {
            GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
            recyclerView.setLayoutManager(layoutManager);
            ItemSelected1Adapter itemClassAdapter = new ItemSelected1Adapter(context, s.data, new SelectedClick() {
                @Override
                public void click(Selected.DataBeanX.DataBean dataBean) {
                    if (selectedClickBase != null)
                        selectedClickBase.click(dataBean);
                }
            });
            recyclerView.setAdapter(itemClassAdapter);
        } else if ("HOT_ALBUMS".equals(s.type)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
            recyclerView.setLayoutManager(layoutManager);
            ItemSelected2Adapter itemClassAdapter = new ItemSelected2Adapter(context, s.data, new SelectedClick() {
                @Override
                public void click(Selected.DataBeanX.DataBean dataBean) {
                    if (selectedClickBase != null)
                        selectedClickBase.click(dataBean);
                }
            });
            recyclerView.setAdapter(itemClassAdapter);
        }


    }

    public class ItemSelected1Adapter extends CommonAdapter<Selected.DataBeanX.DataBean> {
        private SelectedClick tagClick;

        public ItemSelected1Adapter(Context context, List<Selected.DataBeanX.DataBean> datas, SelectedClick tagClick) {
            super(context, R.layout.item_item1_selected_home, datas);
            this.tagClick = tagClick;
        }

        @Override
        protected void convert(ViewHolder holder, final Selected.DataBeanX.DataBean dataBean, int position) {
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

    }

    public class ItemSelected2Adapter extends CommonAdapter<Selected.DataBeanX.DataBean> {
        private SelectedClick tagClick;

        public ItemSelected2Adapter(Context context, List<Selected.DataBeanX.DataBean> datas, SelectedClick tagClick) {
            super(context, R.layout.item_item2_selected_home, datas);
            this.tagClick = tagClick;
        }

        @Override
        protected void convert(ViewHolder holder, final Selected.DataBeanX.DataBean dataBean, int position) {
            holder.setText(R.id.tvTitle, dataBean.title);
            holder.setText(R.id.tvContent, dataBean.lastest_news);
            holder.setText(R.id.tvTime, dataBean.play_count + "次播放");
            ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
            Glide.with(BSApplication.getInstance()).load(dataBean.logo_url)// Glide
                    .placeholder(R.mipmap.oval_defut_other)
                    .error(R.mipmap.oval_defut_other)
                    .into(ivPhoto);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagClick != null)
                        tagClick.click(dataBean);
                }
            });
        }

    }

    private interface SelectedClick {
        void click(Selected.DataBeanX.DataBean dataBean);
    }

    public interface SelectedClickBase {
        void click(Selected.DataBeanX.DataBean dataBean);

        void clickMore(Selected.DataBeanX dataBeanX);
    }
}
