package com.wotingfm.ui.play.find.classification.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.play.find.classification.main.model.Classification;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 分类的gridView的适配器
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ClassificationAdapter extends CommonAdapter<Classification.DataBeanX> {
    private TagClick tagClick;

    public ClassificationAdapter(Context context, List<Classification.DataBeanX> datas, TagClick tagClick) {
        super(context, R.layout.item_item_class_home, datas);
        this.tagClick = tagClick;
    }

    @Override
    protected void convert(ViewHolder holder, final Classification.DataBeanX s, final int position) {
        holder.setText(R.id.tvContent, s.title);
        ImageView ivClass = (ImageView) holder.itemView.findViewById(R.id.ivClass);
        if (s.logo != null && !s.logo.equals("")) {
            GlideUtils.loadImageViewRound(s.logo.trim(), ivClass);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.oval_defut_other, ivClass, 60, 60);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagClick != null)
                    tagClick.click(s);
            }
        });
    }

    public interface TagClick {
        void click(Classification.DataBeanX dataBean);
    }

}
