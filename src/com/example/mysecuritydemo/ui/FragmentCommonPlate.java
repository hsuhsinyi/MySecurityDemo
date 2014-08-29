package com.example.mysecuritydemo.ui;

import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;
import com.example.mysecuritydemo.adapter.AppManagerAdapter;
import com.example.mysecuritydemo.adapter.PlateProvinceAdapter;
import com.example.mysecuritydemo.common.DatabaseDAO;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentCommonPlate extends Fragment{
	
	private PlateProvinceAdapter numbersAdapter;
	private List<Map<String, Object>> mList = null;
	private ListView listViewCommon;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.attribution_commonplate, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		DatabaseDAO dao = new DatabaseDAO(getActivity());
		mList = dao.queryPlateProvince();
		listViewCommon = (ListView) getActivity().findViewById(R.id.listview_commonplate);
		numbersAdapter = new PlateProvinceAdapter(getActivity(), mList, R.id.listview_commonplate, new String[]{"name", "short"}, new int[]{R.id.district_name, R.id.short_name});
		listViewCommon.setAdapter(numbersAdapter);
		listViewCommon.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), ResultCommonQuery.class);   
				Bundle bundle = new Bundle();       
				bundle.putString("type", "plate");  
				bundle.putLong("id", id);
				intent.putExtras(bundle); 
				startActivity(intent); 
			}
		});
	}
}