package com.example.mysecuritydemo.adapter;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.example.mysecuritydemo.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonExpendListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<Map<String, Object>> mGroupList = null;
	private List<List<Map<String, Object>>> mItemList = null;
	private LayoutInflater mInflater = null;

	public CommonExpendListAdapter(Context context,
			List<Map<String, Object>> groupList,
			List<List<Map<String, Object>>> itemList) {
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mGroupList = groupList;
		this.mItemList = itemList;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mItemList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mGroupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mItemList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.expendlist_group, null);
			groupHolder = new GroupHolder();
			groupHolder.expendGroupImage = (ImageView) convertView
					.findViewById(R.id.expend_image);
			groupHolder.expendGroupText = (TextView) convertView
					.findViewById(R.id.expend_grouptext);
			groupHolder.expendArrowImage = (ImageView) convertView.findViewById(R.id.expend_arrow);

		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		convertView.setTag(groupHolder);
		groupHolder.expendGroupImage.setImageResource((Integer) mGroupList.get(
				groupPosition).get("image"));
		groupHolder.expendGroupText.setText(mGroupList.get(groupPosition)
				.get("grouptitle").toString());
		if(isExpanded){
			groupHolder.expendArrowImage.setImageResource(R.drawable.expandable_indicator_up);
		}else{
			groupHolder.expendArrowImage.setImageResource(R.drawable.expandable_indicator_down);
		}
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemHolder itemHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.expendlist_item, null);
			itemHolder = new ItemHolder();
			itemHolder.expendItemTitle = (TextView) convertView
					.findViewById(R.id.expend_itemtitle);
			itemHolder.expendItemContent = (TextView) convertView
					.findViewById(R.id.expend_itemcontent);

		} else {
			itemHolder = (ItemHolder) convertView.getTag();
		}
		convertView.setTag(itemHolder);
		itemHolder.expendItemTitle.setText(mItemList.get(groupPosition)
				.get(childPosition).get("title").toString());
		itemHolder.expendItemContent.setText(mItemList.get(groupPosition)
				.get(childPosition).get("number").toString());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class GroupHolder {
		private ImageView expendGroupImage;
		private TextView expendGroupText;
		private ImageView expendArrowImage;
	}

	class ItemHolder {
		private TextView expendItemTitle;
		private TextView expendItemContent;
	}

}
