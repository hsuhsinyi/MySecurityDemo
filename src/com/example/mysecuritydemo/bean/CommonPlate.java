package com.example.mysecuritydemo.bean;

/** 常用车牌封装类 
 * @author hhy
 * @Version 版本
 * @ModifyBy 修改人
 * @ModifyTime 修改时间
 */
public class CommonPlate {
	private int id;
	private String province;
	private String city;
	private String plateNumber;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	
}
