package com.wotingfm.ui.adapter.downloadAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.bean.SinglesDownload;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amine on 2017/6/7.
 * 专辑合辑
 */

public class AlbumsDownloadAdapter extends CommonAdapter<SinglesDownload> {
    private HashMap<String, SinglesDownload> hashMap;
    private HashMap<String, List<SinglesDownload>> hashMapList;

    public AlbumsDownloadAdapter(Context context, List<SinglesDownload> datas, HashMap<String, SinglesDownload> hashMap
            , HashMap<String, List<SinglesDownload>> hashMapList, DeleteClick deleteClick) {
        super(context, R.layout.item_albums_download, datas);
        this.hashMap = hashMap;
        this.hashMapList = hashMapList;
        this.deleteClick = deleteClick;
    }

    private DeleteClick deleteClick;

    @Override
    protected void convert(ViewHolder holder, final SinglesDownload s, final int position) {
        final SinglesDownload sb = getMapContent(s.album_id);
        if (sb != null) {
            final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
            ImageView ivPhoto = (ImageView) holder.itemView.findViewById(R.id.ivPhoto);
            LinearLayout largeLabel = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);
            TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
            TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
            TextView tvTime = (TextView) holder.itemView.findViewById(R.id.tvTime);
            Glide.with(BSApplication.getInstance()).load(sb.album_logo_url)// Glide
                    .error(R.mipmap.oval_defut_other)
                    .placeholder(R.mipmap.oval_defut_other)
                    .into(ivPhoto);
            tvTitle.setText(sb.album_title + "");
            final List<SinglesDownload> downloads = getMapContentList(sb.album_id);
            if (downloads.isEmpty()) {
                tvContent.setText("已下载1集");
            } else {
                tvContent.setText("已下载" + downloads.size() + "集");
            }
            tvTime.setText(sb.albumSize + "M");
            TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteClick != null) {
                        deleteClick.clickDelete(downloads, sb);
                        swipeable_container.quickClose();
                    }
                }
            });
            largeLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteClick != null)
                        deleteClick.click(downloads);
                }
            });
        }
    }

    public SinglesDownload getMapContent(String key) {
        if (hashMap == null && hashMap.isEmpty())
            return null;
        if (hashMap.containsKey(key)) {
            return hashMap.get(key);
        } else {
            return null;
        }
    }

    public List<SinglesDownload> getMapContentList(String key) {
        if (hashMapList == null && hashMapList.isEmpty())
            return new ArrayList<>();
        if (hashMapList.containsKey(key)) {
            return hashMapList.get(key);
        } else {
            return new ArrayList<>();
        }
    }

    public interface DeleteClick {
        void clickDelete(List<SinglesDownload> singlesDownloads, SinglesDownload s);

        void click(List<SinglesDownload> singlesDownloads);
    }
}
