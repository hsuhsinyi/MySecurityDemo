package com.example.mysecuritydemo.common;

import java.util.HashMap;
import java.util.Map;

import com.example.mysecuritydemo.ui.Attribution;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**数据库辅助类,管理和更新数据
 * @author hhy
 * @修改时间 2014-8-23 19:37:26
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static SQLiteDatabase db;
	private static final int VERSION = 1;
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper(Context context, String name, int version){
		this(context, name, null, VERSION);
	}
	
	public DatabaseHelper(Context context, String name){
		this(context, name, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
