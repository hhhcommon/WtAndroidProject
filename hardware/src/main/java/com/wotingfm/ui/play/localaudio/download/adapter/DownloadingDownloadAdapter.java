package com.wotingfm.ui.play.localaudio.download.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.bean.SinglesDownload;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 下载中
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */

public class DownloadingDownloadAdapter extends CommonAdapter<SinglesDownload> {

    public DownloadingDownloadAdapter(Context context, List<SinglesDownload> datas, DeleteClick deleteClick) {
        super(context, R.layout.item_downloading_download, datas);
        this.deleteClick = deleteClick;
    }

    private DeleteClick deleteClick;

    @Override
    protected void convert(ViewHolder holder, final SinglesDownload s, final int position) {
        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
        RelativeLayout largeLabel = (RelativeLayout) holder.itemView.findViewById(R.id.largeLabel);
        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);

        tvTitle.setText(s.single_title + "");
        tvContent.setText(s.album_title);

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
