package com.web_pos.beans;

import java.util.ArrayList;

public class OrdersBean {
	private String storeCode;
	private String epCode;
	private String orderDate;
	private String caCode;
	private ArrayList<OrdersDetailBean> ordersList;
	private String memberCode;
	private int amount;
	private int action;
	
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getEpCode() {
		return epCode;
	}
	public void setEpCode(String epCode) {
		this.epCode = epCode;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCaCode() {
		return caCode;
	}
	public void setCaCode(String caCode) {
		this.caCode = caCode;
	}
	public ArrayList<OrdersDetailBean> getOrdersList() {
		return ordersList;
	}
	public void setOrdersList(ArrayList<OrdersDetailBean> ordersList) {
		this.ordersList = ordersList;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
}
