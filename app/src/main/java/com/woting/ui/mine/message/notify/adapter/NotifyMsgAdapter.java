package com.woting.ui.mine.message.notify.adapter;

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
import com.woting.common.utils.TimeUtil;
import com.woting.common.view.slidingbutton.SlidingButtonView;
import com.woting.ui.mine.message.notify.model.Msg;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/12 13:42
 * 邮箱：645700751@qq.com
 */

public class NotifyMsgAdapter extends RecyclerView.Adapter<NotifyMsgAdapter.SimpleHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private List<Msg> mData;
    private Context mContext;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu;

    public NotifyMsgAdapter(Context context, List<Msg> Msg) {
        this.mContext = context;
        this.mData = Msg;
    }

    public void changeData(List<Msg> Msg) {
        this.mData = Msg;
        notifyDataSetChanged();
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_msg, parent, false);
        return new SimpleHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(final SimpleHolder holder, int position) {
        holder.layout_content.getLayoutParams().width = getScreenWidth(mContext);

        Msg m = mData.get(position);
        if (m.getMsg_type() != null && m.getMsg_type().equals("1")) {
            holder.tv_introduce.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.GONE);
            holder.tv_oks.setVisibility(View.GONE);
        } else if (m.getMsg_type() != null && m.getMsg_type().equals("2")) {
            holder.tv_introduce.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.GONE);
            holder.tv_oks.setVisibility(View.GONE);
        } else if (m.getMsg_type() != null && m.getMsg_type().equals("3")) {
            holder.tv_introduce.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.GONE);
            holder.tv_oks.setVisibility(View.GONE);
        } else if (m.getMsg_type() != null && m.getMsg_type().equals("4")) {
            holder.tv_introduce.setVisibility(View.VISIBLE);
            if (m.getIntroduce() != null && !m.getIntroduce().equals("")) {
                holder.tv_introduce.setText(m.getIntroduce());
            } else {
                holder.tv_introduce.setText("验证消息");
            }

            if (m.getStatus() != null && m.getStatus().equals("2")) {
                holder.tv_ok.setVisibility(View.VISIBLE);
                holder.tv_oks.setVisibility(View.GONE);
            } else if (m.getStatus() != null && m.getStatus().equals("1")) {
                holder.tv_ok.setVisibility(View.GONE);
                holder.tv_oks.setVisibility(View.VISIBLE);
            } else {
                holder.tv_ok.setVisibility(View.GONE);
                holder.tv_oks.setVisibility(View.GONE);
            }

        }else if (m.getMsg_type() != null && m.getMsg_type().equals("5")) {
            // 群主邀请好友入群
            holder.tv_introduce.setVisibility(View.GONE);
            if (m.getStatus() != null && m.getStatus().equals("2")) {
                holder.tv_ok.setVisibility(View.VISIBLE);
                holder.tv_oks.setVisibility(View.GONE);
            } else if (m.getStatus() != null && m.getStatus().equals("1")) {
                holder.tv_ok.setVisibility(View.GONE);
                holder.tv_oks.setVisibility(View.VISIBLE);
            } else {
                holder.tv_ok.setVisibility(View.GONE);
                holder.tv_oks.setVisibility(View.GONE);
            }
        }
        if (m.getAvatar() != null && !m.getAvatar().equals("") && m.getAvatar().startsWith("http")) {
            GlideUtils.loadImageViewRound(m.getAvatar(), holder.img_url, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, holder.img_url, 60, 60);
        }
        if (m.getTime() != null && !m.getTime().equals("")) {
            holder.tv_time.setText(TimeUtil.stampToDateForH(m.getTime()));
        } else {
            holder.tv_time.setText("00:00");
        }
        if (m.getTitle() != null && !m.getTitle().equals("")) {
            holder.tv_title.setText(m.getTitle());
        } else {
            holder.tv_title.setText("我听");
        }
        if (m.getNews() != null && !m.getNews().equals("")) {
            holder.tv_news.setText(m.getNews());
        } else {
            holder.tv_news.setText("我听");
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
        public RelativeLayout lin_img;
        public TextView tv_title, tv_news, tv_ok, tv_Delete, tv_oks, tv_introduce, tv_time;
        public RelativeLayout layout_content, re_adapter;

        public SimpleHolder(View itemView) {
            super(itemView);
            tv_Delete = (TextView) itemView.findViewById(R.id.tv_delete);// 删除按钮
            img_url = (ImageView) itemView.findViewById(R.id.img_url);// 头像
            lin_img = (RelativeLayout) itemView.findViewById(R.id.lin_img);// 头像背景图

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_news = (TextView) itemView.findViewById(R.id.tv_news);
            tv_introduce = (TextView) itemView.findViewById(R.id.tv_introduce);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

            tv_oks = (TextView) itemView.findViewById(R.id.tv_oks);// 已同意
            tv_ok = (TextView) itemView.findViewById(R.id.tv_ok);// 同意

            re_adapter = (RelativeLayout) itemView.findViewById(R.id.re_adapter);
            layout_content = (RelativeLayout) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(NotifyMsgAdapter.this);
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

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}