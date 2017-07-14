package com.wotingfm.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 作者：xinLong on 2017/6/15 17:54
 * 邮箱：645700751@qq.com
 */
public class GlideUtils {
    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     * 其中radius的取值范围是1-25，radius越大，模糊度越高。
     * Glide.with(this).load(R.drawable.defalut_photo).bitmapTransform(new BlurTransformation(this, 15)).into(img_bg);
     */

    /**
     * 默认加载
     *
     * @param mContext
     * @param path       路径
     * @param mImageView 图片
     * @param b          是否是圆形图
     */
    public static void loadImageView(Context mContext, String path, ImageView mImageView, boolean b) {
        if (b) {
            Glide.with(mContext).load(path).transform(new GlideCircleTransform(mContext)).into(mImageView);
        } else {
            Glide.with(mContext).load(path).into(mImageView);
            Glide.with(mContext).load(path).bitmapTransform(new BlurTransformation(mContext, 15)).into(mImageView);
        }
    }

    /**
     * 默认加载
     *
     * @param mContext
     * @param path       路径
     * @param mImageView 图片
     */
    public static void loadImageViewBlur(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).crossFade(1000).placeholder(R.mipmap.p).bitmapTransform(new BlurTransformation(mContext, 23, 18)).into(mImageView);
    }
    /**
     * 加载指定大小
     *
     * @param mContext
     * @param path       路径
     * @param width      宽度
     * @param height     高度
     * @param mImageView 图片
     * @param b          是否是圆形图
     */
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView, boolean b) {
        if (b) {
            Glide.with(mContext).load(path).transform(new GlideCircleTransform(mContext)).override(width, height).into(mImageView);
        } else {
            Glide.with(mContext).load(path).override(width, height).into(mImageView);
        }

    }

    /**
     * 设置加载中以及加载失败图片
     *
     * @param mContext
     * @param path
     * @param mImageView
     * @param loadingImage
     * @param errorImageView
     */
    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int loadingImage, int errorImageView, boolean b) {
        if (b) {
            Glide.with(mContext).load(path).transform(new GlideCircleTransform(mContext)).placeholder(loadingImage).error(errorImageView).into(mImageView);
        } else {
            Glide.with(mContext).load(path).placeholder(loadingImage).error(errorImageView).into(mImageView);
        }
    }

    /**
     * 设置加载中以及加载失败图片并且指定大小
     *
     * @param mContext
     * @param path
     * @param width
     * @param height
     * @param mImageView
     * @param loadingImage
     * @param errorImageView
     */
    public static void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int loadingImage, int errorImageView, boolean b) {
        if (b) {
            Glide.with(mContext).load(path).transform(new GlideCircleTransform(mContext)).override(width, height).placeholder(loadingImage).error(errorImageView).into(mImageView);
        } else {
            Glide.with(mContext).load(path).override(width, height).placeholder(loadingImage).error(errorImageView).into(mImageView);
        }

    }

    /**
     * 设置跳过内存缓存
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    /**
     * 设置下载优先级
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    /**
     * 设置缓存策略
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * 设置加载动画
     * api也提供了几个常用的动画：比如crossFade()
     *
     * @param mContext
     * @param path
     * @param anim
     * @param mImageView
     */
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 设置缩略图支持
     * 会先加载缩略图
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * 设置动态转换
     * api提供了比如：centerCrop()、fitCenter()等
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    /**
     * 设置动态GIF加载方式
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().into(mImageView);
    }

    /**
     * 设置静态GIF加载方式
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().into(mImageView);
    }

    /**
     * 设置监听请求接口
     * 设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘
     *
     * @param mContext
     * @param path
     * @param mImageView
     * @param requestListener
     */
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requestListener) {
        Glide.with(mContext).load(path).listener(requestListener).into(mImageView);
    }

    /**
     * 设置要加载的内容
     * 项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
     *
     * @param mContext
     * @param path
     * @param simpleTarget
     */
    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }

    /**
     * 清理磁盘缓存
     *
     * @param mContext
     */
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    /**
     * 清理内存缓存
     *
     * @param mContext
     */
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

}
