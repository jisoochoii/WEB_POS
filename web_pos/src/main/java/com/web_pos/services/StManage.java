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


public class StManage {
	public StManage() {}
	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;
		/* jobCode 분리 */
		/* jobCode에 따른 제어메서드 분기 */
		switch(req.getRequestURI().substring(req.getContextPath().length()+1)){
		case "GoMgr":
			action = this.access(req);
			break;
		case "UpdGoInfo":
			action = this.UpdGoInfo(req);
			break;
		case "RegGo":
			action = this.regGo(req);
			break;
		case "RegEp":
			action = this.RegEp(req);
			break; 
		case "UpdPass":
			action = this.UpdPass(req);
			break;
		case "UpdLevel":
			action = this.UpdLevel(req);
			break;
		}

		return action;
	}
	
	private ActionBean access(HttpServletRequest req) {
		ActionBean action = null;
		AuthBean auth = null;
		DAO dao = null;
		Connection connection = null;
		
		/* Session 정보 확인 */
		HttpSession session = req.getSession();
		auth = new AuthBean();
		
		/* DataAccessObject */
		dao = new DAO();
		connection = dao.connectionOpen();
		System.out.println("매장관리:"+session.getAttribute("epCode"));
		if(session.getAttribute("epCode") != null) {
			auth.setEpCode((String)session.getAttribute("epCode"));
			
			Authentication authentication = new Authentication();
			authentication.getLoginInfo(req, auth, dao, connection);

			req.setAttribute("left", this.left(req, auth, dao, connection));
			req.setAttribute("middle", this.middle(req, auth, dao, connection));
			req.setAttribute("right", this.right(req, auth, dao, connection));

			action = new ActionBean();
			action.setPage("stManage.jsp");
			action.setDispatcher(true);
			
			session.setMaxInactiveInterval(7200);
		}else {
				action = new ActionBean();
				action.setPage("login.jsp");
				action.setDispatcher(false);
			}
		
		dao.connectionClose(connection);
		
		System.out.println("매장관리:"+action.getPage());
		return action;
	}
	
	private String left(HttpServletRequest req, AuthBean auth, DAO dao, Connection connection) {
		ArrayList<getInfoBean> list = null;
		String data = null;
		StringBuffer sb = new StringBuffer();
		
		//직원정보
		list = dao.getemployeeInfo(connection, auth);
		sb.append("<div class=\"left\">\n");
		sb.append("<div class=\"epMenu\">\n");
		sb.append("<div class=\"epCode\">직원코드</div>\n"
				+ "<div class=\"epName\">직원이름</div>\n"
				+ "<div class=\"epLevel\">직원등급</div>\n"
				+ "<div class=\"password\">패스워드</div>\n"
				+ "<div class=\"name\">이름</div>\n");
		sb.append("</div>\n");
		
		for (int idx = 0; idx < list.size(); idx++) {
			data = list.get(idx).getEpCode() + ":" + list.get(idx).getEpName() + ":" + list.get(idx).getEpLevel(); 
			sb.append("<div class=\"epContent\">\n");
			sb.append("<div class=\"epCode\">"+list.get(idx).getEpCode()+"</div>\n");
			sb.append("<div class=\"epName\">"+list.get(idx).getEpName()+"</div>\n");
			sb.append("<div class=\"epLevel\">"+list.get(idx).getEpLevel()+"</div>\n");
			sb.append("<div class=\"password\">"
					+ "<input type = \"button\" value = \"변경하기\" class = \"btn\""
					+ "onMouseOver=\"changeBtnCss(this, \'btn-over\')\" onMouseOut=\"changeBtnCss(this, \'btn\')\""
					+ "onClick = \"changePassword(\'" + data + "\')\"/></div>\n");
			sb.append("<div class=\"name\">"
					+ "<input type = \"button\" value = \"변경하기\" class = \"btn\""
					+ "onMouseOver=\"changeBtnCss(this, \'btn-over\')\" onMouseOut=\"changeBtnCss(this, \'btn\')\""
					+ "onClick = \"changeLevel(\'" + data + "\')\"/></div>\n");
			sb.append("</div>\n");
		}

		sb.append("<div class = \"epContent\" onClick = \"insertEp(\'직원추가\')\">⨁</div>\n");
		sb.append("</div>\n");
		
		return sb.toString();
	}
	
	private String middle(HttpServletRequest req, AuthBean auth, DAO dao, Connection connection) {
		ArrayList<getInfoBean> list = null;
		String data = null;
		StringBuffer sb = new StringBuffer();
		
		//회원정보
		list = dao.getMemberInfo(connection, auth);
		sb.append("<div class=\"middle\">\n");
		sb.append("<div class=\"mmMenu\">\n");
		sb.append("<div class=\"memberCode\">회원코드</div>\n"
				+ "<div class=\"memberName\">회원이름</div>\n"
				+ "<div class=\"memberPhone\">전화번호</div>\n");
		sb.append("</div>\n");
		
		for (int idx = 0; idx < list.size(); idx++) {
			data = list.get(idx).getMemberCode() + ":" + list.get(idx).getMemberName() + ":" + list.get(idx).getMemberPhone();
			sb.append("<div class=\"mmContent\">\n");
			sb.append("<div class=\"memberCode\">"+list.get(idx).getMemberCode()+"</div>\n");
			sb.append("<div class=\"memberName\">"+list.get(idx).getMemberName()+"</div>\n");
			sb.append("<div class=\"memberPhone\">"+list.get(idx).getMemberPhone()+"</div>\n");
			sb.append("</div>\n");
		}
		sb.append("</div>\n");
		
		return sb.toString();
	}
	
	private String right(HttpServletRequest req, AuthBean auth, DAO dao, Connection connection) {
		ArrayList<getInfoBean> list = null;
		String data = null;
		StringBuffer sb = new StringBuffer();
		
		//상품정보
		list = dao.getGoodsInfo(connection, auth);
		sb.append("<div class=\"right\">\n");
		sb.append("<div class=\"goMenu\">\n");
		sb.append("<div class=\"goodsCode\">상품코드</div>\n"
				+ "<div class=\"goodsName\">상품이름</div>\n"
				+ "<div class=\"caName\">분류</div>\n"
				+ "<div class=\"goodsCost\">원가</div>\n"
				+ "<div class=\"goodsPrice\">판매가</div>\n"
				+ "<div class=\"goodsStock\">재고</div>\n"
				+ "<div class=\"changeInfo\">정보변경</div>\n");
		sb.append("</div>\n");
		
		for (int idx = 0; idx < list.size(); idx++) {
			data = list.get(idx).getGoodsCode() + ":" + list.get(idx).getGoodsName() + ":"
					+ list.get(idx).getGoodsCost() + ":" + list.get(idx).getGoodsPrice() + ":" + list.get(idx).getGoodsStock()+ ":"
					+ list.get(idx).getGoodsCAName();
			sb.append("<div class=\"goContent\">\n");
			sb.append("<div class=\"goodsCode\">"+list.get(idx).getGoodsCode()+"</div>\n");
			sb.append("<div class=\"goodsName\">"+list.get(idx).getGoodsName()+"</div>\n");
			sb.append("<div class=\"caName\">"+list.get(idx).getGoodsCAName()+"</div>\n");
			sb.append("<div class=\"goodsCost\">"+list.get(idx).getGoodsCost()+"</div>\n");
			sb.append("<div class=\"goodsPrice\">"+list.get(idx).getGoodsPrice()+"</div>\n");
			sb.append("<div class=\"goodsStock\">"+list.get(idx).getGoodsStock()+"</div>\n");
			sb.append("<div class=\"changeInfo\">"
					+ "<input type = \"button\" value = \"변경하기\" class = \"btn\""
					+ "onMouseOver=\"changeBtnCss(this, \'btn-over\')\" onMouseOut=\"changeBtnCss(this, \'btn\')\""
					+ "onClick = \"changeInfo(\'"+ data +"\')\"/></div>\n");
			sb.append("</div>\n");
		}

		sb.append("</div>\n");
		sb.append("<div class = \"insContent\" onClick = \"insertGo()\">⨁</div>\n");
		
		return sb.toString();
	}
	
	private ActionBean UpdGoInfo(HttpServletRequest req) {
		/* 상품정보 변경 */
		ActionBean action = new ActionBean();
		getInfoBean info = null;
		DAO dao = null;
		Connection conn = null;
		String page = null;
		boolean isDispatcher = false;
		
		try {
			page = "login.jsp?message=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.","UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		
		HttpSession session = req.getSession();

		if(session.getAttribute("epCode") != null ) {
			info = new getInfoBean();
			info.setGoodsCode(req.getParameter("goodsCode"));
			info.setGoodsName(req.getParameter("goodsName"));
			info.setGoodsCost(Integer.parseInt(req.getParameter("goodsCost")));
			info.setGoodsPrice(Integer.parseInt(req.getParameter("goodsPrice")));
			info.setGoodsStock(Integer.parseInt(req.getParameter("goodsStock")));


			
			dao  = new DAO();
			conn = dao.connectionOpen();
			
			if(this.convertToBoolean(dao.upGoods(conn, info))) {
				page = "GoMgr";
				isDispatcher = true;
			}		
			dao.connectionClose(conn);			
		};
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		
		return action;
	}
	
	private ActionBean RegEp(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		AuthBean auth = null;
		DAO dao = null;
		Connection conn = null;
		String page = null;
		boolean isDispatcher = false;
		
		try {
			page = "login.jsp?message=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.","UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		
		HttpSession session = req.getSession();

		if(session.getAttribute("epCode") != null ) {
			auth = new AuthBean();
			auth.setEpCode(req.getParameter("epCode"));
			auth.setEpName(req.getParameter("epName"));
			auth.setEpLevel(req.getParameter("epLevel"));
			auth.setPassword(req.getParameter("epPassword"));
			
			dao  = new DAO();
			conn = dao.connectionOpen();
			
			if(this.convertToBoolean(dao.insertEmp(conn, auth))) {
				page = "GoMgr";
				isDispatcher = true;
			}		
			dao.connectionClose(conn);			
		};
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		
		return action;
		
	}
	
	private ActionBean UpdPass(HttpServletRequest req) {
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
		
		if(session.getAttribute("epCode") != null) {
			System.out.println("업뎃 체크2");

			auth = new AuthBean();
			auth.setEpCode(req.getParameter("epCode"));
			auth.setPassword(req.getParameter("password"));

			dao = new DAO();
			connection = dao.connectionOpen();
			
			if (this.convertToBoolean(dao.upEpPassword(connection, auth))) {
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
	
	private ActionBean regGo (HttpServletRequest req) {
		System.out.println("인설트 굿즈 체크1");
		ActionBean action = new ActionBean();
		getInfoBean info = null;
		DAO dao = null;
		Connection connection = null;
		String page = null;
		boolean isDispatcher = false;
		
		try {
			page = "success.jsp?message="+URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpSession session = req.getSession();
		
		if(session.getAttribute("epCode") != null) {

			System.out.println("인설트 굿즈 체크2");
			info = new getInfoBean();
			info.setGoodsName(req.getParameter("goodsName"));
			info.setGoodsCost(Integer.parseInt(req.getParameter("goodsCost")));
			info.setGoodsPrice(Integer.parseInt(req.getParameter("goodsPrice")));
			info.setGoodsStock(Integer.parseInt(req.getParameter("goodsStock")));
			info.setGoodsCode(req.getParameter("goodsCode"));
			info.setGoodsCACode(req.getParameter("categoryCode"));

			dao = new DAO();
			connection = dao.connectionOpen();
			
			if (this.convertToBoolean((dao.insGoods(connection, info)))) {
				System.out.println("인설트 굿즈 체크3");
				page = "GoMgr";
				isDispatcher = true;
			}
			
			dao.connectionClose(connection);
		}
		
		System.out.println("인설트 굿즈 페이지 : "+action.getPage());
		
		action.setPage(page);
		action.setDispatcher(isDispatcher);
		
		return action;
	}
	
	private ActionBean UpdLevel(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		AuthBean auth = null;
		DAO dao = null;
		Connection conn = null;
		String page = "test.jsp";
		boolean isDispatcher = false;
		
		try {
			page = "?message=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		
		HttpSession session = req.getSession();
		
		if(session.getAttribute("epCode")!= null) {
			auth = new AuthBean();
			auth.setEpCode(req.getParameter("epCode"));
			auth.setEpLevel(req.getParameter("epLevel"));
			
			dao = new DAO();
			conn = dao.connectionOpen();
			
			if(this.convertToBoolean(dao.updEmployeeLevel(conn, auth))) {
				page = "GoMgr";
				isDispatcher = true;
			}
			dao.connectionClose(conn);
	}	
			action.setPage(page);
			action.setDispatcher(isDispatcher);
			return action;
	}
	
	public boolean convertToBoolean(int value) {
		return (value == 1)? true : false;
	}
}
