package com.example.mysecuritydemo.ui;

import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.common.AssetsDatabaseManager;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QueryAttribution extends Activity {
	
	private ActionBar actionBar;
	private SQLiteDatabase sqliteDB;
	private Button queryBtn;
	private EditText queryEdit;
	private TextView result_query;
	private DatabaseDAO dao ;
	private QueryAsyncTask queryAsyncTask; 
	private String phoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.queryattribution_main);
		initView();
		initQueryDatabase();
	}
	
	public void initView(){
		//queryBtn = (Button) findViewById(R.id.btn_query);
		queryEdit = (EditText) findViewById(R.id.edit_query);
		queryEdit.addTextChangedListener(editWatcher);
		//queryBtn.setOnClickListener(queryButtonListener);
		result_query = (TextView) findViewById(R.id.result_query);
	}
	
	public void initQueryDatabase(){
		AssetsDatabaseManager.initManager(this);
		AssetsDatabaseManager.getAssetsDatabaseManager().getDatabase("number_location.db");
		dao = new DatabaseDAO(this);
	}
	
	android.view.View.OnClickListener queryButtonListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(queryEdit.getText().toString().trim() == null){
				Toast.makeText(QueryAttribution.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
			}
			phoneNumber = queryEdit.getText().toString();
			//Toast.makeText(QueryAttribution.this, dao.queryClickNumber(phoneNumber), Toast.LENGTH_SHORT).show();
		}
	};
	
	TextWatcher editWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			System.out.println("改变中"+s);
			queryAsyncTask =  new QueryAsyncTask();
			queryAsyncTask.execute(s.toString());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			//System.out.println("改变前"+s);

		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			//System.out.println("改变后"+s);

		}
	};
	
	
	
	private class QueryAsyncTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return dao.queryListenerNumber(params[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			if(result.equals("")){
//				result_query.setText("未知");
//			}
			result_query.setText(result);
		}
	}
	
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	};
}