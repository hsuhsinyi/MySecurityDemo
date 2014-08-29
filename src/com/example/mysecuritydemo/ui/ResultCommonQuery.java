package com.example.mysecuritydemo.ui;

import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.PlateNumberAdapter;
import com.example.mysecuritydemo.adapter.PlateProvinceAdapter;
import com.example.mysecuritydemo.adapter.PostNumbersAdapter;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class ResultCommonQuery extends Activity {

	private ListView listViewResult;
	private String dataType;
	private long itemId;
	private PostNumbersAdapter postNumbersAdapter;
	private PlateNumberAdapter plateNumberAdapter;
	private List<Map<String, Object>> mlistList;
	private DatabaseDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.result_querycommon);
		super.onCreate(savedInstanceState);
		initView();
		initType();
		
	}

	public void initType() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		dataType = bundle.getString("type");
		itemId = bundle.getLong("id");
		if (dataType.equals("post")) {
			dao = new DatabaseDAO(this);
			int queryId = (int) (itemId + 1);
			mlistList = dao.queryPostCity(queryId);// 数据库读取的id需要加1
			postNumbersAdapter = new PostNumbersAdapter(this, mlistList,
					R.id.listview_result, new String[] { "name", "code" },
					new int[] { R.id.district_name, R.id.post_code });
			listViewResult.setAdapter(postNumbersAdapter);
		} else if (dataType.equals("plate")) {
			dao = new DatabaseDAO(this);
			int queryId = (int) (itemId + 1);
			mlistList = dao.queryPlateNumber(queryId);// 数据库读取的id需要加1
			plateNumberAdapter = new PlateNumberAdapter(this, mlistList,
					R.id.listview_result, new String[] { "name", "plate" },
					new int[] { R.id.district_name, R.id.plate_number });
			listViewResult.setAdapter(plateNumberAdapter);
		}
	}

	public void initView() {
		listViewResult = (ListView) findViewById(R.id.listview_result);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
