package com.web_pos.services;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.AuthBean;
import com.web_pos.beans.getInfoBean;
import com.web_pos.dao.DAO;
import com.web_pos.beans.ActionBean;

public class Statistics {
	public Statistics() {}
	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;
		/* jobCode 분리 */
		/* jobCode에 따른 제어메서드 분기 */
		switch (req.getRequestURI().substring(req.getContextPath().length() + 1)) {
		case "goStatistics":
			action = this.movePage(req);
			break;
		case "accessInfo":
			action = this.movePage(req);
			break;
		case "goodsProfit":
			action = this.movePage(req);
			break;

		}
		return action;
	}

	private ActionBean movePage(HttpServletRequest req) {
		ActionBean action = null;
		AuthBean auth = null;
		getInfoBean info = null;
		DAO dao = null;
		Connection conn = null;
		boolean tf = true;

		/* Session 정보 확인 */
		HttpSession session = req.getSession();
		auth = new AuthBean();

		/* DAO */
		dao = new DAO();
		conn = dao.connectionOpen();
		System.out.println("통계 들어와라");
		if (session.getAttribute("epCode") != null) {
			auth.setEpCode((String) session.getAttribute("epCode"));
			
			if (this.convertToBool(dao.loginState(conn, auth))) {
				this.getAccessInfo(req, auth, dao, conn);
				switch (req.getRequestURI().substring(req.getContextPath().length() + 1)) {
				case "goStatistics":
					action = new ActionBean();
					action.setPage("statistics.jsp");
					action.setDispatcher(true);
					req.setAttribute("epInfo", this.getEmployeeInfo(req, conn, dao));
					req.setAttribute("goInfo", this.getGoodsInfo(req, conn, dao));
					break;

				case "accessInfo":
					action = new ActionBean();
					action.setAjaxData((tf) ? this.getAccessHistory(req, conn, dao, auth) : "실패");
					break;

				case "goodsProfit":
					action = new ActionBean();
					action.setAjaxData((tf) ? this.getGoodsProfit(req, conn, dao, info) : "실패");
					break;

				default:
					action = new ActionBean();
					action.setPage("login.jsp");
					action.setDispatcher(false);
					break;
				}
			} else {
				action = new ActionBean();
				action.setPage("login.jsp");
				action.setDispatcher(false);
			}
		} else {
			action = new ActionBean();
			action.setPage("login.jsp");
			action.setDispatcher(false);
		}

		dao.connectionClose(conn);
		return action;

	}

	private void getAccessInfo(HttpServletRequest req, AuthBean auth, DAO dao, Connection conn) {
		ArrayList<AuthBean> list = null;

		/* 직원이름 + AccessTime + 직원등급 (SELECT) */
		list = dao.getUserInfo(auth, conn);
		req.setAttribute("employeeName", list.get(0).getEpName());
		req.setAttribute("accessTime", list.get(0).getAccessTime());
		req.setAttribute("employeeLevel", list.get(0).getEpLevel());
		if (list.get(0).getEpLevel().equals("MA")) {
			req.setAttribute("levelName", "매니저");
		} else if (list.get(0).getEpLevel().equals("PR")) {
			req.setAttribute("levelName", "사장");
		} else {
			req.setAttribute("levelName", "아르바이트");
		}

	}

	private String getEmployeeInfo(HttpServletRequest req, Connection conn, DAO dao) {
		ArrayList<AuthBean> list = null;

		list = dao.getEmployInfo(conn, req);
		StringBuffer sb = new StringBuffer();

		for (int idx = 0; idx < list.size(); idx++) {
			String data = list.get(idx).getEpCode() + ":" + list.get(idx).getEpName() + ":"
					+ list.get(idx).getEpLevel() + ":" + list.get(idx).getEmployHiredate();
			sb.append("<tr>");
			sb.append("<td>" + list.get(idx).getEpCode() + "</td>");
			sb.append("<td>" + list.get(idx).getEpName() + "</td>");
			sb.append("<td>" + list.get(idx).getEpLevel() + "</td>");
			sb.append("<td>" + list.get(idx).getEmployHiredate() + "</td>");
			sb.append("<td name=\"mmBtn\">" + "<input type = \"button\" value =\"출퇴근기록\" onClick =\"noMeanfunction(\'"
					+ data
					+ "\')\" class=\"objbtn\" onMouseOver=\"changeBtnCss(this, 'goBtn-over')\" onMouseOut=\"changeBtnCss(this, 'goBtn')\"></td>");
			sb.append("</tr>");

		}
		return sb.toString();
	}

	private String getAccessHistory(HttpServletRequest req, Connection conn, DAO dao, AuthBean auth) {
		ArrayList<AuthBean> list = null;
		auth = new AuthBean();
		auth.setAccessEpCode(req.getParameter("epCode"));
		list = dao.getAccessInfo(conn, req, auth);
		StringBuffer sb = new StringBuffer();

		for (int idx = 0; idx < list.size(); idx++) {

			sb.append("<tr>");
			sb.append("<td>" + list.get(idx).getAccessTime() + "</td>");
			if(list.get(idx).getAccessAction() == 1) {
				sb.append("<td>출근</td>");
			}else if(list.get(idx).getAccessAction() == -1) {
				sb.append("<td>퇴근</td>");
			}
			sb.append("</tr>");

		}
		return sb.toString();
	}
	
	//220623 추가
	private String getGoodsProfit(HttpServletRequest req, Connection conn, DAO dao, getInfoBean goBean) {
		ArrayList<getInfoBean> list = null;
		goBean = new getInfoBean();
		goBean.setGoodsCode(req.getParameter("goCode"));
		list = dao.getProfit(conn, req, goBean);
		StringBuffer sb = new StringBuffer();
		System.out.println("짱걸지");
		for (int idx = 0; idx < list.size(); idx++) {

			sb.append("<tr>");
			sb.append("<td>" + list.get(idx).getGoodsCode() + "</td>");
			sb.append("<td>" + list.get(idx).getGoodsName() + "</td>");
			sb.append("<td>" + list.get(idx).getOdDate() + "</td>");
			sb.append("<td>" + list.get(idx).getDdSales() + "원</td>");
			sb.append("<td>" + list.get(idx).getProfit() + "원</td>");
			sb.append("</tr>");
				
		}

		return sb.toString();
	}

	//220623 추가
	private String getGoodsInfo(HttpServletRequest req, Connection conn, DAO dao) {
		ArrayList<getInfoBean> list = null;
		list = dao.getGoInfo(req, conn);
		StringBuffer sb = new StringBuffer();
		System.out.print(req.getParameter("goCode"));
		for (int idx = 0; idx < list.size(); idx++) {
			String data = list.get(idx).getGoodsCode() + ":" + list.get(idx).getGoodsName() + ":" + list.get(idx).getGoodsCost()
					+ ":" + list.get(idx).getGoodsPrice() + ":" + list.get(idx).getGoodsStock() + ":"
					+ list.get(idx).getGoodsCAName();
			sb.append("<tr>");
			sb.append("<td>" + list.get(idx).getGoodsCode() + "</td>");
			sb.append("<td>" + list.get(idx).getGoodsName() + "</td>");
			sb.append("<td>" + list.get(idx).getGoodsCost() + "</td>");
			sb.append("<td>" + list.get(idx).getGoodsPrice() + "</td>");
			sb.append("<td>" + list.get(idx).getGoodsStock() + "</td>");
			sb.append("<td>" + list.get(idx).getGoodsCAName() + "</td>");
			sb.append("<td name=\"mmBtn\">"
					+ "<input type = \"button\" value =\"매출통계\" onClick =\"meanFunction(\'" +data
					+ "')\" class=\"objbtn\" onMouseOver=\"changeBtnCss(this, 'goBtn-over')\" onMouseOut=\"changeBtnCss(this, 'goBtn')\"></td>");
			sb.append("</tr>");

		}
		return sb.toString();

	}

	private boolean convertToBool(int value) {
		System.out.println("value 췤:" + value);
		return (value == 1) ? true : false;
	}
}
