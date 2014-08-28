package com.example.mysecuritydemo.adapter;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CommonNumbersAdapter extends SimpleAdapter {
	List<Map<String, Object>> mlist = null;
	HashMap<String, Object> map;
	LayoutInflater mInflater;
	public Map<Integer, Boolean> isSelected;
	private LayoutInflater mLayoutInflater;
	public final int SHOW_LIST = 1;
	public final int SHOW_GRID = 2;
	public int show_type = SHOW_GRID;

	public CommonNumbersAdapter(Context context,
			List<Map<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mLayoutInflater = LayoutInflater.from(context);
		mlist = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setShowType(int show_type) {
		this.show_type = show_type;
	}

	public final class ViewHolder {
		private TextView districtName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.listview_commonplate,
					null);
			holder = new ViewHolder();
			holder.districtName = (TextView) convertView
					.findViewById(R.id.district_name);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setTag(holder);
		holder.districtName.setText(mlist.get(position).get("name").toString());
		return convertView;
	}

}
