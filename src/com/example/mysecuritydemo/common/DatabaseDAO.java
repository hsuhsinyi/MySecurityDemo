package com.example.mysecuritydemo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.bean.CommonPlate;
import com.example.mysecuritydemo.ui.Attribution;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.TextView;

public class DatabaseDAO {
	private Context context;
	private String sql;
	private String[] selectionArgs;

	private String result;
	private TextView result_query;
	private SQLiteDatabase sqliteDatabase ;

	public DatabaseDAO(Context context) {
		this.context = context;
	}

	private String querySingle(String sql, String[] selectionArgs) {
		String result = "";
		// ����DatabaseHelper����
		DatabaseHelper dbHelper = new DatabaseHelper(context,
				"number_location.db", 2);
		// �õ�һ��ֻ����SQLiteDatabase����
		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		return result;
	}


	public String queryListenerNumber(String number) {
		String address = "";
		// ��ѯ��0��ͷ������
		int numLength = getNumLength(number);
		if (isZeroStarted(number) && numLength > 2) {
			String newNumberString = "";
			// if (isZeroStarted(number)) {
			newNumberString = number.substring(1, numLength);
			// }
			System.out.println("newNumberString" + newNumberString);
			// newNumberString = number;
			address = querySingle(
					"select location from location_tel where _id = ? ",
					new String[] { newNumberString.substring(0, numLength - 1) });
			if (address.equals("")) {
				address = "δ֪";
			}
			return address + "-" + "�̻�";
		} else if (!isZeroStarted(number) && getNumLength(number) > 6) {
			address = querySingle("select location from location_mob where _id = ? ",
					new String[] { number.substring(0, 7) });
			if (address.equals("")) {
				address = "δ֪";
			}
			return address;
		}
		return "";
	}

	public List<Map<String, Object>> queryPostProvince() {
		List<Map<String, Object>> mlist = new ArrayList<Map<String,Object>>();
		// ����DatabaseHelper����
		DatabaseHelper dbHelper = new DatabaseHelper(context,
				"number_location.db", 2);
		// �õ�һ��ֻ����SQLiteDatabase����
		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery("select * from provinces ", selectionArgs);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cursor.getString(cursor.getColumnIndex("province_id")));
			//System.out.println("province_id" +  cursor.getString(cursor.getColumnIndex("province_id")));
			map.put("name", cursor.getString(cursor.getColumnIndex("province_name")));
			//System.out.println("province_name" + cursor.getString(cursor.getColumnIndex("province_name")));
			mlist.add(map);
		}
		sqliteDatabase.close();
		return mlist;	
	}
	
	public List<Map<String, Object>> queryPlateProvince() {
		List<Map<String, Object>> mlist = new ArrayList<Map<String,Object>>();
		// ����DatabaseHelper����
		DatabaseHelper dbHelper = new DatabaseHelper(context,
				"number_location.db", 2);
		// �õ�һ��ֻ����SQLiteDatabase����
		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery("select * from provinces ", selectionArgs);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cursor.getString(cursor.getColumnIndex("province_id")));
			//System.out.println("province_id" +  cursor.getString(cursor.getColumnIndex("province_id")));
			map.put("name", cursor.getString(cursor.getColumnIndex("province_name")));
			//System.out.println("province_name" + cursor.getString(cursor.getColumnIndex("province_name")));
			map.put("short", cursor.getString(cursor.getColumnIndex("province_short")));
			mlist.add(map);
		}
		sqliteDatabase.close();
		return mlist;	
	}
	
	public List<Map<String, Object>> queryPostCity(int province_id) {
		List<Map<String, Object>> mlist = new ArrayList<Map<String,Object>>();
		// ����DatabaseHelper����
		DatabaseHelper dbHelper = new DatabaseHelper(context,
				"number_location.db", 2);
		// �õ�һ��ֻ����SQLiteDatabase����
		sqliteDatabase= dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery("select * from post_city where province_id  = (" + province_id + ") ", null);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", cursor.getString(cursor.getColumnIndex("post_code")));
			System.out.println("code"+cursor.getString(cursor.getColumnIndex("post_code")));
			map.put("name", cursor.getString(cursor.getColumnIndex("city_name")));	
			mlist.add(map);
		}
		sqliteDatabase.close();
		return mlist;
	}
	
	public List<Map<String, Object>> queryPlate(int province_id) {
		List<Map<String, Object>> mlist = new ArrayList<Map<String,Object>>();
		// ����DatabaseHelper����
		DatabaseHelper dbHelper = new DatabaseHelper(context,
				"number_location.db", 2);
		// �õ�һ��ֻ����SQLiteDatabase����
		sqliteDatabase= dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery("select * from post_city where province_id  = (" + province_id + ") ", null);
		// ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false
		for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", cursor.getString(cursor.getColumnIndex("post_code")));
			System.out.println("code"+cursor.getString(cursor.getColumnIndex("post_code")));
			map.put("name", cursor.getString(cursor.getColumnIndex("city_name")));	
			mlist.add(map);
		}
		sqliteDatabase.close();
		return mlist;
	}

	public boolean isZeroStarted(String number) {
		if (number == null || number.isEmpty()) {
			return false;
		}
		return number.charAt(0) == '0';
	}

	public int getNumLength(String number) {
		if (number == null || number.isEmpty())
			return 0;
		return number.length();
	}

	public void closeDB() {
		if (sqliteDatabase != null) {
			sqliteDatabase = null;
			sqliteDatabase.close();
		}
	}

}
