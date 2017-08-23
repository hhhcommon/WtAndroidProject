package com.wotingfm.common.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.ui.bean.HomeBanners;

import java.util.ArrayList;
import java.util.List;

/**
 * banner
 * <p>
 * Created by amine on 2017/6/21.
 */

public class BannerView extends FrameLayout {
    private RecyclerView mRecyclerView;
    private BannerAdapter mAdapter;

    private LinearLayout mIndicatorContainer;
    private GradientDrawable mDefault, mSelected;

    private Handler mHandler = new Handler();
    private int mPageIndex;
    private long mDuring;

    private int mWidth, mHeight;


    private Runnable mTurningRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mTurningRunnable, mDuring);
            if (!isShown()) {
                return;
            }
            int itemCount = mAdapter.mBanners.size();
            if (itemCount == 0) {
                return;
            }
            mPageIndex = mPageIndex + 1;
            mRecyclerView.smoothScrollToPosition(mPageIndex);
            changeIndicator(mPageIndex);
        }
    };

    public BannerView(@NonNull Context context) {
        this(context, null);

    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new BannerAdapter();
        mRecyclerView = new RecyclerView(context);
        mIndicatorContainer = new LinearLayout(context);


        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    mPageIndex = manager.findFirstCompletelyVisibleItemPosition();
                    changeIndicator(mPageIndex);
                    startTurning(mDuring);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    stopTurning();
                }
            }
        });

        int size = DementionUtil.dip2px(context, 8);

        mIndicatorContainer.setPadding(size, size, size, size);
        mIndicatorContainer.setGravity(Gravity.CENTER);
        addView(mIndicatorContainer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));


        mDefault = new GradientDrawable();
        mDefault.setSize(size, size);
        mDefault.setColor(0x7bffffff);
        mDefault.setCornerRadius(size);
        mSelected = new GradientDrawable();
        mSelected.setSize(size, size);
        mSelected.setColor(0xffffffff);
        mSelected.setCornerRadius(size);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void setData(List<HomeBanners.DataBean.BannersBean> banners) {
        mIndicatorContainer.removeAllViews();
        for (int i = 0; i < banners.size(); i++) {
            ImageView img = new ImageView(getContext());
            img.setImageDrawable(i == 0 ? mSelected : mDefault);
            int size = DementionUtil.dip2px(getContext(), 8);
            img.setPadding(size / 3, 0, size / 3, 0);
            mIndicatorContainer.addView(img, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        mAdapter.mBanners.addAll(banners);
        mAdapter.notifyDataSetChanged();
        mPageIndex = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % banners.size();

        mRecyclerView.scrollToPosition(mPageIndex);
    }

    public void startTurning(long during) {
        this.mDuring = during;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mTurningRunnable, during);
    }

    public void stopTurning() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTurning(mDuring);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTurning();
    }

    private class BannerAdapter extends RecyclerView.Adapter<PagerHolder> {

        private List<HomeBanners.DataBean.BannersBean> mBanners;
        private OnItemClickListener mOnItemClickListener;

        BannerAdapter() {
            mBanners = new ArrayList<>();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public PagerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, parent, false);
            final PagerHolder holder = new PagerHolder(view);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        if (mBanners != null && !mBanners.isEmpty()) {
                            int position = holder.getAdapterPosition() % mBanners.size();
                            HomeBanners.DataBean.BannersBean banner = mBanners.get(position);
                            mOnItemClickListener.onItemClick(v, position, banner);
                        }
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(final PagerHolder holder, final int position) {
            if (mBanners != null && !mBanners.isEmpty()) {
                final int realPosition = position % mBanners.size();
                final HomeBanners.DataBean.BannersBean data = mBanners.get(realPosition);
                Context context = holder.itemView.getContext();
                holder.mImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(data.logo_url).into(holder.mImageView);
            }
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public void onViewRecycled(PagerHolder holder) {
            super.onViewRecycled(holder);
        }

        @Override
        public void onViewAttachedToWindow(PagerHolder holder) {
            super.onViewAttachedToWindow(holder);
            if (mBanners != null && !mBanners.isEmpty()) {
                int index = holder.getAdapterPosition() % mBanners.size();
                if (index < 0 || index >= mBanners.size()) {
                    return;
                }
            }
        }

        @Override
        public void onViewDetachedFromWindow(PagerHolder holder) {
            super.onViewDetachedFromWindow(holder);
            if (mBanners != null && !mBanners.isEmpty()) {
                int index = holder.getAdapterPosition() % mBanners.size();
                if (index < 0 || index >= mBanners.size()) {
                    return;
                }
            }
        }
    }

    private static class PagerHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;


        PagerHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.home_banner_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, HomeBanners.DataBean.BannersBean banner);
    }

    private void changeIndicator(int i) {
        int size = mIndicatorContainer.getChildCount();
        i = i % size;
        for (int child = 0; child < size; child++) {
            ImageView view = (ImageView) mIndicatorContainer.getChildAt(child);
            view.setImageDrawable(child == i ? mSelected : mDefault);
        }
    }

}
