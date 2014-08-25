package com.example.mysecuritydemo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.AppManagerAdapter;
import com.example.mysecuritydemo.adapter.MainUiAdapter;
import com.example.mysecuritydemo.common.AssetsDatabaseManager;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.R.anim;
import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InstalledApp extends Activity {
	private AppManagerAdapter appAdapter;
	private ActionBar actionBar;
	private ListView listviewApp;
	private TextView AppNumView;
	private List<Map<String, Object>> appList = null;
	private List<PackageInfo> packageinfo;
	private int selectedNum = 0;
	private int allAppCount = 0;
	private int ThirdAppCount = 0;
	private Button uninstallBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.install_app);
		actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayHomeAsUpEnabled(true);
		appList = getAPPInstalled();
		initView();
	}

	private List<Map<String, Object>> getAPPInstalled() {

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		// 获取系统内的所有程序信息
		Intent mainintent = new Intent(Intent.ACTION_MAIN, null);
		mainintent.addCategory(Intent.CATEGORY_LAUNCHER);
		packageinfo = this.getPackageManager().getInstalledPackages(0);

		allAppCount = packageinfo.size();
		for (int i = 0; i < allAppCount; i++) {

			PackageInfo pinfo = packageinfo.get(i);
			ApplicationInfo appInfo = pinfo.applicationInfo;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统程序 忽略
			} else {
				// 非系统程序
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", pinfo.applicationInfo.loadIcon(this
						.getPackageManager()));
				map.put("name", pinfo.applicationInfo.loadLabel(this
						.getPackageManager()));
				map.put("version", pinfo.versionName);
				map.put("packagename", pinfo.packageName);
				//map.put("app_version_code", pinfo.versionCode);
				listItems.add(map);
				ThirdAppCount++;
			}
		}
		return listItems;

	}

	public void initView() {
		listviewApp = (ListView) findViewById(R.id.listview_app);
		uninstallBtn = (Button) findViewById(R.id.btn_uninstall);
		AppNumView = (TextView) findViewById(R.id.num_installed);
		appAdapter = new AppManagerAdapter(this, appList, R.id.listview_app,
				new String[] { "image", "name", "version", "size" }, new int[] {
						R.id.app_image,R.id.app_name, R.id.app_version, R.id.app_size });
		listviewApp.setAdapter(appAdapter);
		listviewApp.setOnItemClickListener(mItemClickListener);
		uninstallBtn.setOnClickListener(btnClickListener);
		AppNumView.setText("已安装" + ThirdAppCount + "个");
	}
	
	OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (appAdapter.isSelected.get(position)) {
				appAdapter.isSelected.put(position, false);
				selectedNum--;
				appAdapter.notifyDataSetChanged();
			} else if (!appAdapter.isSelected.get(position)) {
				appAdapter.isSelected.put(position, true);
				selectedNum++;
				appAdapter.notifyDataSetChanged();
			}
		}
	};
	
	android.view.View.OnClickListener btnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(selectedNum == 0){
				Toast.makeText(InstalledApp.this, "请至少选择一项APP", Toast.LENGTH_SHORT).show();
			}
			for(int i =0; i<appList.size(); i++){
				if(appAdapter.isSelected.get(i)){
					String packageName = (String) appList.get(i).get("packagename");
					Uri packageURI = Uri.parse(packageName);
					//创建Intent意图
					Intent intent = new Intent(Intent.ACTION_DELETE,packageURI);
					//执行卸载程序
					startActivity(intent);
				}
			}
		}
	};
	
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
