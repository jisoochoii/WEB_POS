package com.web_pos.controller;

import java.io.IOException;


import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web_pos.beans.ActionBean;
import com.web_pos.services.Authentication;
import com.web_pos.services.Sales;
import com.web_pos.services.StManage;
import com.web_pos.services.Statistics;

@WebServlet({ "/goodsProfit", "/accessInfo", "/goStatistics", "/Landing", "/Login", "/Logout", "/GoMgr", "/GoPos", "/GoStatistics", "/UpdGoInfo", "/UpdPass", "/UpdLevel", "/RegGo", "/RegEp", "/SearchGo", "/Orders", "/GoLeave"})
public class Frontcontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Frontcontroller() {
        super();
    }
    
    private void doProcess (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ActionBean action  = null;
    	Authentication auth = null;
		String jobCode = null;
		StManage sm = null;
		Sales sales = null;
		Statistics statistics = null;
		/* 한글화 지원 --> request.setCharacterEncode(UTF-8) */
		request.setCharacterEncoding("UTF-8");
		
		/* JobCode 분리 */
		jobCode = request.getRequestURI().substring(request.getContextPath().length()+1);

		/* JobCode에 따른 서비스(Class) 분기 */
		if(jobCode.equals("Landing") || jobCode.equals("Login") || jobCode.equals("Logout")) {
			auth = new Authentication();
			action = auth.backController(request);
		}else if(jobCode.equals("GoMgr") || jobCode.equals("UpdGoInfo") || jobCode.equals("RegGo") || jobCode.equals("UpdPass") || jobCode.equals("UpdLevel") || jobCode.equals("RegEp")) {
			sm = new StManage();
			action = sm.backController(request);
		}else if(jobCode.equals("GoPos") || jobCode.equals("SearchGo") || jobCode.equals("Orders")) {
			sales = new Sales();
			action = sales.backController(request);
		}else if(jobCode.equals("goStatistics") || jobCode.equals("accessInfo") || jobCode.equals("goodsProfit")) {
			statistics = new Statistics();
			action = statistics.backController(request);
		}

		if (action.getAjaxData() != null) {
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(action.getAjaxData());
		} else {
			if (action.isDispatcher()) {
				RequestDispatcher dispatcher = request.getRequestDispatcher(action.getPage());
				dispatcher.forward(request, response);
			} else {
				response.sendRedirect(action.getPage());
			}
		}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* 한글화 지원 --> server.xml : URIEncoding=UTF-8 */
    	String page = null;
    	switch(request.getRequestURI().substring(request.getContextPath().length()+1)) {
    	case "Landing" :
    		this.doProcess(request, response);
    		break;
    	default:
    		page = "login.jsp?message=" + URLEncoder.encode("잘못된 경로로 접근하셨습니다.", "UTF-8");
    		response.sendRedirect(page);
    	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doProcess(request, response);
	}
}
