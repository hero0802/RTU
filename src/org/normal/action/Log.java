package org.normal.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;
import org.normal.function.Func;
import org.normal.function.JSON;
import org.normal.sql.MysqlConnection;

import com.opensymphony.xwork2.ActionSupport;

public class Log extends ActionSupport {
	private boolean success;
	private String deleteids;
	private String message;
	private int start=0;
	private int limit=10;
	
	private MysqlConnection mysql=new MysqlConnection();
	private Func fun = new Func();
	private JSON json=new JSON();
	
	/**
	 * 日志记录
	 */
	public void log_data(){
		String sql="select * from web_log limit "+start+","+limit+"";
		String sql_count="select count(id) from web_log";
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
	/**
	 * 删除日志
	 * @return
	 */
	public String del_log(){
		String sql="delete from web_log where id in ("+deleteids+")";
		mysql.Update(sql);
		this.success=true;
		this.message="删除日志成功";
		//mysql.writeLog(3, "删除日志"+deleteids, fun.getSession("username"));
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
	

}
