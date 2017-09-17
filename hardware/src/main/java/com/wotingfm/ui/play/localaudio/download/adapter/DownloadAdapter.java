package com.wotingfm.ui.play.localaudio.download.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.wotingfm.R;
import com.wotingfm.common.view.roundprogressbar.RoundProgressBar;
import com.wotingfm.ui.play.localaudio.model.FileInfo;

import java.text.DecimalFormat;
import java.util.List;

public class DownloadAdapter extends BaseAdapter {
    private List<FileInfo> list;
    private Context context;
    private DecimalFormat df;
    private DeleteClick deleteClick;

    public DownloadAdapter(Context context, List<FileInfo> list) {
        this.context = context;
        this.list = list;
        df = new DecimalFormat("0.00");
    }

    public void setOnListener(DeleteClick downloadCheck) {
        this.deleteClick = downloadCheck;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_downloading_download, null);
            holder.swipeable_container = (SwipeMenuLayout) convertView.findViewById(R.id.swipeable_container);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.largeLabel = (LinearLayout) convertView.findViewById(R.id.largeLabel);
            holder.btnDelete = (TextView) convertView.findViewById(R.id.btnDelete);
            holder.mRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar);
            holder.textStart = (TextView) convertView.findViewById(R.id.download_start);// 已下载文件大小
            holder.textEnd = (TextView) convertView.findViewById(R.id.download_end);// 文件总大小
            holder.img_type = (ImageView) convertView.findViewById(R.id.img_type);//
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setData(holder, position);
        return convertView;
    }


    /**
     * 设置viewHolder的数据
     *
     * @param holder
     * @param itemIndex
     */
    private void setData(final ViewHolder holder, int itemIndex) {
       final FileInfo s = list.get(itemIndex);
        holder.tvTitle.setText(s.single_title + "");
        holder.tvContent.setText(s.album_title);
        //  0为未下载 1为下载中,2暂停状态
        String downLoadType = s.download_type;
        if (!TextUtils.isEmpty(downLoadType) && downLoadType.trim().equals("1")) {
            holder.img_type.setVisibility(View.VISIBLE);
            holder.img_type.setImageResource(R.mipmap.create_group_icon_selected_s);
        } else if (!TextUtils.isEmpty(downLoadType) && downLoadType.trim().equals("2")) {
            holder.img_type.setVisibility(View.VISIBLE);
            holder.img_type.setImageResource(R.mipmap.create_group_icon_selected_n);
        } else {// 未下载
            holder.img_type.setVisibility(View.GONE);
        }

        // 文件总大小
        String endString;
        int end = s.end;
        if (end >= 0) {
            endString = df.format(end / 1000.0 / 1000.0) + "MB";
        } else {
            endString = df.format(0 / 1000.0 / 1000.0) + "MB";
        }
        holder.textEnd.setText(endString);

        // 已下载文件大小
        int start = s.start;
        if (start >= 0) {
            float a = (float) start;
            float b = (float) end;
            String c = df.format(a / b);
            int d = (int) (Float.parseFloat(c) * 100);
            Log.e("当前进度", "" + d);
            holder.mRoundProgressBar.setProgress(d);
            holder.textStart.setText(df.format(start / 1000.0 / 1000.0) + "MB/");
        } else {
            holder.mRoundProgressBar.setProgress(0);
            holder.textStart.setText(df.format(0 / 1000.0 / 1000.0) + "MB/");
        }
        holder.mRoundProgressBar.setMax(100);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null) {
                    deleteClick.clickDelete(s);
                    holder.swipeable_container.quickClose();
                }
            }
        });
        holder.largeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null)
                    deleteClick.click(s);
            }
        });
    }

    /**
     * 局部刷新
     * @param convertView
     * @param itemIndex
     */
    public void updateView(View convertView, int itemIndex) {
        if(convertView == null) {
            return;
        }
        //从view中取得holder
        ViewHolder holder = (ViewHolder)convertView.getTag();
        holder.swipeable_container = (SwipeMenuLayout) convertView.findViewById(R.id.swipeable_container);
        holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        holder.largeLabel = (LinearLayout) convertView.findViewById(R.id.largeLabel);
        holder.btnDelete = (TextView) convertView.findViewById(R.id.btnDelete);
        holder.mRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar);
        holder.textStart = (TextView) convertView.findViewById(R.id.download_start);// 已下载文件大小
        holder.textEnd = (TextView) convertView.findViewById(R.id.download_end);// 文件总大小
        holder.img_type = (ImageView) convertView.findViewById(R.id.img_type);//
        setData(holder, itemIndex);
    }

    public interface DeleteClick {
        void clickDelete(FileInfo s);

        void click(FileInfo s);
    }

    private class ViewHolder {

        public TextView tvTitle;
        public TextView tvContent;
        public TextView btnDelete;
        public TextView textStart;
        public TextView textEnd;
        public ImageView img_type;
        public LinearLayout largeLabel;
        public RoundProgressBar mRoundProgressBar;
        public SwipeMenuLayout swipeable_container;
    }
}
