package com.example.mysecuritydemo.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.MainUiAdapter;
import com.example.mysecuritydemo.common.UpdateManager;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class SecurityMain extends Activity {
	
	private MainUiAdapter mainAdapter;
	private GridView mainGridView;
	List<Map<String, Object>> titleList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);
		
		checkUpdateApp();
		initMainUi();
	}
	
	private void initMainUi() {
		mainGridView = (GridView)findViewById(R.id.main_gridview);
		titleList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", R.drawable.ic_toolbox_number_query);
		map.put("title", "归属地查询");
		titleList.add(map);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("image", R.drawable.ic_toolbox_app_check);
		map1.put("title", "已安装应用");
		titleList.add(map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("image", R.drawable.ic_toolbox_intercept);
		map2.put("title", "防骚扰");
		titleList.add(map2);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("image", R.drawable.ic_toolbox_monitor);
		map3.put("title", "流量监控");
		titleList.add(map3);
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("image", R.drawable.ic_toolbox_privacy);
		map4.put("title", "用户隐私");
		titleList.add(map4);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("image", R.drawable.ic_toolbox_call_setting);
		map5.put("title", "高级设置");
		titleList.add(map5);
		
		mainAdapter = new MainUiAdapter(this, titleList,
				R.layout.gridview_allcontent, new String[] { "image", "title" }, 
				new int[] { R.id.image_content,R.id.text_content});
		mainGridView.setAdapter(mainAdapter);
	}

	private void checkUpdateApp() {
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();  
        if(networkInfo == null || !networkInfo.isAvailable())  
        {  
            Toast.makeText(this, "网络连接不可用, 请检查网络连接",Toast.LENGTH_LONG).show();
        }  
        else   
        {  System.out.println("i am here");
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.checkUpdate();
        }  
	}
}
