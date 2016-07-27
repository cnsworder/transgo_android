package com.cnsworder.transgo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 伊冲
 * @version 1.0
 * 2010-5-24
 * @param <T>
 */
public abstract class AbstractData<T> implements IData<T> {

	protected Context context;
	DataHelper dataHelper;
	SQLiteDatabase db;
	Cursor cursor;
	
	String tableName;
	
	public AbstractData(Context context) {
		this.context = context;
		dataHelper = new  DataHelper(context);
	}
	
	public boolean delete(T info) {
		return true;
	}
	
	public void closeCursorAndDb() {
		cursor.close();
		db.close();
	}
	
	public int getUncount() {
		int count = 0;
		try {
			db = dataHelper.getReadableDatabase();
			String[] columns = {"status"};
			String whereis = "status=?";
			String[] args = {"-1"};
			cursor = db.query(tableName, columns, whereis, args, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			if(db != null) {
			    db.close();
			}
			return count;
		}
	    count = cursor.getCount();
		return count;
	}
	
	public boolean clear(){
		db = dataHelper.getWritableDatabase();
		if (db.delete(tableName, null, null) < 0) {

			db.close();
			return false;
		}
		db.close();
		return true;
	}
}
