package com.wotingfm.ui.base.basefragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.woting.commonplat.widget.LoadingDialog;
import com.woting.commonplat.widget.WTToolbar;
import com.wotingfm.R;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ProgressDialogUtils;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.look.activity.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by amine on 2017/6/14.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    public Toolbar toolbar;
    private PlayerActivity playerActivity;
    private MineActivity playerActivityMain;
    private InterPhoneActivity interPhoneActivity;
    private LookListActivity lookListActivity;

    public void startMain(String albumsId) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
    }

    public void startMain(ChannelsBean channelsBean) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(channelsBean, 1));
    }

    public void startMain(SinglesBase singlesBase) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(singlesBase, 2));
    }

    public void startMain(List<SinglesDownload> singlesDownloadsd) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(singlesDownloadsd, 3));
    }

    @Override
    public void onClick(View view) {

    }

    protected WTToolbar o2Toolbar;


    public interface CallBack {
        void call();
    }


    private BaseToolBarActivity.CallBack callBack;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideSoftKeyboard();
            if (callBack != null) {
                callBack.call();
            }
            closeFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setStatusBarPaddingAndHeight(View toolBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolBar != null) {
                //  int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
                toolBar.setPadding(toolBar.getPaddingLeft(), 20, toolBar.getPaddingRight(),
                        toolBar.getPaddingBottom());
                toolBar.getLayoutParams().height = 20 +
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
            }
        }
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
        } else if (toolbar != null && playerActivityMain != null) {
            toolbar.setDividerColor(getResources().getColor(R.color.line_color));
            toolbar.setShowDivider(true);
            playerActivityMain.setSupportActionBar(toolbar);
            ActionBar actionBar = playerActivityMain.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                setHasOptionsMenu(true);
                actionBar.setHomeAsUpIndicator(R.mipmap.nav_icon_back_black);
                actionBar.setTitle("");
            }
        } else if (toolbar != null && interPhoneActivity != null) {
            toolbar.setDividerColor(getResources().getColor(R.color.line_color));
            toolbar.setShowDivider(true);
            interPhoneActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = interPhoneActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                setHasOptionsMenu(true);
                actionBar.setHomeAsUpIndicator(R.mipmap.nav_icon_back_black);
                actionBar.setTitle("");
            }
        } else if (toolbar != null && lookListActivity != null) {
            toolbar.setDividerColor(getResources().getColor(R.color.line_color));
            toolbar.setShowDivider(true);
            lookListActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = lookListActivity.getSupportActionBar();
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
        } else if (playerActivityMain != null) {
            ActionBar actionBar = playerActivityMain.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        } else if (interPhoneActivity != null) {
            ActionBar actionBar = interPhoneActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        } else if (lookListActivity != null) {
            ActionBar actionBar = lookListActivity.getSupportActionBar();
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
        } else if (playerActivityMain != null) {
            ActionBar actionBar = playerActivityMain.getSupportActionBar();
            this.callBack = callBackW;
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        } else if (interPhoneActivity != null) {
            ActionBar actionBar = interPhoneActivity.getSupportActionBar();
            this.callBack = callBackW;
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        } else if (lookListActivity != null) {
            ActionBar actionBar = lookListActivity.getSupportActionBar();
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
        } else if (playerActivityMain != null) {
            ActionBar actionBar = playerActivityMain.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
            o2Toolbar.setShowDivider(false);
        } else if (interPhoneActivity != null) {
            ActionBar actionBar = interPhoneActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
            o2Toolbar.setShowDivider(false);
        } else if (lookListActivity != null) {
            ActionBar actionBar = lookListActivity.getSupportActionBar();
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
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public InputMethodManager inputMethodManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (inputMethodManager == null)
            inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        if (getActivity() instanceof PlayerActivity) {
            playerActivity = (PlayerActivity) getActivity();
        } else if (getActivity() instanceof MineActivity) {
            playerActivityMain = (MineActivity) getActivity();
        } else if (getActivity() instanceof InterPhoneActivity) {
            interPhoneActivity = (InterPhoneActivity) getActivity();
        }else if (getActivity() instanceof LookListActivity){
            lookListActivity = (LookListActivity) getActivity();}
        toolbar = (WTToolbar) rootView.findViewById(R.id.toolbar);
        callBack = null;
        o2Toolbar = (WTToolbar) toolbar;
        //  setStatusBarPaddingAndHeight(o2Toolbar);
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
       /* if (!(this instanceof PlayerFragment))
            BSApplication.fragmentBase = this;*/
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.open(fragment);
        } else if (getActivity() instanceof MineActivity) {
            MineActivity playerActivity = (MineActivity) getActivity();
            playerActivity.open(fragment);
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity interPhoneActivity = (InterPhoneActivity) getActivity();
            interPhoneActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity interPhoneActivity = (LookListActivity) getActivity();
            interPhoneActivity.open(fragment);
        }

    }


    public void closeFragment() {
        hideSoftKeyboard();
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.close();
        } else if (getActivity() instanceof MineActivity) {
            MineActivity mineActivity = (MineActivity) getActivity();
            mineActivity.close();
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity mineActivity = (InterPhoneActivity) getActivity();
            mineActivity.close();
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity mineActivity = (InterPhoneActivity) getActivity();
            mineActivity.close();
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity mineActivity = (LookListActivity) getActivity();
            mineActivity.close();
        }
    }

}
