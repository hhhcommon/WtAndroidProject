package com.wotingfm.common.net;


import android.text.TextUtils;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.ui.bean.AnchorInfo;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.bean.CLive;
import com.wotingfm.ui.bean.Channels;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.HomeBanners;
import com.wotingfm.ui.bean.LiveBean;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.Provinces;
import com.wotingfm.ui.bean.Radio;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.ui.bean.Room;
import com.wotingfm.ui.bean.SelectedMore;
import com.wotingfm.ui.bean.SerchList;
import com.wotingfm.ui.bean.Subscrible;
import com.wotingfm.ui.bean.TrailerInfo;
import com.wotingfm.ui.play.find.classification.main.model.Classification;
import com.wotingfm.ui.play.report.model.Reports;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
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

    private static final int DEFAULT_TIMEOUT = 40000;
    private RetrofitService retrofitService;
    public static RetrofitUtils INSTANCE;
    private OkHttpClient.Builder builder;

    private RetrofitUtils() {
        // 创建网络连接
        createService();
    }

    private void createService() {
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

    private OkHttpClient.Builder getBuild(){
        if(builder==null){
            builder = new OkHttpClient.Builder();
            builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return builder;
    }

    private OkHttpClient genericClient() {
        final String _token = BSApplication.SharedPreferences.getString(StringConstant.TOKEN, null);
        if (!TextUtils.isEmpty(_token)) {
            return getBuild().addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

//                            Request request = chain.request()
//                                    .newBuilder()
//                                    .addHeader("token",_token )
//                                    .build();
//                            return chain.proceed(request);
//
//                            Request request = chain.request()
//                                    .newBuilder()
//                                    .addHeader("Authorization", "Bearer {" + _token + "}")
//                                    .build();
//                            return chain.proceed(request);

                            Request r = chain.request();
                            Request request = addParam(r, _token);
                            return chain.proceed(request);
                        }
                    }).build();
        } else {
            return getBuild().build();
        }
    }

    /**
     * 添加公共参数
     *
     * @param request
     * @return
     */
    private Request addParam(Request request, String token) {

        HttpUrl.Builder builder = request.url()
                .newBuilder()
                .setEncodedQueryParameter("token", token);

        Request newRequest = request.newBuilder()
                .method(request.method(), request.body())
                .url(builder.build())
                .build();

        return newRequest;
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

    public Observable<List<HomeBanners.DataBean.BannersBean>> getHomeBanners(String type) {
        return retrofitService.getHomeBanners(type)
                .map(new Func1<HomeBanners, List<HomeBanners.DataBean.BannersBean>>() {
                    @Override
                    public List<HomeBanners.DataBean.BannersBean> call(HomeBanners homeBanners) {
                        if (homeBanners.ret != 0) {
                            throw new IllegalStateException(homeBanners.msg);
                        }
                        return homeBanners.data.banners;
                    }
                });
    }

    /**
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

    public Observable<Object> reservations(String uid) {
        return retrofitService.reservations(uid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> deleteReservations(String pid) {
        return retrofitService.deleteReservations(pid)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<List<Classification.DataBeanX>> getClassifications() {
        return retrofitService.getClassifications()
                .map(new Func1<Classification, List<Classification.DataBeanX>>() {
                    @Override
                    public List<Classification.DataBeanX> call(Classification classification) {
                        if (classification.ret != 0) {
                            throw new IllegalStateException(classification.msg);
                        }
                        return classification.data;
                    }
                });
    }

    public Observable<Object> getSelecteds() {
        return retrofitService.getSelecteds()
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    public Observable<List<AlbumsBean>> getSelectedsMore(int page, String type) {
        return retrofitService.getSelectedsMore(page, type)
                .map(new Func1<SelectedMore, List<AlbumsBean>>() {
                    @Override
                    public List<AlbumsBean> call(SelectedMore classification) {
                        if (classification.ret != 0) {
                            throw new IllegalStateException(classification.msg);
                        }
                        return classification.data.albums;
                    }
                });
    }

    public Observable<List<ChannelsBean>> getChannels(String albums) {
        return retrofitService.getChannels(albums)
                .map(new Func1<Channels, List<ChannelsBean>>() {
                    @Override
                    public List<ChannelsBean> call(Channels player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.channels;
                    }
                });
    }

    public Observable<CLive.DataBean.VoiceLiveBean> carteLive(String userId) {
        return retrofitService.carteLive(userId)
                .map(new Func1<CLive, CLive.DataBean.VoiceLiveBean>() {
                    @Override
                    public CLive.DataBean.VoiceLiveBean call(CLive player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.voice_live;
                    }
                });
    }

    public Observable<TrailerInfo.DataBean.VoiceLiveBean> getTrailerInfo(String userId) {
        return retrofitService.getTrailerInfo(userId)
                .map(new Func1<TrailerInfo, TrailerInfo.DataBean.VoiceLiveBean>() {
                    @Override
                    public TrailerInfo.DataBean.VoiceLiveBean call(TrailerInfo player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.voice_live;
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
    public Observable<List<AlbumsBean>> getSubscriptionsList(String pid) {
        return retrofitService.getSubscriptionsList(pid)
                .map(new Func1<Subscrible, List<AlbumsBean>>() {
                    @Override
                    public List<AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<RadioInfo.DataBean> getRadioInfo(String pid) {
        return retrofitService.getRadioInfo(pid)
                .map(new Func1<RadioInfo, RadioInfo.DataBean>() {
                    @Override
                    public RadioInfo.DataBean call(RadioInfo player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data;
                    }
                });
    }

    public Observable<List<Provinces.DataBean.ProvincesBean>> getProvinces() {
        return retrofitService.getProvinces()
                .map(new Func1<Provinces, List<Provinces.DataBean.ProvincesBean>>() {
                    @Override
                    public List<Provinces.DataBean.ProvincesBean> call(Provinces player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.provinces;
                    }
                });
    }

    public Observable<List<ChannelsBean>> getChannelsRadioLocation(String type, String area_code, int page) {
        return retrofitService.getChannelsRadioLocation(type, area_code, page)
                .map(new Func1<Radio, List<ChannelsBean>>() {
                    @Override
                    public List<ChannelsBean> call(Radio player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.channels;
                    }
                });
    }

    public Observable<List<ChannelsBean>> getChannelsRadio(String type, int page) {
        return retrofitService.getChannelsRadio(type, page)
                .map(new Func1<Radio, List<ChannelsBean>>() {
                    @Override
                    public List<ChannelsBean> call(Radio player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.channels;
                    }
                });
    }

    public Observable<List<ChannelsBean>> getChannelsRadioHots(String scope) {
        return retrofitService.getChannelsRadioHots(scope)
                .map(new Func1<Radio, List<ChannelsBean>>() {
                    @Override
                    public List<ChannelsBean> call(Radio player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.channels;
                    }
                });
    }

    public Observable<List<ChannelsBean>> getChannelsRadioList(String scope, int page) {
        return retrofitService.getChannelsRadioList(scope, page)
                .map(new Func1<Radio, List<ChannelsBean>>() {
                    @Override
                    public List<ChannelsBean> call(Radio player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.channels;
                    }
                });
    }

    public Observable<List<LiveBean.DataBean>> getRecommandations(int page) {
        return retrofitService.getRecommandations(page)
                .map(new Func1<LiveBean, List<LiveBean.DataBean>>() {
                    @Override
                    public List<LiveBean.DataBean> call(LiveBean player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data;
                    }
                });
    }

    public Observable<List<AlbumsBean>> getChannelsAlbums(String pid, int page) {
        return retrofitService.getChannelsAlbums(pid, page)
                .map(new Func1<Subscrible, List<AlbumsBean>>() {
                    @Override
                    public List<AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<List<AlbumsBean>> getSimilarsList(String pid) {
        return retrofitService.albumsSimilars(pid)
                .map(new Func1<Subscrible, List<AlbumsBean>>() {
                    @Override
                    public List<AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<List<AlbumsBean>> getAlbumsList(String pid, int page) {
        return retrofitService.albumsList(pid, page)
                .map(new Func1<Subscrible, List<AlbumsBean>>() {
                    @Override
                    public List<AlbumsBean> call(Subscrible player) {
                        if (player.ret != 0) {
                            throw new IllegalStateException(player.msg);
                        }
                        return player.data.albums;
                    }
                });
    }

    public Observable<SerchList> serchList(String type, String q, int page) {
        return retrofitService.serchList(type, q, page)
                .map(new Func1<SerchList, SerchList>() {
                    @Override
                    public SerchList call(SerchList serchList) {
                        if (serchList.ret != 0) {
                            throw new IllegalStateException(serchList.msg);
                        }
                        return serchList;
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

    public Observable<Room> apprtcRoom() {
        return retrofitService.apprtcRoom()
                .map(new Func1<Room, Room>() {
                    @Override
                    public Room call(Room s) {
                        return s;
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

    public Observable<BaseResult> endLive(String voiceLiveId, String user_id) {
        return retrofitService.endLive(voiceLiveId, user_id, "end")
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> playSingles(String singleId) {
        return retrofitService.playSingles(singleId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> downloadSingle(String singleId) {
        return retrofitService.downloadSingle(singleId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> reservationsRadio(String albumsId) {
        return retrofitService.reservationsRadio(albumsId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> deleteReservationsRadio(String albumsId) {
        return retrofitService.deleteReservationsRadio(albumsId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> subscriptionsAlbums(String albumsId) {
        return retrofitService.subscriptionsAlbums(albumsId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> deleteSubscriptionsAlbums(String albumsId) {
        return retrofitService.deleteSubscriptionsAlbums(albumsId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> subscriptionsRadio(String albumsId) {
        return retrofitService.subscriptionsRadio(albumsId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> deleteSubscriptionsRadio(String albumsId) {
        return retrofitService.deleteSubscriptionsRadio(albumsId)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> submitFans(String idol_id, String user_id) {
        return retrofitService.submitFans(idol_id, user_id)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
                        return s;
                    }
                });
    }

    public Observable<BaseResult> submitNoFans(String idol_id, String user_id) {
        return retrofitService.submitNoFans(idol_id, user_id)
                .map(new Func1<BaseResult, BaseResult>() {
                    @Override
                    public BaseResult call(BaseResult s) {
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

    public Observable<Object> postFollowUser(String pid,String type) {
        return retrofitService.followUsers(pid,type)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object s) {
                        return s;
                    }
                });
    }

    public Observable<Object> postUnfollowUser(String pid,String type) {
        return retrofitService.unfollowUsers(pid,type)
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
    public Observable<Object> registerForYzm(String userName) throws Exception {
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
     *
     * @param userName 用户名
     * @param password 手机号
     * @param yzm      验证码
     * @return Object
     */
    public Observable<Object> register(String userName, String password, String yzm) throws Exception {
        return retrofitService.register(userName, password, yzm)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 忘记密码/修改密码：共同
     *
     * @param userName
     * @param password
     * @param yzm
     * @return
     */
    public Observable<Object> resetPasswords(String userName, String password, String yzm) throws Exception {
        return retrofitService.resetPasswords(userName, password, yzm)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改手机号
     *
     * @param oldP
     * @param newP
     * @param yzm
     * @return
     */
    public Observable<Object> resetPhoneNumber(String oldP, String newP, String yzm) throws Exception {
        return retrofitService.resetPhoneNumber(oldP, newP, yzm)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }


    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    public Observable<Object> login(String userName, String password) throws Exception {
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
     *
     * @param password
     * @param type
     * @return
     */
    public Observable<Object> applyGroupType(String id, String password, int type) throws Exception {
        return retrofitService.applyGroupType(id, password, type)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 入组申请
     *
     * @param news
     * @param password
     * @return
     */
    public Observable<Object> groupApply(String id, String news, String password) throws Exception {
        return retrofitService.groupApply(id, news, password)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 提交偏好设置
     *
     * @param id 用户id
     * @param s  提交的数据
     * @return Object
     */
    public Observable<Object> preference(String id, String s) throws Exception {
        return retrofitService.preference(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取自己的偏好设置
     *
     * @param id 用户id
     * @return Object
     */
    public Observable<Object> getPreference(String id) throws Exception {
        return retrofitService.getPreference(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }


    /**
     * 注销登录
     *
     * @param s 提交的数据
     * @return Object
     */
    public Observable<Object> cancel(String s) throws Exception {
        return retrofitService.cancel(s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取用户数据
     *
     * @param s 提交的数据
     * @return Object
     */
    public Observable<Object> getUserInfo(String s) throws Exception {
        return retrofitService.getUserInfo(s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取好友列表
     *
     * @param id 当前用户id
     * @return Object
     */
    public Observable<Object> getFriends(String id) throws Exception {
        return retrofitService.getFriends(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取组列表
     *
     * @param id 当前用户id
     * @return Object
     */
    public Observable<Object> getGroups(String id) throws Exception {
        return retrofitService.getGroups(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取好友信息
     *
     * @param id    当前用户id
     * @param token
     * @return Object
     */
    public Observable<Object> getPersonNews(String id, String token) throws Exception {
        return retrofitService.getPersonNews(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获新的好友申请请求
     *
     * @param id    当前用户id
     * @param token
     * @return Object
     */
    public Observable<Object> newFriend(String id, String token) throws Exception {
        return retrofitService.newFriend(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取群组信息
     *
     * @param id 当前群id
     * @return Object
     */
    public Observable<Object> getGroupNews(String id) throws Exception {
        return retrofitService.getGroupNews(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取群组成员
     *
     * @param id 当前群id
     * @return Object
     */
    public Observable<Object> getGroupPerson(String id) throws Exception {
        return retrofitService.getGroupPerson(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取推荐的成员()
     *
     * @param id   当前群id
     * @param type
     * @return Object
     */
    public Observable<Object> getRecommendPerson(String id, String type) throws Exception {
        return retrofitService.getRecommendPerson(id, type)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取推荐的群组
     *
     * @param id   当前群id
     * @param type
     * @return Object
     */
    public Observable<Object> getRecommendGroup(String id, String type) throws Exception {
        return retrofitService.getRecommendGroup(id, type)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取搜索的群组
     *
     * @param s 搜索的内容
     * @return Objects
     */
    public Observable<Object> getSearchGroup(String s) throws Exception {
        return retrofitService.getSearchGroup("GROUP", s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取搜索的好友
     *
     * @param s 搜索的内容
     * @return Objects
     */
    public Observable<Object> getSearchPerson(String s) throws Exception {
        return retrofitService.getSearchPerson("USER", s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }


    /**
     * 删除好友申请
     *
     * @param id    操作用户id
     * @param token
     * @return Objects
     */
    public Observable<Object> newFriendDel(String id, String token) throws Exception {
        return retrofitService.newFriendDel(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 同意好友申请
     *
     * @param id    操作用户id
     * @param token
     * @return Objects
     */
    public Observable<Object> newFriendApply(String id, String token) throws Exception {
        return retrofitService.newFriendApply(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 拒绝好友申请
     *
     * @param id    操作用户id
     * @param token
     * @return Objects
     */
    public Observable<Object> newFriendRefuse(String id, String token) throws Exception {
        return retrofitService.newFriendRefuse(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 添加好友
     *
     * @param id 操作用户id
     * @param s  申请消息
     * @return Objects
     */
    public Observable<Object> personApply(String id, String s) throws Exception {
        return retrofitService.personApply(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 创建群组
     *
     * @param Name
     * @param password
     * @param type
     * @return Objects
     */
    public Observable<Object> CreateGroup(String Name, String password, int type, String url) throws Exception {
        return retrofitService.CreateGroup(Name, password, type, url)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 设置群组备用频道
     *
     * @param c
     * @param id
     * @return Objects
     */
    public Observable<Object> setChannel(String id, String c) throws Exception {
        return retrofitService.setChannel(id, c)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 设置管理员
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> setManager(String id, String s) throws Exception {
        return retrofitService.setManager(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改群名称
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editGroupName(String id, String s) throws Exception {
        return retrofitService.editGroupName(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改群地址
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editGroupAddress(String id, String s) throws Exception {
        return retrofitService.editGroupAddress(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改群介绍
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editGroupIntroduce(String id, String s) throws Exception {
        return retrofitService.editGroupIntroduce(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改群头像
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editGroupUrl(String id, String s) throws Exception {
        return retrofitService.editGroupUrl(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 删除群成员
     *
     * @param gid
     * @param id
     * @return Objects
     */
    public Observable<Object> groupNumDel(String gid, String id) throws Exception {
        return retrofitService.groupNumDel(gid, id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 添加群成员
     *
     * @param gid
     * @param id
     * @return Objects
     */
    public Observable<Object> groupNumAdd(String gid, String id) throws Exception {
        return retrofitService.groupNumAdd(gid, id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改好友备注
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editPersonNote(String pid, String id, String s) throws Exception {
        return retrofitService.editPersonNote(pid, id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取好友订阅专辑
     *
     * @param id
     * @return Objects
     */
    public Observable<Object> getPersonSub(String id) throws Exception {
        return retrofitService.getPersonSub(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 退出群组
     *
     * @param gId
     * @param pId
     * @return Objects
     */
    public Observable<Object> exitGroup(String gId, String pId) throws Exception {
        return retrofitService.exitGroup(gId, pId)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 移交群主
     *
     * @param gId
     * @param pId
     * @return Objects
     */
    public Observable<Object> transferManager(String gId, String pId) throws Exception {
        return retrofitService.transferManager(gId, pId)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }


    /**
     * 意见反馈
     *
     * @param information
     * @param feedback
     * @return Objects
     */
    public Observable<Object> feedback(String information, String feedback) throws Exception {
        return retrofitService.feedback(information, feedback)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改个人信息
     *
     * @param id
     * @param news
     * @param type
     * @return Objects
     */
    public Observable<Object> editUser(String id, String news, int type) throws Exception {
        // 设置返回监听参数
        if (type == 1) {
            return retrofitService.editUserForName(id, news)
                    .map(new Func1<Object, Object>() {
                        @Override
                        public Object call(Object O) {
                            return O;
                        }
                    });
        } else if (type == 2) {
            return retrofitService.editUserForIntroduce(id, news)
                    .map(new Func1<Object, Object>() {
                        @Override
                        public Object call(Object O) {
                            return O;
                        }
                    });
        } else {
            return retrofitService.editUserForAge(id, news)
                    .map(new Func1<Object, Object>() {
                        @Override
                        public Object call(Object O) {
                            return O;
                        }
                    });
        }
    }

    /**
     * 修改个人地址
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editUserAddress(String id, String s) throws Exception {
        return retrofitService.editUserForAddress(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改个人性别
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editUserSex(String id, String s) throws Exception {
        return retrofitService.editUserSex(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 修改个人头像
     *
     * @param s
     * @return Objects
     */
    public Observable<Object> editUserImg(String id, String s) throws Exception {
        return retrofitService.editUserImg(id, s)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 删除好友
     *
     * @param pid
     * @param id
     * @return Objects
     */
    public Observable<Object> delPerson(String pid, String id) throws Exception {
        return retrofitService.delPerson(pid, id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }


    /**
     * 获取消息
     *
     * @return Objects
     */
    public Observable<Object> applies() throws Exception {
        return retrofitService.applies()
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 消息==删除
     *
     * @return Objects
     */
    public Observable<Object> msgDel(String id, String type) throws Exception {
        return retrofitService.msgDel(id, type)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 消息==同意
     *
     * @return Objects
     */
    public Observable<Object> msgApply(String gid, String pid) throws Exception {
        return retrofitService.msgApply(gid, pid, "1")
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 消息==拒绝
     *
     * @return Objects
     */
    public Observable<Object> msgRefuse(String gid, String pid) throws Exception {
        return retrofitService.msgRefuse(gid, pid, "0")
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 完善个人信息
     *
     * @param id
     * @param news
     * @param url
     * @return Objects
     */
    public Observable<Object> editInformation(String id, String news, String url) throws Exception {
        // 设置返回监听参数
        return retrofitService.editUserForInformation(id, news, url)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 绑定极光
     *
     * @param id 极光id
     * @return Object
     */
    public Observable<Object> bingJG(String id) throws Exception {
        return retrofitService.bingJG(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取版本号
     */
    public Observable<Object> getVersion() throws Exception {
        return retrofitService.getVersion()
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 取消关注
     */
    public Observable<Object> delFocus(String idol_id, String id) throws Exception {
        return retrofitService.delFocus(idol_id, id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取我关注的人
     */
    public Observable<Object> getMyFocus(String id) throws Exception {
        return retrofitService.getMyFocus(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取我喜欢的列表
     */
    public Observable<Object> getMyFavorite(String id) throws Exception {
        return retrofitService.getMyFavorite(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 解散群
     */
    public Observable<Object> groupDissolve(String id) throws Exception {
        return retrofitService.groupDissolve(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }


    /**
     * 好友同意加入群组
     */
    public Observable<Object> msgAgreeEnterGroup(String id) throws Exception {
        return retrofitService.agreeEnterGroup(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 好友拒绝加入群组
     */
    public Observable<Object> msgRefuseEnterGroup(String id) throws Exception {
        return retrofitService.refuseEnterGroup(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 删除加入群组后的展示消息
     */
    public Observable<Object> msgDelEnterGroup(String id) throws Exception {
        return retrofitService.delEnterGroup(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取当前电台播放的节目
     */
    public Observable<Object> getSeek(String id) throws Exception {
        return retrofitService.getSeek(id)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 续播上传
     */
    public Observable<Object> upDataPlayMsg(String playingId, String playingType, String listType, String currentTime) throws Exception {
        return retrofitService.upDataPlayMsg(playingId, playingType, listType, currentTime)
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

    /**
     * 获取续播
     */
    public Observable<Object> getOnPlay() throws Exception {
        return retrofitService.getOnPlay()
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object O) {
                        return O;
                    }
                });
    }

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
