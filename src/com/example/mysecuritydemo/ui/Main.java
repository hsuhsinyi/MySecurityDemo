package com.example.mysecuritydemo.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.MainUiAdapter;
import com.example.mysecuritydemo.common.MainItemManager;
import com.example.mysecuritydemo.common.UpdateManager;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Main extends Activity {
	
	private MainUiAdapter mainAdapter;
	private GridView mainGridView;
	List<Map<String, Object>> titleList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_ui);
		
		checkUpdateApp();
		initMainUi();
	}

	private void initMainUi() {
		mainGridView = (GridView) findViewById(R.id.main_gridview);
		// titleList = new ArrayList<Map<String,Object>>();
		MainItemManager itemManager = new MainItemManager(this);
		titleList = itemManager.getMainList();
		mainAdapter = new MainUiAdapter(this, titleList,
				R.layout.gridview_allcontent,
				new String[] { "image", "title" }, new int[] {
						R.id.image_content, R.id.text_content });
		mainGridView.setAdapter(mainAdapter);
		mainGridView.setOnItemClickListener(itemClickListener);
	}

	OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//System.out.println("current position" + position);
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				Intent intentQuery = new Intent();
				intentQuery.setClass(Main.this, Attribution.class);
				startActivity(intentQuery);
				break;
			case 1:
				Intent intentApp = new Intent();
				intentApp.setClass(Main.this, InstalledApp.class);
				startActivity(intentApp);
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;

			default:
				break;
			}
		}

	};


	private void checkUpdateApp() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			Toast.makeText(this, "网络连接不可用, 请检查网络连接", Toast.LENGTH_LONG).show();
		} else {
			UpdateManager updateManager = new UpdateManager(this);
			updateManager.checkUpdate();
		}
	}
}
