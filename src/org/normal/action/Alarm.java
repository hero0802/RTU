package org.normal.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.normal.function.Func;
import org.normal.function.JSON;
import org.normal.sql.MysqlConnection;

import com.opensymphony.xwork2.ActionSupport;

public class Alarm extends ActionSupport {
	private boolean success;
	private String deleteids;
	private String message;
	private int start = 0;
	private int limit = 10;

	private int id;
	private String site;
	private int siteId;
	private int siteType = 0;
	private String timeStart;
	private String timeEnd;
	
	private String xmlPath = Alarm.class.getResource("/config.xml").getPath();
	
	private MysqlConnection mysql = new MysqlConnection();
	private Func fun = new Func();
	private JSON json = new JSON();
	
/*	private int i_max=Integer.parseInt(fun.readXml("System", "I_maxValue"));
	private int r_max=Integer.parseInt(fun.readXml("System", "R_maxValue"));*/
	
	
	public void spd_alarm(){
		String count_spd = null, sql_spd = null;
		sql_spd = "select a.*,b.province,b.city,b.company,b.county,b.name"
				+ " from site_spd as a left join site as b on a.siteId=b.siteId"
				+ " where a.status=1 and a.siteId='" + site
				+ "' order by a.deviceId asc  limit "+start+","+limit+"";
		count_spd = "select count(id) from site_spd where status=1 and siteId ='"
				+ site + "'";

		/*
		 * sql="select * from alarm limit "+start+","+limit+"";
		 * sql_count="select count(id) from alarm";
		 */

		HashMap result = new HashMap();
		result.put("spd_items",mysql.DBList(sql_spd));
		result.put("spd_total", mysql.getCount(count_spd));
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
	public void r_alarm(){
		String count_r = null, sql_r = null;
		sql_r = "select a.*,b.province,b.city,b.company,b.county,b.name"
				+ "  from site_lr as a left join site as b on a.siteId=b.siteId"
				+ " where  a.value>a.maxValues"+" and a.siteId like '" + site
				+ "%'  order by a.siteId asc  limit "+start+","+limit+"";
		count_r = "select count(id) from site_lr where value>maxValues"+" and siteId like '"
				+ site + "%'";
		HashMap result = new HashMap();
		result.put("r_items",mysql.DBList(sql_r));
		result.put("r_total", mysql.getCount(count_r));
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
	public void i_alarm(){
		String count_i = null,sql_i = null;

		sql_i = "select a.*,b.province,b.city,b.company,b.county,b.name"
				+ "  from site_li as a left join site as b on a.siteId=b.siteId "
				+ " where a.value>maxValues"+" and a.siteId  like '" + site
				+ "%'  order by a.siteId asc  limit "+start+","+limit+"";
		count_i = "select count(id) from site_li where value>maxValues"+" and siteId like '"
				+ site + "%'";


		HashMap result = new HashMap();
		result.put("i_items",mysql.DBList(sql_i));
		result.put("i_total", mysql.getCount(count_i));
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

	// 确认告警
	public String sureAlarm() {
		String sql = "update alarm set status=1 where id=" + id;
		mysql.Update(sql);
		mysql.writeLog(4, "确认告警信息" + id, fun.getSession("username"));
		this.success = true;
		return SUCCESS;
	}

	// 统计
	public void count() {
		String sql1 = null, sql2 = null, sql3 = null;

		if (!timeStart.equals("") && !timeEnd.equals("")) {
			sql1 = "select count(id) from alarm where (siteId like '" + site
					+ "%' or siteName  like '" + site
					+ "%') and sitetype=1  and alarmTime between '" + timeStart
					+ "' and '" + timeEnd + "'";
			sql2 = "select count(id) from alarm where (siteId like '" + site
					+ "%' or siteName  like '" + site
					+ "%') and sitetype=2  and alarmTime between '" + timeStart
					+ "' and '" + timeEnd + "'";
			sql3 = "select count(id) from alarm where (siteId like '" + site
					+ "%' or siteName  like '" + site
					+ "%') and sitetype=3  and alarmTime between '" + timeStart
					+ "' and '" + timeEnd + "'";
		} else {
			sql1 = "select count(id) from alarm where (siteId like '" + site
					+ "%' or siteName  like '" + site + "%') and sitetype=1";
			sql2 = "select count(id) from alarm where (siteId like '" + site
					+ "%' or siteName  like '" + site + "%') and sitetype=2";
			sql3 = "select count(id) from alarm where (siteId like '" + site
					+ "%' or siteName  like '" + site + "%') and sitetype=3";
		}

		HashMap result = new HashMap();
		result.put("spd", mysql.getCount(sql1));
		result.put("r", mysql.getCount(sql2));
		result.put("i", mysql.getCount(sql3));
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
	//导出站点设备告警详细记录
	

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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public int getSiteType() {
		return siteType;
	}

	public void setSiteType(int siteType) {
		this.siteType = siteType;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

}
