package com.woting.commonplat.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.woting.commonplat.R;


/**
 * Created by amine on 16/1/21.
 */
public class TitleToolbar extends Toolbar {

    private TextView mTitleTextView;


    public TitleToolbar(Context context) {
        super(context, null);
        init(context, null, 0);
    }

    public TitleToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.toolbarStyle);
    }

    public TitleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.Toolbar, defStyleAttr, 0);
        a.recycle();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            if (mTitleTextView != null) {
                mTitleTextView.setText(title);
            }
        } else {
            if (mTitleTextView == null) {
                initTitleTextView();
            }
            mTitleTextView.setText(title);
        }
    }

    private void initTitleTextView() {
        Context context = getContext();
        mTitleTextView = new TextView(context);
        //mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
        LayoutParams layoutParams = generateDefaultLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        mTitleTextView.setLayoutParams(layoutParams);
        mTitleTextView.setMaxEms(10);
        mTitleTextView.setTextSize(17);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleTextView.setSingleLine(true);
        mTitleTextView.setTextColor(Color.parseColor("#ffffff"));
        addView(mTitleTextView);
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }
}
