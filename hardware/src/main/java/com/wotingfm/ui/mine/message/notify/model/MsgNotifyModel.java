package com.wotingfm.ui.mine.message.notify.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;

import java.util.ArrayList;
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
            List<SrcMsg.groupApplyMes> apply = s_msg.getGroup_apply_mes();
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
            List<SrcMsg.groupApproveMes> approve = s_msg.getGroup_approve_mes();
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
            List<SrcMsg.friendApplyMes> person = s_msg.getFriend_apply_mes();
            if (person != null && person.size() > 0) {
                List<Msg> _msg = assemblyMsgForPerson(person);
                if (_msg != null && _msg.size() > 0) {
                    msg.addAll(_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            List<SrcMsg.inviteeMes> invitee = s_msg.getInvitee_mes();
            if (invitee != null && invitee.size() > 0) {
                List<Msg> _msg = assemblyInvitee(invitee);
                if (_msg != null && _msg.size() > 0) {
                    msg.addAll(_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<SrcMsg.groupAdminMes> admin = s_msg.getGroup_admin_mes();
            if (admin != null && admin.size() > 0) {
                List<Msg> _msg = assemblyAdminMsg(admin);
                if (_msg != null && _msg.size() > 0) {
                    msg.addAll(_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 按照时间进行排序
        if (msg != null && msg.size() > 0) {
            return sorting(msg);
        } else {
            return msg;
        }
    }

    // 群主邀请好友入群
    private List<Msg> assemblyInvitee(List<SrcMsg.inviteeMes> invitee) {
        List<Msg> msg = new ArrayList<>();
        for (int i = 0; i < invitee.size(); i++) {
            Msg _msg = new Msg();

            if (invitee.get(i).getId() != null && !invitee.get(i).getId().equals("")) {
                _msg.setMsg_id(invitee.get(i).getId());
            } else {
                _msg.setMsg_id("");
            }
            _msg.setMsg_type("5");

            if (invitee.get(i).getAvatar() != null && !invitee.get(i).getAvatar().equals("")) {
                _msg.setAvatar(invitee.get(i).getAvatar());
            } else {
                _msg.setAvatar("");
            }

            if (invitee.get(i).getGroup_name() != null && !invitee.get(i).getGroup_name().equals("")) {
                _msg.setTitle(invitee.get(i).getGroup_name());
            } else {
                _msg.setTitle("群消息");
            }

            if (invitee.get(i).getInvitee_agreed() != null && !invitee.get(i).getInvitee_agreed().equals("")) {
                _msg.setStatus(invitee.get(i).getInvitee_agreed());
            } else {
                _msg.setStatus("0");
            }

            if (invitee.get(i).getStatus() != null) {
                if (invitee.get(i).getStatus().equals("2")) {
                    _msg.setNews("群主邀请您加入该群");
                } else {
                    _msg.setNews("您已经同意了群主的加群邀请");
                }
            } else {
                _msg.setNews("您已经同意了群主的加群邀请");
            }

            if (invitee.get(i).getApprove_at() != null && !invitee.get(i).getApprove_at().equals("")) {
                _msg.setTime(invitee.get(i).getApprove_at());
            } else {
                _msg.setTime("");
            }
            msg.add(_msg);
        }

        return msg;
    }


    // 管理员收到的组申请消息
    private List<Msg> assemblyAdminMsg(List<SrcMsg.groupAdminMes> admin) {
        List<Msg> msg = new ArrayList<>();
        for (int i = 0; i < admin.size(); i++) {
            Msg _msg = new Msg();
            if (admin.get(i).getId() != null && !admin.get(i).getId().equals("")) {
                _msg.setMsg_id(admin.get(i).getId());
            } else {
                _msg.setMsg_id("");
            }
            _msg.setMsg_type("4");

            if (admin.get(i).getAvatar() != null && !admin.get(i).getAvatar().equals("")) {
                _msg.setAvatar(admin.get(i).getAvatar());
            } else {
                _msg.setAvatar("");
            }

            if (admin.get(i).getApplier_name() != null && !admin.get(i).getApplier_name().equals("")) {
                _msg.setTitle(admin.get(i).getApplier_name());
            } else {
                _msg.setTitle("群消息");
            }

            if (admin.get(i).getGroup_name() != null && !admin.get(i).getGroup_name().equals("")) {
                _msg.setNews("申请加入   " + admin.get(i).getGroup_name());
            } else {
                _msg.setNews("申请加入群");
            }

            if (admin.get(i).getContent() != null && !admin.get(i).getContent().trim().equals("")) {
                _msg.setIntroduce("验证信息： " + admin.get(i).getContent());
            } else {
                _msg.setIntroduce("暂无验证信息");
            }

            if (admin.get(i).getGroup_id() != null && !admin.get(i).getGroup_id().equals("")) {
                _msg.setGroup_id(admin.get(i).getGroup_id());
            } else {
                _msg.setGroup_id("");
            }

            if (admin.get(i).getUser_id() != null && !admin.get(i).getUser_id().equals("")) {
                _msg.setApply_id(admin.get(i).getUser_id());
            } else {
                _msg.setApply_id("");
            }

            if (admin.get(i).getStatus() != null && !admin.get(i).getStatus().equals("")) {
                _msg.setStatus(admin.get(i).getStatus());
            } else {
                _msg.setStatus("0");
            }

            if (admin.get(i).getApprove_at() != null && !admin.get(i).getApprove_at().equals("")) {
                _msg.setTime(admin.get(i).getApprove_at());
            } else {
                _msg.setTime("");
            }
            msg.add(_msg);
        }

        return msg;
    }

    // 群主收到的组申消息
    private List<Msg> assemblyApprove(List<SrcMsg.groupApproveMes> approve) {
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

            if (approve.get(i).getApplier_name() != null && !approve.get(i).getApplier_name().equals("")) {
                _msg.setTitle(approve.get(i).getApplier_name());
            } else {
                _msg.setTitle("群消息");
            }

            if (approve.get(i).getGroup_name() != null && !approve.get(i).getGroup_name().equals("")) {
                _msg.setNews("申请加入   " + approve.get(i).getGroup_name());
            } else {
                _msg.setNews("申请加入群");
            }

            if (approve.get(i).getContent() != null && !approve.get(i).getContent().trim().equals("")) {
                _msg.setIntroduce("验证信息： " + approve.get(i).getContent());
            } else {
                _msg.setIntroduce("暂无验证信息");
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

            if (approve.get(i).getStatus() != null && !approve.get(i).getStatus().equals("")) {
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

    // 组申请消息被处理（申请者收到的消息）
    private List<Msg> assemblyApply(List<SrcMsg.groupApplyMes> apply) {
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

    // 组装好友展示消息
    private List<Msg> assemblyMsgForPerson(List<SrcMsg.friendApplyMes> person) {
        List<Msg> msg = new ArrayList<>();
        for (int i = 0; i < person.size(); i++) {
            Msg _msg = new Msg();
            if (person.get(i).getId() != null && !person.get(i).getId().equals("")) {
                _msg.setMsg_id(person.get(i).getId());
            } else {
                _msg.setMsg_id("");
            }
            _msg.setMsg_type("2");

            if (person.get(i).getReceiver_avatar() != null && !person.get(i).getReceiver_avatar().equals("")) {
                _msg.setAvatar(person.get(i).getReceiver_avatar());
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
        Msg temp_r = new Msg();
        //冒泡排序，大的在数组的前列
        for (int i = 0; i < msg.size() - 1; i++) {
            for (int j = i + 1; j < msg.size(); j++) {
                long d1 = Long.parseLong(msg.get(i).getTime());
                long d2 = Long.parseLong(msg.get(j).getTime());
                if (d1 < d2) {
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

    /**
     * 消息==删除
     *
     * @param listener 监听
     */
    public void loadNewsForDel(String id,String type, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().msgDel(id,type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("消息处理删除==返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 消息==同意
     *
     * @param listener 监听
     */
    public void loadNewsForApply(String gid, String pid, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().msgApply(gid, pid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("消息处理同意==返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 消息==同意加入群组
     *
     * @param listener 监听
     */
    public void loadNewsForAgreeEnterGroup(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().msgAgreeEnterGroup(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("同意加入群组==返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 消息==拒绝加入群组
     *
     * @param listener 监听
     */
    public void loadNewsForRefuseEnterGroup(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().msgRefuseEnterGroup(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("同意加入群组==返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 消息==删除加入群组后的展示消息
     *
     * @param listener 监听
     */
    public void loadNewsForDelEnterGroup(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().msgDelEnterGroup(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("删除加入群组后的展示消息==返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
     * 消息==拒绝
     *
     * @param listener 监听
     */
    public void loadNewsForRefuse(String gid, String pid, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().msgRefuse(gid, pid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("消息处理拒绝==返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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
