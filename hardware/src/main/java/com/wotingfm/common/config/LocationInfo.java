package com.wotingfm.common.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.location.gaode.GDLocation;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;

import static com.woting.commonplat.constant.BroadcastConstants.CITY_CHANGE;


/**
 * LocationInfo
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class LocationInfo implements GDLocation.Location {
    private Context context;
    private GDLocation mGDLocation;
    private String region;

    public LocationInfo(Context context) {
        this.context = context;
        startLocation();
    }

    // 开启定位
    private void startLocation() {
        mGDLocation = GDLocation.getInstance(context, this);
        mGDLocation.startLocation();
    }

    // 定位成功需要将定位信息保存
    @Override
    public void locationSuccess(AMapLocation amapLocation) {
        String city = amapLocation.getCity();
        String district = amapLocation.getDistrict();
        String adCode = amapLocation.getAdCode();// 地区编码
        String latitude = String.valueOf(amapLocation.getLatitude());
        String longitude = String.valueOf(amapLocation.getLongitude());

        if (!TextUtils.isEmpty(city)) {
            region = city;
        }

        if (!TextUtils.isEmpty(district)) {
            if (region == null) {
                region = district;
            } else {
                region += district;
            }
        }

        if (!TextUtils.isEmpty(amapLocation.getStreet())) {
            if (region == null) {
                region = amapLocation.getStreet();
            } else {
                region += amapLocation.getStreet();
            }
        }

        if (!TextUtils.isEmpty(amapLocation.getAddress())) {
            if (region == null) {
                region = amapLocation.getAddress();
            } else {
                region += amapLocation.getAddress();
            }
        }

        if (!TextUtils.isEmpty(region)) {
            GlobalAddressConfig.Region = region;
        } else {
            GlobalAddressConfig.Region = "未获取到本次地理位置信息";
        }

        if (GlobalAddressConfig.District == null || !GlobalAddressConfig.District.equals(district)) {
            GlobalAddressConfig.District = district;
        }

        if (GlobalAddressConfig.latitude == null || !GlobalAddressConfig.latitude.equals(latitude)) {
            GlobalAddressConfig.latitude = latitude;
        }

        if (GlobalAddressConfig.longitude == null || !GlobalAddressConfig.longitude.equals(longitude)) {
            GlobalAddressConfig.longitude = longitude;
        }

        if (GlobalAddressConfig.AdCode == null || !GlobalAddressConfig.AdCode.equals(adCode)) {
            GlobalAddressConfig.AdCode = adCode;
        }

        if (GlobalAddressConfig.CityName == null || !GlobalAddressConfig.CityName.equals(city)) {
            GlobalAddressConfig.CityName = city;
        }
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.IS_LOCATION, "true");
        et.putString(StringConstant.CITY_NAME, city);
        et.putString(StringConstant.CITY_ID, GlobalAddressConfig.AdCode);
        et.putString(StringConstant.LATITUDE, String.valueOf(latitude));
        et.putString(StringConstant.LONGITUDE, String.valueOf(longitude));
        et.commit();
        sendBroadcast();// 发送广播
    }

    // 发送广播
    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction(CITY_CHANGE);
        context.sendBroadcast(intent);
    }

    @Override
    public void locationFail(AMapLocation amapLocation) {
        Log.e("TAG", "定位失败");
        SharedPreferences.Editor et = BSApplication.SharedPreferences.edit();
        et.putString(StringConstant.IS_LOCATION, "false");
        et.commit();
        sendBroadcast();// 发送广播
    }

    public void stopLocation() {
        if (mGDLocation != null)
            mGDLocation.stopLocation();
    }
}
