package com.example.mysecuritydemo.common;

import java.util.HashMap;
import java.util.Map;

import com.example.mysecuritydemo.ui.QueryAttribution;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**���ݿ⸨����,����͸�������
 * @author hhy
 * @�޸�ʱ�� 2014-8-23 19:37:26
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


	private static String query(String sql, String[] selectionArgs) {
		String result = "";
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, selectionArgs);
			if (cursor.moveToNext()) {
				result = cursor.getString(0);
			}
		}
		return result;
	}

	public String queryListenerNumber(String number) {
		String address = "";

		if (isZeroStarted(number) && getNumLength(number) > 1) {
			String newNumberString = "";
			if (isZeroStarted(number)) {
				newNumberString = number.substring(1, getNumLength(number) - 1);
			}
			newNumberString = number;
			address = query(
					"select location from mob_location where areacode = ? limit 1",
					new String[] { newNumberString.substring(0, getNumLength(number) - 1) });
		}
		return address;
	}

	public String queryClickNumber(String number) {
		String pattern = "^1[3458]\\d{9}$";
		String address = "";
		if (number.matches(pattern))
		{
			address = query("select location from mob_location where _id = ? ",
					new String[] { number.substring(0, 7) });
		} else {
			int len = number.length();
			switch (len) {
			case 4: 
				address = "";
				break;

			case 7: 
				address = "";
				break;

			case 8: 
				address = "";
				break;

			case 10: // 3λ���ţ�7λ����
				// �ж��Ƿ���0��ͷ�ģ��������ݿ��е�0��ͷ��ʡ�Ե��ˣ�����Ҫ��0��
				String newNumberString = "";
				if (isZeroStarted(number)) {
					newNumberString = number.substring(1, 9);
				}
				newNumberString = number;
				address = query(
						"select location from mob_location where areacode = ? limit 1",
						new String[] { number.substring(0, 3) });
				break;

			case 11: // 3λ���ţ�8λ���� ��4λ���ţ�7λ����
				address = query(
						"select location from mob_location where areacode = ? limit 1",
						new String[] { number.substring(0, 3) });
				if (address.equals("")) {
					address = query(
							"select location from mob_location where areacode = ? limit 1",
							new String[] { number.substring(0, 4) });
					if (address.equals("")) {
						address = number;
					}
				}
				break;

			case 12: // 4λ���ţ�8λ����
				address = query(
						"select location from mob_location where areacode = ? limit 1",
						new String[] { number.substring(0, 4) });
				if (address.equals("")) {
					address = number;
				}
				break;

			default:
				break;
			}
		}
		return address;
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
