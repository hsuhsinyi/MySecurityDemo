package com.example.mysecuritydemo.ui;

import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.common.AssetsDatabaseManager;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentQueryAttribution extends Fragment {
	
	
	private static final String TAB_QUERY = "tabquery";
	private static final String TAB_COMMON = "tabcommon";
	private TabHost mTabHost;
	private ActionBar actionBar;
	private SQLiteDatabase sqliteDB;
	private Button queryBtn;
	private EditText queryEdit;
	private TextView result_query;
	private DatabaseDAO dao;
	private QueryAsyncTask queryAsyncTask;
	private String phoneNumber;
	private RadioButton queryRadioButton;
	private RadioButton commonRadioButton;
	private RadioGroup footerRadioGroup;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.attribution_query, container, false);  
	}

//	@Override
//	protected void onCreateView(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.query_attribution);
//		initView();
//		initQueryDatabase();
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initQueryDatabase();
	}


	public void initView() {
		queryEdit = (EditText) getActivity().findViewById(R.id.edit_query);
		queryEdit.addTextChangedListener(editWatcher);
		result_query = (TextView) getActivity().findViewById(R.id.result_query);
	}

	public void initQueryDatabase() {
		AssetsDatabaseManager.initManager(getActivity());
		AssetsDatabaseManager.getAssetsDatabaseManager().getDatabase(
				"number_location.db");
		dao = new DatabaseDAO(getActivity());
	}

	android.widget.RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.query_radiobtn:
				mTabHost.setCurrentTabByTag(TAB_QUERY);
				break;
			case R.id.common_radiobtn:
				mTabHost.setCurrentTabByTag(TAB_COMMON);
				break;

			default:
				break;
			}
		}
	};

	TextWatcher editWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			System.out.println("改变中" + s);
			queryAsyncTask = new QueryAsyncTask();
			queryAsyncTask.execute(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// System.out.println("改变前"+s);

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			// System.out.println("改变后"+s);

		}
	};

	private class QueryAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return dao.queryListenerNumber(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// if(result.equals("")){
			// result_query.setText("未知");
			// }
			result_query.setText(result);
		}
	}


}