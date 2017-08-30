package com.wotingfm.ui.play.radio.fragment.adapter;

import android.content.Context;
import android.view.View;

import com.wotingfm.R;
import com.wotingfm.ui.bean.RadioInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 电台详情，昨天
 */

public class RadioYesterdayAdapter extends CommonAdapter<RadioInfo.DataBean.YesterdayBean> {
    private YesterdayBeanClick playerClick;

    public RadioYesterdayAdapter(Context context, List<RadioInfo.DataBean.YesterdayBean> datas, YesterdayBeanClick radioClick) {
        super(context, R.layout.item_yesterday, datas);
        this.playerClick = radioClick;
    }

    @Override
    protected void convert(ViewHolder holder, final RadioInfo.DataBean.YesterdayBean s, final int position) {
        holder.setText(R.id.tvTitle, s.title);
        holder.setText(R.id.tvTime, s.start_time + "-" + s.end_time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null)
                    playerClick.clickAlbums(s);
            }
        });
    }


    public interface YesterdayBeanClick {
        void clickAlbums(RadioInfo.DataBean.YesterdayBean singlesBean);
    }

}
