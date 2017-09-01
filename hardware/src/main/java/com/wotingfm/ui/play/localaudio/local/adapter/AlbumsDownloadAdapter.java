package com.wotingfm.ui.play.localaudio.local.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 专辑合辑
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */

public class AlbumsDownloadAdapter extends CommonAdapter<FileInfo> {
    private DeleteClick deleteClick;
    private DecimalFormat df;

    public AlbumsDownloadAdapter(Context context, List<FileInfo> datas, DeleteClick deleteClick) {
        super(context, R.layout.item_albums_download, datas);
        this.deleteClick = deleteClick;
        df = new DecimalFormat("0.00");
    }

    @Override
    protected void convert(ViewHolder holder, final FileInfo s, final int position) {
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
        LinearLayout largeLabel = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
        ImageView img_play = (ImageView) holder.itemView.findViewById(R.id.img_play);

        if (s.album_logo_url != null && !s.album_logo_url.equals("")) {
            GlideUtils.loadImageViewRoundCornersMusic(s.album_logo_url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
        }
        String name;
        try {
            name = s.album_title;
        } catch (Exception e) {
            e.printStackTrace();
            name = "专辑";
        }
        tvTitle.setText(name + "");

        int count;
        try {
            count = s.count;
        } catch (Exception e) {
            e.printStackTrace();
            count = 1;
        }
        tvContent.setText("已下载" + String.valueOf(count) + "集");

        int sum;
        try {
            sum = s.sum;
        } catch (Exception e) {
            e.printStackTrace();
            sum = 1;
        }
        tvTime.setText(df.format(sum / 1000.0 / 1000.0) + "MB");

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

        // 播放本专辑
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null)
                    deleteClick.play(s);
            }
        });
    }

    public interface DeleteClick {
        void clickDelete(FileInfo s);

        void play(FileInfo s);

        void click(FileInfo s);
    }
}
