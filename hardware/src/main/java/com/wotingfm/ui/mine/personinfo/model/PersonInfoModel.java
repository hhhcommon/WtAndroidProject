package com.wotingfm.ui.mine.personinfo.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.utils.JsonEncloseUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.AddressModel;
import com.wotingfm.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.mine.fm.model.FMInfo;
import com.wotingfm.ui.mine.personinfo.view.PersonalInfoFragment;

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
public class PersonInfoModel {
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
                        ToastUtils.show_always(activity.getActivity(), "address数据解析成功，开始处理");
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

    /**
     * 修改地理位置
     *
     * @param address
     * @param listener 监听
     */
    public void loadNewsForAddress(String address, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        RetrofitUtils.getInstance().editUserAddress(id, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("修改个人地理位置返回数据", new Gson().toJson(o));
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
    }

    /**
     * 修改性别
     *
     * @param sex
     * @param listener 监听
     */
    public void loadNewsForSex(String sex, final OnLoadInterface listener) {
        String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
        RetrofitUtils.getInstance().editUserSex(id, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("修改个人性别返回数据", new Gson().toJson(o));
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
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
