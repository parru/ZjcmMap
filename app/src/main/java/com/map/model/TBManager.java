package com.map.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class TBManager {
	private SQLiteDatabase database;
	public String PATH = "/data";
	
	public TBManager(SQLiteDatabase database) {
		this.database = database;
	}
	
	public SQLiteDatabase getDatabase(){
		return database;
	}

	public abstract void createTable();

	public abstract void dropTable();
	
	public abstract void updateTable();

	/**
	 * 判断某张表是否存在
	 * @param tabName  表名
	 * @return result 
	 */
	public boolean tableIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from Sqlite_master "
					+ "where type ='table' and name ='" + tableName.trim() + "' ";
			cursor = database.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(cursor != null){
		    cursor.close();
		}
		return result;
	}
}
