package com.wotingfm.common.helper;


import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.config.GlobalUrlConfig;
import com.wotingfm.common.constant.CollocationConstant;

/**
 * 配置设置
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class CollocationHelper {

    private static String _PCDType = "1";
    private static String _socketPort = "16789";
//    private static String _uploadBaseUrl = "http://123.56.254.75:908/CM/";
    private static String _uploadBaseUrl = "http://182.92.175.134:908/CM/";
    private static String _socketUrl = "182.92.175.134";//生产服务器地址
//    private static String _socketUrl = "123.56.254.75";//测试服务器地址
//    	private static String _socketUrl = "192.168.5.17";//
    private static String _baseUrl = "http://182.92.175.134:808/";//生产服务器地址
//    private static String _baseUrl = "http://123.56.254.75:808/";//测试服务器地址
    //  private static String _baseUrl = "http://192.168.5.17:808/";

    public static String apkUrl= "http://www.wotingfm.com/download/WoTing.apk";

    public static void setCollocation() {
        // 是不是读取配置文件
        if (GlobalStateConfig.isCollocation) {
            // 是否弹出提示框，0提示，1不提示
            String isToast = BSApplication.SharedPreferences.getString(CollocationConstant.isToast, "1");
            //PersonClientDevice(个人客户端设备) 终端类型1=app,2=设备，3=pc
            String PCDType = BSApplication.SharedPreferences.getString(CollocationConstant.PCDType, "1");
            //socket请求路径
            String socketUrl = BSApplication.SharedPreferences.getString(CollocationConstant.socketUrl, "");
            //socket端口号
            String socketPort = BSApplication.SharedPreferences.getString(CollocationConstant.socketPort, "");
            //http请求总url
            String baseUrl = BSApplication.SharedPreferences.getString(CollocationConstant.baseUrl, "");
            //上传文件总接口路径
            String uploadBaseUrl = BSApplication.SharedPreferences.getString(CollocationConstant.uploadBaseUrl, "");

            if (isToast != null && !isToast.equals("") && !isToast.equals("isToast") && isToast.trim().equals("0")) {
                GlobalStateConfig.isToast = true;
            } else {
                GlobalStateConfig.isToast = false;
            }

            if (PCDType != null && !PCDType.equals("")) {
                GlobalStateConfig.PCDType = Integer.parseInt(PCDType);
            } else {
                GlobalStateConfig.PCDType = Integer.parseInt(_PCDType);
            }

            if (socketUrl != null && !socketUrl.equals("")) {
                GlobalUrlConfig.socketUrl = socketUrl;
            } else {
                GlobalUrlConfig.socketUrl = _socketUrl;
            }

            if (socketPort != null && !socketPort.equals("")) {
                GlobalUrlConfig.socketPort = Integer.parseInt(socketPort);
            } else {
                GlobalUrlConfig.socketPort = Integer.parseInt(_socketPort);
            }

            if (baseUrl != null && !baseUrl.equals("")) {
                GlobalUrlConfig.baseUrl = baseUrl;
                GlobalUrlConfig.imageurl = baseUrl + "wt/";
            } else {
                GlobalUrlConfig.baseUrl = _baseUrl;
                GlobalUrlConfig.imageurl = _baseUrl + "wt/";
            }

            if (uploadBaseUrl != null && !uploadBaseUrl.equals("")) {
                GlobalUrlConfig.uploadBaseUrl = uploadBaseUrl;
            } else {
                GlobalUrlConfig.uploadBaseUrl = _uploadBaseUrl;
            }

            GlobalUrlConfig.apkUrl = apkUrl;
        } else {
            GlobalStateConfig.isToast = false;
            GlobalStateConfig.PCDType = Integer.parseInt(_PCDType);

            GlobalUrlConfig.apkUrl = apkUrl;
            GlobalUrlConfig.socketUrl = _socketUrl;
            GlobalUrlConfig.socketPort = Integer.parseInt(_socketPort);
            GlobalUrlConfig.baseUrl = _baseUrl;
            GlobalUrlConfig.uploadBaseUrl = _uploadBaseUrl;
            GlobalUrlConfig.imageurl = _baseUrl + "wt/";
        }
    }
}
