package com.wotingfm.ui.play.localaudio.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.woting.commonplat.utils.SequenceUUID;
import com.wotingfm.common.database.SQLiteHelper;
import com.wotingfm.ui.play.localaudio.model.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件存储：存储要下载到本地的文件url，图片url等信息，已经下载过的程序标记finished="true"
 * 未下载的程序标记finished="false" 1：查詢部份已經完成，目前僅需要查詢未完成的列表，后续可扩展提供已完成的下载
 * 2：添加部分已经完成，目前支持传递入一个可下载的URL地址进行下载，后续可传入一个包含aurhor或者其他信息的对象，表中已经预留字段
 * 3：修改功能已经完成，目前支持根据文件名对完成状态进行修改 4:删除功能本业务暂不涉及，未处理
 */
public class FileInfoDao {
    private SQLiteHelper helper;

    // 构造方法
    public FileInfoDao(Context context) {
        helper = new SQLiteHelper(context);
    }

    /**
     * 插入数据库
     *
     * @param urlList
     */
    public void insertFileInfo(List<FileInfo> urlList) {
        SQLiteDatabase db = helper.getWritableDatabase();
        // 通过helper的实现对象获取可操作的数据库db
        for (urlList.size(); urlList.size() > 0; ) {
            FileInfo content = urlList.remove(0);
            String fileName;
            String name = content.single_title;
            if (name == null || name.trim().equals("")) {
                fileName = SequenceUUID.getUUIDSubSegment(0) + ".mp3";
            } else {
                fileName = name.replaceAll(
                        "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]",
                        "") + ".mp3";
            }
            db.execSQL("insert into file_info(file_name,start,end,user_id,download_type,finished," +
                    "id,single_title,single_logo_url,single_file_url," +
                    "album_title,album_logo_url,album_id,creator_id,single_seconds,albumSize) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                    fileName, content.start, content.end, content.user_id, content.download_type, content.finished,
                    content.id, content.single_title, content.single_logo_url, content.single_file_url,
                    content.album_title, content.album_logo_url, content.album_id, content.creator_id, content.single_seconds, content.albumSize});// sql语句
        }
        db.close();// 关闭数据库对象据库对象
    }

    /**
     * 获取数据，默认的finished设置为false；
     */
    public List<FileInfo> queryFileInfo(String s, String user_id) {
        List<FileInfo> m = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // 执行查询语句 返回一个cursor对象
            cursor = db.rawQuery("Select * from file_info where finished like ? and user_id like ? order by _id desc", new String[]{s, user_id});
            // 循环遍历cursor中储存的键值对
            while (cursor.moveToNext()) {
                FileInfo h = new FileInfo();
                h.start = cursor.getInt(1);
                h.end = cursor.getInt(2);
                h.user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                h.download_type = cursor.getString(cursor.getColumnIndex("download_type"));
                h.finished = cursor.getString(cursor.getColumnIndex("finished"));
                h.id = cursor.getString(cursor.getColumnIndex("id"));
                h.single_title = cursor.getString(cursor.getColumnIndex("single_title"));
                h.single_logo_url = cursor.getString(cursor.getColumnIndex("single_logo_url"));
                h.single_file_url = cursor.getString(cursor.getColumnIndex("single_file_url"));
                h.album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                h.album_logo_url = cursor.getString(cursor.getColumnIndex("album_logo_url"));
                h.album_id = cursor.getString(cursor.getColumnIndex("album_id"));
                h.creator_id = cursor.getString(cursor.getColumnIndex("creator_id"));
                h.albumSize = cursor.getString(cursor.getColumnIndex("albumSize"));
                h.fileName = cursor.getString(cursor.getColumnIndex("file_name"));
                h.single_seconds = cursor.getString(cursor.getColumnIndex("single_seconds"));
                // 往m里储存每个history对象
                m.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return m;
    }

    /**
     * 更改数据库中下载数据库中用户的下载状态值
     */
    public void upDataDownloadStatus(String id, String download_type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update file_info set download_type=? where id=?", new Object[]{download_type, id});
        db.close();
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<FileInfo> queryFileInfoAll(String userid) {
        List<FileInfo> m = new ArrayList<FileInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // 执行查询语句 返回一个cursor对象
            cursor = db.rawQuery("Select * from file_info where user_id like ? ", new String[]{userid});
            // 循环遍历cursor中储存的键值对
            while (cursor.moveToNext()) {
                FileInfo h = new FileInfo();
                h.start = cursor.getInt(1);
                h.end = cursor.getInt(2);
                h.user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                h.download_type = cursor.getString(cursor.getColumnIndex("download_type"));
                h.finished = cursor.getString(cursor.getColumnIndex("finished"));
                h.id = cursor.getString(cursor.getColumnIndex("id"));
                h.single_title = cursor.getString(cursor.getColumnIndex("single_title"));
                h.single_logo_url = cursor.getString(cursor.getColumnIndex("single_logo_url"));
                h.single_file_url = cursor.getString(cursor.getColumnIndex("single_file_url"));
                h.album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                h.album_logo_url = cursor.getString(cursor.getColumnIndex("album_logo_url"));
                h.album_id = cursor.getString(cursor.getColumnIndex("album_id"));
                h.creator_id = cursor.getString(cursor.getColumnIndex("creator_id"));
                h.albumSize = cursor.getString(cursor.getColumnIndex("albumSize"));
                h.fileName = cursor.getString(cursor.getColumnIndex("file_name"));
                h.single_seconds = cursor.getString(cursor.getColumnIndex("single_seconds"));
                // 网m里储存每个history对象
                m.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return m;
    }

    //	//对表中标记ture的数据进行分组
    public List<FileInfo> GroupFileInfoAll(String userid) {
        List<FileInfo> m = new ArrayList<FileInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // 执行查询语句 返回一个cursor对象
            cursor = db.rawQuery("Select count(single_title),sum(end),album_title,album_logo_url,album_id,creator_id from file_info where finished='true' and user_id =? group by album_id ", new String[]{userid});
            // 循环遍历cursor中储存的键值对
            while (cursor.moveToNext()) {
                FileInfo h = new FileInfo();
                int count = cursor.getInt(0);
                int sum = cursor.getInt(1);
                h.album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                h.album_logo_url = cursor.getString(cursor.getColumnIndex("album_logo_url"));
                h.album_id = cursor.getString(cursor.getColumnIndex("album_id"));
                h.creator_id = cursor.getString(cursor.getColumnIndex("creator_id"));
                h.count = count;
                h.sum = sum;
                // 储存每个history对象
                m.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return m;
    }

    /**
     * 查询专辑数据
     *
     * @param album_id
     * @param user_id
     * @return
     */
    public List<FileInfo> queryAlbumInfo(String album_id, String user_id) {
        List<FileInfo> m = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // 执行查询语句 返回一个cursor对象
            cursor = db.rawQuery(
                    "Select * from file_info where finished='true'and album_id=? and user_id=?",
                    new String[]{album_id, user_id});
            // 循环遍历cursor中储存的键值对
            while (cursor.moveToNext()) {
                FileInfo h = new FileInfo();
                h.start = cursor.getInt(1);
                h.end = cursor.getInt(2);
                h.user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                h.download_type = cursor.getString(cursor.getColumnIndex("download_type"));
                h.finished = cursor.getString(cursor.getColumnIndex("finished"));
                h.id = cursor.getString(cursor.getColumnIndex("id"));
                h.single_title = cursor.getString(cursor.getColumnIndex("single_title"));
                h.single_logo_url = cursor.getString(cursor.getColumnIndex("single_logo_url"));
                h.single_file_url = cursor.getString(cursor.getColumnIndex("single_file_url"));
                h.album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                h.album_logo_url = cursor.getString(cursor.getColumnIndex("album_logo_url"));
                h.album_id = cursor.getString(cursor.getColumnIndex("album_id"));
                h.creator_id = cursor.getString(cursor.getColumnIndex("creator_id"));
                h.albumSize = cursor.getString(cursor.getColumnIndex("albumSize"));
                h.fileName = cursor.getString(cursor.getColumnIndex("file_name"));
                h.single_seconds = cursor.getString(cursor.getColumnIndex("single_seconds"));
                // 储存每个history对象
                m.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return m;
    }

    // 改
    public void updataFileInfo(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update file_info set finished=? where id=?",
                new Object[]{"true", id});
        db.close();
    }

    /**
     * 保存关于该url的起始跟结束
     *
     * @param id
     * @param start
     * @param end
     */
    public void upDataFileProgress(String id, int start, int end) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update file_info set start=?,end =? where id=?",
                new Object[]{start, end, id});
        db.close();
    }

    //删除专辑信息
    public void deleteSequ(String album_id, String user_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from file_info where album_id=? and user_id=?", new Object[]{album_id, user_id});
        db.close();
    }


    /**
     * 删实现两个方法 一种依据url删除 一种依据完成状态删除
     */
    public void deleteFileByUserId(String userid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from fileinfo where finished='false' and userid=?", new Object[]{userid});
        db.close();
    }

    /**
     * 删除一条数据
     *
     * @param id
     * @param user_id
     */
    public void deleteFileInfo(String id, String user_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from file_info where id=? and user_id=?", new Object[]{id, user_id});
        db.close();
    }

    /*
     *关闭目前打开的所有数据库对象
     *
     */
    public void closeDB() {
        helper.close();
    }
}
