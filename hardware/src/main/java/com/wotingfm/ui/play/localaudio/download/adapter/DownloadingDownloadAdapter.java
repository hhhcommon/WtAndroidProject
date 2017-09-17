//package com.wotingfm.ui.play.localaudio.download.adapter;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
//import com.wotingfm.R;
//import com.wotingfm.common.view.roundprogressbar.RoundProgressBar;
//import com.wotingfm.ui.play.localaudio.model.FileInfo;
//import com.zhy.adapter.recyclerview.CommonAdapter;
//import com.zhy.adapter.recyclerview.base.ViewHolder;
//
//import java.text.DecimalFormat;
//import java.util.List;
//
///**
// * 下载中
// * 作者：xinLong on 2017/6/5 13:55
// * 邮箱：645700751@qq.com
// */
//
//public class DownloadingDownloadAdapter extends CommonAdapter<FileInfo> {
//
//    private List<FileInfo> list;
//    private DecimalFormat df;
//
//    public DownloadingDownloadAdapter(Context context, List<FileInfo> datas, DeleteClick deleteClick) {
//        super(context, R.layout.item_downloading_download, datas);
//        this.deleteClick = deleteClick;
//        this.list = datas;
//        df = new DecimalFormat("0.00");
//    }
//
//    private DeleteClick deleteClick;
//
//    @Override
//    protected void convert(ViewHolder holder, final FileInfo s, final int position) {
//        final SwipeMenuLayout swipeable_container = (SwipeMenuLayout) holder.itemView.findViewById(R.id.swipeable_container);
//        TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvTitle);
//        TextView tvContent = (TextView) holder.itemView.findViewById(R.id.tvContent);
//        LinearLayout largeLabel = (LinearLayout) holder.itemView.findViewById(R.id.largeLabel);
//        TextView btnDelete = (TextView) holder.itemView.findViewById(R.id.btnDelete);
//        RoundProgressBar  mRoundProgressBar = (RoundProgressBar) holder.itemView.findViewById(R.id.roundProgressBar);
//        TextView textStart = (TextView) holder.itemView.findViewById(R.id.download_start);// 已下载文件大小
//        TextView textEnd = (TextView) holder.itemView.findViewById(R.id.download_end);// 文件总大小
//        ImageView img_type = (ImageView) holder.itemView.findViewById(R.id.img_type);//
//
//
//        tvTitle.setText(s.single_title + "");
//        tvContent.setText(s.album_title);
//        //  0为未下载 1为下载中,2暂停状态
//        String downLoadType = s.download_type;
//        if (!TextUtils.isEmpty(downLoadType) && downLoadType.trim().equals("1")) {
//            img_type.setVisibility(View.VISIBLE);
//            img_type.setImageResource(R.mipmap.create_group_icon_selected_s);
//        } else if (!TextUtils.isEmpty(downLoadType) && downLoadType.trim().equals("2")) {
//            img_type.setVisibility(View.VISIBLE);
//            img_type.setImageResource(R.mipmap.create_group_icon_selected_n);
//        } else {// 未下载
//            img_type.setVisibility(View.GONE);
//        }
//
//        // 文件总大小
//        String endString;
//        int end = s.end;
//        if (end >= 0) {
//            endString = df.format(end / 1000.0 / 1000.0) + "MB";
//        } else {
//            endString = df.format(0 / 1000.0 / 1000.0) + "MB";
//        }
//        textEnd.setText(endString);
//
//        // 已下载文件大小
//        int start = s.start;
//        if (start >= 0) {
//            float a = (float) start;
//            float b = (float) end;
//            String c = df.format(a / b);
//            int d = (int) (Float.parseFloat(c) * 100);
//            Log.e("当前进度",""+d);
//            mRoundProgressBar.setProgress(d);
//            textStart.setText(df.format(start / 1000.0 / 1000.0) + "MB/");
//        } else {
//            mRoundProgressBar.setProgress(0);
//            textStart.setText(df.format(0 / 1000.0 / 1000.0) + "MB/");
//        }
//        mRoundProgressBar.setMax(100);
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (deleteClick != null) {
//                    deleteClick.clickDelete(s);
//                    swipeable_container.quickClose();
//                }
//            }
//        });
//        largeLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (deleteClick != null)
//                    deleteClick.click(s);
//            }
//        });
//    }
//
//    // 更改时间进度
//    public void updateProgress(String id, int start, int end) {
//        int _id = 0;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).id.trim().equals(id)) {
//                _id = i;
//                break;
//            }
//        }
//        if (list != null && list.size() != 0) {
//            FileInfo fileInfo = list.get(_id);
//            fileInfo.finished = String.valueOf((start / end));
//            fileInfo.start = start;
//            fileInfo.end = end;
//            Log.e("adapter===",""+start);
//            Log.e("adapter===",""+end);
//
//            notifyDataSetChanged();
//        }
//    }
//
//
//    public interface DeleteClick {
//        void clickDelete(FileInfo s);
//
//        void click(FileInfo s);
//    }
//}
