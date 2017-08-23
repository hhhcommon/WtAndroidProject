package com.wotingfm.ui.adapter.radioAdapter;

import android.content.Context;
import android.view.View;

import com.wotingfm.R;
import com.wotingfm.ui.bean.Provinces;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 */

public class ProvincesAdapter extends CommonAdapter<Provinces.DataBean.ProvincesBean> {
    private ProvincesClick playerClick;

    public ProvincesAdapter(Context context, List<Provinces.DataBean.ProvincesBean> datas, ProvincesClick radioClick) {
        super(context, R.layout.item_provinces, datas);
        this.playerClick = radioClick;
    }

    @Override
    protected void convert(ViewHolder holder, final Provinces.DataBean.ProvincesBean s, final int position) {
        holder.setText(R.id.tvLocal, s.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerClick != null)
                    playerClick.clickAlbums(s);
            }
        });
    }


    public interface ProvincesClick {
        void clickAlbums(Provinces.DataBean.ProvincesBean singlesBean);
    }

}
