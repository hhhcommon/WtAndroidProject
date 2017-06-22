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
public class UserInfo  {

    /**
     * 保存用户信息到本地
     *  // id=9f5d4ddb6fe1,
        // userName=null,
        // userNum=xinlong666,
        // loginName=null,
        // nickName=小苹果本尊,
        // userSign=null,
           password=123456,
        // mailAddress=null,
        // mainPhoneNum=13260018007,
        // phoneNumIsPub=0.0,
        // birthday=1992-10-25 00:00:00,
        // starSign=天蝎座,
         userType=1.0,
         userClass=0.0,
         userState=0.0,
         rUserType=0.0,
         aUserType=0.0,
         portraitBig=##userimg##user_42f7.png,
         portraitMini=##userimg##user_42f7.png,
         descn=null,
         homepage=null,
         cTime=2017-04-08 11:19:23,
         lmTime=2017-05-05 12:41:26,
         gender=0.0,
         fans_count=0.0,
         idols_count=0.0,
         fans=[],
         idols=[]
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

        // 此字段不用了
        try {
            String userName = userInfo.getString("userName");
            if (userName != null && !userName.equals("")) {
                if (userName.equals("&null")) {
                    et.putString(StringConstant.USER_NAME, "");
                } else {
                    et.putString(StringConstant.USER_NAME, userName);
                }
            } else {
                et.putString(StringConstant.USER_NAME, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_NAME, "");
        }

        try {
            String UserNum = userInfo.getString("userNum");
            et.putString(StringConstant.USER_NUM, UserNum);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_NUM, "");
        }

        try {
            String loginName = userInfo.getString("loginName");
            if (loginName != null && !loginName.equals("")) {
                if (loginName.equals("&null")) {
                    et.putString(StringConstant.LOGIN_NAME, "");
                } else {
                    et.putString(StringConstant.LOGIN_NAME, loginName);
                }
            } else {
                et.putString(StringConstant.LOGIN_NAME, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.LOGIN_NAME, "");
        }

        try {
            String nickName = userInfo.getString("nickName");
            if (nickName != null && !nickName.equals("")) {
                if (nickName.equals("&null")) {
                    et.putString(StringConstant.NICK_NAME, "");
                } else {
                    et.putString(StringConstant.NICK_NAME, nickName);
                }
            } else {
                et.putString(StringConstant.NICK_NAME, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.NICK_NAME, "");
        }

        try {
            String starSign = userInfo.getString("starSign");// 星座
            et.putString(StringConstant.STAR_SIGN, starSign);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.STAR_SIGN, "");
        }

        try {
            String birthday = userInfo.getString("birthday");// 生日
            et.putString(StringConstant.BIRTHDAY, birthday);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.BIRTHDAY, "");
        }

        try {
            String isPub = userInfo.getString("PhoneNumIsPub");
            et.putString(StringConstant.PHONE_NUMBER_FIND, isPub);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.PHONE_NUMBER_FIND, "0");
        }

        try {
            String phoneNumber = userInfo.getString("mainPhoneNum");
            et.putString(StringConstant.USER_PHONE_NUMBER, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_PHONE_NUMBER, "");
        }

        try {
            String email = userInfo.getString("mailAddress");// 邮箱
            if (email != null && !email.equals("")) {
                if (email.equals("&null")) {
                    et.putString(StringConstant.EMAIL, "");
                } else {
                    et.putString(StringConstant.EMAIL, email);
                }
            } else {
                et.putString(StringConstant.EMAIL, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.EMAIL, "");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

        try {
            String imageUrl = userInfo.getString("Portrait");
            et.putString(StringConstant.PORTRAIT, imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.PORTRAIT, "");
        }

        try {
            String userSign = userInfo.getString("userSign");// 签名
            if (userSign != null && !userSign.equals("")) {
                if (userSign.equals("&null")) {
                    et.putString(StringConstant.USER_SIGN, "");
                } else {
                    et.putString(StringConstant.USER_SIGN, userSign);
                }
            } else {
                et.putString(StringConstant.USER_SIGN, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            et.putString(StringConstant.USER_SIGN, "");
        }

        if (!et.commit()) {
            Log.e("commit", "数据 commit 失败!");
        }
    }

    /**
     * 保存token
     * @param token
     */
    public void saveToken(String token) {
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.TOKEN,token);
        if (!et.commit()) {
            Log.v("commit", "token commit 失败!");
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
        et.putString(StringConstant.PORTRAIT, "");
        et.putString(StringConstant.USER_PHONE_NUMBER, "");
        et.putString(StringConstant.USER_NUM, "");
        et.putString(StringConstant.GENDERUSR, "");
        et.putString(StringConstant.PHONE_NUMBER_FIND, "0");
        et.putString(StringConstant.EMAIL, "");
        et.putString(StringConstant.REGION, "");
        et.putString(StringConstant.BIRTHDAY, "");
        et.putString(StringConstant.USER_SIGN, "");
        et.putString(StringConstant.STAR_SIGN, "");
//        et.putString(StringConstant.AGE, "");
        et.putString(StringConstant.NICK_NAME, "");
        if (!et.commit()) {
            Log.v("commit", "数据 commit 失败!");
        }
    }

}
