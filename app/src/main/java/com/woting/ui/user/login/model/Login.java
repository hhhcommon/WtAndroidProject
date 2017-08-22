package com.woting.ui.user.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * 测试代码
 * 作者：xinLong on 2017/6/8 11:32
 * 邮箱：645700751@qq.com
 */
public class Login {

    // 下面变量的定义要与接口中的字段名字保持一致
    // 如上面的错误码字段，你就像定义为code，而服务器返回的是error_code，这个时候就应该这么写：
    @SerializedName("error_code")
    public int code;
    public int total;
    public String reason;
//        public List<FamousInfo> result;
//
//
//        public static class FamousInfo {
//            public String famous_name;
//            public String famous_saying;
//        }

}
