package com.wotingfm.ui.intercom.group.groupchat.model;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.login.model.Login;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupChatModel extends UserInfo {

    public List<GroupChat> getData() {
        List<GroupChat> list=new ArrayList<>();
        GroupChat GC= new GroupChat();
        GC.setGroupNumber("12");
        GC.setName("我创建的群");
        GC.setPerson(getGroupList());
        list.add(GC);
        GroupChat GC1= new GroupChat();
        GC1.setGroupNumber("12");
        GC1.setName("我加入的群");
        GC1.setPerson(getGroupList());
        list.add(GC1);
        return list;
    }

    private  ArrayList<GroupChat.news>  getGroupList() {
        ArrayList<GroupChat.news> srcList_G = new ArrayList<>();
        srcList_G.add(getGroup("兔子群-1","1"));
        srcList_G.add(getGroup("野鸡大学群-2","2"));
        srcList_G.add(getGroup("三国群-3","3"));
        srcList_G.add(getGroup("相亲交友群-4","4"));
        srcList_G.add(getGroup("一路一带群-5","5"));
        srcList_G.add(getGroup("丝绸之路群-6","6"));
        srcList_G.add(getGroup("辽宁航母群-7","7"));
        srcList_G.add(getGroup("G20峰会群-8","8"));
        srcList_G.add(getGroup("我听郊游群-9","9"));
        srcList_G.add(getGroup("临时群-10","10"));
        srcList_G.add(getGroup("临时群-11","11"));
        srcList_G.add(getGroup("临时群-12","12"));

        return  srcList_G;
    }

    // 生成一条组数据
    private  GroupChat.news getGroup(String name, String id) {
        GroupChat.news group = new GroupChat.news();
        group.setName(name);
        group.setNickName(name);
        group.setId(id);
        return group;
    }

    /**
     * 进行数据交互
     *
     * @param userName
     * @param password
     * @param listener 监听
     */
    public void loadNews(String userName, String password, final OnLoadInterface listener) {
//        RetrofitUtils.getInstance().retrofitService.login(userName, password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Login>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        listener.onFailure("");
//                    }
//
//                    @Override
//                    public void onNext(Login login) {
//                        //填充UI
//                        listener.onSuccess(login);
//                    }
//                });
    }

    public interface OnLoadInterface {
        void onSuccess(Login login);

        void onFailure(String msg);
    }

}
