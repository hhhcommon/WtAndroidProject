package com.wotingfm.ui.base.basefragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.woting.commonplat.widget.LoadingDialog;
import com.woting.commonplat.widget.WTToolbar;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.utils.ProgressDialogUtils;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;
import com.wotingfm.ui.play.look.activity.LookListFragment;
import com.wotingfm.ui.play.look.activity.RadioMoreFragment;
import com.wotingfm.ui.play.look.activity.SelectedMoreFragment;
import com.wotingfm.ui.play.look.activity.classification.fragment.MinorClassificationFragment;
import com.wotingfm.ui.play.look.fragment.ClassificationFragment;
import com.wotingfm.ui.play.look.fragment.LiveFragment;
import com.wotingfm.ui.play.look.fragment.RadioStationFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.test.PlayerFragment;

import butterknife.ButterKnife;

import static com.wotingfm.common.application.BSApplication.isIS_BACK;

/**
 * Created by amine on 2017/6/14.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    public Toolbar toolbar;
    private PlayerActivity playerActivity;

    @Override
    public void onClick(View view) {

    }

    protected WTToolbar o2Toolbar;


    public boolean IS_BACK = false;

    public interface CallBack {
        void call();
    }

    private BaseToolBarActivity.CallBack callBack;
    public int SerchCode = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home && IS_BACK == false) {
            hideSoftKeyboard();
            if (callBack != null) {
                callBack.call();
            } else {
                if (BSApplication.isIS_BACK == true) {
                    closeFragment();
                    BSApplication.isIS_BACK = false;
                    BSApplication.fragmentBase = null;
                    openFragmentNoAnim(LookListFragment.newInstance(0));
              /*      if (this instanceof SelectedMoreFragment) {
                        openFragmentNoAnim(LookListFragment.newInstance(0));
                    } else if (this instanceof ClassificationFragment) {
                        openFragmentNoAnim(LookListFragment.newInstance(1));
                    } else if (this instanceof RadioMoreFragment) {
                        openFragmentNoAnim(LookListFragment.newInstance(2));
                    } else if (this instanceof LiveFragment) {
                        openFragmentNoAnim(LookListFragment.newInstance(3));
                    } else if (this instanceof MinorClassificationFragment) {
                        openFragmentNoAnim(LookListFragment.newInstance(1));
                    } else {
                        openFragmentNoAnim(LookListFragment.newInstance(0));
                    }*/
                    return true;
                } else {
                    closeFragment();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initToolBar(WTToolbar toolbar) {
        if (toolbar != null && playerActivity != null) {
            toolbar.setDividerColor(getResources().getColor(R.color.line_color));
            toolbar.setShowDivider(true);
            playerActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = playerActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                setHasOptionsMenu(true);
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
        if (playerActivity != null) {
            ActionBar actionBar = playerActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    public void setTitle(CharSequence title, BaseToolBarActivity.CallBack callBackW) {
        if (playerActivity != null) {
            ActionBar actionBar = playerActivity.getSupportActionBar();
            this.callBack = callBackW;
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    public void setTitleNo(CharSequence title) {
        callBack = null;
        if (playerActivity != null) {
            ActionBar actionBar = playerActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
            o2Toolbar.setShowDivider(false);
        }
    }

    public void setTitleDivider(CharSequence title) {
        if (playerActivity != null) {
            ActionBar actionBar = playerActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(null);
                o2Toolbar.setDividerColor(0);
                actionBar.setHomeAsUpIndicator(R.mipmap.nav_icon_back_black);
                actionBar.setTitle(title);
            }
        }
    }


    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void hideSoftKeyboard() {
        if (inputMethodManager == null)
            inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private InputMethodManager inputMethodManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        if (getActivity() instanceof PlayerActivity)
            playerActivity = (PlayerActivity) getActivity();
        toolbar = (WTToolbar) rootView.findViewById(R.id.toolbar);
        callBack = null;
        o2Toolbar = (WTToolbar) toolbar;
        initToolBar(o2Toolbar);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private LoadingDialog mLdDialog;

    public void showLodingDialog() {
        mLdDialog = ProgressDialogUtils.instance(getActivity()).getLoadingDialog();
        mLdDialog.show();
    }

    public void dissmisDialog() {
        if (mLdDialog != null)
            mLdDialog.dismiss();
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //初始化view
    protected abstract void initView();


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    public void openFragment(Fragment fragment) {
        if (!(this instanceof PlayerFragment))
            BSApplication.fragmentBase = this;
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.open(fragment);
        }
    }

    public void openFragmentNoAnim(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.openNoAnim(fragment);
        }
    }

    public void openFragmentMain(Fragment fragment) {
        if (!(this instanceof PlayerFragment))
            BSApplication.fragmentBase = this;
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.openMain(fragment);
        }
    }

    public void closeFragment() {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.close();
        }
    }

}
