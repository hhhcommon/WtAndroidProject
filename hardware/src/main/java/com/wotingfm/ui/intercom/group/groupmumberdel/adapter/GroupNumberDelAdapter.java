package com.wotingfm.ui.intercom.group.groupmumberdel.adapter;

import android.content.Context;
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

import com.wotingfm.R;
import com.wotingfm.common.view.slidingbutton.SlidingButtonView;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriend;

import java.util.List;

/**
 * 新的好友申请的适配器
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class GroupNumberDelAdapter extends RecyclerView.Adapter<GroupNumberDelAdapter.SimpleHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private final List<Contact.user> mData;
    private Context mContext;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu;

    public GroupNumberDelAdapter(Context context, List<Contact.user> friendList) {
        mContext = context;
        mData = friendList;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_group_num_del, parent, false);
        return new SimpleHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(final SimpleHolder holder, int position) {

        holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);
        holder.tv_name.setText(mData.get(position).getName());

        holder.tv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        public TextView tv_name, tv_Delete;
        public RelativeLayout layout_content, re_adapter;

        public SimpleHolder(View itemView) {
            super(itemView);
            tv_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            img_url = (ImageView) itemView.findViewById(R.id.img_url);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            re_adapter = (RelativeLayout) itemView.findViewById(R.id.re_adapter);
            layout_content = (RelativeLayout) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(GroupNumberDelAdapter.this);
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