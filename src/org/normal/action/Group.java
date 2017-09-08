package org.normal.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;
import org.normal.function.Func;
import org.normal.function.JSON;
import org.normal.sql.MysqlConnection;

import com.opensymphony.xwork2.ActionSupport;

public class Group extends ActionSupport{
	private boolean success;
	private String deleteids;
	private String message;
	private int start=0;
	private int limit=10;
	
	private int id;
	private String name;
	private int level;
	private MysqlConnection mysql=new MysqlConnection();
	private Func fun = new Func();
	private JSON json=new JSON();
	
	public void data(){
		String sql="select * from web_group order by level desc limit "+start+","+limit;
		String sql_count="select count(id) from web_group";
		ArrayList data = mysql.DBList(sql);

		HashMap result = new HashMap();
		result.put("items", data);
		result.put("total", mysql.getCount(sql_count));
		String jsonstr = json.Encode(result);
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().write(jsonstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String add_data(){
		String sql_exists="select id from web_group where  name='"+name+"' or level="+level;
		String sql="insert into web_group(name,level,date)VALUES('"+name+"','"+level+"','"+fun.nowDate()+"')";
		if (mysql.isExists(sql_exists)) {
			this.success=false;
			this.message="你添加的用户组 或者相同的级别已经存在";
		}else {
			mysql.Update(sql);
			this.success=true;
			this.message="添加用户组成功";
			mysql.writeLog(1, "添加用户组:"+name, fun.getSession("username"));
			
		}
		return SUCCESS;
	}
	public String update_data(){
		String sql_exists="select id from web_group where  name='"+name+"' and id!="+id;
		String sql="update web_group set name='"+name+"',level='"+level+"' where id="+id;
		if (mysql.isExists(sql_exists)) {
			this.success=false;
			this.message="你修改的用户组名称已经存在";
		}else {
			mysql.Update(sql);
			this.success=true;
			this.message="修改用户组成功";
			mysql.writeLog(2, "修改用户组:"+name, fun.getSession("username"));
			
		}
		return SUCCESS;
	}
	public String del_data(){
		String sql="delete from web_group where id in ("+deleteids+") and level<=5";
		if(level>5){
			this.message="不能删除超级管理员";
			this.success=false;
		}else{
			mysql.Update(sql);
			this.success=true;
			this.message="删除用户组成功";
			mysql.writeLog(3, "删除用户组"+deleteids, fun.getSession("username"));
		}
		
		return SUCCESS;
	}	
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getDeleteids() {
		return deleteids;
	}
	public void setDeleteids(String deleteids) {
		this.deleteids = deleteids;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	

}
