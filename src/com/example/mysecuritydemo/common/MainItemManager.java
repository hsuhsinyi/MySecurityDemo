package com.example.mysecuritydemo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.bean.MainItemInfo;

/** ������Item������
 * @author hhy
 * @Version V1.0
 * @ModifyTime 2014-8-23 10:38:10
 */
public class MainItemManager {
	
	List<MainItemInfo> dataList = null;
	List<Map<String, Object>> itemList = null;
	private Context context;
	
	public MainItemManager(Context context){
		this.context = context;
	}
	
	public List<MainItemInfo> getMainItem(){
		dataList = new ArrayList<MainItemInfo>();
		MainItemInfo itemQuery = new MainItemInfo("�����ز�ѯ", R.drawable.ic_toolbox_number_query);
		dataList.add(itemQuery);
		MainItemInfo itemCheck = new MainItemInfo("�Ѱ�װӦ��", R.drawable.ic_toolbox_app_check);
		dataList.add(itemCheck);
		MainItemInfo itemIntercept = new MainItemInfo("��ɧ��", R.drawable.ic_toolbox_intercept);
		dataList.add(itemIntercept);
		MainItemInfo itemMonitor = new MainItemInfo("�������", R.drawable.ic_toolbox_monitor);
		dataList.add(itemMonitor);
		MainItemInfo itemPrivacy = new MainItemInfo("�û���˽",  R.drawable.ic_toolbox_privacy);
		dataList.add(itemPrivacy);
		MainItemInfo itemSetting = new MainItemInfo("�߼�����", R.drawable.ic_toolbox_call_setting);
		dataList.add(itemSetting);
		return dataList;
	}
	
	public List<Map<String, Object>> getMainList(){
		List<MainItemInfo> list = getMainItem();
		itemList = new ArrayList<Map<String,Object>>();
		for (MainItemInfo mainItemInfo : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", mainItemInfo.getItemName());
			System.out.println(mainItemInfo.getItemName());
			map.put("image", mainItemInfo.getItemResourceId());
			System.out.println(mainItemInfo.getItemResourceId());
			itemList.add(map);
		}
		return itemList;
	}
}
