package com.woting.commonplat.amine;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.woting.commonplat.R;


/**
 * Created by amine on 16/3/14.
 */
public class RefreshHeaderView extends LinearLayout implements RefreshTrigger {

    private ProgressBar progressBar;

    private TextView tvRefresh;

    private int mHeight;

    private int mContainerFinalHeight;

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);

        inflate(context, R.layout.layout_irecyclerview_refresh_header_view, this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        this.mHeight = headerHeight;
        this.mContainerFinalHeight = finalHeight;
        progressBar.setIndeterminate(false);
    }

    @Override
    public void onMove(boolean finished, boolean automatic, int moved) {
        if (!finished) {
            if (moved <= mHeight) {
                tvRefresh.setText("正在刷新");
            } else {
                tvRefresh.setText("松手刷新");
            }
        }
        progressBar.setIndeterminate(false);
        if (mHeight != 0)
            progressBar.setProgress(moved / mHeight);
    }

    @Override
    public void onRefresh() {
        progressBar.setIndeterminate(true);
        tvRefresh.setText("正在刷新");
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        tvRefresh.setText("刷新成功");
    }

    @Override
    public void onReset() {

    }
}
