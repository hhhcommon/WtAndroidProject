package com.wotingfm.ui.intercom.main.chat.adapter;

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
import com.wotingfm.ui.intercom.main.chat.model.TalkHistory;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.SimpleHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private List<TalkHistory> t;
    private Context mContext;

    private IonSlidingViewClickListener clickListener;
    private SlidingButtonView mMenu;

    public ChatAdapter(Context context, List<TalkHistory> friendList) {
        mContext = context;
        t = friendList;
    }

    public void ChangeData( List<TalkHistory> friendList) {
        t = friendList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return t.size();
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chat, parent, false);
        return new SimpleHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(final SimpleHolder holder, int position) {
        holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);
        String name="";
        try {
            name=t.get(position).getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_name.setText(name);
        if (t.get(position).getURL()!= null && !t.get(position).getURL().equals("")) {
            GlideUtils.loadImageViewSize(mContext, t.get(position).getURL(), 60, 60, holder.img_url, true);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(mContext, R.mipmap.icon_avatar_d);
            holder.img_url.setImageBitmap(bmp);
        }
        holder.img_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    clickListener.onItemClick(view, n);
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
                clickListener.onDeleteBtnClick(view, n);
            }
        });
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                clickListener.onClick(view, n);
            }
        });

    }

    class SimpleHolder extends RecyclerView.ViewHolder {
        public ImageView img_url,img_voice;
        public TextView tv_Delete;
        public TextView tv_name, tv_news;
        public RelativeLayout layout_content;

        public SimpleHolder(View itemView) {
            super(itemView);
            tv_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            img_url = (ImageView) itemView.findViewById(R.id.img_url);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_news = (TextView) itemView.findViewById(R.id.tv_news);
            img_voice = (ImageView) itemView.findViewById(R.id.img_voice);
            layout_content = (RelativeLayout) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(ChatAdapter.this);
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
        clickListener = listener;
    }

    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);
        void onClick(View view, int position);
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