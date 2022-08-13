package com.web_pos.beans;

public class AuthBean {

	private String epCode;
	private String epName;
	private String loginTime;
	private String epLevel;
	private String password;
	private int loginAction;
	private String employHiredate;
	private String accessEpCode;
	private int accessAction;
	private String accessTime;
	
	
	
	
	public int getAccessAction() {
		return accessAction;
	}
	public void setAccessAction(int accessAction) {
		this.accessAction = accessAction;
	}
	public String getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}
	public String getAccessEpCode() {
		return accessEpCode;
	}
	public void setAccessEpCode(String accessEpCode) {
		this.accessEpCode = accessEpCode;
	}
	public String getEmployHiredate() {
		return employHiredate;
	}
	public void setEmployHiredate(String employHiredate) {
		this.employHiredate = employHiredate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLoginAction() {
		return loginAction;
	}
	public void setLoginAction(int loginAction) {
		this.loginAction = loginAction;
	}
	public String getEpCode() {
		return epCode;
	}
	public void setEpCode(String epCode) {
		this.epCode = epCode;
	}
	public String getEpName() {
		return epName;
	}
	public void setEpName(String epName) {
		this.epName = epName;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getEpLevel() {
		return epLevel;
	}
	public void setEpLevel(String epLevel) {
		this.epLevel = epLevel;
	}
}
