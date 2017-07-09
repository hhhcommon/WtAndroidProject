package com.wotingfm.ui.message.group.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.slidingbutton.SlidingButtonView;
import com.wotingfm.ui.message.group.model.GroupMsg;
import com.wotingfm.ui.message.notify.model.NotifyMsg;

import java.util.List;

/**
 *
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class GroupMsgAdapter extends RecyclerView.Adapter<GroupMsgAdapter.SimpleHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private List<GroupMsg> mData;
    private Context mContext;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu;

    public GroupMsgAdapter(Context context, List<GroupMsg> friendList) {
        this.mContext = context;
        this.mData = friendList;
    }

    public void changeData(List<GroupMsg> friendList) {
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
        GroupMsg m = mData.get(position);

        if (m.getApply_type().equals("1")) {
            holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);
            holder.tv_name.setText(m.getName());
            holder.tv_oks.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.VISIBLE);
        } else {
            holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);
            holder.tv_name.setText(m.getName());
            holder.tv_oks.setVisibility(View.VISIBLE);
            holder.tv_ok.setVisibility(View.GONE);
        }

        if (m.getAvatar() != null && !m.getAvatar().equals("")) {
            GlideUtils.loadImageViewSize(mContext, m.getAvatar(), 60, 60, holder.img_url, true);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(mContext, R.mipmap.icon_avatar_d);
            holder.img_url.setImageBitmap(bmp);
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
            ((SlidingButtonView) itemView).setSlidingButtonListener(GroupMsgAdapter.this);
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

    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onAdapterClick(View view, int position);

        void onDeleteBtnClick(View view, int position);
    }

    /**
     * dpתpx
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}