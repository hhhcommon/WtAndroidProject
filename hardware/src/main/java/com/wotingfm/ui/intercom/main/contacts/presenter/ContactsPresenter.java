package com.wotingfm.ui.intercom.main.contacts.presenter;

import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.model.ContactsModel;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;

import java.util.Collections;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ContactsPresenter {

    private final ContactsFragment activity;
    private final ContactsModel model;
    private final CharacterParser characterParser;
    private final PinyinComparator pinyinComparator;


    public ContactsPresenter(ContactsFragment activity) {
        this.activity = activity;
        this.model = new ContactsModel();

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }

    /**
     * 获取数据，数据适配
     */
    public void getData() {

                if(CommonUtils.isLogin()){
                    if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {

                    }
                }else{

                }
    }


    /**
     * 发送网络请求,获取好友
     */
    public void getFriends() {
        try {
            List<Contact.user> userList = GetTestData.getFriendList();
            if(userList!=null&&userList.size()>0){
                // 根据 a - z 进行排序源数据
                List<Contact.user> srcList=filledData(userList);
                Collections.sort(srcList, pinyinComparator);
                activity.setData(srcList);
            }else{
                activity.setData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.setData();
        }
    }

//    /**
//     * 发送网络请求,获取好友
//     */
//    public void getFriends() {
//        String s="";
//        model.loadNews(s, new ContactsModel.OnLoadInterface  () {
//            @Override
//            public void onSuccess(Contact result) {
////                loginView.removeDialog();
//                dealSuccess(result);
//            }
//
//            @Override
//            public void onFailure(String msg) {
////                loginView.removeDialog();
////                ToastUtils.showVolleyError(loginView);
//            }
//        });
//    }

    // 处理返回数据
    private void dealSuccess(Contact result) {
        try {
            List<Contact.user> userList = result.friends;
            if(userList!=null&&userList.size()>0){
                // 根据 a - z 进行排序源数据
                List<Contact.user> srcList=filledData(userList);
                Collections.sort(srcList, pinyinComparator);
                activity.setData(srcList);
            }else{
                activity.setData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.setData();
        }
    }

    // 为 ListView 填充数据
    private List<Contact.user>  filledData(List<Contact.user> person) {
        for (int i = 0; i < person.size(); i++) {
            person.get(i).setName(person.get(i).getNickName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(person.get(i).getNickName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                person.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                person.get(i).setSortLetters("#");
            }
        }
        return person;
    }

}
