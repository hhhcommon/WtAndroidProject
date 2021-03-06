package com.wotingfm.ui.play.report.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.ui.play.report.model.Reports;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 举报界面原因列表
 */

public class ReportAdapter extends CommonAdapter<Reports.DataBean.Reasons> {
    private ReportsSelect reportsSelect;
    private List<Reports.DataBean.Reasons> datas;

    public ReportAdapter(Context context, List<Reports.DataBean.Reasons> datas, ReportsSelect reportsSelect) {
        super(context, R.layout.item_player_reports, datas);
        this.reportsSelect = reportsSelect;
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, final Reports.DataBean.Reasons s, final int position) {
        holder.setText(R.id.tvTitle, s.title);
        ImageView ivSelect = (ImageView) holder.itemView.findViewById(R.id.ivSelect);
        ivSelect.setImageResource(s.isSelect == true ? R.mipmap.icon_select_s : R.mipmap.icon_select_n);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportsSelect != null) {
                    if (s.isSelect == true) {
                        s.isSelect = false;
                        datas.set(position, s);
                        notifyDataSetChanged();
                        reportsSelect.select(null);
                        return;
                    }
                    reportsSelect.select(s);
                    for (int i = 0; i < datas.size(); i++) {
                        Reports.DataBean.Reasons r = datas.get(i);
                        r.isSelect = false;
                    }
                    s.isSelect = true;
                    datas.set(position, s);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public interface ReportsSelect {
        void select(Reports.DataBean.Reasons reasons);
    }

}
