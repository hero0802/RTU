package org.normal.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;
import org.normal.function.Func;
import org.normal.sql.MysqlConnection;
import org.normal.function.JSON;

import com.opensymphony.xwork2.ActionSupport;

public class User  extends ActionSupport {
	private boolean success;
	private String deleteids;
	private String message;
	private int start=0;
	private int limit=10;
	
	private int id;
	private String username;
	private String password;
	private String email;
	private int groupid;
	private String tel;
	
	private int addUser;
	private int updateUser;
	private int delUser;
	private int lockUser;
	
	private int addGroup;
	private int updateGroup;
	private int delGroup;
	
	private int addSite;
	private int updateSite;
	private int delSite;
	
	private int delLog;
	private int editPower;
	
	private String province;
	private String city;
	private String county;
	private String company;
	
	
	
	private String xmlPath=User.class.getResource("/config.xml").getPath();
	
	private MysqlConnection mysql=new MysqlConnection();
	private Func fun = new Func();
	private JSON json=new JSON();
	
	/**
	 * 注册账号
	 * @return
	 */
	public String register(){
		this.password=fun.getMd5(password);
		String sql="select id from web_user where username='"+username+"'";
		if (mysql.isExists(sql)) {
			this.success=false;
			this.message="用户已经存在";
		}else {
			String m_sql="insert into web_user(username,password,email,tel,date,groupid,status)values('"+username+"',"
					+ "'"+password+"','"+email+"','"+tel+"','"+fun.nowDate()+"',1,1)";
			mysql.Update(m_sql);
			this.success=true;
			this.message="用户注册成功";
			mysql.writeLog(1, "注册会员账号"+username, "游客");
		}
		return SUCCESS;
	}
	//添加用户
	public String add_user(){
		this.password=fun.getMd5(password);
		String sql="select id from web_user where username='"+username+"'";
		if (mysql.isExists(sql)) {
			this.success=false;
			this.message="用户已经存在";
		}else {
			String m_sql="insert into web_user(username,password,email,tel,date,groupid,status)values('"+username+"',"
					+ "'"+password+"','"+email+"','"+tel+"','"+fun.nowDate()+"','"+groupid+"',1)";
			if (mysql.Update(m_sql)) {
				id=mysql.username_userid(username);
				String m_sql_action="insert into user_action (userid)values('"+id+"')";
				//userid 用户Id	provinceId	cityId	countyId	province 城市	city 城市	county 县级	company 单位
				String m_sql_zone="insert into user_zone(userid,provinceId,cityId,countyId,province,city,county,company)"
						+ "values('"+id+"',0,0,0,'全部','全部','全部','全部')";
				mysql.Update(m_sql_action);
				mysql.Update(m_sql_zone);				
			}
			
			this.success=true;
			this.message="用户添加成功";
			mysql.writeLog(1, "添加会员账号"+username, fun.getSession("username"));
		}
		return SUCCESS;
	}
	/**
	 * 登录系统
	 * @return
	 */
	public String login(){
		String sql="select id from web_user where username='"+username+"' and password='"+fun.getMd5(password)+"'";
		String sql_status="select status from web_user where username='"+username+"'";
		if (mysql.isExists(sql)) {
			if (mysql.userStatus(sql_status)==0) {
				this.success=false;
				this.message="账号被禁用，请联系管理员";
			}else {
				this.success=true;
				this.message="登录成功";
				fun.setSession("username", username);
				fun.setSession("userid",String.valueOf(mysql.username_userid(username)));
				fun.setSession("groupname", mysql.username_groupName(username));
				mysql.writeLog(4, "登录系统", username);
			}
		}else {
			this.success=false;
			this.message="账号密码错误";
		}
		return SUCCESS;
	}
	//获取会员列表
	public void user_data(){
		//String sql="select * from web_user limit "+start+","+limit+"";
		String sql="select a.*,b.name as groupName  from web_user as a left join web_group as b on a.groupid=b.id  order"
				+ " by id desc limit "+start+","+limit+"";
		String sql_count="select count(id) from web_user";
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
	
	//获取会员的地域权限
	public void user_zone_data(){
		String sql="select * from user_zone where userid="+id;
		ArrayList data = mysql.DBList(sql);
		HashMap result = new HashMap();
		result.put("items", data);
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
	//获取会员组列表
	public void user_group_data(){
		String sql="select * from web_group order by level desc";
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
	//删除用户
	public String del_user(){
		String[] ids=deleteids.split(",");
		for (String str : ids) {
			
			if(mysql.userid_username(Integer.parseInt(str)).equals("admin")){
				
				this.success=false;
				this.message="默认账户不能删除";
			}else {
				String sql="delete from web_user where id="+Integer.parseInt(str);
				mysql.Update(sql);
				this.success=true;
				this.message="删除用户成功";
				mysql.writeLog(3, "删除会员账号"+username, fun.getSession("username"));
			}
		}
		return SUCCESS;
	}
	//加锁
	public String addLock(){
		String sql="update web_user set status=0 where id="+id;
		mysql.Update(sql);
		this.message="会员被禁用";
		mysql.writeLog(4, "禁用账号"+username, fun.getSession("username"));
		this.success=true;
		return SUCCESS;
		
	}
	//解锁
	public String unLock(){
		String sql="update web_user set status=1 where id="+id;
		mysql.Update(sql);
		this.message="会员启用成功";
		mysql.writeLog(4, "启用会员账号"+username, fun.getSession("username"));
		this.success=true;
		return SUCCESS;
	}
	//修改用户
	public String update_user(){
		String sql=null;
		if (password.equals("")) {
			 sql="update web_user set email='"+email+"',tel='"+tel+"',groupid='"+groupid+"' where id="+id;
		}else {
			 sql="update web_user set email='"+email+"',tel='"+tel+"',groupid='"+groupid+"',"
			 		+ "password='"+fun.getMd5(password)+"' where id="+id;
		}
		mysql.Update(sql);
		this.success=true;
		this.message="修改会员成功";
		mysql.writeLog(2, "修改会员账号"+username, fun.getSession("username"));
		return SUCCESS;
		
	}
	//修改个人资料
	public String update_myself(){
		String sql=null;
		if (password.equals("")) {
			 sql="update web_user set email='"+email+"',tel='"+tel+"'  where username='"+username+"'";
		}else {
			 sql="update web_user set email='"+email+"',tel='"+tel+"',"
			 		+ "password='"+fun.getMd5(password)+"' where username='"+username+"'";
		}
		mysql.Update(sql);
		this.success=true;
		this.message="修改个人成功";
		mysql.writeLog(2, "修改个人资料"+username, fun.getSession("username"));
		return SUCCESS;
		
	}
	//退出系统
	public String  LoginOut(){
		if (!fun.getSession("username").equals(null)) {
			fun.distorySession("username");
		}
		this.success=true;
		return SUCCESS;
	}
    //获取登录用户资料
	public void LoginUser(){
		String sql="select * from web_user where username='"+fun.getSession("username")+"'";
		ArrayList data = mysql.DBList(sql);

		HashMap result = new HashMap();
		result.put("items", data);
		result.put("total", data.size());
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
	//获取登录用户权限
	public void LoginUserPower(){
		String sql="select * from user_action where userid='"+fun.getSession("userid")+"'";
		ArrayList data = mysql.DBList(sql);

		HashMap result = new HashMap();
		result.put("items", data);
		result.put("total", data.size());
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
	//获取用户权限
	public void UserPower(){
		String sql="select * from user_action where userid='"+id+"'";
		String sql2="select * from user_zone where userid='"+id+"'";
		ArrayList data = mysql.DBList(sql);

		HashMap result = new HashMap();
		result.put("power", data);
		result.put("zone", mysql.DBList(sql2));
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
	//获取用户地域列表
		public void UserZone(){
			String sql="select * from user_zone where userid='"+id+"'";
			ArrayList data = mysql.DBList(sql);

			HashMap result = new HashMap();
			result.put("items", data);
			result.put("total", data.size());
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
	//保存权限
	public String savePower(){
		String sql1="update user_action set m_addUser='"+addUser+"',m_updateUser='"+updateUser+"',m_deleteUser='"+delUser+"',"
				+ "m_addGroup='"+addGroup+"',m_updateGroup='"+updateGroup+"',m_deleteGroup='"+delGroup+"',"
						+ "m_addSite='"+addSite+"',m_updateSite='"+updateSite+"',m_deleteSite='"+delSite+"',m_deleteLog='"+delLog+"',"
								+ "m_lockUser='"+lockUser+"',m_editPower='"+editPower+"'  where userid="+id;
		String sql2="update user_zone set "
				+ "province='"+MysqlConnection.areaName(province)+"',"
				+ "city='"+MysqlConnection.areaName(city)+"',"
				+ "county='"+MysqlConnection.areaName(county)+"',"
				+ "company='"+MysqlConnection.areaName(company)+"',"
				+ "provinceId='"+province+"',"
				+ "cityId='"+city+"',"
				+ "countyId='"+county+"',company='"+company+"'"
				+ "  where userid="+id;
		mysql.Update(sql1);
		mysql.Update(sql2);
		this.message="用户权限更改成功";
		this.success=true;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getAddUser() {
		return addUser;
	}
	public void setAddUser(int addUser) {
		this.addUser = addUser;
	}
	public int getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(int updateUser) {
		this.updateUser = updateUser;
	}
	public int getDelUser() {
		return delUser;
	}
	public void setDelUser(int delUser) {
		this.delUser = delUser;
	}
	public int getLockUser() {
		return lockUser;
	}
	public void setLockUser(int lockUser) {
		this.lockUser = lockUser;
	}
	public int getAddGroup() {
		return addGroup;
	}
	public void setAddGroup(int addGroup) {
		this.addGroup = addGroup;
	}
	public int getUpdateGroup() {
		return updateGroup;
	}
	public void setUpdateGroup(int updateGroup) {
		this.updateGroup = updateGroup;
	}
	public int getDelGroup() {
		return delGroup;
	}
	public void setDelGroup(int delGroup) {
		this.delGroup = delGroup;
	}
	public int getAddSite() {
		return addSite;
	}
	public void setAddSite(int addSite) {
		this.addSite = addSite;
	}
	public int getUpdateSite() {
		return updateSite;
	}
	public void setUpdateSite(int updateSite) {
		this.updateSite = updateSite;
	}
	public int getDelSite() {
		return delSite;
	}
	public void setDelSite(int delSite) {
		this.delSite = delSite;
	}
	public int getDelLog() {
		return delLog;
	}
	public void setDelLog(int delLog) {
		this.delLog = delLog;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getEditPower() {
		return editPower;
	}
	public void setEditPower(int editPower) {
		this.editPower = editPower;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	

	

}
