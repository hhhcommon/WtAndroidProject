package com.woting.common.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.woting.common.config.GlobalStateConfig;

/**
 * 作者：xinLong on 2017/7/21 16:39
 * 邮箱：645700751@qq.com
 */
public class GlideConfiguration implements GlideModule {

    // 需要在AndroidManifest.xml中声明
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GlobalStateConfig.GLIDE_CARCH_DIR,
                GlobalStateConfig.GLIDE_CATCH_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //nil
    }
}