package com.wotingfm.ui.mine.set.presenter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.commonplat.manager.CacheManager;
import com.woting.commonplat.utils.ResourceUtil;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GlideCatchUtil;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.set.model.SettingModel;
import com.wotingfm.ui.mine.set.view.SettingFragment;

import java.io.File;

/**
 * 个人中心界面处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SettingPresenter {

    private SettingFragment activity;
    private SettingModel model;
    private final String cachePath = Environment.getExternalStorageDirectory() + "/woting/image";// 缓存路径
    private String cache;

    public SettingPresenter(SettingFragment activity) {
        this.activity = activity;
        this.model = new SettingModel();
        initCache();
    }

    /**
     * 判断注销按钮是否显示
     */
    public void getData() {
        if (CommonUtils.isLogin()) {
            activity.setCloseView(true);
        } else {
            activity.setCloseView(false);
        }
    }

    /**
     * 发送注销请求
     */
    public void cancel() {
        if (true) {
            // 测试代码
            dealCancelSuccess();
        } else {
            // 正式代码
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
                model.loadNews(new SettingModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        dealCancelSuccess();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dealCancelSuccess();
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    /**
     * 处理注销登录返回数据
     */
    private void dealCancelSuccess() {
        activity.dialogCancel();
        // 保存注销后数据
        model.cancel();
        // 隐藏注销登录按钮
        activity.setCloseView(false);
        // 发送注销登录广播通知所有界面
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.CANCEL));
        activity.closeFragment();
    }

    // 启动统计缓存的线程
    private void initCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(cachePath);
                File file1 = new File(BSApplication.getInstance().getCacheDir() + "/" + GlobalStateConfig.GLIDE_CARCH_DIR);
                String fileUrl;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    fileUrl = com.woting.commonplat.config.GlobalUrlConfig.playCacheDirO + com.woting.commonplat.config.GlobalUrlConfig.ksyPlayCache;
                } else {
                    fileUrl = ResourceUtil.getSDCardPath()[0] + com.woting.commonplat.config.GlobalUrlConfig.ksyPlayCache;
                }
                File file2 = new File(fileUrl);
                try {
                    cache = CacheManager.getCacheSize(file, file1, file2);
                    activity.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.setCache(cache);
                        }
                    });
                } catch (Exception e) {
                }
            }
        }).start();
    }

    /**
     * 开始清除缓存
     */
    public void clear() {
        new ClearCacheTask().execute();
    }

    // 清除缓存异步任务
    private class ClearCacheTask extends AsyncTask<Void, Void, Void> {
        private boolean clearResult;

        @Override
        protected void onPreExecute() {
            activity.clearDialogCancel();
            activity.dialogShow();
        }

        @Override
        protected Void doInBackground(Void... params) {
            GlideCatchUtil.getInstance().clearCacheMemory();
            GlideCatchUtil.getInstance().clearCacheDiskSelf();
            String fileUrl;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                fileUrl = com.woting.commonplat.config.GlobalUrlConfig.playCacheDirO + com.woting.commonplat.config.GlobalUrlConfig.ksyPlayCache;
            } else {
                fileUrl = ResourceUtil.getSDCardPath()[0] + com.woting.commonplat.config.GlobalUrlConfig.ksyPlayCache;
            }
            CacheManager.delAllFile(fileUrl);
            clearResult = CacheManager.delAllFile(cachePath);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            activity.dialogCancel();
            if (clearResult) {
                ToastUtils.show_always(activity.getActivity(), "缓存已清除");
                activity.setCache("0MB");
            } else {
                Log.e("缓存异常", "缓存清理异常");
                initCache();
            }
        }
    }

    /**
     * 数据销毁
     */
    public void destroy(){
        model=null;
    }
}
