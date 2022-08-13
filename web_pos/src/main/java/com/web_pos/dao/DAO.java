package com.web_pos.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.web_pos.beans.AuthBean;
import com.web_pos.beans.OrdersBean;
import com.web_pos.beans.getInfoBean;

public class DAO {
	public DAO() {}
	
	public Connection connectionOpen () {
		Connection connection = null;
		
		String url = "jdbc:oracle:thin:@192.168.0.143:1521:xe";
		String user = "JISOO";
		String password = "1234";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("oracle DBMS 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("OJDBC가 없음");
			e.printStackTrace();
		}
		 catch (SQLException e) {
			 System.out.println("해당 주소정보에 오라클이 없음");
				e.printStackTrace();
		}
		
		return connection;
	}
	
	public void connectionClose (Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* 질의방식
	 * 1. statement -> 매번 parse를 수행한다. where절을 사용하지 않는 쿼리(정적인 쿼리)에선 statement가 더 빠르다. query에 대한 결과값을 SGA에 저장하여 똑같은 질의가 오면 바로 결과를 리턴. parse->compile->execute
	 * 2. prepare(d) statement -> 조건절이 있다면 효율적. 첫 실행하면 캐시에 구문들이 저장된다. 다음 실행에 구문 해석과 컴파일은 생략된다. SQL Injection 공격으로부터 방어가 가능. 빠르진 않다. parse->cache->compile->execute
	 * 3. callable statement -> 가장 효율적(DB에서 생성) << Procedure
	 * */
	
	public int isLogin (AuthBean auth, Connection connection) {
		ResultSet rs = null;
		int result = -1;
		
		String sql = "SELECT COUNT(*) FROM EMP WHERE EMP_CODE = ? AND EMP_PASSWORD = ?";

		try {
			PreparedStatement prst = connection.prepareStatement(sql);
			prst.setNString(1, auth.getEpCode());
			prst.setNString(2, auth.getPassword());
			
			rs = prst.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int insLoginHistory (AuthBean auth, Connection connection) {
		int result = -1;
		String sql = "INSERT INTO AHT(AHT_EMPCODE, AHT_DATE, AHT_ACTION)"
					 + "VALUES(?, DEFAULT, ?)"; 

		try {
			PreparedStatement prst = connection.prepareStatement(sql);
			prst.setNString(1, auth.getEpCode());
			prst.setInt(2, auth.getLoginAction());
			
			result = prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<AuthBean> getUserInfo (AuthBean auth, Connection connection) {
		ArrayList<AuthBean> loginList = new ArrayList<AuthBean>();
		AuthBean ab = null;
		PreparedStatement prst;
		ResultSet rs;
		String sql = "SELECT * FROM ACCESSINFO WHERE EMPCODE = ?"; 
		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, auth.getEpCode());
			
			rs = prst.executeQuery();
			
			while(rs.next()) {
				ab = new AuthBean();
				ab.setEpCode(rs.getNString("EMPCODE"));
				ab.setEpName(rs.getNString("EMPNAME"));
				ab.setLoginTime(rs.getNString("LOGINTIME"));
				ab.setEpLevel(rs.getNString("EMPLEVEL"));
				
				loginList.add(ab);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginList;
	}
	
	public ArrayList<getInfoBean> getemployeeInfo(Connection conn, AuthBean auth) {
		ArrayList<getInfoBean> loginList = new ArrayList<getInfoBean>();
		getInfoBean ab = null;
		PreparedStatement prst;
		ResultSet rs;
		
		String sql = "SELECT EMP_CODE AS EMPCODE, EMP_NAME AS EMPNAME, EMP_LEVEL AS EMPLEVEL FROM EMP";
		try {
			prst = conn.prepareStatement(sql);
			
			rs = prst.executeQuery();
			while(rs.next()) {
				ab = new getInfoBean();
				ab.setEpCode(rs.getNString("EMPCODE"));
				ab.setEpName(rs.getNString("EMPNAME"));
				ab.setEpLevel(rs.getNString("EMPLEVEL"));
				
				loginList.add(ab);
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return loginList;
	}
	
	public ArrayList<getInfoBean> getMemberInfo(Connection conn, AuthBean auth) {
		ArrayList<getInfoBean> loginList = new ArrayList<getInfoBean>();
		getInfoBean ab = null;
		PreparedStatement prst;
		ResultSet rs;
		
		String sql = "SELECT MEM_CODE AS MEMCODE, MEM_NAME AS MEMNAME, MEM_PHONE AS MEMPHONE FROM MEM";
		try {
			prst = conn.prepareStatement(sql);
			
			rs = prst.executeQuery();
			while(rs.next()) {
				ab = new getInfoBean();
				ab.setMemberCode(rs.getNString("MEMCODE"));
				ab.setMemberName(rs.getNString("MEMNAME"));
				ab.setMemberPhone(rs.getNString("MEMPHONE"));
				loginList.add(ab);
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return loginList;
	}
	
	public ArrayList<getInfoBean> getGoodsInfo(Connection conn, AuthBean auth) {
		ArrayList<getInfoBean> loginList = new ArrayList<getInfoBean>();
		getInfoBean ab = null;
		PreparedStatement prst;
		ResultSet rs;
		
		String sql = "SELECT GOO.GOO_CODE AS GOOCODE, GOO.GOO_NAME AS GOONAME, GOO.GOO_CGICODE AS CGICODE, CGI.CGI_NAME AS CGINAME, GOO.GOO_COST AS GOOCOST, GOO.GOO_PRICE AS GOOPRICE, GOO.GOO_STOCK AS GOOSTOCK FROM GOO INNER JOIN CGI ON GOO.GOO_CGICODE = CGI.CGI_CODE ORDER BY GOO.GOO_CODE";
		try {
			prst = conn.prepareStatement(sql);
			
			rs = prst.executeQuery();
			while(rs.next()) {
				ab = new getInfoBean();
				ab.setGoodsCode(rs.getNString("GOOCODE"));
				ab.setGoodsName(rs.getNString("GOONAME"));
				ab.setGoodsCACode(rs.getNString("CGICODE"));
				ab.setGoodsCAName(rs.getNString("CGINAME"));
				ab.setGoodsCost(rs.getInt("GOOCOST"));
				ab.setGoodsPrice(rs.getInt("GOOPRICE"));
				ab.setGoodsStock(rs.getInt("GOOSTOCK"));
				
				loginList.add(ab);
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return loginList;
	}
	
	public int loginState(Connection conn, AuthBean auth) {
		int result = -1;
		ResultSet rs;
		PreparedStatement prst;
		
		String sql = "SELECT SUM(AHT_ACTION) FROM AHT WHERE AHT_EMPCODE = ?";
		
		try {
			prst = conn.prepareStatement(sql);
			prst.setNString(1, auth.getEpCode());
			
			rs = prst.executeQuery();
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		System.out.println("result : "+result);
		
		return result;
	}
	
	public int upEpPassword(Connection connection, AuthBean auth) {
		int result = -1;
		PreparedStatement prst;
		
		String sql = "UPDATE EMP SET EMP_PASSWORD = ? WHERE EMP_CODE = ?";
		System.out.println("업뎃 체크4");
		System.out.println(auth.getEpCode());
		System.out.println(auth.getPassword());
		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, auth.getPassword());
			prst.setNString(2, auth.getEpCode());
			System.out.println("업뎃 체크5");
			result = prst.executeUpdate();
			System.out.println("업뎃 체크6");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	
	public int updEmployeeLevel(Connection conn, AuthBean auth) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE EMP SET EMP_LEVEL = ? WHERE EMP_CODE = ?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEpLevel());
			pstmt.setNString(2, auth.getEpCode());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}
	
	public int upMmPhone(Connection connection, getInfoBean info) {
		int result = -1;
		PreparedStatement prst;
		
		String sql = "UPDATE MM SET MM_PHONE = ? WHERE MM_CODE = ?";
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, info.getMemberPhone());
			prst.setNString(2, info.getMemberCode());
			result = prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int upGoods(Connection connection, getInfoBean info) {
		int result = -1;
		PreparedStatement prst;
		
		String sql = "UPDATE GOO SET GOO_NAME = ?, GOO_COST = ?, GOO_PRICE = ?, GOO_STOCK = ? WHERE  GOO_CODE = ?";

		System.out.println("업뎃 굿즈 체크4");
		System.out.println(info.getGoodsName());
		System.out.println(info.getGoodsCost());
		System.out.println(info.getGoodsPrice());
		System.out.println(info.getGoodsStock());
		System.out.println(info.getGoodsCode());
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, info.getGoodsName());
			prst.setInt(2, info.getGoodsCost());
			prst.setInt(3, info.getGoodsPrice());
			prst.setInt(4, info.getGoodsStock());
			prst.setNString(5, info.getGoodsCode());
			
			result = prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	
	public int insOrders(OrdersBean orders, Connection connection) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "INSERT INTO ORD(ORD_EMPCODE, ORD_DATE, ORD_STATE) "
				+ "VALUES(?, SYSDATE, ?)";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setNString(1, orders.getEpCode());
			pstmt.setNString(2, orders.getCaCode());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}
		return result;
	} 
	
	public void getOrderDate(OrdersBean orders, Connection connection) {
		PreparedStatement pstmt;
		ResultSet rs;
		String sql = "SELECT TO_CHAR(MAX(ORD_DATE), 'YYYYMMDDHH24MISS') AS ODDATE FROM ORD "
				+ "WHERE ORD_EMPCODE=?";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setNString(1, orders.getEpCode());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				orders.setOrderDate(rs.getNString("ODDATE"));	
			}
			 
			
		} catch (SQLException e) {e.printStackTrace();}
		
	}
	
	public int insOrdersDetail(OrdersBean orders, Connection connection) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "INSERT INTO ODT(ODT_ORDEMPCODE, ODT_ORDDATE, ODT_GOOCODE, ODT_QUANTITY) "
				+ "VALUES(?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?)";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setNString(1, orders.getEpCode());
			pstmt.setNString(2, orders.getOrderDate());
			for(int recordIdx=0; recordIdx<orders.getOrdersList().size(); recordIdx++) {
				pstmt.setNString(3, orders.getOrdersList().get(recordIdx).getGoCode());
				pstmt.setInt(4, orders.getOrdersList().get(recordIdx).getQtt());
				if(pstmt.executeUpdate() == 0) {
					result = 0;
					break;
				}else {result = 1;}
			}
						
		} catch (SQLException e) {e.printStackTrace();}
		
		return result;
	}
	
	public int updOrders(OrdersBean orders, Connection connection) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE ORD SET ORD_STATE = 'OC' "
				+ "WHERE ORD_EMPCODE = ? AND ORD_DATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setNString(1, orders.getEpCode());
			pstmt.setNString(2, orders.getOrderDate());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int isMemberCode(OrdersBean orders, Connection connection) {
		int result = -1;
		String sql = "SELECT COUNT(*) FROM MEM WHERE MEM_CODE = ?";
		ResultSet rs = null;
		PreparedStatement pstmt;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setNString(1, orders.getMemberCode());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1); //하나의 레코드 하나의 데이터 = 스칼라데이터
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int updatePoint(OrdersBean orders, int point, Connection connection) {
		int result = -1;
		PreparedStatement prst;
		
		System.out.println("포인트 췤");
		
		String sql = "INSERT INTO PO(PO_ODSTCODE, PO_ODEPCODE, PO_ODDATE, PO_MMCODE, PO_AMOUNT, PO_ACTION) VALUES(?, ?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?, ?)";

		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, orders.getStoreCode());
			prst.setNString(2, orders.getEpCode());
			prst.setNString(3, orders.getOrderDate());
			prst.setNString(4, orders.getMemberCode());
			prst.setInt(5, point);
			prst.setInt(6, 1);
			System.out.println("스토어 : "+orders.getStoreCode());
			System.out.println("직원 : "+orders.getEpCode());
			System.out.println("날짜 : "+orders.getOrderDate());
			System.out.println("멤버 : "+orders.getMemberCode());
			System.out.println("포인트 : "+point);

			result = prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int insPoint(Connection conn, OrdersBean order) {
		int result = -1;
		String sql = "INSERT INTO POI(POI_ORDEMPCODE, POI_ORDDATE, POI_MEMCODE, POI_AMOUNT, POI_ACTION) "
				+ "VALUES(?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?, ?)";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, order.getEpCode());
			pstmt.setNString(2, order.getOrderDate());
			pstmt.setNString(3, order.getMemberCode());
			pstmt.setInt(4, order.getAmount());
			pstmt.setInt(5, order.getAction());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int insGoods(Connection connection, getInfoBean info) {
		int result = -1;
		PreparedStatement prst;
		
		String sql = "INSERT INTO GOO(GOO_CODE, GOO_NAME, GOO_COST, GOO_PRICE, GOO_STOCK, GOO_CGICODE) VALUES(?, ?, ?, ?, ?, ?)";

		System.out.println("인설트 굿즈 체크4");
		System.out.println(info.getGoodsCode());
		System.out.println(info.getGoodsName());
		System.out.println(info.getGoodsCost());
		System.out.println(info.getGoodsPrice());
		System.out.println(info.getGoodsStock());
		System.out.println(info.getGoodsCACode());
		
		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, info.getGoodsCode());
			prst.setNString(2, info.getGoodsName());
			prst.setInt(3, info.getGoodsCost());
			prst.setInt(4, info.getGoodsPrice());
			prst.setInt(5, info.getGoodsStock());
			prst.setNString(6, info.getGoodsCACode());
			
			result = prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	
	public void getAmount(Connection conn, OrdersBean order) {
		ResultSet rs;
		String sql = "SELECT (GOO.GOO_PRICE * ODT.ODT_QUANTITY)*10/100 AS AMOUNT "
				+ "FROM ODT INNER JOIN GOO ON ODT.ODT_GOOCODE = GOO.GOO_CODE "
				+ "WHERE ODT_ORDEMPCODE = ? AND ODT_ORDDATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, order.getEpCode());
			pstmt.setNString(2, order.getOrderDate());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				order.setAmount(Integer.parseInt(rs.getNString("AMOUNT")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int insertEmp(Connection connection, AuthBean info) {
		int result = -1;
		PreparedStatement prst;
		
		String sql = "INSERT INTO EMP(EMP_CODE, EMP_NAME, EMP_LEVEL, EMP_PASSWORD, EMP_HIREDATE) " + "VALUES(?,?,?,?,SYSDATE)";

		try {
			prst = connection.prepareStatement(sql);
			prst.setNString(1, info.getEpCode());
			prst.setNString(2, info.getEpName());
			prst.setNString(3, info.getEpLevel());
			prst.setNString(4, info.getPassword());

	

			result = prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	
	public ArrayList<AuthBean> getEmployInfo(Connection conn, HttpServletRequest req) {
		ArrayList<AuthBean> accessList = new ArrayList<AuthBean>();
		PreparedStatement pstmt;
		ResultSet rs;
		HttpSession session = null;
		AuthBean auth;
		session = req.getSession();
		String sql = "SELECT * FROM EMP";
		try {
			pstmt = conn.prepareStatement(sql);			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				auth = new AuthBean();
				auth.setEpCode(rs.getNString("EMP_CODE"));
				auth.setEpName(rs.getNString("EMP_NAME"));
				auth.setEpLevel(rs.getNString("EMP_LEVEL"));
				auth.setEmployHiredate(rs.getNString("EMP_HIREDATE"));
				
				accessList.add(auth);
			}
		} catch (SQLException e) {e.printStackTrace();}		
		return accessList;
	}
	
	public ArrayList<AuthBean> getAccessInfo(Connection conn, HttpServletRequest req, AuthBean auth) {
		ArrayList<AuthBean> accessList = new ArrayList<AuthBean>();
		PreparedStatement pstmt;
		ResultSet rs;
		HttpSession session = null;
		session = req.getSession();
		String sql = "SELECT * FROM AHT WHERE AHT_EMPCODE=? AND TO_DATE(TO_CHAR(SYSDATE,'YYYYMM'),'YYYYMM') = TO_DATE(TO_CHAR(AHT_DATE,'YYYYMM'),'YYYYMM')";
		try {
			pstmt = conn.prepareStatement(sql);		
			pstmt.setNString(1, auth.getAccessEpCode());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				auth = new AuthBean();
				auth.setAccessTime(rs.getNString("AHT_DATE"));
				auth.setAccessAction(rs.getInt("AHT_ACTION"));
				accessList.add(auth);
			}
		} catch (SQLException e) {e.printStackTrace();}		
		return accessList;
	}
	
	/* 금월 매출 일일 판매 */
	public ArrayList<getInfoBean> getProfit(Connection conn, HttpServletRequest req, getInfoBean goBean) {
		ArrayList<getInfoBean> goodsList = new ArrayList<getInfoBean>();
		PreparedStatement pstmt;
		ResultSet rs;
		String sql = "SELECT  GOCODE, GONAME,  ODDATE,  SUM(GOPRICE*QUANTITY) AS DDSALES,  SUM((GOPRICE-GOCOST)*QUANTITY) AS PROFIT"
				+ "  FROM GOODSSALESINFO  WHERE GOCODE = ?  AND SUBSTR(ODDATE,0,6) = TO_CHAR(SYSDATE,'YYYYMM') GROUP BY GOCODE,GONAME,ODDATE";
		try {
			pstmt = conn.prepareStatement(sql);		
			pstmt.setNString(1, goBean.getGoodsCode());
			rs = pstmt.executeQuery();

			System.out.println(goBean.getGoodsCode());

			while(rs.next()) {
				goBean.setGoodsCode(rs.getNString("GOCODE"));
				goBean.setGoodsName(rs.getNString("GONAME"));
				goBean.setOdDate(rs.getNString("ODDATE"));
				goBean.setDdSales(rs.getNString("DDSALES"));
				goBean.setProfit(rs.getNString("PROFIT"));
				goodsList.add(goBean);
			}
		} catch (SQLException e) {e.printStackTrace();}		
	

		return goodsList;
	}
	
	public ArrayList<getInfoBean> getGoInfo(HttpServletRequest req,Connection conn) {
		ArrayList<getInfoBean> accessList = new ArrayList<getInfoBean>();
		PreparedStatement pstmt;
		ResultSet rs;
		HttpSession session = null;
		session = req.getSession();
		getInfoBean gb;
		String sql = "SELECT * FROM GOO";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				gb = new getInfoBean();
				gb.setGoodsCode(rs.getNString("GOO_CODE"));
				gb.setGoodsCACode(rs.getNString("GOO_CGICODE"));
				gb.setGoodsName(rs.getNString("GOO_NAME"));
				gb.setGoodsCost(rs.getInt("GOO_COST"));
				gb.setGoodsPrice(rs.getInt("GOO_PRICE"));
				gb.setGoodsStock(rs.getInt("GOO_STOCK"));
				accessList.add(gb);
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return accessList;
	}
}
