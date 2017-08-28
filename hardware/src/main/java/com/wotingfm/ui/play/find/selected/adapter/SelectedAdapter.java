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
import com.wotingfm.ui.bean.Selected;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 发现=精选列表
 */

public class SelectedAdapter extends CommonAdapter<Selected.DataBeanX> {
    private Context context;
    private SelectedClickBase selectedClickBase;
    private int with = 0;
    private LinearLayout.LayoutParams  layoutParams2;
    private RelativeLayout.LayoutParams layoutParams1;

    public SelectedAdapter(Context context, List<Selected.DataBeanX> datas, SelectedClickBase selectedClickBase) {
        super(context, R.layout.item_selected_home, datas);
        this.context = context;
//        int with = DementionUtil.getScreenWidthInPx(this.getActivity()) - DementionUtil.dip2px(this.getActivity(), 80);
        with = (DementionUtil.getScreenWidthInPx(context) - DementionUtil.dip2px(context, 44)) / 3;
        this.selectedClickBase = selectedClickBase;
//        layoutParams1 = new LinearLayout.LayoutParams(with, with);
        layoutParams1 = new RelativeLayout.LayoutParams(with, with);
        layoutParams2 = new LinearLayout.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void convert(ViewHolder holder, final Selected.DataBeanX s, final int position) {
        TextView textView = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        ImageView ivMore = (ImageView) holder.itemView.findViewById(R.id.ivMore);
        View view = holder.itemView.findViewById(R.id.view);
        RelativeLayout labelContent = (RelativeLayout) holder.itemView.findViewById(R.id.labelContentMore);
        textView.setText(s.title);
        RecyclerView recyclerView = (RecyclerView) holder.itemView.findViewById(R.id.mRecyclerView);
        if ("DAILY_LISTENINGS".equals(s.type) ) {
            if (s.data.isEmpty()) {
                labelContent.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } else {
                labelContent.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
                recyclerView.setLayoutManager(layoutManager);
                ItemSelected1Adapter itemClassAdapter = new ItemSelected1Adapter(context, s.data, new SelectedClick() {
                    @Override
                    public void click(Selected.DataBeanX.DataBean dataBean) {
                        if (selectedClickBase != null)
                            selectedClickBase.click(dataBean);
                    }
                    @Override
                    public void play(Selected.DataBeanX.DataBean dataBean) {
                        if (selectedClickBase != null)
                            selectedClickBase.play(dataBean);
                    }
                });
                recyclerView.setAdapter(itemClassAdapter);
                ivMore.setVisibility(View.VISIBLE);
                ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedClickBase != null)
                            selectedClickBase.clickMore(s);
                    }
                });
            }

        }else  if ("EDITOR_SELECTIONS".equals(s.type)) {
            if (s.data.isEmpty()) {
                labelContent.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } else {
                labelContent.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
                recyclerView.setLayoutManager(layoutManager);
                ItemSelected1Adapter itemClassAdapter = new ItemSelected1Adapter(context, s.data, new SelectedClick() {
                    @Override
                    public void click(Selected.DataBeanX.DataBean dataBean) {
                        if (selectedClickBase != null)
                            selectedClickBase.click(dataBean);
                    }
                    @Override
                    public void play(Selected.DataBeanX.DataBean dataBean) {
                        if (selectedClickBase != null)
                            selectedClickBase.play(dataBean);
                    }
                });
                recyclerView.setAdapter(itemClassAdapter);
                ivMore.setVisibility(View.VISIBLE);
                ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedClickBase != null)
                            selectedClickBase.clickMore(s);
                    }
                });
            }
        } else if ("HOT_ALBUMS".equals(s.type)) {
            if (s.data.isEmpty()) {
                labelContent.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } else {
                labelContent.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
                recyclerView.setLayoutManager(layoutManager);
                ItemSelected2Adapter itemClassAdapter = new ItemSelected2Adapter(context, s.data, new SelectedClick() {
                    @Override
                    public void click(Selected.DataBeanX.DataBean dataBean) {
                        if (selectedClickBase != null)
                            selectedClickBase.click(dataBean);
                    }
                    @Override
                    public void play(Selected.DataBeanX.DataBean dataBean) {
                        if (selectedClickBase != null)
                            selectedClickBase.play(dataBean);
                    }
                });
                ivMore.setVisibility(View.GONE);
                recyclerView.setAdapter(itemClassAdapter);
            }
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
            ImageView img_play = (ImageView) holder.itemView.findViewById(R.id.img_play);

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
            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagClick != null)
                        tagClick.play(dataBean);
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
                holder.setText(R.id.tvTime,  "0次播放");
            }
            ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);

            if (dataBean.single_logo_url!= null && !dataBean.single_logo_url.equals("") ) {
                GlideUtils.loadImageViewRoundCornersMusic(dataBean.single_logo_url, ivPhoto, 150, 150);
            } else {
                GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
            }
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
        void play(Selected.DataBeanX.DataBean dataBean);
        void click(Selected.DataBeanX.DataBean dataBean);
    }

    public interface SelectedClickBase {
        void click(Selected.DataBeanX.DataBean dataBean);
        void play(Selected.DataBeanX.DataBean dataBean);
        void clickMore(Selected.DataBeanX dataBeanX);
    }
}
