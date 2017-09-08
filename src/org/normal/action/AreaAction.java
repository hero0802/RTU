package org.normal.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.normal.function.Func;
import org.normal.function.JSON;
import org.normal.sql.MysqlConnection;

import com.opensymphony.xwork2.ActionSupport;

public class AreaAction extends ActionSupport {
	
	private int parentId;
	private String name;
	
	//省份
	public void province(){
		Map<String,Object> map=LoginUserZone();		
		String sql="";
		int provinceId=Integer.parseInt(map.get("provinceId").toString());
		if(provinceId==0){
			sql="select ID,Name,ParentId from areas where LevelType=1 order by ID asc";
		}else{
			sql="select ID,Name,ParentId from areas where LevelType=1 and ID='"+provinceId+"' order by ID asc";
		}
		HashMap result = new HashMap();
		result.put("items",MysqlConnection.DBList(sql));
		result.put("totals",MysqlConnection.DBList(sql).size());
		String jsonstr =JSON.Encode(result);
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().write(jsonstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//市级
	public void city(){
		Map<String,Object> map=LoginUserZone();		
		String sql="";
		int id=Integer.parseInt(map.get("cityId").toString());
		if(id==0){
			sql="select ID,Name,ParentId from areas where LevelType=2 and ParentId='"+parentId+"'";
		}else{
			sql="select ID,Name,ParentId from areas where LevelType=2 and ParentId='"+parentId+"' and ID="+id;
		}
		HashMap result = new HashMap();
		result.put("items",MysqlConnection.DBList(sql));
		result.put("totals",MysqlConnection.DBList(sql).size());
		String jsonstr =JSON.Encode(result);
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().write(jsonstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//县级
	public void district(){
		Map<String,Object> map=LoginUserZone();		
		String sql="";
		int id=Integer.parseInt(map.get("countyId").toString());
		if(id==0){
			sql="select ID,Name,ParentId from areas where LevelType=3 and ParentId='"+parentId+"'";
		}else{
			sql="select ID,Name,ParentId from areas where LevelType=3 and ParentId='"+parentId+"' and ID="+id;
		}
		HashMap result = new HashMap();
		result.put("items",MysqlConnection.DBList(sql));
		result.put("totals",MysqlConnection.DBList(sql).size());
		String jsonstr =JSON.Encode(result);
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().write(jsonstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 获取登录用户数据访问权限
		public Map<String, Object> LoginUserZone() {
			String sql = "select * from user_zone where userid="+ Func.getSession("userid");
			Connection conn = MysqlConnection.getConn();
			Statement stmt;
			ResultSet rst;
			String str = "";
			Map<String,Object> map=new HashMap<String,Object>();
			try {
				stmt = conn.createStatement();
				rst = stmt.executeQuery(sql);
				rst.next();
				map.put("company", rst.getString("company"));
				map.put("provinceId", rst.getInt("provinceId"));
				map.put("cityId", rst.getInt("cityId"));
				map.put("countyId", rst.getInt("countyId"));
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/* System.out.println("LoginUserZone:"+str); */
			return map;

		}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
