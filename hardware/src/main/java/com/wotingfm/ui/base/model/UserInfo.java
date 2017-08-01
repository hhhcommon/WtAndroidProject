package com.wotingfm.ui.base.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/31 12:08
 * 邮箱：645700751@qq.com
 */
public class UserInfo {

    /**
     * 保存用户信息到本地
     * // id=9f5d4ddb6fe1,
     * // userName=null,
     * // userNum=xinlong666,
     * // loginName=null,
     * // nickName=小苹果本尊,
     * // userSign=null,
     * password=123456,
     * // mailAddress=null,
     * // mainPhoneNum=13260018007,
     * // phoneNumIsPub=0.0,
     * // birthday=1992-10-25 00:00:00,
     * // starSign=天蝎座,
     * userType=1.0,
     * userClass=0.0,
     * userState=0.0,
     * rUserType=0.0,
     * aUserType=0.0,
     * portraitBig=##userimg##user_42f7.png,
     * portraitMini=##userimg##user_42f7.png,
     * descn=null,
     * homepage=null,
     * cTime=2017-04-08 11:19:23,
     * lmTime=2017-05-05 12:41:26,
     * gender=0.0,
     * fans_count=0.0,
     * idols_count=0.0,
     * fans=[],
     * idols=[]
     *
     * @param userInfo
     */
    public void saveUserInfo(JSONObject userInfo) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.IS_LOGIN, "true");

        try {
            String userId = userInfo.getString("id");// 用户 ID
            et.putString(StringConstant.USER_ID, userId);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_ID, "");
        }

        try {
            String UserNum = userInfo.getString("userNum");
            if (UserNum != null && !UserNum.equals("")&&!UserNum.equals("null")) {
                et.putString(StringConstant.USER_NUM, UserNum);
            } else {
                et.putString(StringConstant.USER_NUM, "******");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_NUM, "******");
        }

        try {
            String nickName = userInfo.getString("nickName");
            if (nickName != null && !nickName.equals("")&&!nickName.equals("null")) {
                et.putString(StringConstant.NICK_NAME, nickName);
            } else {
                et.putString(StringConstant.NICK_NAME, "暂未填写");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.NICK_NAME, "暂未填写");
        }

        try {
            String age = userInfo.getString("userAge");// 年龄
            if (age != null && !age.equals("")) {
                et.putString(StringConstant.AGE, age);
            } else {
                et.putString(StringConstant.AGE, "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.AGE, "0");
        }

        try {
            String phoneNumber = userInfo.getString("mainPhoneNum");
            et.putString(StringConstant.USER_PHONE_NUMBER, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_PHONE_NUMBER, "");
        }

        try {
            String imageUrl = userInfo.getString("portraitMini");
            if (imageUrl != null && !imageUrl.equals("")&& !imageUrl.equals("null")) {
                et.putString(StringConstant.PORTRAIT, imageUrl);
            } else {
                et.putString(StringConstant.PORTRAIT, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.PORTRAIT, "");
        }

        try {
            String userSign = userInfo.getString("userSign");// 签名
            if (userSign != null && !userSign.equals("")&& !userSign.equals("null")) {
                et.putString(StringConstant.USER_SIGN, userSign);
            } else {
                et.putString(StringConstant.USER_SIGN, "您还没有填写");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_SIGN, "您还没有填写");
        }

        try {
            String area = userInfo.getString("area");// 地区
            if (area != null && !area.equals("")) {
                et.putString(StringConstant.REGION, area);
            } else {
                et.putString(StringConstant.REGION, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.REGION, "");
        }
        try {
            String gender = userInfo.getString("gender");// 性别
            if (gender != null && !gender.equals("")) {
                if(gender.equals("1")){
                    et.putString(StringConstant.GENDER, "男");
                }else{
                    et.putString(StringConstant.GENDER, "女");
                }
            } else {
                et.putString(StringConstant.GENDER, "男");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.GENDER, "男");
        }

        if (!et.commit()) {
            Log.e("commit", "数据 commit 失败!");
        }
    }

    /**
     * 更改一下登录状态
     */
    public void unRegisterLogin() {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.IS_LOGIN, "false");
        et.putString(StringConstant.USER_ID, "");
        et.putString(StringConstant.USER_NUM, "");
        et.putString(StringConstant.NICK_NAME, "");
        et.putString(StringConstant.PORTRAIT, "");
        et.putString(StringConstant.USER_PHONE_NUMBER, "");
        et.putString(StringConstant.GENDER, "");
        et.putString(StringConstant.AGE, "");
        et.putString(StringConstant.REGION, "");
        et.putString(StringConstant.USER_SIGN, "");

        if (!et.commit()) {
            Log.v("commit", "数据 commit 失败!");
        }
    }

    /**
     * 保存登录后数据==测试数据
     */
    public void saveTestLogin() {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.IS_LOGIN, "true");
        et.putString(StringConstant.USER_ID, "666666");
        et.putString(StringConstant.USER_NUM, "666666");
        et.putString(StringConstant.NICK_NAME, "测试==对象");
        et.putString(StringConstant.PORTRAIT, "");
        et.putString(StringConstant.USER_PHONE_NUMBER, "13260018007");
        et.putString(StringConstant.GENDER, "男");
        et.putString(StringConstant.AGE, "18");
        et.putString(StringConstant.REGION, "北京");
        et.putString(StringConstant.USER_SIGN, "我还是一个宝宝");
        if (!et.commit()) {
            Log.v("commit", "数据 commit 失败!");
        }
    }

    /**
     * 保存token
     *
     * @param token
     */
    public void saveToken(String token) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.TOKEN, token);
        if (!et.commit()) {
            Log.v("commit", "token commit 失败!");
        }
    }

    /**
     * 保存用户昵称
     *
     * @param name
     */
    public void saveName(String name) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.NICK_NAME, name);
        if (!et.commit()) {
            Log.v("commit", "name commit 失败!");
        }
    }

    /**
     * 保存用户手机号
     *
     * @param num
     */
    public void savePhoneNumber(String num) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.USER_PHONE_NUMBER, num);
        if (!et.commit()) {
            Log.v("commit", "num commit 失败!");
        }
    }

    /**
     * 保存用户性别
     *
     * @param sex
     */
    public void saveSex(String sex) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.GENDER, sex);
        if (!et.commit()) {
            Log.v("commit", "sex commit 失败!");
        }
    }

    /**
     * 保存用户address
     *
     * @param address
     */
    public void saveAddress(String address) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.REGION, address);
        if (!et.commit()) {
            Log.v("commit", "address commit 失败!");
        }
    }

    /**
     * 保存用户sign
     *
     * @param sign
     */
    public void saveSign(String sign) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.USER_SIGN, sign);
        if (!et.commit()) {
            Log.v("commit", "sign commit 失败!");
        }
    }

    /**
     * 保存用户img
     *
     * @param img
     */
    public void saveImg(String img) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.PORTRAIT, img);
        if (!et.commit()) {
            Log.v("commit", "sign commit 失败!");
        }
    }

    /**
     * 保存用户age
     *
     * @param age
     */
    public void saveAge(String age) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.AGE, age);
        if (!et.commit()) {
            Log.v("commit", "sign commit 失败!");
        }
    }
}
