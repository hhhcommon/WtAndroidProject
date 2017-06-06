package com.woting.commonplat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;

/**
 * Created by amine on 16/1/18.
 */
public class WTToolbar extends TitleToolbar {

    private boolean mShowDivider;

    private Paint mPaint;

    public WTToolbar(Context context) {
        super(context, null);
        init();
    }

    public WTToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WTToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
    }

    public void setShowDivider(boolean isShow) {
        this.mShowDivider = isShow;
        invalidate();
    }


    public void setDividerColor(@ColorInt int color) {
        mPaint.setColor(color);
    }


    /**
     * @param alpha [0..255]
     */
    public void setDividerAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShowDivider) {
            canvas.save();
            canvas.drawLine(0, getHeight() - 1.5f, getWidth(), getHeight() - 1.5f, mPaint);
        }
    }
}
