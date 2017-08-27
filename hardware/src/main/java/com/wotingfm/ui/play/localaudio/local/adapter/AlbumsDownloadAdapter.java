package com.wotingfm.ui.play.localaudio.local.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.bean.SinglesDownload;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 专辑合辑
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */

public class AlbumsDownloadAdapter extends CommonAdapter<SinglesDownload> {
    private HashMap<String, SinglesDownload> hashMap;
    private HashMap<String, List<SinglesDownload>> hashMapList;
    private DeleteClick deleteClick;

    public AlbumsDownloadAdapter(Context context, List<SinglesDownload> datas, HashMap<String, SinglesDownload> hashMap
            , HashMap<String, List<SinglesDownload>> hashMapList, DeleteClick deleteClick) {
        super(context, R.layout.item_albums_download, datas);
        this.hashMap = hashMap;
        this.hashMapList = hashMapList;
        this.deleteClick = deleteClick;
    }

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
            TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
            ImageView img_play = (ImageView) holder.itemView.findViewById(R.id.img_play);

            if (sb.album_logo_url != null && !sb.album_logo_url.equals("") ) {
                GlideUtils.loadImageViewRoundCornersMusic(sb.album_logo_url, ivPhoto, 150, 150);
            } else {
                GlideUtils.loadImageViewRoundCornersMusic(R.mipmap.oval_defut_other, ivPhoto, 60, 60);
            }
             String name;
            try {
                 name=sb.album_title;
            } catch (Exception e) {
                e.printStackTrace();
                name="专辑";
            }
            tvTitle.setText(name + "");
            final List<SinglesDownload> downloads = getMapContentList(sb.album_id);
            if (downloads.isEmpty()) {
                tvContent.setText("已下载1集");
            } else {
                tvContent.setText("已下载" + downloads.size() + "集");
            }
            tvTime.setText(sb.albumSize + "M");

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
                        deleteClick.click(downloads,sb.album_title);
                }
            });

            // 播放本专辑
            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteClick != null)
                        deleteClick.play(downloads);
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
        void play(List<SinglesDownload> singlesDownloads);
        void click(List<SinglesDownload> singlesDownloads,String name);
    }
}
