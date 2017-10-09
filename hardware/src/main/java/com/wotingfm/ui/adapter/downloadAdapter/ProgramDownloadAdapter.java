package com.wotingfm.ui.adapter.downloadAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.SinglesDownload;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 下载完成的节目列表
 */

public class ProgramDownloadAdapter extends CommonAdapter<SinglesDownload> {

    public ProgramDownloadAdapter(Context context, List<SinglesDownload> datas, DeleteClick deleteClick) {
        super(context, R.layout.item_program_download, datas);
        this.deleteClick = deleteClick;
    }

    private DeleteClick deleteClick;

    @Override
    protected void convert(ViewHolder holder, final SinglesDownload s, final int position) {
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        LinearLayout largeLabel = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);
        if (s.album_logo_url!= null && !s.album_logo_url.equals("") ) {
            GlideUtils.loadImageViewRoundCornersMusic(s.album_logo_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
        tvTitle.setText(s.single_title + "");
        tvContent.setText(s.album_title);
        tvTime.setText(s.album_play_count + "次播放");
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null) {
                    deleteClick.clickDelete(s);
                    swipeable_container.quickClose();
                }
            }
        });
        largeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null)
                    deleteClick.click(s);
            }
        });
    }

    public interface DeleteClick {
        void clickDelete(SinglesDownload s);

        void click(SinglesDownload s);
    }
}
