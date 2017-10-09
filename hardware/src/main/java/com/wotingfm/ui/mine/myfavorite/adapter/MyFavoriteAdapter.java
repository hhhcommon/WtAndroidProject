package com.wotingfm.ui.mine.myfavorite.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.mine.myfavorite.model.Favorite;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class MyFavoriteAdapter extends BaseAdapter {
    private List<Favorite> list;
    private Context context;

    public MyFavoriteAdapter(Context context, List<Favorite> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Favorite> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_myfavorite, null);
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.img_view);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.tv_introduce = (TextView) convertView.findViewById(R.id.tv_introduce);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Favorite lists = list.get(position);

        if (lists.getLogo_url() != null && !lists.getLogo_url().equals("") && lists.getLogo_url().startsWith("http")) {
            GlideUtils.loadImageViewRoundCorners(lists.getLogo_url(), holder.img_touXiang, 250, 250);
        } else {
            GlideUtils.loadImageViewRoundCorners(R.mipmap.p, holder.img_touXiang, 60, 60);
        }

        if (lists.getTitle() == null || lists.getTitle().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getTitle());//名
        }

        if(!TextUtils.isEmpty(lists.getLiked_type())&&lists.getLiked_type().trim().equals("single")){
            // 节目
            if (lists.getAlbum_name() == null || lists.getAlbum_name().equals("")) {
                holder.tv_introduce.setText("暂无专辑");//专辑
            } else {
                holder.tv_introduce.setText(lists.getAlbum_name());//专辑
            }
        }else{
            // 电台
            if (lists.getPlaying_title() == null || lists.getPlaying_title().equals("")) {
                holder.tv_introduce.setText("直播中");// 正在直播的节目
            } else {
                holder.tv_introduce.setText(lists.getPlaying_title());// 正在直播的节目
            }
        }

        if (lists.getPlay_count() == null || lists.getPlay_count().equals("")) {
            holder.tv_num.setText("0次播放");//播放次数
        } else {
            holder.tv_num.setText(lists.getPlay_count() + "次播放");//播放次数
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tv_name, tv_introduce, tv_num;
        public ImageView img_touXiang;
    }

}