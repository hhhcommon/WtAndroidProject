package com.wotingfm.ui.mine.message.notify.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MsgNotifyModel {


    /**
     * 组装展示数据
     *
     * @param s_msg
     * @return
     */
    public List<Msg> assemblyData(SrcMsg s_msg) {
        List<Msg> msg = new ArrayList<>();

        try {
            List<SrcMsg.applyMes> apply = s_msg.getGroup_apply_mes();
            if (apply != null && apply.size() > 0) {
                List<Msg> _msg = assemblyApply(apply);
                if (_msg != null && _msg.size() > 0) {
                    msg.addAll(_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<SrcMsg.approveMes> approve = s_msg.getGroup_approve_mes();
            if (approve != null && approve.size() > 0) {
                List<Msg> _msg = assemblyApprove(approve);
                if (_msg != null && _msg.size() > 0) {
                    msg.addAll(_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<SrcMsg.MesPerson> person = s_msg.getFriend_apply_mes();
            if (person != null && person.size() > 0) {
                List<Msg> _msg = assemblyMsgForPerson(person);
                if (_msg != null && _msg.size() > 0) {
                    msg.addAll(_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 按照时间进行排序
        if(msg!=null&&msg.size()>0){
            return sorting(msg);
        }else{
            return msg;
        }

    }

    // 组消息（主动）
    private List<Msg> assemblyApply(List<SrcMsg.applyMes> apply) {
        List<Msg> msg = new ArrayList<>();
        for (int i = 0; i < apply.size(); i++) {
            Msg _msg = new Msg();

            if (apply.get(i).getId() != null && !apply.get(i).getId().equals("")) {
                _msg.setMsg_id(apply.get(i).getId());
            } else {
                _msg.setMsg_id("");
            }
            _msg.setMsg_type("3");

            if (apply.get(i).getAvatar() != null && !apply.get(i).getAvatar().equals("")) {
                _msg.setAvatar(apply.get(i).getAvatar());
            } else {
                _msg.setAvatar("");
            }

            if (apply.get(i).getGroup_name() != null && !apply.get(i).getGroup_name().equals("")) {
                _msg.setTitle(apply.get(i).getGroup_name());
            } else {
                _msg.setTitle("群消息");
            }

            if (apply.get(i).getStatus() != null) {
                if (apply.get(i).getStatus().equals("1")) {
                    _msg.setNews("已通过了您的加群申请");
                } else {
                    _msg.setNews("拒绝了您的加群申请");
                }
            } else {
                _msg.setNews("拒绝了您的加群申请");
            }

            if (apply.get(i).getApprove_at() != null && !apply.get(i).getApprove_at().equals("")) {
                _msg.setTime(apply.get(i).getApprove_at());
            } else {
                _msg.setTime("");
            }
            msg.add(_msg);
        }

        return msg;
    }

    // 组消息（接收）
    private List<Msg> assemblyApprove(List<SrcMsg.approveMes> approve) {
        List<Msg> msg = new ArrayList<>();
        for (int i = 0; i < approve.size(); i++) {
            Msg _msg = new Msg();
            if (approve.get(i).getId() != null && !approve.get(i).getId().equals("")) {
                _msg.setMsg_id(approve.get(i).getId());
            } else {
                _msg.setMsg_id("");
            }
            _msg.setMsg_type("4");

            if (approve.get(i).getAvatar() != null && !approve.get(i).getAvatar().equals("")) {
                _msg.setAvatar(approve.get(i).getAvatar());
            } else {
                _msg.setAvatar("");
            }

            if (approve.get(i).getUser_name() != null && !approve.get(i).getUser_name().equals("")) {
                _msg.setTitle(approve.get(i).getUser_name());
            } else {
                _msg.setTitle("群消息");
            }

            if (approve.get(i).getGroup_name()!= null && !approve.get(i).getGroup_name().equals("")) {
                _msg.setNews("申请加入 "+approve.get(i).getGroup_name());
            } else {
                _msg.setNews("申请加入 ");
            }

            if (approve.get(i).getIntroduce() != null && !approve.get(i).getIntroduce().equals("")) {
                _msg.setIntroduce("验证信息： "+approve.get(i).getIntroduce());
            } else {
                _msg.setIntroduce("验证信息： ");
            }

            if (approve.get(i).getGroup_id() != null && !approve.get(i).getGroup_id().equals("")) {
                _msg.setGroup_id(approve.get(i).getGroup_id());
            } else {
                _msg.setGroup_id("");
            }

            if (approve.get(i).getUser_id() != null && !approve.get(i).getUser_id().equals("")) {
                _msg.setApply_id(approve.get(i).getUser_id());
            } else {
                _msg.setApply_id("");
            }

            if (approve.get(i).getStatus()!= null && !approve.get(i).getStatus().equals("")) {
                _msg.setStatus(approve.get(i).getStatus());
            } else {
                _msg.setStatus("0");
            }

            if (approve.get(i).getApprove_at() != null && !approve.get(i).getApprove_at().equals("")) {
                _msg.setTime(approve.get(i).getApprove_at());
            } else {
                _msg.setTime("");
            }
            msg.add(_msg);
        }

        return msg;
    }

    // 组装好友展示消息
    private List<Msg> assemblyMsgForPerson(List<SrcMsg.MesPerson> person) {
        List<Msg> msg = new ArrayList<>();
        for (int i = 0; i < person.size(); i++) {
            Msg _msg = new Msg();
            if (person.get(i).getId() != null && !person.get(i).getId().equals("")) {
                _msg.setMsg_id(person.get(i).getId());
            } else {
                _msg.setMsg_id("");
            }
            _msg.setMsg_type("2");

            if (person.get(i).getAvatar() != null && !person.get(i).getAvatar().equals("")) {
                _msg.setAvatar(person.get(i).getAvatar());
            } else {
                _msg.setAvatar("");
            }

            if (person.get(i).getReceiver_name() != null && !person.get(i).getReceiver_name().equals("")) {
                _msg.setTitle(person.get(i).getReceiver_name());
            } else {
                _msg.setTitle("新消息");
            }

            _msg.setNews("已通过了您的好友申请，赶快找TA聊天吧~");

            if (person.get(i).getApproved_at() != null && !person.get(i).getApproved_at().equals("")) {
                _msg.setTime(person.get(i).getApproved_at());
            } else {
                _msg.setTime("");
            }
            msg.add(_msg);
        }

        return msg;
    }


    /**
     * 根据时间进行排序
     *
     * @param msg
     * @return
     */
    private List<Msg> sorting(List<Msg> msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Msg temp_r = new Msg();
        //冒泡排序，大的在数组的前列
        for (int i = 0; i < msg.size() - 1; i++) {
            for (int j = i + 1; j < msg.size(); j++) {
                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                Date d1 = sdf.parse(msg.get(i).getTime(), pos1);
                Date d2 = sdf.parse(msg.get(j).getTime(), pos2);
                if (d1.before(d2)) {
                    //如果队前日期靠前，调换顺序
                    temp_r = msg.get(i);
                    msg.set(i, msg.get(j));
                    msg.set(j, temp_r);
                }
            }
        }
        return msg;
    }


    /**
     * 进行数据交互
     *
     * @param listener 监听
     */
    public void loadNews(final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().applies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取消息返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }
}
