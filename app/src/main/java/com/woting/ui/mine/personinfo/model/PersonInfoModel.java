package com.woting.ui.mine.personinfo.model;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.utils.JsonEncloseUtils;
import com.woting.common.application.BSApplication;
import com.woting.common.constant.StringConstant;
import com.woting.common.net.RetrofitUtils;
import com.woting.ui.base.model.UserInfo;
import com.woting.ui.intercom.group.editgroupmessage.model.AddressModel;
import com.woting.ui.intercom.main.contacts.model.Contact;
import com.woting.ui.mine.personinfo.view.PersonalInfoFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class PersonInfoModel extends UserInfo {
    private final PersonalInfoFragment activity;

    public PersonInfoModel(PersonalInfoFragment activity) {
        this.activity = activity;
    }

    /**
     * 获取用户数据
     *
     * @return
     */
    public Contact.user getUser() {
        Contact.user user = new Contact.user();
        user.setAvatar("");
        user.setName("大美丽");
        user.setIntroduction("我是大美丽");
        user.setGender("女");
        user.setAge("18");
        user.setLocation("北京");
        return user;
    }

    /**
     * 获取地理位置
     *
     * @return
     */
    public Object[] getAddress() {
        String src = JsonEncloseUtils.getJsonDataForFile(activity.getActivity(), "address.json");
        if (src != null && !src.equals("")) {
            try {
                JSONObject js = new JSONObject(src);
                String ret = js.getString("ReturnType");
                Log.e("获取地理位置列表==ret", String.valueOf(ret));
                if (ret.equals("1001")) {
                    String msg = js.getString("CatalogData");
                    JSONTokener jsonParser = new JSONTokener(msg);
                    JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                    String d = arg1.getString("SubCata");
                    // 地理位置列表
                    List<AddressModel> list = new Gson().fromJson(d, new TypeToken<List<AddressModel>>() {
                    }.getType());
                    if (list != null && list.size() > 0) {
//                        ToastUtils.show_always(activity.getActivity(), "address数据解析成功，开始处理");
                        Object[] m = handleCityList(list);
                        return m;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 处理数据
     *
     * @param list
     * @return
     */
    private Object[] handleCityList(List<AddressModel> list) {
        Map<String, List<AddressModel.SubCata>> tempMap = new HashMap<>();// 二级联动所有数据
        Map<String, List<String>> positionMap = new HashMap<>(); // 二级联动展示数据
        List<String> provinceList = new ArrayList<>();// 省份展示数据

        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.isEmpty(list.get(i).getCatalogId()) && !TextUtils.isEmpty(list.get(i).getCatalogName())) {
                // 组装省份数据
                provinceList.add(list.get(i).getCatalogName());
                // 组装二级联动所有数据
                tempMap.put(list.get(i).getCatalogName(), list.get(i).getSubCata());
            }
        }

        // 组装二级联动展示数据
        if (tempMap != null && tempMap.size() > 0 && provinceList != null && provinceList.size() > 0) {
            for (int i = 0; i < provinceList.size(); i++) {
                // 当前省份数据不为空，获取省份里边的城市数据
                List<AddressModel.SubCata> mList = tempMap.get(provinceList.get(i));
                if (mList != null && mList.size() > 0) {
                    // 城市数据不为空，获取城市数据进行组装，组装成二级联动展示数据
                    ArrayList<String> cityList = new ArrayList<>();
                    for (int j = 0; j < mList.size(); j++) {
                        if (mList.get(j).getCatalogName() != null) {
                            cityList.add(mList.get(j).getCatalogName());
                        }
                    }
                    positionMap.put(provinceList.get(i), cityList);
                }
            }
        }
        Object[] m = {tempMap, positionMap, provinceList};

        return m;

    }

    // 获取今年年份
    private int getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        if (str == null || str.trim().equals("")) {
            int y = 2100;
            return y;
        } else {
            int y = Integer.parseInt(str);
            return y;
        }
    }

    /**
     * 组装年份数据
     * @return
     */
    public List<String> getYearList() {
        int y = getYear();
        List<String> yearList = new ArrayList<>();
        for (int i = 1930; i <= y; i++) {
            yearList.add(""+i + "");
        }
        return yearList;
    }

    /**
     * 组装月份数据
     * @return
     */
    public List<String> getMonthList() {
        List<String> MonthList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            MonthList.add(" "+"0" + i + "");
        }
        MonthList.add(" "+10 + "");
        MonthList.add(" "+11 + "");
        MonthList.add(" "+12 + "");
        return MonthList;
    }

    /**
     * 组装天数
     * @return
     */
    public List<String> getDayList31() {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            dayList.add(" "+"0" + i + "");
        }
        for (int i = 10; i < 32; i++) {
            dayList.add(" "+i + "");
        }
        return dayList;
    }

    /**
     * 组装天数
     * @return
     */
    public List<String> getDayList30() {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            dayList.add(" "+"0" + i + "");
        }
        for (int i = 10; i < 31; i++) {
            dayList.add(" "+i + "");
        }
        return dayList;
    }

    /**
     * 组装天数
     * @return
     */
    public List<String> getDayList29() {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            dayList.add(" "+"0" + i + "");
        }
        for (int i = 10; i < 30; i++) {
            dayList.add(" "+i + "");
        }
        return dayList;
    }

    /**
     * 组装天数
     * @return
     */
    public List<String> getDayList28() {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            dayList.add(" "+"0" + i + "");
        }
        for (int i = 10; i < 29; i++) {
            dayList.add(" "+i + "");
        }
        return dayList;
    }

    public static int getAgeFromBirthTime(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

    /**
     * 修改地理位置
     *
     * @param address
     * @param listener 监听
     */
    public void loadNewsForAddress(String address, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        try {
            RetrofitUtils.getInstance().editUserAddress(id, address)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改个人地理位置返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    /**
     * 修改性别
     *
     * @param sex
     * @param listener 监听
     */
    public void loadNewsForSex(String sex, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        try {
            RetrofitUtils.getInstance().editUserSex(id, sex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改个人性别返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }


    /**
     * 修改年龄
     * @param news
     * @param type
     * @param listener 监听
     */
    public void loadNews(String id, String news,int type, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().editUser(id,news,type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改用户信息返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }
    /**
     * 修改头像
     *
     * @param img
     * @param listener 监听
     */
    public void loadNewsForImg(String img, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        try {
            RetrofitUtils.getInstance().editUserImg(id, img)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改个人性别返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    // API 19 以下获取图片路径的方法
    public String getFilePath_below19(final Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        System.out.println("***************" + column_index);
        cursor.moveToFirst();

        // 最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
        String path = cursor.getString(column_index);
        System.out.println("path:" + path);
        return path;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath_above19(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
