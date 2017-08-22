package com.woting.common.adapter.downloadAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.woting.R;
import com.woting.common.application.BSApplication;
import com.woting.common.bean.SinglesDownload;
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
        Glide.with(BSApplication.getInstance()).load(s.album_logo_url)// Glide
                .error(R.mipmap.oval_defut_other)
                .placeholder(R.mipmap.oval_defut_other)
                .into(ivPhoto);
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
