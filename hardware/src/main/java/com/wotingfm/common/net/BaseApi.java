package com.wotingfm.common.net;

import com.wotingfm.common.net.RetrofitService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.wotingfm.common.net.RetrofitService.BASE_URL;

/**
 * 作者：xinLong on 2017/6/8 11:22
 * 邮箱：645700751@qq.com
 */
public abstract class BaseApi {

    private static Retrofit mRetrofit;
    private static final int DEFAULT_TIMEOUT = 20;

    protected static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            //构建Retrofit
            mRetrofit = new Retrofit.Builder()
                    //设置OKHttpClient为网络客户端
                    .client(builder.build())
                    //配置转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //配置回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //配置服务器路径
                    .baseUrl(BASE_URL)
                    .build();
            getRetrofitService();
        }
        return mRetrofit;
    }

    protected static RetrofitService getRetrofitService() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            //构建Retrofit
            mRetrofit = new Retrofit.Builder()
                    //设置OKHttpClient为网络客户端
                    .client(builder.build())
                    //配置转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //配置回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //配置服务器路径
                    .baseUrl(BASE_URL)
                    .build();
            RetrofitService retrofitService = mRetrofit.create(RetrofitService.class);
            return retrofitService;
        } else {
            RetrofitService retrofitService = mRetrofit.create(RetrofitService.class);
            return retrofitService;
        }
    }
}