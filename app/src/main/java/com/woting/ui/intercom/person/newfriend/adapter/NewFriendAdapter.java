package com.woting.ui.intercom.person.newfriend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.R;
import com.woting.common.utils.GlideUtils;
import com.woting.common.view.slidingbutton.SlidingButtonView;
import com.woting.ui.intercom.person.newfriend.model.NewFriend;

import java.util.List;

/**
 * 新的好友申请的适配器
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.SimpleHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private List<NewFriend> mData;
    private Context mContext;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu;

    public NewFriendAdapter(Context context, List<NewFriend> friendList) {
        this.mContext = context;
        this.mData = friendList;
    }

    public void changeData(List<NewFriend> friendList) {
        this.mData = friendList;
        notifyDataSetChanged();
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_new_friend, parent, false);
        return new SimpleHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(final SimpleHolder holder, int position) {
        NewFriend m = mData.get(position);
        String name = "未知";
        try {
            name = m.getApply_user().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String news = "未知";
        try {
            news = m.getApply_message();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!m.isHad_approved()) {
            holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);
            holder.tv_name.setText(name);
            holder.tv_news.setText(news);
            holder.tv_oks.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.VISIBLE);
        } else {
            holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);
            holder.tv_name.setText(name);
            holder.tv_news.setText(news);
            holder.tv_oks.setVisibility(View.VISIBLE);
            holder.tv_ok.setVisibility(View.GONE);
        }

        if (m.getApply_user() != null && m.getApply_user().getAvatar() != null && !m.getApply_user().getAvatar().equals("") && m.getApply_user().getAvatar().startsWith("http")) {
            GlideUtils.loadImageViewRound(m.getApply_user().getAvatar(), holder.img_url, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, holder.img_url, 60, 60);
        }

        holder.tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(view, n);
                }
            }
        });

        holder.tv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                }
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnClick(view, n);
            }
        });

        holder.re_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onAdapterClick(view, n);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class SimpleHolder extends RecyclerView.ViewHolder {
        public ImageView img_url;
        public TextView tv_name, tv_news, tv_ok, tv_Delete, tv_oks;
        public RelativeLayout layout_content, re_adapter;

        public SimpleHolder(View itemView) {
            super(itemView);
            tv_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            img_url = (ImageView) itemView.findViewById(R.id.img_url);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_news = (TextView) itemView.findViewById(R.id.tv_news);
            tv_oks = (TextView) itemView.findViewById(R.id.tv_oks);
            tv_ok = (TextView) itemView.findViewById(R.id.tv_ok);
            re_adapter = (RelativeLayout) itemView.findViewById(R.id.re_adapter);
            layout_content = (RelativeLayout) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(NewFriendAdapter.this);
        }
    }

    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    @Override
    public void onDownOrMove(SlidingButtonView slidingDeleteView) {
        if (menuIsOpen()) {
            if (mMenu != slidingDeleteView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    public void setOnSlidListener(IonSlidingViewClickListener listener) {
        mIDeleteBtnClickListener = listener;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onAdapterClick(View view, int position);

        void onDeleteBtnClick(View view, int position);
    }


}