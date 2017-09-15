package com.woting.commonplat.ksy;

import android.content.Context;

import com.kingsoft.media.httpcache.KSYProxyService;

/**
 * 作者：xinLong on 2017/9/13 11:30
 * 邮箱：645700751@qq.com
 */
public class KSYProxy {
    private static KSYProxyService proxyService = null;
    private static Context cc;
    private static KSYProxy app = null;
    public static KSYProxyService getKSYProxy(Context context) {
        cc = context;
        if(app == null){
            app = new KSYProxy();
        }
        return app.proxyService == null ? (app.proxyService = newKSYProxy()) : app.proxyService;
    }

    private static KSYProxyService newKSYProxy()  {
        return new KSYProxyService(cc);
    }
}
