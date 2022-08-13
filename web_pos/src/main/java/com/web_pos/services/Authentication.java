package com.web_pos.services;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.ActionBean;
import com.web_pos.beans.AuthBean;
import com.web_pos.beans.getInfoBean;
import com.web_pos.dao.DAO;

/* 로그인(login), 로그아웃(loginOut), 매장가입(RegStore), 직원등록(RegEmployee), 액세스상태 */
public class Authentication {

	public Authentication() {}

	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;
		/* jobCode 분리*/
		/* jobCode에 따른 제어메서드 분기 */
		switch(req.getRequestURI().substring(req.getContextPath().length()+1)) {
		case "Login" :
			action = this.login(req);
			break;
		case "Logout" :
			action = this.logoutCtl(req);
			break;
		case "RegStore" :
			//action = this.regStoreCtl(req);
			break;
		case "RegEmployee" :
			action = this.regEmployeeCtl(req);
			break;
		case "Landing":
			System.out.println("진입 체크1");
			action = this.login(req);
			break;
		case "UpdPassword" :
			action = this.changePassword(req);
			break;
		case "UpdLevel" :
			action = this.ChangeGrade(req);
			break;
		case "UpdPhone" :
			action = this.ChangePhone(req);
			break;

		}
		return action;
	}
	
	private ActionBean login(HttpServletRequest req) {
		System.out.println("진입 체크2");
		ActionBean action = null;
		AuthBean auth = null;
		DAO dao = null;
		Connection connection = null;
		
		HttpSession session = req.getSession();
		auth = new AuthBean();

		dao = new DAO();
		connection = dao.connectionOpen();
		
		if(session.getAttribute("epCode") != null) {
			System.out.println("진입 체크3");
			System.out.println("2체크 ep : "+(String)session.getAttribute("epCode"));
			auth.setEpCode((String)session.getAttribute("epCode"));
			this.getLoginInfo(req, auth, dao, connection);
			
			req.setAttribute("btn", this.makeHtml(req));
			
			action = new ActionBean();
			action.setPage("success.jsp");
			action.setDispatcher(true);
		} else {
			System.out.println("진입 체크4");
			if (req.getParameter("epCode") != null) {
				System.out.println("진입 체크5");
				auth.setEpCode(req.getParameter("epCode"));
				auth.setPassword(req.getParameter("password"));
				
				action = this.loginCtl(req, auth, dao, connection);
			} else {
				System.out.println("진입 체크6");
				action = new ActionBean();
				action.setPage("login.jsp");
				action.setDispatcher(false);
			}
		}
		
		dao.connectionClose(connection);
		
		return action;
	}

	void getLoginInfo(HttpServletRequest req, AuthBean auth, DAO dao, Connection connection) {
		String name = null;
		ArrayList<AuthBean> list = null;

		/* 매장이름 + 직원이름 + loginTime + 직원등급 >> (SELECT) */
		list = dao.getUserInfo(auth, connection);
		req.setAttribute("epName", list.get(0).getEpName());
		req.setAttribute("loginTime", list.get(0).getLoginTime());
		req.setAttribute("epLevel", list.get(0).getEpLevel());
		if (list.get(0).getEpLevel().equals("PR")) {
			name = "대표";
		} else if (list.get(0).getEpLevel().equals("MA")) {
			name = "매니저";
		} else {
			name = "알바";
		}
		req.setAttribute("levelName", name);
	}

	/* 로그인 : 매장코드(C), 직원코드(C), 패스워드(C) >> request 
	 * storeCode=1111111111&employeeCode=101&employeePassword=4321 
	 * String storeCode = 1111111111 */
	private ActionBean loginCtl(HttpServletRequest req, AuthBean auth, DAO dao, Connection connection) {
		ActionBean action = new ActionBean();
		String page = "login.jsp";
		boolean isDispatcher = false;
		String message = null;
		HttpSession session = req.getSession();
			/* Employee 검증 */
			if(this.convert(dao.isLogin(auth, connection))) {

				this.getLoginInfo(req, auth, dao, connection);
				
				/* Log */
				if (dao.loginState(connection, auth) == 1) {
					auth.setLoginAction(-1);
					dao.insLoginHistory(auth, connection);
				}
				auth.setLoginAction(1);
				if(this.convert(dao.insLoginHistory(auth, connection))) {
					System.out.println("진입 체크9");
					session.setAttribute("epCode", req.getParameter("epCode"));
					session.setMaxInactiveInterval(7200);
					
					req.setAttribute("btn", this.makeHtml(req));
					
					page = "success.jsp";
					isDispatcher = true;
				}else {
					message = "네트워크가 불안정합니다. 잠시후 다시 로그인 해 주세요~"; 
				}
			}else {
				message = "로그인 정보를 확인해 주세요~"; 
			}
		
		
		if(message != null) {
			try {page += "?message=" + URLEncoder.encode(message, "UTF-8");} 
			catch (UnsupportedEncodingException e) {e.printStackTrace();}
		}
		
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}
	
	private String makeHtml (HttpServletRequest req) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div id = \"outline\">\n");
		sb.append("<div id = \"GoPos\" class=\"goBtn\" onMouseOver=\"changeBtnCss(this, 'goBtn-over')\" onMouseOut=\"changeBtnCss(this, 'goBtn')\" onClick = \"GoPage('"+req.getAttribute("epLevel")+"', 'GoPos')\">상 품 판 매</div>\n");
		sb.append("<div id = \"GoMgr\" class=\"goBtn\" onMouseOver=\"changeBtnCss(this, 'goBtn-over')\" onMouseOut=\"changeBtnCss(this, 'goBtn')\" onClick = \"GoPage('"+req.getAttribute("epLevel")+"', 'GoMgr')\">매 장 관 리</div>\n");
		sb.append("<div id = \"GoStatistics\" class=\"goBtn\" onMouseOver=\"changeBtnCss(this, 'goBtn-over')\" onMouseOut=\"changeBtnCss(this, 'goBtn')\" onClick = \"GoPage('"+req.getAttribute("epLevel")+"', 'goStatistics')\">통 계 확 인</div>\n");
		sb.append("</div>");
		
		return sb.toString();
	}

	
	/* 로그아웃 : 매장코드(C), 직원코드(C) >> request */
	private ActionBean logoutCtl(HttpServletRequest req) {
		System.out.println("로그아웃 진입 체크");
		ActionBean action  = new ActionBean();
		AuthBean auth = new AuthBean();
		HttpSession session = req.getSession();
		boolean dispatcher = true;
		String page = null;
		String message = null;
		
		auth.setEpCode((String)session.getAttribute("epCode"));
		auth.setLoginAction(-1);

		DAO dao = new DAO();
		Connection connection = dao.connectionOpen();
		
		if (this.convert(dao.loginState(connection, auth))) {
			if (this.convert(dao.insLoginHistory(auth, connection))) {
				session.invalidate();
				page = "login.jsp";
				message = "로그아웃 되었습니다.";
				dispatcher = false;
			} else {
				message = "로그아웃은 되었지만 데이터베이스에는 반영되지 않았습니다.";
				page = "suuccess.jsp";
			}
		}else {
			message = "네트워크가 불안정합니다. 잠시후 다시 시도해 주세요.";
			page = "success.jsp";
		}
		
		dao.connectionClose(connection);
		
		if(message != null)
			try {
				page += "?message=" + URLEncoder.encode(message, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		
		action.setPage(page);
		action.setDispatcher(dispatcher);
		
		return action;
	}
	
	/* 직원등록 */
	private ActionBean regEmployeeCtl(HttpServletRequest req) {
		ActionBean action  = new ActionBean();
		AuthBean auth = new AuthBean();
		auth.setEpCode(req.getParameter("epCode"));
		auth.setPassword(req.getParameter("password"));
		//auth.setEpName(req.getParameter("epName"));
		
		return action;
	}
	
	private ActionBean changePassword(HttpServletRequest req) {
		System.out.println("업뎃 체크1");
		ActionBean action = new ActionBean();
		AuthBean auth = null;
		DAO dao = null;
		Connection connection = null;
		String page = null;
		boolean isDispatcher = false;
		
		try {
			page = "login.jsp?message="+URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpSession session = req.getSession();
		
		if(session.getAttribute("storeCode") != null) {
			System.out.println("업뎃 체크2");
			auth = new AuthBean();
			auth.setEpCode(req.getParameter("epCode"));
			auth.setPassword(req.getParameter("password"));

			dao = new DAO();
			connection = dao.connectionOpen();
			
			if (this.convert(dao.upEpPassword(connection, auth))) {
				System.out.println("업뎃 체크3");
				page = "GoMgr";
				isDispatcher = true;
			}
			
			dao.connectionClose(connection);
		}
		
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}
	
	private ActionBean ChangeGrade (HttpServletRequest req) {
		System.out.println("업뎃 체크1");
		ActionBean action = new ActionBean();
		//MenuBean menu = null;
		DAO dao = null;
		Connection connection = null;
		String page = null;
		boolean isDispatcher = false;
		
		try {
			page = "login.jsp?message="+URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpSession session = req.getSession();
		/*
		if(session.getAttribute("storeCode") != null) {
			System.out.println("업뎃 체크2");
			//menu = new MenuBean();
			//menu.setStoreCode((String)session.getAttribute("storeCode"));
			//menu.setEpCode(req.getParameter("epCode"));
			//menu.setEpLevel(req.getParameter("epLevel"));

			dao = new DAO();
			connection = dao.connectionOpen();
			
			if (this.convert(dao.upEpGrade(connection, menu))) {
				System.out.println("업뎃 체크3");
				page = "GoMgr";
				isDispatcher = true;
			}
			
			dao.connectionClose(connection);
		}*/
		
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}
	
	private ActionBean ChangePhone (HttpServletRequest req) {
		System.out.println("업뎃 체크1");
		ActionBean action = new ActionBean();
		getInfoBean info = null;
		DAO dao = null;
		Connection connection = null;
		String page = null;
		boolean isDispatcher = false;
		
		try {
			page = "login.jsp?message="+URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpSession session = req.getSession();
		
		if(session.getAttribute("storeCode") != null) {
			System.out.println("업뎃 체크2");
			info = new getInfoBean();
			info.setMemberCode(req.getParameter("mmCode"));
			info.setMemberPhone(req.getParameter("phone"));

			dao = new DAO();
			connection = dao.connectionOpen();
			
			if (this.convert(dao.upMmPhone(connection, info))) {
				System.out.println("업뎃 체크3");
				page = "GoMgr";
				isDispatcher = true;
			}
			
			dao.connectionClose(connection);
		}
		
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}
	
	
	public boolean convert (int value) {
		return (value >= 1)? true:false;
	}
}

