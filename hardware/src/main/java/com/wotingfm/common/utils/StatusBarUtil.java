package com.wotingfm.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wotingfm.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者：xinLong on 2017/6/12 14:39
 * 邮箱：645700751@qq.com
 */
public class StatusBarUtil {

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本,保持沉浸式状态
     *
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        setStatusBarColor(activity, colorId, true);
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId  直接使用资源ID，即R.color.xxx
     * @param isFollow 是否保持沉浸式状态
     */
    public static void setStatusBarColor(Activity activity, int colorId, boolean isFollow) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (!isFollow) {
                window.setStatusBarColor(colorId);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                parentView.setFitsSystemWindows(true);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(colorId);
            /*// set a custom tint color for all system bars
            tintManager.setTintColor(Color.parseColor("#99000FF"));
            // set a custom navigation bar resource
            tintManager.setNavigationBarTintResource(R.drawable.my_tint);
            // set a custom status bar drawable
            tintManager.setStatusBarTintDrawable(MyDrawable);*/
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity, boolean dark) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), dark)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体，即白色字体
     */
    public static void StatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    private static final int BUILD_VERSION_KITKAT = 19;
    private static final int BUILD_VERSION_LOLLIPOP = 21;
    public static final int FLAG_TRANSLUCENT_STATUS = 0x04000000;
    private Activity mActivity;
    private View statusBarView;
    private int statusBarHeight;

    public StatusBarUtil(Activity activity) {
        this.mActivity = activity;
        statusBarHeight = getStatusBarHeight(activity);
    }

    public void setStatusBarView(View statusBarView) {
        this.statusBarView = statusBarView;
        setTransparent();
    }

    public void setStatusBarView() {
        setTransparent();
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    /**
     * 设置状态栏全透明
     */
    private void setTransparent() {
        if (Build.VERSION.SDK_INT < BUILD_VERSION_KITKAT) {
            return;
        }
        if (statusBarHeight <= 0) {
            return;
        }
        transparentStatusBar();
        showStatusBarView();
    }

    @TargetApi(19)
    private void showStatusBarView() {
        int color = mActivity.getResources().getColor(R.color.background_color);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(mActivity));
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);
            ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            FrameLayout content = (FrameLayout) decorView.findViewById(android.R.id.content);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) content.getChildAt(0).getLayoutParams();
            layoutParams.setMargins(0, statusBarHeight, 0, 0);
            decorView.addView(statusBarView);
        } else {
            ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
            layoutParams.height = getStatusBarHeight(mActivity);
            statusBarView.setLayoutParams(layoutParams);
            statusBarView.setBackgroundColor(color);
        }
    }

    // 参考上面注释掉的代码 因为需要用隐藏API 调用方式进行改成反射
    private void transparentStatusBar() {
        Window window = mActivity.getWindow();
        if (Build.VERSION.SDK_INT >= BUILD_VERSION_LOLLIPOP) {
            // 不 add 此条 flag 会导致在 EMUI3.1(华为) 上失效，add 这个 flag 会导致在其它机型上面添加一个半透明黑条
            window.addFlags(FLAG_TRANSLUCENT_STATUS);
            // 因为需要用隐藏 API，没有重新编译 5.x 版本的 android.jar，使用的还是 18 的 api，这里用的反射
            try {
                Class[] argsClass = new Class[]{int.class};
                Method setStatusBarColorMethod = Window.class.getMethod("setStatusBarColor", argsClass);
                setStatusBarColorMethod.invoke(window, Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            window.addFlags(FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity context) {
        if (Build.VERSION.SDK_INT < BUILD_VERSION_KITKAT) {
            return 0;
        }
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public void setStatusbarVisibility(int visibility) {
        if (statusBarView != null) {
            this.statusBarView.setVisibility(visibility);
        }
    }

    public void setColor(int color) {
        if (statusBarView != null) {
            this.statusBarView.setBackgroundColor(color);
        }
    }
}
