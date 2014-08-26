package com.example.mysecuritydemo.common;

import java.util.HashMap;
import java.util.Map;

import com.example.mysecuritydemo.R;
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
	private SQLiteDatabase db;
	private Context context;
	private String sql;
	private String[] selectionArgs;
	
	private String result;
	private TextView result_query;
	
	public DatabaseDAO(Context context){
		this.context = context;
	}

	private String query(String sql, String[] selectionArgs) {
        String result = "";
        //����DatabaseHelper����  
        DatabaseHelper dbHelper = new DatabaseHelper(context,  
        		"number_location.db", 2);  
        // �õ�һ��ֻ����SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  
        Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
        // ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false  
       if(cursor.moveToNext()) {  
        	result = cursor.getString(0); 
        }  
       return result;
	}
	


	public String queryListenerNumber(String number) {
		String address = "";
		//��ѯ��0��ͷ������
		int numLength = getNumLength(number);
		if (isZeroStarted(number) && numLength > 2) {
			String newNumberString = "";
			//if (isZeroStarted(number)) {
				newNumberString = number.substring(1, numLength);
			//}
			System.out.println("newNumberString" + newNumberString);
			//newNumberString = number;
			address = query(
					"select location from tel_location where _id = ? ",
					new String[] { newNumberString.substring(0, numLength - 1)});
			if(address.equals("")){
				address = "δ֪";
			}
			return address + "-"+"�̻�";
		}else if(!isZeroStarted(number)&& getNumLength(number) > 6){
			address = query("select location from mob_location where _id = ? ",
					new String[] { number.substring(0, 7) });
			if(address.equals("")){
				address = "δ֪";
			}
			return address;
		}
		return "";
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

	public void closeDB(SQLiteDatabase db) {
		if (db != null) {
			db = null;
			db.close();
		}
	}


}
