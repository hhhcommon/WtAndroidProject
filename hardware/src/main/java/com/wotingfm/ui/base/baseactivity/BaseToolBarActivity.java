package com.wotingfm.ui.base.baseactivity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.woting.commonplat.widget.WTToolbar;
import com.wotingfm.R;


/**
 * Created by amine on 16/1/28.
 */
public abstract class BaseToolBarActivity extends BaseActivity implements View.OnClickListener {
    public Toolbar toolbar;

    @Override
    public void onClick(View view) {

    }

    protected WTToolbar o2Toolbar;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (WTToolbar) findViewById(R.id.toolbar);
        callBack = null;
        o2Toolbar = (WTToolbar) toolbar;
        initToolBar(o2Toolbar);
    }

    public boolean IS_BACK = false;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        o2Toolbar = (WTToolbar) toolbar;
        initToolBar(o2Toolbar);
    }

    public interface CallBack {
        void call();
    }

    private CallBack callBack;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && IS_BACK == false) {
            if (callBack != null)
                callBack.call();
            else
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initToolBar(WTToolbar toolbar) {
        if (toolbar != null) {
            toolbar.setDividerColor(getResources().getColor(R.color.white));
            toolbar.setShowDivider(true);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.mipmap.nav_icon_back_black);
                actionBar.setTitle("");
            }
        }
    }

    protected void setHomeAsUpIndicatorColor(@ColorInt int color) {
        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setTitle(CharSequence title) {
        callBack = null;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setTitle(CharSequence title, CallBack callBackW) {
        ActionBar actionBar = getSupportActionBar();
        this.callBack = callBackW;
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setTitleNo(CharSequence title) {
        callBack = null;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
        o2Toolbar.setShowDivider(false);
    }

    public void setTitleDivider(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(null);
            o2Toolbar.setDividerColor(0);
            actionBar.setHomeAsUpIndicator(R.mipmap.nav_icon_back_black);
            actionBar.setTitle(title);
        }
    }


    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }


}
