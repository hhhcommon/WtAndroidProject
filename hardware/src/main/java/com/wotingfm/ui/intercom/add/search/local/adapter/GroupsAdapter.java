package com.wotingfm.ui.intercom.add.search.local.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 群组的适配器
 * 作者：xinLong on 2017/6/8 14:36
 * 邮箱：645700751@qq.com
 */
public class GroupsAdapter extends BaseAdapter{
    private List<Contact.group> list;
    private Context context;
    private OnListener onListener;

    public GroupsAdapter(Context context, List<Contact.group> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Contact.group> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_groups, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.image);
            holder.img_chat = (ImageView) convertView.findViewById(R.id.img_chat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact.group lists = list.get(position);

        if (lists.getTitle() == null || lists.getTitle().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getTitle());//名
        }

//        if (lists.avatar == null || lists.avatar.equals("") || lists.avatar.equals("null") || lists.avatar.trim().equals("")) {
//            Bitmap bmp = BitmapUtils.readBitMap(context, R.mipmap.wt_image_tx_qz);
//            holder.img_touXiang.setImageBitmap(bmp);
//        } else {
//            String url;
//            if (lists.avatar.startsWith("http:")) {
//                url = lists.avatar;
//            } else {
//                url = GlobalConfig.imageurl + lists.avatar;
//            }
//            // 加载图片
//            AssembleImageUrlUtils.loadImage(_url, url, holder.img_touXiang, IntegerConstant.TYPE_GROUP);
//        }

        holder.img_chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onListener.add(position);
            }
        });
        return convertView;
    }

    public interface OnListener {
        public void add(int position);
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView img_touXiang;
        public ImageView img_chat;

        public LinearLayout indexLayout;
        public TextView indexTv;
    }

}
