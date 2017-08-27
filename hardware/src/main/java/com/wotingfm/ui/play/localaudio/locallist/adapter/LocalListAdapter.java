package com.wotingfm.ui.play.localaudio.locallist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesDownload;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 播放历史
 */

public class LocalListAdapter extends CommonAdapter<SinglesDownload> {
    private localListClick localListClick;

    public LocalListAdapter(Context context, List<SinglesDownload> datas, localListClick localListClick) {
        super(context, R.layout.item_local_list, datas);
        this.localListClick = localListClick;
    }

    @Override
    protected void convert(ViewHolder holder, final SinglesDownload s, final int position) {

        LinearLayout labelContent = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        TextView tv_name = (TextView) holder.itemView.findViewById(R.id.tv_name);
        TextView tv_time = (TextView) holder.itemView.findViewById(R.id.tv_time);

        tv_name.setText(s.single_title);
        tv_time.setText(s.single_seconds+ "");

        labelContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localListClick != null)
                    localListClick.play(s);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localListClick != null) {
                    swipeable_container.quickClose();
                    localListClick.delete(s);
                }
            }
        });
    }


    public interface localListClick {
        void play(SinglesDownload s);

        void delete(SinglesDownload s);
    }

}
