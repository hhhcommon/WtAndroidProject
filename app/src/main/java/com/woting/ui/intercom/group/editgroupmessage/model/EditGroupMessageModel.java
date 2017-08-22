package com.woting.ui.intercom.group.editgroupmessage.model;

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
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.utils.JsonEncloseUtils;
import com.woting.common.net.RetrofitUtils;
import com.woting.ui.base.model.UserInfo;
import com.woting.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
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
public class EditGroupMessageModel extends UserInfo {
    private final EditGroupMessageFragment activity;

    public EditGroupMessageModel(EditGroupMessageFragment activity) {
        this.activity = activity;
    }

    /**
     * 获取地理位置
     *
     * @return
     */
    public Object[] getAddress() {
       String src= JsonEncloseUtils.getJsonDataForFile(activity.getActivity(),"address.json");
        if(src!=null&&!src.equals("")){
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
//                        ToastUtils.show_always(activity.getActivity(),"address数据解析成功，开始处理");
                        Object[]  m= handleCityList(list);
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
        }else{
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
        Object[] m = {tempMap,positionMap,provinceList};

        return m;

    }

    /**
     * 修改群地址
     * @param s
     * @param listener 监听
     */
    public void loadNews( String id,String s, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().editGroupAddress(id,s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改群地址返回数据",o.toString());
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
     * 修改群头像
     * @param s
     * @param listener 监听
     */
    public void loadNewsForImg( String id,String s, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().editGroupUrl(id,s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改群头像返回数据",o.toString());
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
