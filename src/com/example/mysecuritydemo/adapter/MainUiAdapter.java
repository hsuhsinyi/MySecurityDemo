package com.example.mysecuritydemo.adapter;

import java.util.List;
import java.util.Map;

import com.example.mysecuritydemo.R;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainUiAdapter extends SimpleAdapter {
	
	private LayoutInflater mLayoutInflater;
	List<Map<String, Object>> mlist = null;

	public MainUiAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mLayoutInflater = LayoutInflater.from(context);
		mlist = data;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public final class ViewHolder{
		public TextView titleTextView;
		public ImageView titleImageView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.gridview_allcontent, null);
			holder.titleImageView = (ImageView) convertView.findViewById(R.id.image_content);
			holder.titleTextView = (TextView)convertView.findViewById(R.id.text_content);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setTag(holder);
		holder.titleImageView.setBackgroundResource((Integer) mlist.get(position).get("image"));
		holder.titleTextView.setText(mlist.get(position).get("title").toString());
		return convertView;
	}

}
