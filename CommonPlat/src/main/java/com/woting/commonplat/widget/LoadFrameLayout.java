package com.woting.commonplat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.woting.commonplat.R;


/**
 * Created by amine on 16/2/4.
 */
public class LoadFrameLayout extends FrameLayout {
    @LayoutRes
    private int mEmptyViewLayoutResId;
    @LayoutRes
    private int mErrorViewLayoutResId;
    @LayoutRes
    private int mLoadingViewLayoutResId;
    @LayoutRes
    private int mNoWifiData;
    @LayoutRes
    private int mNoOrderData;

    private int mContentViewId;

    private View emptyView;

    private View errorView;

    private View loadingView;

    private View contentView;

    private View noWifiDataView;

    private View noOrder;

    public LoadFrameLayout(Context context) {
        this(context, null);
    }

    public LoadFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundColor(getResources().getColor(R.color.white));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadFrameLayout, defStyleAttr, 0);
        try {
            mEmptyViewLayoutResId = a.getResourceId(R.styleable.LoadFrameLayout_emptyView, -1);
            mErrorViewLayoutResId = a.getResourceId(R.styleable.LoadFrameLayout_errorView, -1);
            mLoadingViewLayoutResId = a.getResourceId(R.styleable.LoadFrameLayout_loadingView, -1);
            mNoWifiData = a.getResourceId(R.styleable.LoadFrameLayout_nowifiView, -1);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);

        if (mEmptyViewLayoutResId != -1) {
            setEmptyView(mEmptyViewLayoutResId);
        }

        if (mErrorViewLayoutResId != -1) {
            setErrorView(mErrorViewLayoutResId);
        }
        if (mLoadingViewLayoutResId != -1) {
            setLoadingView(mLoadingViewLayoutResId);
        }
    }

    public void setEmptyView(View emptyView) {
        if (this.emptyView != emptyView) {
            if (this.emptyView != null) {
                removeView(this.emptyView);
            }
            this.emptyView = emptyView;
            addView(this.emptyView);
        }

    }

    public void setErrorView(View errorView) {
        if (this.errorView != null) {
            removeView(this.errorView);
        }
        if (this.errorView != errorView) {
            this.errorView = errorView;
            addView(errorView);
            this.errorView.setVisibility(GONE);
        }
    }


    public void setNoWifiView(View noWifiDataView) {
        if (this.noWifiDataView != null) {
            removeView(this.noWifiDataView);
        }
        if (this.noWifiDataView != noWifiDataView) {
            this.noWifiDataView = noWifiDataView;
            addView(noWifiDataView);
            this.noWifiDataView.setVisibility(GONE);
        }
    }

    public void setLoadingView(View loadingView) {
        if (this.loadingView != null) {
            removeView(this.loadingView);
        }
        if (this.loadingView != loadingView) {
            this.loadingView = loadingView;
            addView(loadingView);
            this.loadingView.setVisibility(GONE);
        }
    }

    public void setContentView(View contentView) {
        if (this.contentView != null) {
            removeView(this.contentView);
        }
        if (this.contentView != contentView) {
            this.contentView = contentView;
            addView(contentView);
        }
    }

    public void setEmptyView(@LayoutRes int emptyViewResId) {
        View view = LayoutInflater.from(getContext()).inflate(emptyViewResId, this, false);
        setEmptyView(view);
    }


    public void setErrorView(@LayoutRes int errorViewResId) {
        View view = LayoutInflater.from(getContext()).inflate(errorViewResId, this, false);
        setErrorView(view);
    }


    public void setLoadingView(@LayoutRes int loadingViewResId) {
        View view = LayoutInflater.from(getContext()).inflate(loadingViewResId, this, false);
        setLoadingView(view);
    }

    public void setContentView(@LayoutRes int contentViewResId) {
        View view = LayoutInflater.from(getContext()).inflate(contentViewResId, this, false);
        setContentView(view);
    }

    public void showEmptyView() {
        showSingleView(this.emptyView);
    }
    public View showEmptyViewOder() {
        showSingleView(this.emptyView);
        return emptyView;
    }
    public void showErrorView() {
        showSingleView(this.errorView);
    }

    public void showNoWifi() {
        showSingleView(this.noWifiDataView);
    }

    public void showLoadingView() {
        showSingleView(this.loadingView);
    }

    public void showContentView() {
        showSingleView(this.contentView);
    }

    private void showSingleView(View specialView) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == specialView) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }
}
