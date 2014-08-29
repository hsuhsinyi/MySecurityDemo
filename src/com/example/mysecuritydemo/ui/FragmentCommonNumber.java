package com.example.mysecuritydemo.ui;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.CommonExpendListAdapter;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class FragmentCommonNumber extends Fragment {

	private List<Map<String, Object>> groupList = null;
	private List<Map<String, Object>> itemList = null;
	private String[] mGroupStrings = null;  
    private List<List<Map<String, Object>>> mData = null;  
	private CommonExpendListAdapter expandableListAdapter;
	private ExpandableListView expandableListView;

	private String[] mGroupArrays = new String[] { "生活常用", "运营商", "快递服务",
			"银行保险", "投诉咨询", "火车航空" };
	private int[] mImageIds = new int[] { R.drawable.ic_group_common,
			R.drawable.ic_group_operate, R.drawable.ic_group_deliver,
			R.drawable.ic_group_bank, R.drawable.ic_group_consultation,
			R.drawable.ic_group_railway, };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.expendlist_common, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initGroupData();
		initData();
		//initItemData();
		expandableListView = (ExpandableListView) getActivity().findViewById(
				R.id.expendlist);
		expandableListView.setGroupIndicator(null);
		expandableListAdapter = new CommonExpendListAdapter(getActivity(),
				groupList, mData);
		expandableListView.setAdapter(expandableListAdapter);
	}

	private void initData() {
		mData = new ArrayList<List<Map<String,Object>>>();
		DatabaseDAO dao = new DatabaseDAO(getActivity());
		for (int i = 0; i < mGroupArrays.length; i++) {
			//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			itemList = dao.queryAllCommonNumber(i+1);
			mData.add(itemList);
		}
	}
	
//    private i[] getStringArray(int resId) {  
//        return getResources().getStringArray(resId);  
//    }  

	public void initGroupData() {
		groupList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mGroupArrays.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("grouptitle", mGroupArrays[i]);
			map.put("image", mImageIds[i]);
			//System.out.println(mImageIds[0]);
			groupList.add(map);
		}
	}

//	public void initItemData() {
//		itemList = new ArrayList<Map<String, Object>>();
//		DatabaseDAO dao = new DatabaseDAO(getActivity());
//		itemList = dao.queryAllCommonNumber();
//		
//	}
}