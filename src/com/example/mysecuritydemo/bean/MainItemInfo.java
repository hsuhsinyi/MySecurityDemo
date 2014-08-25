package com.example.mysecuritydemo.bean;

import android.R.integer;

/**主界面GridView Item封装实体类
 * @author hhy
 * @Version V1.0
 * @ModifyTime 2014-8-23 10:34:46
 */
public class MainItemInfo {
	private String itemName;
	private int itemResourceId;
	
	public MainItemInfo(String itemName, int itemResourceId) {
		super();
		this.itemName = itemName;
		this.itemResourceId = itemResourceId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemResourceId() {
		return itemResourceId;
	}

	public void setItemResourceId(int itemResourceId) {
		this.itemResourceId = itemResourceId;
	}
	
	
}
