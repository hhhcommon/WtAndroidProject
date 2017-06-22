package com.wotingfm.common.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amine on 2017/6/20.
 */

public class DoubleScrollViewPager extends ViewPager {
    public DoubleScrollViewPager(Context context) {
        super(context);
    }

    private ViewGroup parent;

    public DoubleScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int tagHeight;

    public void setTagHeight(int height) {
        tagHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (tagHeight > 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(tagHeight, View.MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setNestedpParent(ViewGroup parent) {
        this.parent = parent;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }
}
