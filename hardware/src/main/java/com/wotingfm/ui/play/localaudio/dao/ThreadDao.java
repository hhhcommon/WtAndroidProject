package com.wotingfm.ui.play.localaudio.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wotingfm.common.database.SQLiteHelper;
import com.wotingfm.ui.play.localaudio.model.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

public class ThreadDao {
    private  Context context;
    private SQLiteHelper helper;

	// 构造方法
	public ThreadDao(Context contexts) {
        context=contexts;
		helper = new SQLiteHelper(context);
	}

	public void insertThread(ThreadInfo threadInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
				new Object[] { threadInfo.getId(), threadInfo.getUrl(),
						threadInfo.getStart(), threadInfo.getEnd(),
						threadInfo.getFinished() });
		db.close();
	}

	public void deleteThread(String url, String thread_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from thread_info where url = ? and thread_id = ?",
				new Object[] { url, thread_id });
		db.close();
	}

	/**
	 * 更新线程信息
	 * @param url
	 * @param thread_id
	 * @param finished
	 */
	public void updateThread(String url, String thread_id, int finished) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update thread_info set finished = ? where url = ? and thread_id = ?",
				new Object[] { finished, url, thread_id });
		db.close();
	}

	/**
	 * 查找ThreadInfo关于该url的对象（list）
	 */
	public List<ThreadInfo> getThreads(String id) {
		List<ThreadInfo> list = new ArrayList<>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from thread_info where thread_id = ?",new String[] { id });
		while (cursor.moveToNext()) {
			ThreadInfo threadInfo = new ThreadInfo();
			threadInfo.setId(cursor.getString(cursor.getColumnIndex("thread_id")));
			threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
			threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
			threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
			list.add(threadInfo);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 通过url或者id判断threadinfo表中是否有该数据
	 * @param url
	 * @param thread_id
	 * @return
	 */
	public boolean isExists(String url, String thread_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from thread_info where url = ? and thread_id = ?",
				new String[] { url, thread_id + "" });
		boolean exists = cursor.moveToNext();
		cursor.close();
		db.close();
		return exists;
	}
}
