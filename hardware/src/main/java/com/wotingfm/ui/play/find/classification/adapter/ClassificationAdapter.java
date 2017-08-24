package com.wotingfm.ui.play.find.classification.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.play.find.classification.model.Classification;
import com.wotingfm.common.view.GridSpacingItemDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 分类的adapter，包含gridView的适配器
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ClassificationAdapter extends CommonAdapter<Classification.DataBeanX> {
    private Context context;
    private TagClickBase tagClickBase;
    private GridSpacingItemDecoration gridSpacingItemDecoration;

    public ClassificationAdapter(Context context, List<Classification.DataBeanX> datas, TagClickBase tagClickBase) {
        super(context, R.layout.item_class_home, datas);
        this.context = context;
        this.tagClickBase = tagClickBase;
        gridSpacingItemDecoration = new GridSpacingItemDecoration(4, context.getResources().getDimensionPixelSize(R.dimen.padding_middle), true);
    }

    @Override
    protected void convert(ViewHolder holder, final Classification.DataBeanX s, final int position) {
        holder.setText(R.id.tvTitle, s.title);
        RecyclerView recyclerView = (RecyclerView) holder.itemView.findViewById(R.id.mRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.removeItemDecoration(gridSpacingItemDecoration);
        recyclerView.addItemDecoration(gridSpacingItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ItemClassAdapter itemClassAdapter = new ItemClassAdapter(context, s.data, new TagClick() {
            @Override
            public void click(Classification.DataBeanX.DataBean dataBean) {
                if (tagClickBase != null)
                    tagClickBase.click(dataBean);
            }
        });
        recyclerView.setAdapter(itemClassAdapter);
    }

    // gridView的适配器
    public class ItemClassAdapter extends CommonAdapter<Classification.DataBeanX.DataBean> {
        private TagClick tagClick;

        public ItemClassAdapter(Context context, List<Classification.DataBeanX.DataBean> datas, TagClick tagClick) {
            super(context, R.layout.item_item_class_home, datas);
            this.tagClick = tagClick;
        }

        @Override
        protected void convert(ViewHolder holder, final Classification.DataBeanX.DataBean dataBean, int position) {
            holder.setText(R.id.tvContent, dataBean.title);
            ImageView ivClass = (ImageView) holder.itemView.findViewById(R.id.ivClass);
            Glide.with(BSApplication.getInstance()).load(dataBean.logo).into(ivClass);// Glide
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagClick != null)
                        tagClick.click(dataBean);
                }
            });
        }
    }

    private interface TagClick {
        void click(Classification.DataBeanX.DataBean dataBean);
    }

    public interface TagClickBase {
        void click(Classification.DataBeanX.DataBean dataBean);
    }
}
