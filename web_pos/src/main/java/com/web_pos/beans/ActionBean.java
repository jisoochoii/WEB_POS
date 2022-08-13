package com.web_pos.beans;

public class ActionBean {
	public String page;
	public boolean isDispatcher;
	private String ajaxData;
	
	public String getAjaxData() {
		return ajaxData;
	}
	public void setAjaxData(String ajaxData) {
		this.ajaxData = ajaxData;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public boolean isDispatcher() {
		return isDispatcher;
	}
	public void setDispatcher(boolean isDispatcher) {
		this.isDispatcher = isDispatcher;
	}
}
