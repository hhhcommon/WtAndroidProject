package com.woting.ui.intercom.person.personmessage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.R;
import com.woting.common.bean.AlbumsBean;
import com.woting.common.utils.GlideUtils;
import com.woting.ui.intercom.main.view.InterPhoneActivity;
import com.woting.ui.play.activity.albums.AlbumsInfoFragmentMain;

import java.util.List;

/**
 * 好友详情的订阅
 */
public class PersonMessageSubAdapter extends BaseAdapter {
    private List<AlbumsBean> list;
    private Context context;
    private InterPhoneActivity activity;

    public PersonMessageSubAdapter(Context context, List<AlbumsBean> list) {
        super();
        this.list = list;
        if (context instanceof InterPhoneActivity)
            activity = (InterPhoneActivity) context;
        this.context = context;
    }

    public void changeData(List<AlbumsBean> list) {
        this.list = list;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_personnewssub, null);
            holder.img_view = (ImageView) convertView.findViewById(R.id.img_view);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AlbumsBean sub = list.get(position);
        String name = "";
        try {
            name = sub.title;
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_name.setText(name);

        if (sub.logo_url != null && !sub.logo_url.equals("")) {
            GlideUtils.loadImageViewSrc(sub.logo_url, holder.img_view, 350,350,false, 0);
        } else {
            GlideUtils.loadImageViewSrc(R.mipmap.p, holder.img_view,350,350, false, 0);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null)
                    activity.open(AlbumsInfoFragmentMain.newInstance(sub.id));
            }
        });
        return convertView;
    }

    class ViewHolder {
        public ImageView img_view;
        public TextView tv_name;
    }
}