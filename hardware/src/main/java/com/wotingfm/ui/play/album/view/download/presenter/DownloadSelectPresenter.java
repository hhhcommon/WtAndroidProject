package com.wotingfm.ui.play.album.view.download.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.BeanCloneUtil;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.album.view.download.view.DownloadSelectFragment;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class DownloadSelectPresenter {

    private DownloadSelectFragment activity;
    private FileInfoDao mFileDao;
    private String albumsID;


    // 对数据进行转换
    List<FileInfo> dataList = new ArrayList<>();
    private List<FileInfo> download_list;

    public DownloadSelectPresenter(DownloadSelectFragment activity) {
        this.activity = activity;
        mFileDao = new FileInfoDao(activity.getActivity());
        getData();
    }

    private void getData() {
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
            albumsID = bundle.getString("albumsID");
            download_list = mFileDao.queryAlbumInfo(albumsID, CommonUtils.getUserId());
            activity.showLoadingView();
            activity.refresh(albumsID);
        }
    }

    public List<FileInfo> getDList(){
        return download_list;
    }

    public String getId(){
        return albumsID;
    }

    public void downLoad(List<Player.DataBean.SinglesBean> singlesBeanList) {
        List<Player.DataBean.SinglesBean>  singlesBeanListSelect= new ArrayList<>();

        if(singlesBeanList!=null&&singlesBeanList.size()>0){
            for(int i=0;i<singlesBeanList.size();i++){
                if(singlesBeanList.get(i).isSelect){
                    singlesBeanListSelect.add(singlesBeanList.get(i));
                }
            }
        }

        if (singlesBeanListSelect.isEmpty()) {
            T.getInstance().showToast("请选择要下载的节目");
            return;
        }


        for (int w = 0;w <singlesBeanListSelect.size();  w++) {
            addList(singlesBeanListSelect.get(w));
        }
        downloadStart();
    }

    // 内容的下载
    private void downloadStart() {
        List<FileInfo> fileDataList = mFileDao.queryFileInfoAll(CommonUtils.getUserId());
        if (fileDataList.size() != 0) {
                List<FileInfo>  dataList= getList();
                List<FileInfo> list= BeanCloneUtil.cloneTo(dataList);
                mFileDao.insertFileInfo(list);
                ToastUtils.show_always(activity.getActivity(), "开始下载");
                List<FileInfo> fileUnDownLoadList = mFileDao.queryFileInfo("false", CommonUtils.getUserId());// 未下载列表
                for (int kk = 0; kk < fileUnDownLoadList.size(); kk++) {
                    if (fileUnDownLoadList.get(kk).download_type.trim().equals("1")) {
                        DownloadClient.workStop(fileUnDownLoadList.get(kk));
                        mFileDao.upDataDownloadStatus(fileUnDownLoadList.get(kk).id, "2");
                    }
                }
                for (int k = 0; k < fileUnDownLoadList.size(); k++) {
                    if (fileUnDownLoadList.get(k).id.equals(dataList.get(0).id)) {
                        FileInfo file = fileUnDownLoadList.get(k);
                        mFileDao.upDataDownloadStatus(dataList.get(0).id, "1");
                        DownloadClient.workStart(file);
                        break;
                    }
                }

        }else {// 此时库里没数据
            List<FileInfo>  dataList= getList();
            List<FileInfo> list= BeanCloneUtil.cloneTo(dataList);
            mFileDao.insertFileInfo(list);
            ToastUtils.show_always(activity.getActivity(), "已经加入下载列表");
            List<FileInfo> fileUnDownloadList = mFileDao.queryFileInfo("false", CommonUtils.getUserId());// 未下载列表
            for (int k = 0; k < fileUnDownloadList.size(); k++) {
                if (fileUnDownloadList.get(k).id.equals(dataList.get(0).id)) {
                    FileInfo file = fileUnDownloadList.get(k);
                    mFileDao.upDataDownloadStatus(dataList.get(0).id, "1");
                    DownloadClient.workStart(file);
                    break;
                }
            }
        }
    }

    // 判断当前节目是否下载过
    private boolean getDownload(SinglesBase pdsBase) {
        boolean t = false;
        // 检查是否重复,如果不重复插入数据库，并且开始下载，重复了提示
        List<FileInfo> fileDataList = mFileDao.queryFileInfoAll(CommonUtils.getUserId());
        if (fileDataList.size() != 0) {// 此时有下载数据
            for (int j = 0; j < fileDataList.size(); j++) {
                if (fileDataList.get(j).id.equals(pdsBase.id)) {
                    if (fileDataList.get(j).single_file_url != null) {
                        t = true;
                        break;
                    }
                }
            }
        }
        return t;
    }

    private void addList(SinglesBase pdsBase) {
        // 对数据进行转换
        FileInfo m = new FileInfo();
        m.id = pdsBase.id;
        m.play_time = String.valueOf(pdsBase.play_time);
        m.single_title = pdsBase.single_title;
        m.creator_id = pdsBase.creator_id;
        m.single_logo_url = pdsBase.single_logo_url;
        m.single_seconds = pdsBase.single_seconds;
        m.single_file_url = pdsBase.single_file_url;
        m.single_file_url_base = pdsBase.single_file_url;
        m.album_title = pdsBase.album_title;
        m.album_lastest_news = pdsBase.album_lastest_news;
        m.album_logo_url = pdsBase.album_logo_url;
        m.album_id = pdsBase.album_id;
        m.albumSize = String.valueOf(pdsBase.albumSize);
        m.user_id = CommonUtils.getUserId();
        m.download_type = "0";
        m.start = 0;
        m.end = 0;
        m.finished = "false";
        dataList.add(m);
    }

    private List<FileInfo> getList() {
        return dataList;
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        mFileDao.closeDB();
        mFileDao=null;
    }

    public void select() {
    }
}
