package com.wotingfm.common.net;


import android.text.TextUtils;

import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Reports;
import com.wotingfm.common.bean.Subscrible;
import com.wotingfm.ui.play.activity.albums.fragment.AlbumsInfoFragment;
import com.wotingfm.common.constant.StringConstant;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    private static final int DEFAULT_TIMEOUT = 2000;
    private RetrofitService retrofitService;
    public static RetrofitUtils INSTANCE;
    public static String TEST_USERID = "00163e00693b";
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMDE2M2UwMDY5M2IiLCJpc3MiOiJodHRwOlwvXC93b3Rpbmcuc3VpdGluZ3dlaS5jb21cL2FwaVwvYWNjb3VudHNcL2xvZ2luIiwiaWF0IjoxNDk2NzE5OTI0LCJleHAiOjE1MDE5MDM5MjQsIm5iZiI6MTQ5NjcxOTkyNCwianRpIjoiZWQ1YmZmMWI4NzM4ZDVmYmQwZjk4ZTU4NjEwZjdkOTMifQ.jobc9DSVQTZUQp57NEOowz-cf1zZG2s05RTekOUd9Yw";
    private OkHttpClient.Builder builder;

    private RetrofitUtils() {
        // 创建网络连接
        createService();
    }

    private void createService() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                //设置OKHttpClient为网络客户端
                .client(genericClient())
                //配置转化库，默认是GSON
                .addConverterFactory(GsonConverterFactory.create())
                //配置回调库，采用RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //配置服务器路径
                .baseUrl(BASE_URL)
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    private OkHttpClient genericClient() {
      final String _token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,token);
        if (!TextUtils.isEmpty(_token)) {
            return new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", "Bearer {" + _token + "}")
                                    .build();
                            return chain.proceed(request);
                        }
                    }).build();
        } else {
            if (builder == null) {
                builder = new OkHttpClient.Builder();
                builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }
            return builder.build();
        }
    }

    //<以下为对外提供接口>////////////////////////////////////////////////////////////////////////////

    /**
     * 单例模式创建网络连接
     *
     * @return
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
    public Observable<Player> getProgramList(String albums, int page) {
        return retrofitService.singlesList(albums, page)
                .map(new Func1<Player, Player>() {
                    @Override
                    public Player call(Player player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player;
                    }
                });
    }

    public Observable<List<Player.DataBean.SinglesBean>> getPlayerList(String albums) {
        return retrofitService.getPlayerList(albums)
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

    //订阅列表，我的
    public Observable<List<Subscrible.DataBean.AlbumsBean>> getSubscriptionsList(String pid) {
        return retrofitService.getSubscriptionsList(pid)
                .map(new Func1<Subscrible, List<Subscrible.DataBean.AlbumsBean>>() {
                    @Override
                    public List<Subscrible.DataBean.AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<List<Subscrible.DataBean.AlbumsBean>> getSimilarsList(String pid) {
        return retrofitService.albumsSimilars(pid)
                .map(new Func1<Subscrible, List<Subscrible.DataBean.AlbumsBean>>() {
                    @Override
                    public List<Subscrible.DataBean.AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<List<Subscrible.DataBean.AlbumsBean>> getAlbumsList(String pid, int page) {
        return retrofitService.albumsList(pid, page)
                .map(new Func1<Subscrible, List<Subscrible.DataBean.AlbumsBean>>() {
                    @Override
                    public List<Subscrible.DataBean.AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<List<Reports.DataBean.Reasons>> getPlayerReports(String type) {
        return retrofitService.getPlayerReports(type)
                .map(new Func1<Reports, List<Reports.DataBean.Reasons>>() {
                    @Override
                    public List<Reports.DataBean.Reasons> call(Reports player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.reasons;
                    }
                });
    }

    public Observable<Object> followAnchor(String uid, String oldid) {
        return retrofitService.followAnchor(uid, oldid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> unFollowAnchor(String uid, String oldid) {
        return retrofitService.unFollowAnchor(uid, oldid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> postFollowUser(String pid) {
        return retrofitService.followUsers(pid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> postUnfollowUser(String pid) {
        return retrofitService.unfollowUsers(pid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> unSubscriptions(String pid) {
        return retrofitService.unSubscriptions(pid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> reportsPlayer(String pid, String report_reason, String content) {
        return retrofitService.reportsPlayer(pid, report_reason, content)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> reportsUser(String pid, String report_reason, String content) {
        return retrofitService.reportsUser(pid, report_reason, content)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<AnchorInfo> getAnchorInfo(String uid) {
        return retrofitService.getAnchorInfo(uid)
                .map(new Func1<AnchorInfo, AnchorInfo>() {

                    @Override
                    public AnchorInfo call(AnchorInfo anchorInfo) {
                        if (anchorInfo != null && anchorInfo.ret != 0) {
                            throw new IllegalStateException(anchorInfo.msg);
                        }
                        return anchorInfo;
                    }
                });
    }

    public Observable<AlbumInfo> getAlbumInfo(String uid) {
        return retrofitService.albumsInfo(uid)
                .map(new Func1<AlbumInfo, AlbumInfo>() {

                    @Override
                    public AlbumInfo call(AlbumInfo anchorInfo) {
                        if (anchorInfo != null && anchorInfo.ret != 0 && anchorInfo.data != null && anchorInfo.data.album != null) {
                            throw new IllegalStateException(anchorInfo.msg);
                        }
                        return anchorInfo;
                    }
                });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取验证码
     *
     * @param userName 手机号
     * @return Object
     */
    public Observable<Object> registerForYzm(String userName) {
        return retrofitService.registerForYzm(userName)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 注册
     * @param userName 用户名
     * @param password 手机号
     * @param yzm 验证码
     * @return Object
     */
    public Observable<Object> register(String userName, String password, String yzm) {
        return retrofitService.register(userName, password, yzm)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 忘记密码
     * @param userName
     * @param password
     * @param yzm
     * @return
     */
    public Observable<Object> resetPasswords(String userName, String password, String yzm) {
        return retrofitService.resetPasswords(userName, password, yzm)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    public Observable<Object> login(String userName, String password) {
        return retrofitService.login(userName, password)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 加群方式
     * @param password
     * @param type
     * @return
     */
    public Observable<Object> applyGroupType(String password, int type) {
        return retrofitService.applyGroupType(password, type)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 入组申请
     * @param s
     * @return
     */
    public Observable<Object> groupApply(String s) {
        return retrofitService.groupApply(s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 提交偏好设置
     * @param s 提交的数据
     * @return Object
     */
    public Observable<Object> preference(String s) {
        return retrofitService.preference(s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }
}
