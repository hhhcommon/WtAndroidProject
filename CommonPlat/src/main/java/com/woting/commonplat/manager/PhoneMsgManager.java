package com.woting.commonplat.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 手机信息类   获取手机的信息
 *
 * @author 辛龙
 *         2016年7月21日
 */
public class PhoneMsgManager {

    public static int ScreenWidth;             // 手机屏幕的宽
    public static int ScreenHeight;            // 手机屏幕的高
    public static float density;               // 手机屏幕的高
    public static float densityDpi;            // 手机屏幕的密度
    public static String appVersionName = "";  // 本机的版本号
    public static String productor = "";       // 手机厂商
    public static String model = "";           // 手机型号
    public static String imei = "";            // 手机IMEI
    public static int versionCode;

    public static void getPhoneInfo(Context context) {
        getWindowInfo(context);
        getSysInfo(context);
        getAppInfo(context);
    }

    /**
     * 获得手机屏幕信息
     */
    private static void getWindowInfo(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        PhoneMsgManager.ScreenWidth = display.getWidth();
        PhoneMsgManager.ScreenHeight = display.getHeight();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        PhoneMsgManager.density = dm.density;
        PhoneMsgManager.densityDpi = dm.densityDpi;
        Log.i("woTing", "ScreenWidth=" + PhoneMsgManager.ScreenWidth + "  ScreenHeight=" + PhoneMsgManager.ScreenHeight + "  densityDpi=" + PhoneMsgManager.densityDpi);
    }

    /**
     * 获得手机硬件信息
     */
    private static void getSysInfo(Context context) {
        // PhoneMessage.imei = CommonHelper.getIMEI(context);
        // PhoneMessage.imei = CommonHelper.getLocalMacAddress(context);
        PhoneMsgManager.imei = getDeviceId(context);
        // PhoneMessage.imei = CommonHelper.getUniqueID(context);
        PhoneMsgManager.productor = Build.MANUFACTURER;
        PhoneMsgManager.model = Build.MODEL;
        // StringUtil.sdk_int = Build.VERSION.SDK_INT;
        Log.i("woTing", "加密imei=" + PhoneMsgManager.imei + ",model=" + PhoneMsgManager.model + ",productor=" + PhoneMsgManager.productor);
    }

    /**
     * 获取应用版本
     *
     * @param context
     */
    private static void getAppInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            PhoneMsgManager.appVersionName = info.versionName;
            PhoneMsgManager.versionCode = info.versionCode;
            Log.i("woTing", "appVersionName=" + PhoneMsgManager.appVersionName + ",versionCode=" + PhoneMsgManager.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机设备号IMEI
     *
     * @return MD5校验值（32位字符串）
     */
    public static String getDeviceId(Context mContext) {
        TelephonyManager TelephonyMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        String m_szImei = TelephonyMgr.getDeviceId();
        if (m_szImei == null || "".equals(m_szImei)) {
            m_szImei = "0000000000000000";
        }
        return getMD5Str(m_szImei);
    }

    /**
     * 获取手机设备号IMEI
     * @return MD5校验值（32位字符串）
     */
//	public static String getDeviceId(Context mContext) {
//		TelephonyManager TelephonyMgr = (TelephonyManager) mContext
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		String m_szImei = TelephonyMgr.getDeviceId();
//		if (m_szImei == null || "".equals(m_szImei)) {
//			m_szImei = "0000000000000000";
//		}
//		StringBuffer res = new StringBuffer();
//		res.append(m_szImei);
//		res.append(Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID));
//		WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//		res.append(wm.getConnectionInfo().getMacAddress());
//		BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		res.append(m_BluetoothAdapter.getAddress());
//		res.append(Build.BOARD).append(Build.BRAND).append(Build.CPU_ABI).append(Build.DEVICE);
//		res.append(Build.DISPLAY).append(Build.HOST).append(Build.ID).append(Build.MANUFACTURER);
//		res.append(Build.MODEL).append(Build.PRODUCT).append(Build.TAGS).append(Build.TYPE).append(Build.USER);
//		return getMD5Str(res.toString());
//	}

    /**
     * @param m_szLongID
     * @return
     */
    public static String getMD5Str(String m_szLongID) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        }
        // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }


}
