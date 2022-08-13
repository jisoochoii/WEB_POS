package com.web_pos.services;

import java.io.UnsupportedEncodingException;


import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.ActionBean;
import com.web_pos.beans.AuthBean;
import com.web_pos.beans.OrdersBean;
import com.web_pos.beans.OrdersDetailBean;
import com.web_pos.beans.getInfoBean;
import com.web_pos.dao.DAO;

public class Sales {
	public Sales () {}
	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;
		/* jobCode 분리 */
		/* jobCode에 따른 제어메서드 분기 */
		if(this.isSession(req)) {
			System.out.println("진입 확인");
			switch(req.getRequestURI().substring(req.getContextPath().length()+1)){
			case "GoPos":
				action = this.access(req);
				break;
			case "SearchGo":
				action = this.getGoodsInfo(req);
				break;
			case "Orders":
				action = this.payment(req);
				break;
			}
		} else {
			try {
				action = new ActionBean();
				action.setDispatcher(false);
				action.setPage("login.jsp?message=" + URLEncoder.encode("로그인을 하셔야 서비스를 이용하실 수 있습니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		}

		return action;
	}
	
	private ActionBean access(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		AuthBean auth = new AuthBean();
		DAO dao = null;
		Connection connection = null;
		HttpSession session = req.getSession();

		/* DataAccessObject */
		dao = new DAO();
		connection = dao.connectionOpen();
		System.out.println("access진입 확인");
		auth.setEpCode((String)session.getAttribute("epCode"));
		
		Authentication authentication = new Authentication();
		authentication.getLoginInfo(req, auth, dao, connection);
		
		dao.connectionClose(connection);
		/* 응답 방식 및 페이지 설정*/
		action.setDispatcher(true);
		action.setPage("pos.jsp");
		
		return action;
	}

	private ActionBean getGoodsInfo(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		HttpSession session = req.getSession();
		AuthBean auth = new AuthBean();
		ArrayList<getInfoBean> list = null;
		DAO dao = null;
		Connection connection = null;
		
		System.out.println("되냐?");
		
		req.getParameter("goodsCode");
		
		dao = new DAO();
		connection = dao.connectionOpen();
		
		list = dao.getGoodsInfo(connection, auth);
		
		dao.connectionClose(connection);
		
		for (getInfoBean getInfo:list) {
			if (getInfo.getGoodsCode().equals(req.getParameter("goodsCode"))) {
				System.out.println("찍힘?");
				
				action.setAjaxData(getInfo.getGoodsCode() + ":" + getInfo.getGoodsName() + ":" + getInfo.getGoodsPrice());
				break;
			}
		}
		
		return action;
	}
	
	private ActionBean payment(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		HttpSession session = req.getSession();
		OrdersBean orders = new OrdersBean();
		ArrayList<OrdersDetailBean> orderList = new ArrayList<OrdersDetailBean>();
		OrdersDetailBean ordersDetail = null;
		String message = "적립실패";
		
		boolean tran = false;
		boolean point = false;
		DAO dao = new DAO();
		Connection conn = null;
		
		/* Client Data --> Bean */
		/* OrdersDetail */
		for(int rec=0;rec<req.getParameterValues("goCode").length;rec++) {
			ordersDetail = new OrdersDetailBean();
			ordersDetail.setGoCode(req.getParameterValues("goCode")[rec]);
			ordersDetail.setQtt(Integer.parseInt(req.getParameterValues("qtt")[rec]));
			orderList.add(ordersDetail);
		}
		
		/* Orders */
		orders.setEpCode((String)session.getAttribute("epCode"));
		orders.setCaCode("OS"); //결제중: OS, 결제완료: OC
		orders.setOrdersList(orderList);
		
		/* Points */
		/* memberCode가 null이 아니면 회원조회
		 * 회원 존재 -> po에 ins -> "적립완료"
		 * 회원 존재X -> "회원 존재X" */
		if(req.getParameterValues("mmCode") != null) {
			orders.setMemberCode(req.getParameterValues("mmCode")[0]);
			orders.setAction(1);
			point = true;
		}
		
		conn = dao.connectionOpen();
		try {
			conn.setAutoCommit(false);//jdbc 기본값 = true
			if(this.convert(dao.insOrders(orders ,conn))) { //OD에 입력
				dao.getOrderDate(orders, conn); //날짜 가져오기
				if (orders.getOrderDate() != null) {
					if(this.convert(dao.insOrdersDetail(orders, conn))) { //OT에 입력
						if(this.convert(dao.updOrders(orders, conn))) { //주문상태 업데이트
							if(point) { //회원코드가 존재할때: 회원조회 후 적립/실패
								if(this.convert(dao.isMemberCode(orders, conn))) { //회원조회
									dao.getAmount(conn, orders);
									if(this.convert(dao.insPoint(conn, orders))) { //PO
										message = "적립완료";
										tran = true;
									} else { message = "적립오류"; }
								} else { message = "존재하지 않는 회원번호입니다."; }
							} else tran = true; //회원코드가 없을 때: 바로 결제
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(!conn.isClosed()) {
					if(tran) {
						conn.commit();
					} else {
						conn.rollback();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		dao.connectionClose(conn);
		action.setAjaxData((tran?"주문완료:":"주문실패:")+message);
		
		return action;
	}
	
	private boolean isSession(HttpServletRequest req) {
		AuthBean auth;
		boolean result = false;
		HttpSession session = req.getSession();

		DAO dao = new DAO();
		Connection connection = null;

		if(session.getAttribute("epCode") != null) {
			auth = new AuthBean();
			auth.setEpCode((String)session.getAttribute("epCode"));

			connection = dao.connectionOpen();
			result = this.convert(dao.loginState(connection, auth));
			dao.connectionClose(connection);
		}

		return result;
	}
	
	private boolean convert(int value) {
		return (value == 1)? true : false;
	}
}
