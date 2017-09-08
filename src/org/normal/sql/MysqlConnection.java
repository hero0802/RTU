package org.normal.sql;

import java.sql.Clob;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.justobjects.pushlet.util.Log;

import org.normal.action.Site;
import org.normal.function.*;

public class MysqlConnection {
	private String xmlPath = MysqlConnection.class.getResource("/config.xml")
			.getPath();
	private static Func fun = new Func();
	private String m_serverIp = fun.readXml("WebDataBase", "ip");
	private static String m_root = fun.readXml("WebDataBase", "root");
	private static String m_password = fun.readXml("WebDataBase", "password");;
	private static String m_classname = "com.mysql.jdbc.Driver";
	private String m_tableName = fun.readXml("WebDataBase", "dbname");
	public static String url = "jdbc:mysql://"
			+ fun.readXml("WebDataBase", "ip")
			+ ":"
			+ fun.readXml("WebDataBase", "port")
			+ "/"
			+ fun.readXml("WebDataBase", "dbname")
			+ "?useUnicode=true&characterEncoding=UTF-8&"
			+ "zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false";
	//实例化site类

	// String Ip,String root,String passowrd,String tableName
	public MysqlConnection() {
		/*
		 * this.m_serverIp = fun.readXml(xmlPath, "WebDataBase", "ip");
		 * this.m_root = fun.readXml(xmlPath, "WebDataBase", "root");
		 * this.m_password = fun.readXml(xmlPath, "WebDataBase", "password");
		 * this.m_tableName = fun.readXml(xmlPath, "WebDataBase", "dbname");
		 */
	}

	public static Connection getConn() {
		Connection conn = null;
		/*String url = "jdbc:mysql://"
				+ fun.readXml(xmlPath, "WebDataBase", "ip")
				+ ":3306/"
				+ fun.readXml(xmlPath, "WebDataBase", "dbname")
				+ "?"
				+ "useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";*/
		
		try {
			Class.forName(m_classname).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, m_root, m_password);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 鍒ゆ柇鏁版嵁鏄惁瀛樺湪
	 * 
	 * @param sql
	 * @return
	 */
	public boolean isExists(String sql) {
		if (sql == null) {
			return false;
		}
		Connection conn = getConn();
		Statement stm;
		try {
			stm = conn.createStatement();
			ResultSet rst = stm.executeQuery(sql);
			while (rst.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 鎿嶄綔鏁版嵁搴�
	 * 
	 * @param sql
	 */
	public boolean  Update(String sql) {
		Connection conn = getConn();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			conn.close();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * 鏌ヨ鏁版嵁搴�
	 * 
	 * @param sql
	 */
	public void Select(String sql) {
		Connection conn = getConn();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(sql);
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public int md44id(int rtuId,int deviceId,int model){
		Connection conn = getConn();
		Statement stmt;
       String sql="select md44id from site_lr  where siteId='"+rtuId+"' and deviceId='"+deviceId+"' and model='"+model+"'";
       int result=-1;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			while(rst.next()){
				result=rst.getInt("md44id");
			}
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	  
	public String userid_username(int id){
		Connection conn = getConn();
		Statement stmt;
		System.out.println("id==>"+id);
       String sql="select username from web_user where id="+id;
       String ss="a";
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			while(rst.next()){
				ss= rst.getString("username");
				System.out.println("ida==>"+id);
			}
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ss;
	}
	public int userStatus(String sql) {
		Connection conn = getConn();
		Statement stmt;
		int status= 0;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			status = rst.getInt(1);
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;
	}
	//鏍规嵁鐢ㄦ埛鍚嶈幏鍙栦細鍛樼粍鍚�
	public String username_groupName(String name){
		String sql="select name from web_group where id=(select groupid from web_user where username='"+name+"')";
		Connection conn = getConn();
		Statement stmt;
		String groupname="";
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			groupname= rst.getString("name");
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupname;
	}
	public int username_userid(String name){
		String sql="select id from web_user where username='"+name+"'";
		Connection conn = getConn();
		Statement stmt;
		int id=0;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			id= rst.getInt("id");
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	//鏍规嵁涓插彛鍙锋壘鍒拌澶囩被鍨�
	public int siteId_com_deviceType(int com){
	    int type=0;
	    int a=siteId_comType_R(com);
	    int b=siteId_comType_I(com);
	    if (a==0) {
			type=b;
		}else {
			type=a;
		}
		return type;
	}
	public int siteId_comType_R(int com){
		String sql_lr="select comType from site_lr where com="+com;
		Connection conn = getConn();
		Statement stmt;
		int type=0;
		try {
			stmt = conn.createStatement();
			ResultSet rst2 = stmt.executeQuery(sql_lr);
			while(rst2.next()){
				type=rst2.getInt("comType");
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}
	public int siteId_comType_spd(int com){
		String sql_spd="select comType from site_spd where com="+com;
		Connection conn = getConn();
		Statement stmt;
		int type=0;
		try {
			stmt = conn.createStatement();
			ResultSet rst1 = stmt.executeQuery(sql_spd);
		    while(rst1.next()){
				type=rst1.getInt("comType");
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}
	public int siteId_comType_I(int com){
		String sql_li="select comType from site_li where com="+com;
		Connection conn = getConn();
		Statement stmt;
		int type=0;
		try {
			stmt = conn.createStatement();
			/*ResultSet rst1 = stmt.executeQuery(sql_spd);*/
			ResultSet rst2 = stmt.executeQuery(sql_li);
			/*ResultSet rst3 = stmt.executeQuery(sql_lr);*/
		/*	while(rst1.next()){
				type=rst1.getInt("comType");
			}*/
			while(rst2.next()){
				type=rst2.getInt("comType");
			}
			/*while(rst3.next()){
				type=rst3.getInt("comType");
			}*/
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}
	//鏍规嵁绔欑偣缂栧彿锛岃澶囩紪鍙锋壘鍒颁覆鍙ｅ彿
	public int siteId_I_deviceId_com(int siteId,int deviceId){
		String sql="select com from site_li where siteId='"+siteId+"' and deviceId="+deviceId;
		Connection conn = getConn();
		Statement stmt;
		int com=0;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			com= rst.getInt("com");
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return com;
	}
	public int siteId_R_deviceId_com(int siteId,int deviceId){
		String sql="select com from site_lr where siteId='"+siteId+"' and deviceId="+deviceId;
		Connection conn = getConn();
		Statement stmt;
		int com=0;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			com= rst.getInt("com");
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return com;
	}
	//获取地域
	public static String areaName(String id){
		Log.info("id="+id);
		String sql="select Name from areas where ID="+id;
		Connection conn = getConn();
		Statement stmt;
		String name="全部";
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			name= rst.getString("Name");
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	
	public static ArrayList DBList(String sql) {
		Connection conn = getConn();
		Statement stmt;
		ArrayList list = null;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			list = ResultSetToList(rst);

			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	public ResultSet Excel_List(String sql) {
		Connection conn = getConn();
		ResultSet rst = null;
		try {
			Statement sm = conn.createStatement();
			rst = sm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rst;
	}

	// 鑾峰彇鏁版嵁鏁扮洰
	public int getCount(String sql) {
		Connection conn = getConn();
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();
			count = rst.getInt(1);
			rst.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	//鏍规嵁绔欑偣缂栧彿锛岃澶囩紪鍙疯幏鍙栬澶囦覆鍙ｅ彿
/*	public int deviceComNum(){
		
	}*/

	private static ArrayList ResultSetToList(ResultSet rs) {
		ResultSetMetaData md;
		int columnCount = 0;
		ArrayList list = new ArrayList();
		try {
			md = rs.getMetaData();
			columnCount = md.getColumnCount();
			Map rowData;
			while (rs.next()) {
				rowData = new HashMap(columnCount);
				for (int i = 1; i <= columnCount; i++) {
					Object v = rs.getObject(i);

					if (v != null
							&& (v.getClass() == Date.class || v.getClass() == java.sql.Date.class)) {
						// Timestamp ts= rs.getTimestamp(i);
						// v = new java.util.Date(ts.getTime());
						// v = ts;
						if (v.equals("0000-00-00 00:00:00")) {
							v = "";
						}
					}
					if (v == null || "".equals(v)) {
						v = "";
					}/*
					 * if(v != null && v.getClass() == Clob.class){ v =
					 * clob2String((Clob)v); }
					 */
					rowData.put(md.getColumnName(i), v);
				}
				list.add(rowData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	// 璁板綍鏃ュ織
	public void writeLog(int type, String content, String user) {
		if (user.equals("") || user.equals(null)) {
			user = fun.getSession("username");
		}
		Connection conn = getConn();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "insert into web_log(operator,type,content,time,ip)VALUES('"
					+ user
					+ "',"
					+ ""
					+ type
					+ ",'"
					+ content
					+ "','"
					+ fun.nowDate() + "','" + fun.getClientIpAddr() + "')";
			stmt.executeUpdate(sql);
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
