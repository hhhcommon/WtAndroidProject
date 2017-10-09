package com.wotingfm.common.manager;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.woting.commonplat.nim.DemoCache;
import com.woting.commonplat.nim.base.util.ScreenUtil;
import com.woting.commonplat.nim.base.util.crash.AppCrashHandler;
import com.woting.commonplat.nim.base.util.log.LogUtil;
import com.woting.commonplat.nim.im.config.AuthPreferences;
import com.woting.commonplat.nim.im.util.storage.StorageType;
import com.woting.commonplat.nim.im.util.storage.StorageUtil;
import com.woting.commonplat.nim.inject.FlavorDependent;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.utils.SystemUtil;

/**
 * nim配置文件
 * 作者：xinLong on 2017/8/23 15:45
 * 邮箱：645700751@qq.com
 */
public class NIMManager {


    public static void NIMSet() {
        // init tools
        DemoCache.setContext(BSApplication.getInstance());

        NIMClient.init(BSApplication.getInstance(), getLoginInfo(), getOptions());

        // crash handler
        AppCrashHandler.getInstance(BSApplication.getInstance());

        if (inMainProcess()) {
            // 注册自定义消息附件解析器
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(FlavorDependent.getInstance().getMsgAttachmentParser());
            // init tools
            StorageUtil.init(BSApplication.getInstance(), null);
            ScreenUtil.init(BSApplication.getInstance());
            DemoCache.initImageLoaderKit();

            // init log
            initLog();
            FlavorDependent.getInstance().onApplicationCreate();
        }

    }

    private static LoginInfo getLoginInfo() {
        String account = AuthPreferences.getUserAccount();
        String token = AuthPreferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private static SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + BSApplication.getInstance().getPackageName() + "/nim/";
        options.sdkStorageRootPath = sdkPath;
        Log.i("demo", FlavorDependent.getInstance().getFlavorName() + " demo nim sdk log path=" + sdkPath);

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = (int) (0.5 * ScreenUtil.screenWidth);

        // 用户信息提供者
        options.userInfoProvider = null;

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = null;

        return options;
    }

    private static boolean inMainProcess() {
        String packageName = BSApplication.getInstance().getPackageName();
        String processName = SystemUtil.getProcessName(BSApplication.getInstance());
        return packageName.equals(processName);
    }

    private static void initLog() {
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);
        LogUtil.i("demo", FlavorDependent.getInstance().getFlavorName() + " demo log path=" + path);
    }


}
