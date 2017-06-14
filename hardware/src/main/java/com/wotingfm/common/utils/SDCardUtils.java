package com.wotingfm.common.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by amine on 2017/6/13.
 */

public class SDCardUtils {

    public static  String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            return null;
        }
        return sdDir.toString();
    }
}
