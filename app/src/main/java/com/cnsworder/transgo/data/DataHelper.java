package com.cnsworder.transgo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 伊冲
 * @version 1.0
 * 2010-5-24
 */
public class DataHelper extends SQLiteOpenHelper {
	
	static String databasename = "fertility";
	static int DatabaseVersion = 1;
	
	//基础信息
	String create_table_baseInfo = "CREATE TABLE IF NOT EXISTS base_info" +
	        "(woman_id varchar primary key, " + 
	        "woman_name varchar, woman_unit varchar," + 
	        "woman_address varchar, man_id varchar, " +
	        "man_name varchar, man_unit varchar, " +
	        "man_address varchar, has_girl int," +
	        "has_boy int," +
	        "marry_time varchar, measure_status varchar," +
	        "measure_name varchar, measure_time varchar," +
	        "manager_unit varchar)";

	String create_table_tobepregnant = "CREATE TABLE if not exists tobepregnant" +
			" (id INTEGER PRIMARY KEY  AUTOINCREMENT ," +
			" woman_id varchar, " +
			" time varchar," +
			" manager_man varchar, " +
			" pregnant_status integer, " +
			" other TEXT," +
			" image varchar," +
			" status integer  DEFAULT -1)";

	String create_table_pregnancy  = "CREATE TABLE if not exists pregnancy  " +
			"(id INTEGER PRIMARY KEY AUTOINCREMENT," +
			" woman_id varchar," +
			" time varchar," +
			" manager_man varchar," +
			" day varchar," +
			" baby_status integer," +
			" body_status integer," +
			" other text," +
			" image varchar," +
			" status integer  DEFAULT -1)";
	
	//产后
	String create_table_postpartum = "CREATE TABLE if not exists postpartum " +
			"(id INTEGER PRIMARY KEY  AUTOINCREMENT  ," +
			" woman_id varchar, " +
			" time varchar," +
			" manager_man varchar," +
			" birthday varchar," +
			" day integer," +
			" heat varchar," +
			" water_color varchar," +
			" water_apor varchar," +
			" water_heighr varchar," +
			" breast varchar," +
			" nipple varchar," +
			" other text ," +
			" image varchar, " +
			" status integer  DEFAULT -1)";
	

	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DataHelper(Context context) {
		super(context, databasename, null, DatabaseVersion);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create_table_baseInfo);
		db.execSQL(create_table_tobepregnant);
		db.execSQL(create_table_pregnancy);
		db.execSQL(create_table_postpartum);

	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists base_info");
		db.execSQL("drop table if exists tobepregnant");
		db.execSQL("drop table if exists pregnancy");
		db.execSQL("drop table if exists postpartum");

		
		db.execSQL(create_table_baseInfo);
		db.execSQL(create_table_tobepregnant);
		db.execSQL(create_table_pregnancy);
		db.execSQL(create_table_postpartum);
	}

}
