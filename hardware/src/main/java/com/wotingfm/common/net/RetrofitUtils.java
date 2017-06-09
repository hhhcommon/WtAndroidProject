package com.wotingfm.common.net;


import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.wotingfm.common.net.RetrofitService.BASE_URL;


/**
 * Created by amine on 2017/2/8.
 */

public class RetrofitUtils {

    private static final int DEFAULT_TIMEOUT = 20;
    private Retrofit retrofit;
    public RetrofitService retrofitService;
    public static RetrofitUtils INSTANCE;
    private OkHttpClient.Builder builder;
    private String token = "";


    private RetrofitUtils() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    /* public OkHttpClient genericClient() {
         if (sp == null) {
             return builder.build();
         }

         return new OkHttpClient.Builder()
                 .addInterceptor(new Interceptor() {
                     @Override
                     public Response intercept(Chain chain) throws IOException {
                         Request request = chain.request()
                                 .newBuilder()
                                 .addHeader("Authorization", "Bearer {" + token + "}")
                                 .addHeader("app-os", "android")
                                 .build();
                         Log.e(TAG, "intercept: " + token);
                         return chain.proceed(request);
                     }

                 })
                 .build();
     }
 */
    public static RetrofitUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitUtils();
                }
            }
        }
        return INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public Observable<List<HomeBanners.Banner>> getHomeBanners() {
        return retrofitService.getHomeBanners()
                .map(new Func1<HomeBanners, List<HomeBanners.Banner>>() {
                    @Override
                    public List<HomeBanners.Banner> call(HomeBanners homeBanners) {
                        if (homeBanners.status != 200) {
                            throw new IllegalStateException(homeBanners.message);
                        }
                        return homeBanners.banners;
                    }
                });
    }

    /**
     * banner
     *
     * @return
     */
    public Observable<List<Player.DataBean.SinglesBean>> getPlayerList() {
        return retrofitService.getPlayerList()
                .map(new Func1<Player, List<Player.DataBean.SinglesBean>>() {
                    @Override
                    public List<Player.DataBean.SinglesBean> call(Player player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.singles;
                    }
                });
    }

}
