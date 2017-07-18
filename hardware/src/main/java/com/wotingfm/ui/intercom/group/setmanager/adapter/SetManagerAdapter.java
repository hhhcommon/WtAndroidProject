package com.wotingfm.ui.intercom.group.setmanager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 设置管理员适配器
 * 作者：xinLong on 2017/6/8 14:36
 * 邮箱：645700751@qq.com
 */
public class SetManagerAdapter extends BaseAdapter {
    private List<Contact.user> list;
    private Context context;

    public SetManagerAdapter(Context context, List<Contact.user> list) {
        super();
        this.list = list;
        this.context = context;
    }

    public void ChangeDate(List<Contact.user> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_setmanager, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);//名
            holder.img_touXiang = (ImageView) convertView.findViewById(R.id.img_view);
            holder.img_choose = (ImageView) convertView.findViewById(R.id.img_choose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact.user lists = list.get(position);

        if (lists.getName() == null || lists.getName().equals("")) {
            holder.tv_name.setText("名称");//名
        } else {
            holder.tv_name.setText(lists.getName());//名
        }

        if (!lists.is_admin()) {
            holder.img_choose.setImageResource(R.mipmap.icon_select_n);
        } else {
            holder.img_choose.setImageResource(R.mipmap.icon_select_s);
        }

        if (lists.getAvatar() != null && !lists.getAvatar().equals("")&&lists.getAvatar().startsWith("http")) {
            GlideUtils.loadImageViewSize(context, lists.getAvatar(), 60, 60, holder.img_touXiang, true);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(context, R.mipmap.icon_avatar_d);
            holder.img_touXiang.setImageBitmap(bmp);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView img_touXiang;
        public ImageView img_choose;
    }

}
