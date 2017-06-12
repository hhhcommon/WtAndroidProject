package com.wotingfm.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * toast提示工具类*
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ToastUtils {
	/**长时间提示*/
	public static void show_long(Context context, String content){
//		if(GlobalConfig.isToast){
//			Toast.makeText(context, content, Toast.LENGTH_LONG).show();
//		}
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();

	}
	/**短时间提示*/
	public static void show_short(Context context, String content){
//		if(GlobalConfig.isToast){
//			Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
//		}
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

	}

	/**一直提示*/
	public static void show_always(Context context, String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

    /**Volley 请求发生错误或连接服务器失败*/
    public static void showVolleyError(Context context){
//        if(GlobalConfig.isToast) {
//            Toast.makeText(context, "连接服务器失败!", Toast.LENGTH_SHORT).show();
//        }
        Toast.makeText(context, "连接服务器失败!", Toast.LENGTH_SHORT).show();
    }

    /**没有网络时对用户的提示*/
    public static void showNoNetwork(Context context){
//        if(GlobalConfig.isToast) {
//            Toast.makeText(context, "连接服务器失败!", Toast.LENGTH_SHORT).show();
//        }
        Toast.makeText(context, "网路连接失败，请检查网络设置!", Toast.LENGTH_SHORT).show();
    }
}
