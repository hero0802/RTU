package org.normal.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.sf.ehcache.search.expression.And;
import nl.justobjects.pushlet.util.Log;

import org.apache.commons.validator.Var;
import org.apache.struts2.ServletActionContext;
import org.normal.function.Func;
import org.normal.function.JSON;
import org.normal.javabean.Rbean;
import org.normal.sql.MysqlConnection;

import com.normal.socket.RTUStruct;
import com.normal.socket.TcpClient;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class Site extends ActionSupport {
	private boolean success;
	private String deleteids;
	private String message;
	private int start = 0;
	private int limit = 10;

	private int md44_deviceId;
	private String md44_name;
	private String md44_type;
	private String md44_address;

	private int id;
	private int siteId;
	private String site;
	private String startTime;
	private String endTime;
	private String name;
	private String trade;
	private String province;
	private String city;
	private String county;
	private int provinceId;
	private int cityId;
	private int countyId;
	private String company;
	private double lon;
	private double lat;

	private int spd_deviceId;
	private String spd_name;
	private String spd_model;
	private String spd_address;
	private String spd_md44id;
	private int spd_level;
	private String spd_type;
	
	private String deviceId;
	private String model;

	private int r_deviceId;
	private String r_name;
	private String r_model;
	private String r_modelName;
	private String r_address;
	private String r_maxValues;
	private String r_type;
	private int r_md44id;
	private int flag;

	private int i_deviceId;
	private String i_name;
	private String i_model;
	private String i_address;
	private String i_maxValues;
	private String i_maxNum;

	private String timeS;
	private String timeE;
	private String r1;
	private String r2;
	
	private String excelName;

	// private String
	private String xmlPath = Site.class.getResource("/config.xml").getPath();

	private MysqlConnection mysql = new MysqlConnection();
	private Func fun = new Func();
	private JSON json = new JSON();

	// private float i_max = Float.parseFloat(fun.readXml("System",
	// "i_maxValues"));
	// private float r_max = Float.parseFloat(fun.readXml("System",
	// "R_maxValue"));
	
	//导出站点告警
	public String ExcelToSite(){
		String sql = null, sql_count = null;
		String str = " where company like '"+company+"%'";
		
		if (!province.equals("0")) {
			str += " and province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and county='" +MysqlConnection.areaName(county) + "'";
		}
		
		if (LoginUserZone2().equals("")) {
			sql = "select a.siteId,b.name,b.trade,b.province,b.city,b.county,b.company  from view_alarm_site as a "
					+ "left join site as b on a.siteId=b.siteId  where a.siteId in(select siteId from site "+str+") and a.siteId like '"+site+"%' "
					+ "order by a.siteId asc limit 0,60000";
		} else {
			sql = "select a.siteId,b.name,b.trade,b.province,b.city,b.county,b.company   from view_alarm_site as a "
					+ "left join site as b on a.siteId=b.siteId  where a.siteId in(select siteId from site "+str+" and "+LoginUserZone2()+") and a.siteId like '"+site+"%' "
					+ " order by a.siteId asc limit 0,60000";

		}

		ArrayList data = mysql.DBList(sql);
		try{
			String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
			String pathname=saveDir+"/站点告警.xls";
			File Path=new File(saveDir);
			if(!Path.exists()){Path.mkdirs();}
			File file=new File(pathname);
			WritableWorkbook book=Workbook.createWorkbook(file);			
			WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
			WritableCellFormat fontFormat=new WritableCellFormat(font);
			fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
			fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
			fontFormat.setWrap(true);  //自动换行
			fontFormat.setBackground(Colour.PINK);//背景颜色
			fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
			fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

			//设置头部字体格式
            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
            //应用字体
            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
            //设置其他样式
            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            fontFormat_h.setBackground(Colour.YELLOW);//背景色
            fontFormat_h.setWrap(false);//不自动换行
            

			//设置主题内容字体格式
            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
            //应用字体
            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
            //设置其他样式
            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            fontFormat_Content.setBackground(Colour.WHITE);//背景色
            fontFormat_Content.setWrap(false);//不自动换行
            
            //设置数字格式
            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

			Label title=new Label(0,0,"设备信息",fontFormat);


			WritableSheet sheet=book.createSheet("站点告警记录", 0);
			//sheet.mergeCells(0,0,3,0);

			Label label_1=new Label(0,0,"省份",fontFormat_h);//创建单元格
			Label label_2=new Label(1,0,"城市",fontFormat_h);
			Label label_3=new Label(2,0,"区县",fontFormat_h);
			Label label_4=new Label(3,0,"单位",fontFormat_h);
			Label label_5=new Label(4,0,"站点ID",fontFormat_h);		
			Label label_6=new Label(5,0,"站点名称",fontFormat_h);


			
			sheet.setRowView(0,500);
			sheet.setColumnView(0, 10);
			sheet.setColumnView(1, 20);
			sheet.setColumnView(2, 20);
			sheet.setColumnView(3, 20);
			sheet.setColumnView(4, 20);
			sheet.setColumnView(5, 30);
			

			sheet.addCell(label_1);
			sheet.addCell(label_2);
			sheet.addCell(label_3);
			sheet.addCell(label_4);
			sheet.addCell(label_5);
			sheet.addCell(label_6);
			List<Map<String,String>> list=data;
			for(int i=0;i<list.size();i++){
				Map<String,String> map=list.get(i);
				Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
				Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
				Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
				Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
				Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
				Label value_6=new Label(5,i+1,map.get("name"),fontFormat_Content);
				
				sheet.setRowView(i+1,400);
				sheet.addCell(value_1);
				sheet.addCell(value_2);
				sheet.addCell(value_3);
				sheet.addCell(value_4);
				sheet.addCell(value_5);
				sheet.addCell(value_6);
			}


			book.write();
			book.close(); 
			System.out.print("导出成功");
			this.success=true;
			/*HttpServletResponse response =ServletActionContext.getResponse();
			DownExcelFile(response,pathname);*/
		}catch(Exception e){
			e.printStackTrace();

		}return SUCCESS;}
	//导出雷击电流实时数据
	public String ExcelToI(){
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {

			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName from site_li  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc limit 0,60000";

			sql_count = "select count(*) from site_li  as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName  from site_li  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc ,a.deviceId asc limit  0,60000";
			// sql="select * from site "+str+" and "+LoginUserZone()+" order by siteId asc limit "+start+","+limit+"";
			sql_count = "select count(*) from site_li  as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

		ArrayList data = mysql.DBList(sql);

			try{
				String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
				String pathname=saveDir+"/雷击电流实时告警.xls";
				File Path=new File(saveDir);
				if(!Path.exists()){Path.mkdirs();}
				File file=new File(pathname);
				WritableWorkbook book=Workbook.createWorkbook(file);			
				WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
				WritableCellFormat fontFormat=new WritableCellFormat(font);
				fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
				fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
				fontFormat.setWrap(true);  //自动换行
				fontFormat.setBackground(Colour.PINK);//背景颜色
				fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
				fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

				//设置头部字体格式
	            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
	            //应用字体
	            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
	            //设置其他样式
	            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_h.setBackground(Colour.YELLOW);//背景色
	            fontFormat_h.setWrap(false);//不自动换行
	            

				//设置主题内容字体格式
	            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
	            //应用字体
	            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
	            //设置其他样式
	            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_Content.setBackground(Colour.WHITE);//背景色
	            fontFormat_Content.setWrap(false);//不自动换行
	            
	            //设置数字格式
	            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
				jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

				Label title=new Label(0,0,"设备信息",fontFormat);


				WritableSheet sheet=book.createSheet("雷击电流实时告警", 0);
				//sheet.mergeCells(0,0,3,0);

				Label label_1=new Label(0,0,"省份",fontFormat_h);//创建单元格
				Label label_2=new Label(1,0,"城市",fontFormat_h);
				Label label_3=new Label(2,0,"区县",fontFormat_h);
				Label label_4=new Label(3,0,"单位",fontFormat_h);
				Label label_5=new Label(4,0,"站点ID",fontFormat_h);		
				Label label_6=new Label(5,0,"站点名称",fontFormat_h);
				Label label_7=new Label(6,0,"监测仪ID",fontFormat_h);
				Label label_8=new Label(7,0,"监测仪名称",fontFormat_h);
				Label label_9=new Label(8,0,"监测仪型号",fontFormat_h);
				Label label_10=new Label(9,0,"电流值",fontFormat_h);
				Label label_11=new Label(10,0,"阈值",fontFormat_h);
				Label label_12=new Label(11,0,"时间",fontFormat_h);
				

				
				sheet.setRowView(0,500);
				sheet.setColumnView(0, 10);
				sheet.setColumnView(1, 20);
				sheet.setColumnView(2, 20);
				sheet.setColumnView(3, 20);
				sheet.setColumnView(4, 20);
				sheet.setColumnView(5, 30);
				sheet.setColumnView(6, 30);
				sheet.setColumnView(7, 30);
				sheet.setColumnView(8, 30);
				sheet.setColumnView(9, 30);
				sheet.setColumnView(10, 30);
				sheet.setColumnView(11, 30);
				
				sheet.addCell(label_1);
				sheet.addCell(label_2);
				sheet.addCell(label_3);
				sheet.addCell(label_4);
				sheet.addCell(label_5);
				sheet.addCell(label_6);
				sheet.addCell(label_7);
				sheet.addCell(label_8);
				sheet.addCell(label_9);
				sheet.addCell(label_10);
				sheet.addCell(label_11);
				sheet.addCell(label_12);
				List<Map<String,String>> list=data;
				for(int i=0;i<list.size();i++){
					Map<String,String> map=list.get(i);
					Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
					Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
					Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
					Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
					Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
					Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
					Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
					Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
					Label value_9=new Label(8,i+1,map.get("model"),fontFormat_Content);
					Label value_10=new Label(9,i+1,String.valueOf(map.get("value")),fontFormat_Content);
					Label value_11=new Label(10,i+1,String.valueOf(map.get("maxValues")),fontFormat_Content);
					Label value_12=new Label(11,i+1,String.valueOf(map.get("time")),fontFormat_Content);
					
					sheet.setRowView(i+1,400);
					sheet.addCell(value_1);
					sheet.addCell(value_2);
					sheet.addCell(value_3);
					sheet.addCell(value_4);
					sheet.addCell(value_5);
					sheet.addCell(value_6);
					sheet.addCell(value_7);
					sheet.addCell(value_8);
					sheet.addCell(value_9);
					sheet.addCell(value_10);
					sheet.addCell(value_11);
					sheet.addCell(value_12);
				}


				book.write();
				book.close(); 
				System.out.print("导出成功");
				this.success=true;
				/*HttpServletResponse response =ServletActionContext.getResponse();
				DownExcelFile(response,pathname);*/
			}catch(Exception e){
				e.printStackTrace();

			}
			return SUCCESS;
			}
	
	//导出雷击电流历史数据
	public String ExcelToIh(){
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (!startTime.equals("") && !endTime.equals("")) {
			str += " and a.time between '" + startTime + "' and '" + endTime
					+ "'";
		}
		if (LoginUserZone().equals("")) {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName,c.maxValues,c.model,c.name from site_li_h  as a left join site as b on a.siteId=b.siteId left join site_li as c on a.siteId=c.siteId"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.time desc limit 0,60000";
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName,c.maxValues,c.model,c.name from site_li_h  as a left join site as b on a.siteId=b.siteId left join site_li as c on a.siteId=c.siteId"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.time desc limit 0,60000";
		
		}
			ArrayList data = mysql.DBList(sql);

				try{
					String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
					String pathname=saveDir+"/雷击电流历史告记录.xls";
					File Path=new File(saveDir);
					if(!Path.exists()){Path.mkdirs();}
					File file=new File(pathname);
					WritableWorkbook book=Workbook.createWorkbook(file);			
					WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
					WritableCellFormat fontFormat=new WritableCellFormat(font);
					fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
					fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
					fontFormat.setWrap(true);  //自动换行
					fontFormat.setBackground(Colour.PINK);//背景颜色
					fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
					fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

					//设置头部字体格式
		            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
		                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
		            //应用字体
		            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
		            //设置其他样式
		            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
		            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
		            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
		            fontFormat_h.setBackground(Colour.YELLOW);//背景色
		            fontFormat_h.setWrap(false);//不自动换行
		            

					//设置主题内容字体格式
		            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
		                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
		            //应用字体
		            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
		            //设置其他样式
		            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
		            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
		            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
		            fontFormat_Content.setBackground(Colour.WHITE);//背景色
		            fontFormat_Content.setWrap(false);//不自动换行
		            
		            //设置数字格式
		            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
					jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

					Label title=new Label(0,0,"设备信息",fontFormat);


					WritableSheet sheet=book.createSheet("雷击电流历史告记录", 0);
					//sheet.mergeCells(0,0,3,0);

					Label label_1=new Label(0,0,"省份",fontFormat_h);//创建单元格
					Label label_2=new Label(1,0,"城市",fontFormat_h);
					Label label_3=new Label(2,0,"区县",fontFormat_h);
					Label label_4=new Label(3,0,"单位",fontFormat_h);
					Label label_5=new Label(4,0,"站点ID",fontFormat_h);		
					Label label_6=new Label(5,0,"站点名称",fontFormat_h);
					Label label_7=new Label(6,0,"监测仪ID",fontFormat_h);
					Label label_8=new Label(7,0,"监测仪名称",fontFormat_h);
					Label label_9=new Label(8,0,"监测仪型号",fontFormat_h);
					Label label_10=new Label(9,0,"电流值",fontFormat_h);
					Label label_11=new Label(10,0,"阈值",fontFormat_h);
					Label label_12=new Label(11,0,"时间",fontFormat_h);
					

					
					sheet.setRowView(0,500);
					sheet.setColumnView(0, 10);
					sheet.setColumnView(1, 20);
					sheet.setColumnView(2, 20);
					sheet.setColumnView(3, 20);
					sheet.setColumnView(4, 20);
					sheet.setColumnView(5, 30);
					sheet.setColumnView(6, 30);
					sheet.setColumnView(7, 30);
					sheet.setColumnView(8, 30);
					sheet.setColumnView(9, 30);
					sheet.setColumnView(10, 30);
					sheet.setColumnView(11, 30);
					
					sheet.addCell(label_1);
					sheet.addCell(label_2);
					sheet.addCell(label_3);
					sheet.addCell(label_4);
					sheet.addCell(label_5);
					sheet.addCell(label_6);
					sheet.addCell(label_7);
					sheet.addCell(label_8);
					sheet.addCell(label_9);
					sheet.addCell(label_10);
					sheet.addCell(label_11);
					sheet.addCell(label_12);
					List<Map<String,String>> list=data;
					for(int i=0;i<list.size();i++){
						Map<String,String> map=list.get(i);
						Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
						Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
						Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
						Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
						Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
						Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
						Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
						Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
						Label value_9=new Label(8,i+1,map.get("model"),fontFormat_Content);
						Label value_10=new Label(9,i+1,String.valueOf(map.get("value")),fontFormat_Content);
						Label value_11=new Label(10,i+1,String.valueOf(map.get("maxValues")),fontFormat_Content);
						Label value_12=new Label(11,i+1,String.valueOf(map.get("time")),fontFormat_Content);
						
						sheet.setRowView(i+1,400);
						sheet.addCell(value_1);
						sheet.addCell(value_2);
						sheet.addCell(value_3);
						sheet.addCell(value_4);
						sheet.addCell(value_5);
						sheet.addCell(value_6);
						sheet.addCell(value_7);
						sheet.addCell(value_8);
						sheet.addCell(value_9);
						sheet.addCell(value_10);
						sheet.addCell(value_11);
						sheet.addCell(value_12);
					}


					book.write();
					book.close(); 
					System.out.print("导出成功");
					this.success=true;
					/*HttpServletResponse response =ServletActionContext.getResponse();
					DownExcelFile(response,pathname);*/
				}catch(Exception e){
					e.printStackTrace();

				}
				return SUCCESS;
				}
	
	//导出接地电阻实时数据
	public String ExcelToR(){
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {

			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName from site_lr  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit 0,60000";

		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName from site_lr  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit 0,60000";
			
		}


		ArrayList data = mysql.DBList(sql);

			try{
				String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
				String pathname=saveDir+"/接地电阻实时数据.xls";
				File Path=new File(saveDir);
				if(!Path.exists()){Path.mkdirs();}
				File file=new File(pathname);
				WritableWorkbook book=Workbook.createWorkbook(file);			
				WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
				WritableCellFormat fontFormat=new WritableCellFormat(font);
				fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
				fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
				fontFormat.setWrap(true);  //自动换行
				fontFormat.setBackground(Colour.PINK);//背景颜色
				fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
				fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

				//设置头部字体格式
	            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
	            //应用字体
	            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
	            //设置其他样式
	            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_h.setBackground(Colour.YELLOW);//背景色
	            fontFormat_h.setWrap(false);//不自动换行
	            

				//设置主题内容字体格式
	            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
	            //应用字体
	            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
	            //设置其他样式
	            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_Content.setBackground(Colour.WHITE);//背景色
	            fontFormat_Content.setWrap(false);//不自动换行
	            
	            //设置数字格式
	            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
				jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

				Label title=new Label(0,0,"设备信息",fontFormat);


				WritableSheet sheet2=book.createSheet("接地电阻实时数据", 0);
				//sheet.mergeCells(0,0,3,0);
				Label label_1_r=new Label(0,0,"省份",fontFormat_h);//创建单元格
				Label label_2_r=new Label(1,0,"城市",fontFormat_h);
				Label label_3_r=new Label(2,0,"区县",fontFormat_h);
				Label label_4_r=new Label(3,0,"单位",fontFormat_h);
				Label label_5_r=new Label(4,0,"站点ID",fontFormat_h);		
				Label label_6_r=new Label(5,0,"站点名称",fontFormat_h);
				Label label_7_r=new Label(6,0,"监测仪ID",fontFormat_h);
				Label label_8_r=new Label(7,0,"监测仪名称",fontFormat_h);
				Label label_9_r=new Label(8,0,"监测仪型号",fontFormat_h);
				Label label_10_r=new Label(9,0,"监测点",fontFormat_h);
				Label label_11_r=new Label(10,0,"监测点名称",fontFormat_h);
				Label label_12_r=new Label(11,0,"监测点值",fontFormat_h);
				Label label_13_r=new Label(12,0,"阈值",fontFormat_h);
				Label label_14_r=new Label(13,0,"时间",fontFormat_h);
				

				sheet2.setRowView(0,500);
				sheet2.setColumnView(0, 10);
				sheet2.setColumnView(1, 20);
				sheet2.setColumnView(2, 20);
				sheet2.setColumnView(3, 20);
				sheet2.setColumnView(4, 20);
				sheet2.setColumnView(5, 30);
				sheet2.setColumnView(6, 30);
				sheet2.setColumnView(7, 30);
				sheet2.setColumnView(8, 30);
				sheet2.setColumnView(9, 30);
				sheet2.setColumnView(10, 30);
				sheet2.setColumnView(11, 30);
				sheet2.setColumnView(12, 30);
				sheet2.setColumnView(13, 30);
				
				sheet2.addCell(label_1_r);
				sheet2.addCell(label_2_r);
				sheet2.addCell(label_3_r);
				sheet2.addCell(label_4_r);
				sheet2.addCell(label_5_r);
				sheet2.addCell(label_6_r);
				sheet2.addCell(label_7_r);
				sheet2.addCell(label_8_r);
				sheet2.addCell(label_9_r);
				sheet2.addCell(label_10_r);
				sheet2.addCell(label_11_r);
				sheet2.addCell(label_12_r);
				sheet2.addCell(label_13_r);
				sheet2.addCell(label_14_r);
				List<Map<String,String>> list=data;
				for(int i=0;i<list.size();i++){
					Map<String,String> map=list.get(i);
					Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
					Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
					Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
					Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
					Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
					Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
					Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
					Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
					Label value_9=new Label(8,i+1,map.get("type"),fontFormat_Content);
					Label value_10=new Label(9,i+1,String.valueOf(map.get("model")),fontFormat_Content);
					Label value_11=new Label(10,i+1,map.get("modelName"),fontFormat_Content);			
					Label value_12=new Label(11,i+1,String.valueOf(map.get("value")),fontFormat_Content);
					Label value_13=new Label(12,i+1,String.valueOf(map.get("maxValues")),fontFormat_Content);
					Label value_14=new Label(13,i+1,String.valueOf(map.get("time")),fontFormat_Content);
					
					sheet2.setRowView(i+1,400);
					sheet2.addCell(value_1);
					sheet2.addCell(value_2);
					sheet2.addCell(value_3);
					sheet2.addCell(value_4);
					sheet2.addCell(value_5);
					sheet2.addCell(value_6);
					sheet2.addCell(value_7);
					sheet2.addCell(value_8);
					sheet2.addCell(value_9);
					sheet2.addCell(value_10);
					sheet2.addCell(value_11);
					sheet2.addCell(value_12);
					sheet2.addCell(value_13);
					sheet2.addCell(value_14);
				}


				book.write();
				book.close(); 
				System.out.print("导出成功");
				this.success=true;
				/*HttpServletResponse response =ServletActionContext.getResponse();
				DownExcelFile(response,pathname);*/
			}catch(Exception e){
				e.printStackTrace();

			}
			return SUCCESS;
			}
	//导出接地电阻历史数据
	public String ExcelToRh(){
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (!startTime.equals("") && !endTime.equals("")) {
			str += " and a.time between '" + startTime + "' and '" + endTime
					+ "'";
		}

		if (LoginUserZone().equals("")) {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName,c.maxValues,c.model,c.name,c.modelName,c.type "
					+ "from site_lr_h  as a "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_lr as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.model asc,a.time desc limit 0,60000";
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name as siteName,c.maxValues,c.model,c.name,c.modelName,c.type from site_lr_h  as a "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_lr as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc,a.time desc limit 0,60000";
		}
			ArrayList data = mysql.DBList(sql);

				try{
					String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
					String pathname=saveDir+"/接地电阻历史数据.xls";
					File Path=new File(saveDir);
					if(!Path.exists()){Path.mkdirs();}
					File file=new File(pathname);
					WritableWorkbook book=Workbook.createWorkbook(file);			
					WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
					WritableCellFormat fontFormat=new WritableCellFormat(font);
					fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
					fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
					fontFormat.setWrap(true);  //自动换行
					fontFormat.setBackground(Colour.PINK);//背景颜色
					fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
					fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

					//设置头部字体格式
		            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
		                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
		            //应用字体
		            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
		            //设置其他样式
		            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
		            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
		            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
		            fontFormat_h.setBackground(Colour.YELLOW);//背景色
		            fontFormat_h.setWrap(false);//不自动换行
		            

					//设置主题内容字体格式
		            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
		                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
		            //应用字体
		            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
		            //设置其他样式
		            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
		            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
		            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
		            fontFormat_Content.setBackground(Colour.WHITE);//背景色
		            fontFormat_Content.setWrap(false);//不自动换行
		            
		            //设置数字格式
		            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
					jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

					Label title=new Label(0,0,"设备信息",fontFormat);


					WritableSheet sheet2=book.createSheet("接地电阻历史数据", 0);
					//sheet.mergeCells(0,0,3,0);

					Label label_1_r=new Label(0,0,"省份",fontFormat_h);//创建单元格
					Label label_2_r=new Label(1,0,"城市",fontFormat_h);
					Label label_3_r=new Label(2,0,"区县",fontFormat_h);
					Label label_4_r=new Label(3,0,"单位",fontFormat_h);
					Label label_5_r=new Label(4,0,"站点ID",fontFormat_h);		
					Label label_6_r=new Label(5,0,"站点名称",fontFormat_h);
					Label label_7_r=new Label(6,0,"监测仪ID",fontFormat_h);
					Label label_8_r=new Label(7,0,"监测仪名称",fontFormat_h);
					Label label_9_r=new Label(8,0,"监测仪型号",fontFormat_h);
					Label label_10_r=new Label(9,0,"监测点",fontFormat_h);
					Label label_11_r=new Label(10,0,"监测点名称",fontFormat_h);
					Label label_12_r=new Label(11,0,"监测点值",fontFormat_h);
					Label label_13_r=new Label(12,0,"阈值",fontFormat_h);
					Label label_14_r=new Label(13,0,"时间",fontFormat_h);
					

					sheet2.setRowView(0,500);
					sheet2.setColumnView(0, 10);
					sheet2.setColumnView(1, 20);
					sheet2.setColumnView(2, 20);
					sheet2.setColumnView(3, 20);
					sheet2.setColumnView(4, 20);
					sheet2.setColumnView(5, 30);
					sheet2.setColumnView(6, 30);
					sheet2.setColumnView(7, 30);
					sheet2.setColumnView(8, 30);
					sheet2.setColumnView(9, 30);
					sheet2.setColumnView(10, 30);
					sheet2.setColumnView(11, 30);
					sheet2.setColumnView(12, 30);
					sheet2.setColumnView(13, 30);
					
					sheet2.addCell(label_1_r);
					sheet2.addCell(label_2_r);
					sheet2.addCell(label_3_r);
					sheet2.addCell(label_4_r);
					sheet2.addCell(label_5_r);
					sheet2.addCell(label_6_r);
					sheet2.addCell(label_7_r);
					sheet2.addCell(label_8_r);
					sheet2.addCell(label_9_r);
					sheet2.addCell(label_10_r);
					sheet2.addCell(label_11_r);
					sheet2.addCell(label_12_r);
					sheet2.addCell(label_13_r);
					sheet2.addCell(label_14_r);
					List<Map<String,String>> list=data;
					for(int i=0;i<list.size();i++){
						Map<String,String> map=list.get(i);
						Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
						Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
						Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
						Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
						Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
						Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
						Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
						Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
						Label value_9=new Label(8,i+1,map.get("type"),fontFormat_Content);
						Label value_10=new Label(9,i+1,String.valueOf(map.get("model")),fontFormat_Content);
						Label value_11=new Label(10,i+1,map.get("modelName"),fontFormat_Content);			
						Label value_12=new Label(11,i+1,String.valueOf(map.get("value")),fontFormat_Content);
						Label value_13=new Label(12,i+1,String.valueOf(map.get("maxValues")),fontFormat_Content);
						Label value_14=new Label(13,i+1,String.valueOf(map.get("time")),fontFormat_Content);
						
						sheet2.setRowView(i+1,400);
						sheet2.addCell(value_1);
						sheet2.addCell(value_2);
						sheet2.addCell(value_3);
						sheet2.addCell(value_4);
						sheet2.addCell(value_5);
						sheet2.addCell(value_6);
						sheet2.addCell(value_7);
						sheet2.addCell(value_8);
						sheet2.addCell(value_9);
						sheet2.addCell(value_10);
						sheet2.addCell(value_11);
						sheet2.addCell(value_12);
						sheet2.addCell(value_13);
						sheet2.addCell(value_14);
					}


					book.write();
					book.close(); 
					System.out.print("导出成功");
					this.success=true;
					/*HttpServletResponse response =ServletActionContext.getResponse();
					DownExcelFile(response,pathname);*/
				}catch(Exception e){
					e.printStackTrace();

				}
				return SUCCESS;
				}
	//导出spd实时数据
	public String ExcelToSpd(){
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {

			sql = "select  a.*,"
					+ "b.province,b.city,b.county,b.company,b.name as siteName"
					+ " from site_spd  as a   left join site as b on a.siteId=b.siteId"
					+ " " + str + " order by a.siteId asc,a.deviceId asc,a.model asc limit 0,60000";

		} else {
			sql = "select  a.*,"
					+ "b.province,b.city,b.county,b.company,b.name  from site_spd  as a  left join site as b on a.siteId=b.siteId"
					+ " " + str + " and " + LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit 0,60000";
			
		}


		ArrayList data = mysql.DBList(sql);

			try{
				String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
				String pathname=saveDir+"/SPD历史数据.xls";
				File Path=new File(saveDir);
				if(!Path.exists()){Path.mkdirs();}
				File file=new File(pathname);
				WritableWorkbook book=Workbook.createWorkbook(file);			
				WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
				WritableCellFormat fontFormat=new WritableCellFormat(font);
				fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
				fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
				fontFormat.setWrap(true);  //自动换行
				fontFormat.setBackground(Colour.PINK);//背景颜色
				fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
				fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

				//设置头部字体格式
	            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
	            //应用字体
	            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
	            //设置其他样式
	            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_h.setBackground(Colour.YELLOW);//背景色
	            fontFormat_h.setWrap(false);//不自动换行
	            

				//设置主题内容字体格式
	            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
	            //应用字体
	            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
	            //设置其他样式
	            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_Content.setBackground(Colour.WHITE);//背景色
	            fontFormat_Content.setWrap(false);//不自动换行
	            
	            //设置数字格式
	            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
				jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

				Label title=new Label(0,0,"设备信息",fontFormat);


				WritableSheet sheet3=book.createSheet("SPD历史数据", 0);
				//sheet.mergeCells(0,0,3,0);
				Label label_1_s=new Label(0,0,"省份",fontFormat_h);//创建单元格
				Label label_2_s=new Label(1,0,"城市",fontFormat_h);
				Label label_3_s=new Label(2,0,"区县",fontFormat_h);
				Label label_4_s=new Label(3,0,"单位",fontFormat_h);
				Label label_5_s=new Label(4,0,"站点ID",fontFormat_h);		
				Label label_6_s=new Label(5,0,"站点名称",fontFormat_h);
				Label label_7_s=new Label(6,0,"监测仪ID",fontFormat_h);
				Label label_8_s=new Label(7,0,"监测仪名称",fontFormat_h);
				Label label_9_s=new Label(8,0,"监测仪型号",fontFormat_h);
				Label label_10_s=new Label(9,0,"开关量",fontFormat_h);
				Label label_11_s=new Label(10,0,"时间",fontFormat_h);
				
				sheet3.addCell(label_1_s);
				sheet3.addCell(label_2_s);
				sheet3.addCell(label_3_s);
				sheet3.addCell(label_4_s);
				sheet3.addCell(label_5_s);
				sheet3.addCell(label_6_s);
				sheet3.addCell(label_7_s);
				sheet3.addCell(label_8_s);
				sheet3.addCell(label_9_s);
				sheet3.addCell(label_10_s);
				sheet3.addCell(label_11_s);
				
				
				List<Map<String,String>> list=data;
				for(int i=0;i<list.size();i++){
					Map<String,String> map=list.get(i);
					Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
					Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
					Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
					Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
					Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
					Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
					Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
					Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
					Label value_9=new Label(8,i+1,map.get("type"),fontFormat_Content);
					Label value_10=new Label(9,i+1,String.valueOf(map.get("model")),fontFormat_Content);			
					Label value_11=new Label(10,i+1,String.valueOf(map.get("time")),fontFormat_Content);
					
					sheet3.setRowView(0,500);
					sheet3.setColumnView(0, 10);
					sheet3.setColumnView(1, 20);
					sheet3.setColumnView(2, 20);
					sheet3.setColumnView(3, 20);
					sheet3.setColumnView(4, 20);
					sheet3.setColumnView(5, 30);
					sheet3.setColumnView(6, 30);
					sheet3.setColumnView(7, 30);
					sheet3.setColumnView(8, 30);
					sheet3.setColumnView(9, 30);
					sheet3.setColumnView(10, 30);
					
					sheet3.setRowView(i+1,400);
					sheet3.addCell(value_1);
					sheet3.addCell(value_2);
					sheet3.addCell(value_3);
					sheet3.addCell(value_4);
					sheet3.addCell(value_5);
					sheet3.addCell(value_6);
					sheet3.addCell(value_7);
					sheet3.addCell(value_8);
					sheet3.addCell(value_9);
					sheet3.addCell(value_10);
					sheet3.addCell(value_11);
				}


				book.write();
				book.close(); 
				System.out.print("导出成功");
				this.success=true;
				/*HttpServletResponse response =ServletActionContext.getResponse();
				DownExcelFile(response,pathname);*/
			}catch(Exception e){
				e.printStackTrace();

			}
			return SUCCESS;
			}
	//导出spd历史数据
	public String ExcelToSpdh(){
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (!startTime.equals("") && !endTime.equals("")) {
			str += " and a.time between '" + startTime + "' and '" + endTime
					+ "'";
		}

		if (LoginUserZone().equals("")) {

			sql = "select  b.province,b.city,b.county,b.company,b.siteId,b.name as siteName,a.deviceId,a.model,c.name,c.type,c.level,a.status,a.time  "
					+ "from site_spd_h  as a   "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_spd as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit 0,60000";

			sql_count = "select count(*) from site_spd_h as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select  b.province,b.city,b.county,b.company,b.siteId,b.name as siteName,a.deviceId,a.model,c.name,c.type,c.level,a.status,a.time  "
					+ "from site_spd_h  as a   "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_spd as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc  limit 0,60000";
			sql_count = "select count(*) from site_spd_h as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}


		ArrayList data = mysql.DBList(sql);

			try{
				String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
				String pathname=saveDir+"/SPD历史数据.xls";
				File Path=new File(saveDir);
				if(!Path.exists()){Path.mkdirs();}
				File file=new File(pathname);
				WritableWorkbook book=Workbook.createWorkbook(file);			
				WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
				WritableCellFormat fontFormat=new WritableCellFormat(font);
				fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
				fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
				fontFormat.setWrap(true);  //自动换行
				fontFormat.setBackground(Colour.PINK);//背景颜色
				fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
				fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

				//设置头部字体格式
	            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
	            //应用字体
	            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
	            //设置其他样式
	            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_h.setBackground(Colour.YELLOW);//背景色
	            fontFormat_h.setWrap(false);//不自动换行
	            

				//设置主题内容字体格式
	            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
	                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
	            //应用字体
	            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
	            //设置其他样式
	            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
	            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
	            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
	            fontFormat_Content.setBackground(Colour.WHITE);//背景色
	            fontFormat_Content.setWrap(false);//不自动换行
	            
	            //设置数字格式
	            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
				jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

				Label title=new Label(0,0,"设备信息",fontFormat);


				WritableSheet sheet3=book.createSheet("SPD历史数据", 0);
				//sheet.mergeCells(0,0,3,0);
				Label label_1_s=new Label(0,0,"省份",fontFormat_h);//创建单元格
				Label label_2_s=new Label(1,0,"城市",fontFormat_h);
				Label label_3_s=new Label(2,0,"区县",fontFormat_h);
				Label label_4_s=new Label(3,0,"单位",fontFormat_h);
				Label label_5_s=new Label(4,0,"站点ID",fontFormat_h);		
				Label label_6_s=new Label(5,0,"站点名称",fontFormat_h);
				Label label_7_s=new Label(6,0,"监测仪ID",fontFormat_h);
				Label label_8_s=new Label(7,0,"SPD名称",fontFormat_h);
				Label label_9_s=new Label(8,0,"SPD型号",fontFormat_h);
				Label label_10_s=new Label(9,0,"SPD路数",fontFormat_h);
				Label label_11_s=new Label(10,0,"时间",fontFormat_h);
				
				sheet3.addCell(label_1_s);
				sheet3.addCell(label_2_s);
				sheet3.addCell(label_3_s);
				sheet3.addCell(label_4_s);
				sheet3.addCell(label_5_s);
				sheet3.addCell(label_6_s);
				sheet3.addCell(label_7_s);
				sheet3.addCell(label_8_s);
				sheet3.addCell(label_9_s);
				sheet3.addCell(label_10_s);
				sheet3.addCell(label_11_s);
				
				
				List<Map<String,String>> list=data;
				for(int i=0;i<list.size();i++){
					Map<String,String> map=list.get(i);
					Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
					Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
					Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
					Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
					Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
					Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
					Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
					Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
					Label value_9=new Label(8,i+1,map.get("type"),fontFormat_Content);
					Label value_10=new Label(9,i+1,String.valueOf(map.get("model")),fontFormat_Content);			
					Label value_11=new Label(10,i+1,String.valueOf(map.get("time")),fontFormat_Content);
					
					sheet3.setRowView(0,500);
					sheet3.setColumnView(0, 10);
					sheet3.setColumnView(1, 20);
					sheet3.setColumnView(2, 20);
					sheet3.setColumnView(3, 20);
					sheet3.setColumnView(4, 20);
					sheet3.setColumnView(5, 30);
					sheet3.setColumnView(6, 30);
					sheet3.setColumnView(7, 30);
					sheet3.setColumnView(8, 30);
					sheet3.setColumnView(9, 30);
					sheet3.setColumnView(10, 30);
					
					sheet3.setRowView(i+1,400);
					sheet3.addCell(value_1);
					sheet3.addCell(value_2);
					sheet3.addCell(value_3);
					sheet3.addCell(value_4);
					sheet3.addCell(value_5);
					sheet3.addCell(value_6);
					sheet3.addCell(value_7);
					sheet3.addCell(value_8);
					sheet3.addCell(value_9);
					sheet3.addCell(value_10);
					sheet3.addCell(value_11);
				}


				book.write();
				book.close(); 
				System.out.print("导出成功");
				this.success=true;
				/*HttpServletResponse response =ServletActionContext.getResponse();
				DownExcelFile(response,pathname);*/
			}catch(Exception e){
				e.printStackTrace();

			}
			return SUCCESS;
			}
			
	//导出站点设备告警
	public String ExcelToDevices(){
		String sql_spd = "select a.*,b.province,b.city,b.company,b.county,b.name as siteName"
				+ " from site_spd as a left join site as b on a.siteId=b.siteId"
				+ " where a.status=1 and a.siteId='"+site+"' order by a.siteId asc  limit 0,100";
		String sql_r = "select a.*,b.province,b.city,b.company,b.county,b.name as siteName"
				+ "  from site_lr as a left join site as b on a.siteId=b.siteId"
				+ " where  a.value>a.maxValues"+" and a.siteId ='"+site+"' order by a.siteId asc  limit 0,100";
		String sql_i = "select a.*,b.province,b.city,b.company,b.county,b.name as siteName"
				+ "  from site_li as a left join site as b on a.siteId=b.siteId "
				+ " where a.value>maxValues"+" and a.siteId ='"+site+"' order by a.siteId asc  limit 0,100";

		ArrayList spd_list= mysql.DBList(sql_spd);
		ArrayList r_list= mysql.DBList(sql_r);
		ArrayList i_list= mysql.DBList(sql_i);
		
		/*Log.info("s->"+Arrays.toString(spd_list.toArray()));
		Log.info("r->"+Arrays.toString(r_list.toArray()));
		Log.info("i->"+Arrays.toString(i_list.toArray()));*/
		try{
			String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
			String pathname=saveDir+"/"+site+"号站点告警.xls";
			File Path=new File(saveDir);
			if(!Path.exists()){Path.mkdirs();}
			File file=new File(pathname);
			WritableWorkbook book=Workbook.createWorkbook(file);			
			WritableFont font=new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
			WritableCellFormat fontFormat=new WritableCellFormat(font);
			fontFormat.setAlignment(Alignment.CENTRE);   //水平居中
			fontFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);//垂直居中
			fontFormat.setWrap(true);  //自动换行
			fontFormat.setBackground(Colour.PINK);//背景颜色
			fontFormat.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.DARK_GREEN);
			fontFormat.setOrientation(Orientation.HORIZONTAL);//文字方向

			//设置头部字体格式
            WritableFont font_header = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, 
                    false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
            //应用字体
            WritableCellFormat fontFormat_h = new WritableCellFormat(font_header);
            //设置其他样式
            fontFormat_h.setAlignment(Alignment.CENTRE);//水平对齐
            fontFormat_h.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
            fontFormat_h.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            fontFormat_h.setBackground(Colour.YELLOW);//背景色
            fontFormat_h.setWrap(false);//不自动换行
            

			//设置主题内容字体格式
            WritableFont font_Content = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, 
                    false, UnderlineStyle.NO_UNDERLINE, Colour.GRAY_80);
            //应用字体
            WritableCellFormat fontFormat_Content = new WritableCellFormat(font_Content);
            //设置其他样式
            fontFormat_Content.setAlignment(Alignment.CENTRE);//水平对齐
            fontFormat_Content.setVerticalAlignment(VerticalAlignment.CENTRE);//垂直对齐
            fontFormat_Content.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
            fontFormat_Content.setBackground(Colour.WHITE);//背景色
            fontFormat_Content.setWrap(false);//不自动换行
            
            //设置数字格式
            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式    

			Label title=new Label(0,0,"设备信息",fontFormat);


			WritableSheet sheet=book.createSheet("雷电流检测仪告警记录", 0);
			WritableSheet sheet2=book.createSheet("地阻检测仪告警记录", 1);
			WritableSheet sheet3=book.createSheet("SPD检测仪模块告警记录", 2);
			//sheet.mergeCells(0,0,3,0);

			//雷电流
			Label label_1=new Label(0,0,"省份",fontFormat_h);//创建单元格
			Label label_2=new Label(1,0,"城市",fontFormat_h);
			Label label_3=new Label(2,0,"区县",fontFormat_h);
			Label label_4=new Label(3,0,"单位",fontFormat_h);
			Label label_5=new Label(4,0,"站点ID",fontFormat_h);		
			Label label_6=new Label(5,0,"站点名称",fontFormat_h);
			Label label_7=new Label(6,0,"监测仪ID",fontFormat_h);
			Label label_8=new Label(7,0,"监测仪名称",fontFormat_h);
			Label label_9=new Label(8,0,"监测仪型号",fontFormat_h);
			Label label_10=new Label(9,0,"电流值",fontFormat_h);
			Label label_11=new Label(10,0,"阈值",fontFormat_h);
			Label label_12=new Label(11,0,"时间",fontFormat_h);
			
			//地阻
			Label label_1_r=new Label(0,0,"省份",fontFormat_h);//创建单元格
			Label label_2_r=new Label(1,0,"城市",fontFormat_h);
			Label label_3_r=new Label(2,0,"区县",fontFormat_h);
			Label label_4_r=new Label(3,0,"单位",fontFormat_h);
			Label label_5_r=new Label(4,0,"站点ID",fontFormat_h);		
			Label label_6_r=new Label(5,0,"站点名称",fontFormat_h);
			Label label_7_r=new Label(6,0,"监测仪ID",fontFormat_h);
			Label label_8_r=new Label(7,0,"监测仪名称",fontFormat_h);
			Label label_9_r=new Label(8,0,"监测仪型号",fontFormat_h);
			Label label_10_r=new Label(9,0,"监测点",fontFormat_h);
			Label label_11_r=new Label(10,0,"监测点名称",fontFormat_h);
			Label label_12_r=new Label(11,0,"监测点值",fontFormat_h);
			Label label_13_r=new Label(12,0,"阈值",fontFormat_h);
			Label label_14_r=new Label(13,0,"时间",fontFormat_h);
			
			//SPD
			Label label_1_s=new Label(0,0,"省份",fontFormat_h);//创建单元格
			Label label_2_s=new Label(1,0,"城市",fontFormat_h);
			Label label_3_s=new Label(2,0,"区县",fontFormat_h);
			Label label_4_s=new Label(3,0,"单位",fontFormat_h);
			Label label_5_s=new Label(4,0,"站点ID",fontFormat_h);		
			Label label_6_s=new Label(5,0,"站点名称",fontFormat_h);
			Label label_7_s=new Label(6,0,"监测仪ID",fontFormat_h);
			Label label_8_s=new Label(7,0,"SPD名称",fontFormat_h);
			Label label_9_s=new Label(8,0,"SPD型号",fontFormat_h);
			Label label_10_s=new Label(9,0,"SPD路数",fontFormat_h);
			Label label_11_s=new Label(10,0,"时间",fontFormat_h);
			

			sheet.setRowView(0,500);
			sheet.setColumnView(0, 10);
			sheet.setColumnView(1, 20);
			sheet.setColumnView(2, 20);
			sheet.setColumnView(3, 20);
			sheet.setColumnView(4, 20);
			sheet.setColumnView(5, 30);
			sheet.setColumnView(6, 30);
			sheet.setColumnView(7, 30);
			sheet.setColumnView(8, 30);
			sheet.setColumnView(9, 30);
			sheet.setColumnView(10, 30);
			sheet.setColumnView(11, 30);
			
			sheet2.setRowView(0,500);
			sheet2.setColumnView(0, 10);
			sheet2.setColumnView(1, 20);
			sheet2.setColumnView(2, 20);
			sheet2.setColumnView(3, 20);
			sheet2.setColumnView(4, 20);
			sheet2.setColumnView(5, 30);
			sheet2.setColumnView(6, 30);
			sheet2.setColumnView(7, 30);
			sheet2.setColumnView(8, 30);
			sheet2.setColumnView(9, 30);
			sheet2.setColumnView(10, 30);
			sheet2.setColumnView(11, 30);
			sheet2.setColumnView(12, 30);
			sheet2.setColumnView(13, 30);
			
			sheet3.setRowView(0,500);
			sheet3.setColumnView(0, 10);
			sheet3.setColumnView(1, 20);
			sheet3.setColumnView(2, 20);
			sheet3.setColumnView(3, 20);
			sheet3.setColumnView(4, 20);
			sheet3.setColumnView(5, 30);
			sheet3.setColumnView(6, 30);
			sheet3.setColumnView(7, 30);
			sheet3.setColumnView(8, 30);
			sheet3.setColumnView(9, 30);
			sheet3.setColumnView(10, 30);
			

			sheet.addCell(label_1);
			sheet.addCell(label_2);
			sheet.addCell(label_3);
			sheet.addCell(label_4);
			sheet.addCell(label_5);
			sheet.addCell(label_6);
			sheet.addCell(label_7);
			sheet.addCell(label_8);
			sheet.addCell(label_9);
			sheet.addCell(label_10);
			sheet.addCell(label_11);
			sheet.addCell(label_12);
			
			
			
			sheet2.addCell(label_1_r);
			sheet2.addCell(label_2_r);
			sheet2.addCell(label_3_r);
			sheet2.addCell(label_4_r);
			sheet2.addCell(label_5_r);
			sheet2.addCell(label_6_r);
			sheet2.addCell(label_7_r);
			sheet2.addCell(label_8_r);
			sheet2.addCell(label_9_r);
			sheet2.addCell(label_10_r);
			sheet2.addCell(label_11_r);
			sheet2.addCell(label_12_r);
			sheet2.addCell(label_13_r);
			sheet2.addCell(label_14_r);
			
			
			
			sheet3.addCell(label_1_s);
			sheet3.addCell(label_2_s);
			sheet3.addCell(label_3_s);
			sheet3.addCell(label_4_s);
			sheet3.addCell(label_5_s);
			sheet3.addCell(label_6_s);
			sheet3.addCell(label_7_s);
			sheet3.addCell(label_8_s);
			sheet3.addCell(label_9_s);
			sheet3.addCell(label_10_s);
			sheet3.addCell(label_11_s);
			
			
			//雷电流
			List<Map<String,String>> list=i_list;
			for(int i=0;i<list.size();i++){
				Map<String,String> map=list.get(i);
				Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
				Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
				Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
				Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
				Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
				Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
				Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
				Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
				Label value_9=new Label(8,i+1,map.get("model"),fontFormat_Content);
				Label value_10=new Label(9,i+1,String.valueOf(map.get("value")),fontFormat_Content);
				Label value_11=new Label(10,i+1,String.valueOf(map.get("maxValues")),fontFormat_Content);
				Label value_12=new Label(11,i+1,String.valueOf(map.get("time")),fontFormat_Content);
				
				sheet.setRowView(i+1,400);
				sheet.addCell(value_1);
				sheet.addCell(value_2);
				sheet.addCell(value_3);
				sheet.addCell(value_4);
				sheet.addCell(value_5);
				sheet.addCell(value_6);
				sheet.addCell(value_7);
				sheet.addCell(value_8);
				sheet.addCell(value_9);
				sheet.addCell(value_10);
				sheet.addCell(value_11);
				sheet.addCell(value_12);
			}
			//地阻
			List<Map<String,String>> list2=r_list;
			for(int i=0;i<list2.size();i++){
				Map<String,String> map=list2.get(i);
				Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
				Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
				Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
				Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
				Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
				Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
				Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
				Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
				Label value_9=new Label(8,i+1,map.get("type"),fontFormat_Content);
				Label value_10=new Label(9,i+1,String.valueOf(map.get("model")),fontFormat_Content);
				Label value_11=new Label(10,i+1,map.get("modelName"),fontFormat_Content);			
				Label value_12=new Label(11,i+1,String.valueOf(map.get("value")),fontFormat_Content);
				Label value_13=new Label(12,i+1,String.valueOf(map.get("maxValues")),fontFormat_Content);
				Label value_14=new Label(13,i+1,String.valueOf(map.get("time")),fontFormat_Content);
				
				sheet2.setRowView(i+1,400);
				sheet2.addCell(value_1);
				sheet2.addCell(value_2);
				sheet2.addCell(value_3);
				sheet2.addCell(value_4);
				sheet2.addCell(value_5);
				sheet2.addCell(value_6);
				sheet2.addCell(value_7);
				sheet2.addCell(value_8);
				sheet2.addCell(value_9);
				sheet2.addCell(value_10);
				sheet2.addCell(value_11);
				sheet2.addCell(value_12);
				sheet2.addCell(value_13);
				sheet2.addCell(value_14);
			}
			
			//spd
			List<Map<String,String>> list3=spd_list;
			for(int i=0;i<list3.size();i++){
				Map<String,String> map=list3.get(i);
				Label value_1=new Label(0,i+1,map.get("province"),fontFormat_Content);
				Label value_2=new Label(1,i+1,map.get("city"),fontFormat_Content);
				Label value_3=new Label(2,i+1,map.get("county"),fontFormat_Content);
				Label value_4=new Label(3,i+1,map.get("company"),fontFormat_Content); //格式化数值
				Label value_5=new Label(4,i+1,String.valueOf(map.get("siteId")),fontFormat_Content);
				Label value_6=new Label(5,i+1,map.get("siteName"),fontFormat_Content);
				Label value_7=new Label(6,i+1,String.valueOf(map.get("deviceId")),fontFormat_Content);
				Label value_8=new Label(7,i+1,map.get("name"),fontFormat_Content);
				Label value_9=new Label(8,i+1,map.get("type"),fontFormat_Content);
				Label value_10=new Label(9,i+1,String.valueOf(map.get("model")),fontFormat_Content);			
				Label value_11=new Label(10,i+1,String.valueOf(map.get("time")),fontFormat_Content);
				
				sheet3.setRowView(i+1,400);
				sheet3.addCell(value_1);
				sheet3.addCell(value_2);
				sheet3.addCell(value_3);
				sheet3.addCell(value_4);
				sheet3.addCell(value_5);
				sheet3.addCell(value_6);
				sheet3.addCell(value_7);
				sheet3.addCell(value_8);
				sheet3.addCell(value_9);
				sheet3.addCell(value_10);
				sheet3.addCell(value_11);
			}


			book.write();
			book.close(); 
			System.out.print("导出成功");
			this.success=true;
			/*HttpServletResponse response =ServletActionContext.getResponse();
			DownExcelFile(response,pathname);*/
		}catch(Exception e){
			e.printStackTrace();

		}return SUCCESS;}
	
	//告警站点列表
	public void alarmSiteList() {
		String sql = null, sql_count = null;
		String str = " where company like '"+company+"%'";
		
		if (!province.equals("0")) {
			str += " and province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and county='" +MysqlConnection.areaName(county) + "'";
		}
		
		if (LoginUserZone2().equals("")) {
			sql = "select a.siteId,b.*  from view_alarm_site as a "
					+ "left join site as b on a.siteId=b.siteId  where a.siteId in(select siteId from site "+str+") and a.siteId like '"+site+"%' "
					+ "order by a.siteId asc limit " + start
					+ "," + limit + "";
			sql_count = "select count(siteId) from view_alarm_site where siteId in(select siteId from site "+str+") and siteId like '"+site+"%'";
		} else {
			sql = "select a.siteId,b.*  from view_alarm_site as a "
					+ "left join site as b on a.siteId=b.siteId  where a.siteId in(select siteId from site "+str+" and "+LoginUserZone2()+") and a.siteId like '"+site+"%' "
					+ " order by a.siteId asc limit " + start + "," + limit + "";
			sql_count = "select count(siteId) from view_alarm_site where siteId in(select siteId from site "+str+" and  " + LoginUserZone2()+") and siteId like '"+site+"%' "
					+ "";
		}

		ArrayList data = mysql.DBList(sql);
		HashMap result = new HashMap();
		result.put("items", data);
		result.put("totals", mysql.getCount(sql_count));
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
	

	public void data() {
		String sql = null, sql_count = null;

		/*System.out.println("zone=" + LoginUserZone2());*/
		if (LoginUserZone2().equals("")) {
			sql = "select *  from site  order by siteId asc limit " + start
					+ "," + limit + "";
			sql_count = "select count(id) from site";
		} else {
			sql = "select * from site where " + LoginUserZone2()
					+ " order by siteId asc limit " + start + "," + limit + "";
			sql_count = "select count(id) from site where " + LoginUserZone2()
					+ "";
		}

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

	// 获取站点下的SPD模块
	public void site_spd_data() {
		String sql = "select * from site_spd where siteId=" + siteId;
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

	// 获取站点下的雷电流模块
	public void site_li_data() {
		String sql = "select * from site_li where siteId=" + siteId+" order by deviceId asc";
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

	public void site_trade() {
		String sql = "select * from site_trade";
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

	// 获取阀值
	public void r_max() {
		String sql = "select maxValues from site_lr ";
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

	// 获取检测点
	public void r_model() {
		String sql = "select model from site_lr where siteId='" + siteId
				+ "'and deviceId=" + r_deviceId;
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

	// 获取站点下的MD44
	public void site_md44_data() {
		String sql = "select * from md44 where siteId=" + siteId;
		ArrayList data = mysql.DBList(sql);
		HashMap result = new HashMap();
		result.put("items", data);
		result.put("totals", data.size());
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

	// 获取站点下的地阻模块
	public void site_lr_data() {
		String sql = "select * from site_lr where siteId=" + siteId+" order by deviceId asc";
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

	// 根据id 获取站点
	public void id_data() {
		String sql = "select * from site where siteId=" + siteId;
		String sql_spd = "select * from site_spd where siteId=" + siteId;
		String sql_li = "select * from site_li where siteId=" + siteId;
		String sql_lr = "select * from site_lr where siteId=" + siteId;
		ArrayList data = mysql.DBList(sql);

		HashMap result = new HashMap();
		result.put("site_items", data);
		result.put("spd_items", mysql.DBList(sql_spd));
		result.put("r_items", mysql.DBList(sql_lr));
		result.put("i_items", mysql.DBList(sql_li));
		result.put("spd_total", mysql.DBList(sql_spd).size());
		result.put("i_total", mysql.DBList(sql_li).size());
		result.put("r_total", mysql.DBList(sql_lr).size());

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

	public String add_data() {
		String sql_exists = "select id from site where siteId=" + siteId;
		String sql = "insert into site(siteId,`name`,trade,province,city,county,company,lon,lat,provinceId,cityId,countyId)VALUES"
				+ "('"
				+ siteId
				+ "','"
				+ name
				+ "','"
				+ trade
				+ "','"
				+ MysqlConnection.areaName(province)
				+ "','"
				+ MysqlConnection.areaName(city)
				+ "','"
				+ MysqlConnection.areaName(county)
				+ "',"
				+ "'"
				+ company
				+ "','"
				+ lon
				+ "','"
				+ lat
				+ "','"
				+ province
				+ "','"
				+ city + "','" + county + "')";
		if (mysql.isExists(sql_exists)) {
			this.success = false;
			this.message = "你添加的设备型号或者站点编号已经存在";
		} else {
			mysql.Update(sql);
			this.success = true;
			this.message = "添加站点成功";
			mysql.writeLog(1, "添加站点" + spd_name, fun.getSession("username"));

		}
		return SUCCESS;
	}

	// 添加spd检测仪
	public String add_spd() {
		String sql_exists = "select deviceId from site_spd where siteId='"
				+ siteId + "' and deviceId='" + spd_deviceId + "'";
		String sql = "insert into site_spd(deviceId,siteId,md44id,name,model,type,level,address,time)VALUES('"
				+ spd_deviceId
				+ "','"
				+ siteId
				+ "','"
				+ spd_md44id
				+ "','"
				+ spd_name
				+ "','"
				+ spd_model
				+ "','"
				+ spd_type
				+ "','"
				+ spd_level + "','" + spd_address + "',now())";

		mysql.Update(sql);
		this.success = true;
		this.message = "添加spd成功";
		mysql.writeLog(1, "添加spd" + spd_name, fun.getSession("username"));

		return SUCCESS;
	}

	// 修改spd检测仪
	public String update_spd() {
		String sql = "update site_spd set name='" + spd_name + "',model='"
				+ spd_model + "',type='" + spd_type + "',level='" + spd_level
				+ "',address='" + spd_address + "'" + "where siteId='" + siteId
				+ "' and deviceId='" + spd_deviceId + "' and md44id='"
				+ spd_md44id + "'and model='" + spd_model + "'";

		mysql.Update(sql);
		this.success = true;
		this.message = "修改spd成功";
		mysql.writeLog(2, "修改spd" + spd_name, fun.getSession("username"));
		return SUCCESS;
	}

	// 删除spd检测仪
	public String del_spd() {
		String sql = "delete from site_spd where deviceId='" + spd_deviceId
				+ "' and siteId='" + siteId + "'and model='" + spd_model + "'";
		String sql2="delete from site_spd_h where deviceId='" + spd_deviceId
				+ "' and siteId='" + siteId + "'and model='" + spd_model + "'";
		mysql.Update(sql);
		mysql.Update(sql2);
		this.success = true;
		this.message = "删除spd成功";
		mysql.writeLog(3, "删除spd" + spd_deviceId, fun.getSession("username"));
		return SUCCESS;
	}

	// 添加雷电流检测仪
	public String add_li() {
		String sql_exists = "select deviceId from site_li where siteId='"
				+ siteId + "' and deviceId='" + i_deviceId + "'";
		String sql = "insert into site_li(deviceId,siteId,name,model,address,maxValues,maxNum,time)VALUES('"
				+ i_deviceId
				+ "','"
				+ siteId
				+ "','"
				+ i_name
				+ "','"
				+ i_model
				+ "','"
				+ i_address
				+ "','"
				+ i_maxValues
				+ "','"
				+ i_maxNum + "',now())";
		if (mysql.isExists(sql_exists)) {
			this.success = false;
			this.message = "你添加的spd已经存在";
		} else {
			mysql.Update(sql);
			this.success = true;
			this.message = "添加雷电流检测仪成功";
			// RtuIListenner.getM_ilist().clear();
			mysql.writeLog(1, "添加雷电流检测仪" + i_name, fun.getSession("username"));
		}
		return SUCCESS;
	}

	// 修改雷电流检测仪
	public String update_li() {
		String sql = "update site_li set name='" + i_name + "',model='"
				+ i_model + "',address='" + i_address + "',maxValues='"
				+ i_maxValues + "',maxNum='" + i_maxNum + "'  where siteId='"
				+ siteId + "' and deviceId='" + i_deviceId + "'";

		mysql.Update(sql);
		this.success = true;
		this.message = "修改雷电流检测仪成功";
		// RtuIListenner.getM_ilist().clear();
		mysql.writeLog(2, "修改雷电流检测仪" + i_name, fun.getSession("username"));

		return SUCCESS;
	}

	// 删除雷电流检测仪
	public String del_li() {
		String sql = "delete from site_li where deviceId='" + i_deviceId
				+ "' and siteId=" + siteId;
		String sql2 = "delete from site_li_h where deviceId='" + i_deviceId
				+ "' and siteId=" + siteId;
		mysql.Update(sql);
		mysql.Update(sql2);
		this.success = true;
		this.message = "删除雷电流检测仪成功";
		// RtuIListenner.getM_ilist().clear();
		// for (int i = 0; i < RtuIListenner.getM_ilist().size(); i++) {
		// if (RtuIListenner.getM_ilist().get(i).getRtuId()==siteId &&
		// RtuIListenner.getM_ilist().get(i).getDeviceId()==r_deviceId) {
		// RtuIListenner.getM_ilist().remove(i);
		// }
		// }
		mysql.writeLog(3, "删除雷电流检测仪" + i_deviceId, fun.getSession("username"));
		return SUCCESS;
	}

	// 添加MD44设备
	public String add_md44() {
		String sql_exists = "select deviceId from md44 where deviceId='"+md44_deviceId+"' and siteId=" + siteId;
		String sql = "insert into md44(deviceId,siteId,name,type,address)VALUES('"
				+ md44_deviceId
				+ "','"
				+ siteId
				+ "','"
				+ md44_name
				+ "' ,'"
				+ md44_type + "' ,'" + md44_address + "' )";

		if (mysql.isExists(sql_exists)) {
			this.success = false;
			this.message = "你添加的智能数字量采集器设备已经存在";
		} else {
			mysql.Update(sql);
			this.success = true;
			this.message = "添加智能数字量采集器设备成功";
			/* RtuIListenner.getM_ilist().clear(); */
			mysql.writeLog(1, "添加智能数字量采集器设备" + i_name,
					fun.getSession("username"));
		}
		return SUCCESS;
	}

	// 修改MD44设备
	public String update_md44() {
		String sql = "update md44 set name='" + md44_name + "',type='"
				+ md44_type + "' ,address='" + md44_address + "' "
				+ "where siteId='" + siteId + "' and deviceId=" + md44_deviceId;
		mysql.Update(sql);
		this.success = true;
		this.message = "修改智能数字量采集器成功";
		mysql.writeLog(2, "修改智能数字量采集器" + md44_name, fun.getSession("username"));
		return SUCCESS;
	}

	// 删除MD44设备
	public String del_md44() {
		String sql = "delete from md44 where deviceId='" + md44_deviceId
				+ "' and siteId=" + siteId;
		mysql.Update(sql);
		this.success = true;
		this.message = "删除智能数字量采集器成功";

		mysql.writeLog(3, "删除M智能数字量采集器成功" + md44_deviceId,
				fun.getSession("username"));
		return SUCCESS;
	}

	// 添加地阻检测仪
	public String add_lr() {
		String sql_exists="";
		if(flag==1){
			sql_exists = "select deviceId from site_lr where siteId='"
					+ siteId + "' and deviceId='" + r_deviceId + "' ";
		}else{
			sql_exists = "select deviceId from site_lr where siteId='"
					+ siteId + "' and deviceId='" + r_deviceId + "'and md44id='"
					+ r_md44id + "'";
		}
		
		String sql = "insert into site_lr(deviceId,siteId,md44id,`name`,model,modelName,type,maxValues,address,flag,r1,r2)VALUES('"
				+ r_deviceId
				+ "',"
				+ "'"
				+ siteId
				+ "','"
				+ r_md44id
				+ "','"
				+ r_name
				+ "','"
				+ r_model
				+ "','"
				+ r_modelName
				+ "','"
				+ r_type + "','" + r_maxValues + "','" + r_address + "','"+flag+"','"+r1+"','"+r2+"')";
		if(!mysql.isExists(sql_exists)){
			mysql.Update(sql);
			this.success = true;
			this.message = "添加地阻检测仪成功";
			mysql.writeLog(1, "添加地阻检测仪" + r_name, fun.getSession("username"));
		}else{
			this.success=false;
			this.message="添加失败";
		}
		
		return SUCCESS;
	}

	// 修改地阻检测仪
	public String update_lr() {
		String sql = "update site_lr set name='" + r_name + "',model='"
				+ r_model + "',modelName='" + r_modelName + "',type='" + r_type
				+ "',maxValues='" + r_maxValues + "',flag='"+flag+"',r1='"+r1+"',r2='"+r2+"', address='" + r_address
				+ "' where siteId='" + siteId + "' and deviceId='" + r_deviceId
				+ "'and md44id='" + r_md44id + "'and model='" + r_model + "'";

		mysql.Update(sql);
		this.success = true;
		this.message = "修改地阻检测仪成功";

		// RtuRListenner.getM_rlist().clear();
		mysql.writeLog(2, "修改地阻检测仪" + r_name, fun.getSession("username"));
		return SUCCESS;
	}

	// 删除地租检测仪
	public String del_lr() {
		String sql = "delete from site_lr where deviceId='" + r_deviceId
				+ "' and siteId='" + siteId + "'and md44id='" + r_md44id
				+ "'and model='" + r_model + "'";
		String sql2 = "delete from site_lr_h where deviceId='" + r_deviceId
				+ "' and siteId='" + siteId + "'and model='" + r_model + "'";
		mysql.Update(sql);
		mysql.Update(sql2);
		this.success = true;
		this.message = "删除地阻检测仪成功";

		// RtuRListenner.getM_rlist().clear();
		// for (int i = 0; i < RtuRListenner.getM_rlist().size(); i++) {
		// if (RtuRListenner.getM_rlist().get(i).getSiteId()==siteId &&
		// RtuRListenner.getM_rlist().get(i).getDeviceId()==r_deviceId) {
		// RtuRListenner.getM_rlist().remove(i);
		// }
		// }
		mysql.writeLog(3, "删除地阻检测仪" + r_deviceId, fun.getSession("username"));
		return SUCCESS;
	}

	public String update_data() {
		String sql = "update site set siteId='" + siteId + "',name='" + name
				+ "'" + ",province='" + MysqlConnection.areaName(province) + "',city='" + MysqlConnection.areaName(city)
				+ "',county='" + MysqlConnection.areaName(county) + "',"
				+ "company='" +company + "',trade='"
				+ trade + "',lat='" + lat + "',lon='" + lon
				+ "',provinceId='"+province+"',cityId='"+city+"',countyId='"+county+"'  where siteId=" + siteId;
		mysql.Update(sql);
		this.success = true;
		this.message = "修改站点成功";
		mysql.writeLog(2, "修改站点" + spd_name, fun.getSession("username"));
		return SUCCESS;
	}

	// 删除站点
	public String del_data() {
		String sql = "delete from site where siteId in (" + deleteids + ")";
		String sql2 = "delete from site_li where siteId in (" + deleteids + ")";
		String sql3 = "delete from site_lr where siteId in (" + deleteids + ")";
		String sql4 = "delete from site_spd where siteId in (" + deleteids + ")";
		String sql5 = "delete from site_li_h where siteId in (" + deleteids + ")";
		String sql6 = "delete from site_lr_h where siteId in (" + deleteids + ")";
		String sql7 = "delete from site_spd_h where siteId in (" + deleteids + ")";
		String sql8 = "delete from md44 where siteId in (" + deleteids + ")";
		mysql.Update(sql);
		mysql.Update(sql2);
		mysql.Update(sql3);
		mysql.Update(sql4);
		mysql.Update(sql5);
		mysql.Update(sql6);
		mysql.Update(sql7);
		mysql.Update(sql8);
		this.success = true;
		this.message = "删除站点成功";
		/*
		 * RtuIListenner.getM_ilist().clear();
		 * RtuRListenner.getM_rlist().clear();
		 */
		mysql.writeLog(3, "删除站点" + deleteids, fun.getSession("username"));
		return SUCCESS;
	}

	// 雷电流
	public void i_data() {
		String str = " where company like '" + company
				+ "%' and (siteId like '" + site + "%' or siteName like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}


		if (LoginUserZone().equals("")) {
			sql = "select * from site_i " + str
					+ " order by siteId asc,deviceId asc limit " + start + ","
					+ limit + "";
			sql_count = "select count(id) from site_i " + str;
		} else {
			sql = "select * from site_i " + str + " and " + LoginUserZone()
					+ " order by siteId asc,deviceId asc limit " + start + ","
					+ limit + "";
			sql_count = "select count(id) from site_i " + str + " and "
					+ LoginUserZone() + "";
		}

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

	// 雷电流历史数据
	public void h_data_i() {
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if(!deviceId.equals("")){
			str+=" and a.deviceId='"+deviceId+"' ";
		}
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (!startTime.equals("") && !endTime.equals("")) {
			str += " and a.time between '" + startTime + "' and '" + endTime
					+ "'";
		}
		if (LoginUserZone().equals("")) {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name,c.maxValues from site_li_h  as a left join site as b on a.siteId=b.siteId left join site_li as c on a.siteId=c.siteId"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.time desc limit "
					+ start + "," + limit;

			sql_count = "select count(*) from site_li_h  as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name,c.maxValues from site_li_h  as a left join site as b on a.siteId=b.siteId left join site_li as c on a.siteId=c.siteId"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.time desc limit "
					+ start
					+ ","
					+ limit;
			// sql="select * from site "+str+" and "+LoginUserZone()+" order by siteId asc limit "+start+","+limit+"";
			sql_count = "select count(*) from site_li_h  as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

		/*
		 * System.out.println(sql); System.out.println(sql_count);
		 */

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

	// 地阻检测仪历史数据
	public void h_data_r() {
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if(!deviceId.equals("")){
			str+=" and a.deviceId='"+deviceId+"' ";
		}
		if(!model.equals("")){
			str+=" and a.model='"+model+"' ";
		}
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (!startTime.equals("") && !endTime.equals("")) {
			str += " and a.time between '" + startTime + "' and '" + endTime
					+ "'";
		}

		if (LoginUserZone().equals("")) {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name,c.maxValues "
					+ "from site_lr_h  as a "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_lr as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.time desc limit "
					+ start + "," + limit;

			sql_count = "select count(*) from site_lr_h  as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name,c.maxValues from site_lr_h  as a "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_lr as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.time desc limit "
					+ start
					+ ","
					+ limit;
			// sql="select * from site "+str+" and "+LoginUserZone()+" order by siteId asc limit "+start+","+limit+"";
			sql_count = "select count(*) from site_lr_h  as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

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

	// spd历史数据
	public void h_data_spd() {
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if(!deviceId.equals("")){
			str+=" and a.deviceId='"+deviceId+"' ";
		}
		if(!model.equals("")){
			str+=" and a.model='"+model+"' ";
		}
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (!startTime.equals("") && !endTime.equals("")) {
			str += " and a.time between '" + startTime + "' and '" + endTime
					+ "'";
		}

		if (LoginUserZone().equals("")) {

			sql = "select  b.province,b.city,b.county,b.company,b.siteId,a.deviceId,a.model,c.name,c.level,a.status,a.time  "
					+ "from site_spd_h  as a   "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_spd as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.model asc,a.time desc limit "
					+ start
					+ ","
					+ limit;

			sql_count = "select count(*) from site_spd_h as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select  b.province,b.city,b.county,b.company,b.siteId,a.deviceId,a.model,c.name,c.level,a.status,a.time  "
					+ "from site_spd_h  as a   "
					+ "left join site as b on a.siteId=b.siteId "
					+ "left join site_spd as c on a.siteId=c.siteId and a.deviceId=c.deviceId and a.model=c.model"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc,a.time desc limit " + start + "," + limit;
			sql_count = "select count(*) from site_spd_h as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

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

	// 实时数据
	public void now_data() {
		String str = " where company like '" + company
				+ "%' and (siteId like '" + site + "%' or name like '" + site
				+ "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {
			sql = "select * from site " + str + " order by siteId asc limit "
					+ start + "," + limit + "";
			sql_count = "select count(id) from site " + str;
		} else {
			sql = "select * from site " + str + " and " + LoginUserZone()
					+ " order by siteId asc limit " + start + "," + limit + "";
			sql_count = "select count(id) from site " + str + " and "
					+ LoginUserZone() + "";
		}

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

	// 雷电流实时数据
	public void now_data_i() {
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {

			sql = "select a.*,b.province,b.city,b.county,b.company,b.name from site_li  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc limit "
					+ start
					+ ","
					+ limit;

			sql_count = "select count(*) from site_li  as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name from site_li  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc limit " + start + "," + limit;
			// sql="select * from site "+str+" and "+LoginUserZone()+" order by siteId asc limit "+start+","+limit+"";
			sql_count = "select count(*) from site_li  as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

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

	// 地阻检测仪实时数据
	public void now_data_r() {
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {

			sql = "select a.*,b.province,b.city,b.county,b.company,b.name from site_lr  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit "
					+ start
					+ ","
					+ limit;

			sql_count = "select count(*) from site_lr  as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select a.*,b.province,b.city,b.county,b.company,b.name from site_lr  as a left join site as b on a.siteId=b.siteId"
					+ " "
					+ str
					+ " and "
					+ LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit " + start + "," + limit;
			// sql="select * from site "+str+" and "+LoginUserZone()+" order by siteId asc limit "+start+","+limit+"";
			sql_count = "select count(*) from site_lr as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

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

	// SPD实时数据
	public void now_data_spd() {
		String str = " where b.company like '" + company
				+ "%' and (b.siteId like '" + site + "%' or b.name like '"
				+ site + "%')";
		String sql = null, sql_count = null;
		if (!province.equals("0")) {
			str += " and b.province='" + MysqlConnection.areaName(province) + "'";
		}
		if (!city.equals("0")) {
			str += " and b.city='" + MysqlConnection.areaName(city) + "'";
		}
		if (!county.equals("0")) {
			str += " and b.county='" + MysqlConnection.areaName(county) + "'";
		}
		if (LoginUserZone().equals("")) {

			sql = "select  a.*,"
					+ "b.province,b.city,b.county,b.company,b.name"
					+ " from site_spd  as a   left join site as b on a.siteId=b.siteId"
					+ " " + str + " order by a.siteId asc,a.deviceId asc,a.model asc limit " + start + ","
					+ limit;

			sql_count = "select count(*) from site_spd as a left join site as b on a.siteId=b.siteId"
					+ str;
		} else {
			sql = "select  a.*,"
					+ "b.province,b.city,b.county,b.company,b.name  from site_spd  as a  left join site as b on a.siteId=b.siteId"
					+ " " + str + " and " + LoginUserZone()
					+ " order by a.siteId asc,a.deviceId asc,a.model asc limit " + start + "," + limit;
			// sql="select * from site "+str+" and "+LoginUserZone()+" order by siteId asc limit "+start+","+limit+"";
			sql_count = "select count(*) from site_spd as a left join site as b on a.siteId=b.siteId "
					+ str + " and " + LoginUserZone() + "";
		}

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

	// 获取站点地域树形json select province,city,company,county,id,name from site
	public void menu() throws SQLException {
		Connection conn = mysql.getConn();
		Statement stmt = conn.createStatement();
		Map<String, String> map = zone();
		String str = "", sql = "";
		if (!map.get("province").toString().equals("全部")) {
			sql = " select province from site where province='"
					+ map.get("province").toString() + "' limit 1";
		} else {
			sql = "select province from site  group by province";
		}

		ResultSet rs = stmt.executeQuery(sql);
		ArrayList list = new ArrayList();

		Map rowData;
		while (rs.next()) {
			rowData = new HashMap();
			rowData.put("name", rs.getString("province"));
			rowData.put("children", menu2(rs.getString("province")));
			list.add(rowData);
		}
		HashMap result = new HashMap();
		result.put("items", list);
		stmt.close();
		conn.close();
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

	public ArrayList menu2(String str) throws SQLException {
		Connection conn = mysql.getConn();
		Statement stmt = conn.createStatement();
		
		String c=" where province='"+str+"'";

		Map<String, String> map = zone();
		if (!map.get("city").toString().equals("全部")) {
			c += "  and city='" + map.get("city").toString() + "'";
		}

		String sql = "select city from site "+c+" group by city";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList list = new ArrayList();
		Map rowData;
		while (rs.next()) {
			rowData = new HashMap();
			rowData.put("name", rs.getString("city"));
			rowData.put("children", menu3(rs.getString("city")));
			list.add(rowData);
		}
		stmt.close();
		conn.close();
		return list;

	}

	public ArrayList menu3(String str) throws SQLException {
		Connection conn = mysql.getConn();
		Statement stmt = conn.createStatement();
		Map<String, String> map = zone();
		String c=" where city='"+str+"'";
		if (!map.get("county").toString().equals("全部")) {
			c += " and county='" + map.get("county").toString() + "'";
		}
		String sql = "select county from site "+c+" group by county";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList list = new ArrayList();
		Map rowData;
		while (rs.next()) {
			rowData = new HashMap();
			rowData.put("name", rs.getString("county"));
			rowData.put("children", menu4(rs.getString("county")));
			list.add(rowData);
		}
		stmt.close();
		conn.close();
		return list;
	}

	public ArrayList menu4(String str) throws SQLException {
		Connection conn = mysql.getConn();
		Statement stmt = conn.createStatement();
		String c=" where county='"+str+"'";
		Map<String, String> map = zone();
		if (!map.get("company").toString().equals("全部")) {
			c += " and company='" + map.get("company").toString() + "'";
		}
		String sql = "select company from site "+c+" group by company";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList list = new ArrayList();
		Map rowData;
		while (rs.next()) {
			rowData = new HashMap();
			rowData.put("name", rs.getString("company"));
			rowData.put("children", menu5(str, rs.getString("company")));
			list.add(rowData);
		}
		return list;
	}

	public ArrayList menu5(String county, String str) throws SQLException {
		Connection conn = mysql.getConn();
		Statement stmt = conn.createStatement();
		String sql = "select id,siteId,name from site  where county='" + county
				+ "' and  company='" + str + "'";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList list = new ArrayList();
		Map rowData;
		while (rs.next()) {
			rowData = new HashMap();
			rowData.put("id", rs.getString("id"));
			rowData.put("siteId", rs.getString("siteId"));
			/* rowData.put("idkey", rs.getString("siteId")); */
			rowData.put(
					"name",
					"编号：" + rs.getString("siteId") + "  名称："
							+ rs.getString("name"));
			list.add(rowData);
		}
		return list;

	}

	public Map<String, String> zone() {
		String sql = "select * from user_zone where userid="
				+ fun.getSession("userid");
		Connection conn = mysql.getConn();
		Statement stmt;
		ResultSet rst;
		Map<String, String> map = new HashMap<String, String>();
		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);

			while (rst.next()) {
				map.put("province", rst.getString("province"));
				map.put("city", rst.getString("city"));
				map.put("county", rst.getString("county"));
				map.put("company", rst.getString("company"));
			}

			/*System.out.println("userid=" + fun.getSession("userid"));
			System.out.println("map=" + map);*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* System.out.println("LoginUserZone:"+str); */
		return map;

	}

	// 站点统计
	public void site_count() {
		String sql1 = null, sql2 = null, sql3 = null;

		if (LoginUserZone2().equals("")) {
			sql1 = "select count(id) from site";
			/*
			 * sql2 =
			 * "select count(id) from site where siteId in(select siteId from site_spd where status=0)"
			 * +
			 * " or  siteId in(select siteId from site_li where value>maxValues)  or "
			 * +
			 * " siteId in(select siteId from site_lr where value>maxValues) ";
			 */
			sql2 = "select count(*) from site where siteId in (select siteId from view_alarm_site) and status=1";
			sql3 = "select count(*) from site  where status=0 ";
		} else {
			sql1 = "select count(id) from site where " + LoginUserZone2();
			sql2 = "select count(id) from site where siteId in(select siteId from view_alarm_site) and status=1"
					+ "  and" + LoginUserZone2();
			sql3 = "select count(*) from site where status=0 and "
					+ LoginUserZone2();
			/*
			 * sql2 =
			 * "select count(id) from site where (siteId in(select siteId from site_spd where status=0)"
			 * +
			 * " or  siteId in(select siteId from site_li where value>maxValues)  or "
			 * +
			 * " siteId in(select siteId from site_lr where value>maxValues) ) and"
			 * + LoginUserZone2();
			 */

		}

		HashMap result = new HashMap();
		result.put("totals", mysql.getCount(sql1));
		result.put("alarm_totals", mysql.getCount(sql2));
		result.put("offline", mysql.getCount(sql3));
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

	// 异常设备统计,已修改
	public void equipment_count() {
		String sql1 = null, sql2 = null, sql3 = null;
		if (LoginUserZone2().equals("")) {
			sql1 = "select count(id) from site_spd where status=1";
			sql2 = "select count(id) from site_lr where value>maxValues";
			sql3 = "select count(id) from site_li where value>maxValues";
		} else {
			sql1 = "select count(id) from site_spd where status=1 and siteId  in(select siteId from site where "
					+ LoginUserZone2() + ")";
			sql2 = "select count(id) from site_lr where value >maxValues "
					+ "  and  siteId in(select siteId from site where "
					+ LoginUserZone2() + ")";
			sql3 = "select count(id) from site_li where value>maxValues"
					+ "  and siteId  in(select siteId from site where "
					+ LoginUserZone2() + ")";
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

	// 获县中的单位
	public void company() {
		String sql = "select company from site where countyId='" + city
				+ "' group by company";
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

	// 地阻图形统计
	public void r_chart() {
		String sql = null;
		try {
			sql = "select max(value) as 'value',DATE_FORMAT(time, '%m月%d') AS date from site_lr_h "
					+ "where siteId='"
					+ siteId
					+ "' and deviceId='"
					+ r_deviceId
					+ "' and model='"
					+ r_model
					+ "'and"
					+ " time between '"
					+ timeS
					+ "' and '"
					+ timeE
					+ "' group by date order by date asc";
			ArrayList data = mysql.DBList(sql);
			HashMap result = new HashMap();
			result.put("chartData", data);
			String jsonstr = json.Encode(result);
			ServletActionContext.getResponse().setContentType(
					"text/html;charset=UTF-8");
			ServletActionContext.getResponse().getWriter().write(jsonstr);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 获取登录用户数据访问权限
	public String LoginUserZone() {
		String sql = "select * from user_zone where userid="
				+ fun.getSession("userid");
		Connection conn = mysql.getConn();
		Statement stmt;
		ResultSet rst;
		String str = "";
		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);
			while (rst.next()) {
				if (!rst.getString("company").equals("全部")) {
					str = " b.company='" + rst.getString("company")
							+ "' and b.county='" + rst.getString("county")
							+ "' and  b.city='" + rst.getString("city")
							+ "'and  b.province='" + rst.getString("province")
							+ "' ";
				} else {
					if (!rst.getString("county").equals("全部")) {
						str = " b.county='" + rst.getString("county")
								+ "' and  b.city='" + rst.getString("city")
								+ "'and  b.province='"
								+ rst.getString("province") + "' ";
					} else {
						if (!rst.getString("city").equals("全部")) {
							str = " b.city='" + rst.getString("city")
									+ "'  and b.province='"
									+ rst.getString("province") + "'";
						} else {
							if (!rst.getString("province").equals("全部")) {
								str = " b.province='"
										+ rst.getString("province") + "'";
							} else {
								str = "";
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* System.out.println("LoginUserZone:"+str); */
		return str;

	}

	public String LoginUserZone2() {
		String sql = "select * from user_zone where userid="
				+ fun.getSession("userid");

		Connection conn = mysql.getConn();
		Statement stmt;
		ResultSet rst;
		String str = "";

		String province = "", city = "", county = "", compny = "";

		try {
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);

			while (rst.next()) {
				if (!rst.getString("company").equals("全部")) {
					str = " company='" + rst.getString("company")
							+ "' and county='" + rst.getString("county")
							+ "' and  city='" + rst.getString("city") + "' ";
				} else {
					if (!rst.getString("county").equals("全部")) {
						str = " county='" + rst.getString("county")
								+ "' and  city='" + rst.getString("city")
								+ "' ";
					} else {
						if (!rst.getString("city").equals("全部")) {
							str = " city='" + rst.getString("city")
									+ "'  and province='"
									+ rst.getString("province") + "'";
						} else {
							if (!rst.getString("province").equals("全部")) {
								str = " province='" + rst.getString("province")
										+ "'";
							} else {
								str = "";
							}
						}
					}
				}
			}
			/*
			 * str="province='"+province+"' and city='"+city+"' and county='"+county
			 * +"' and company='"+compny+"'"
			 */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* System.out.println("LoginUserZone:"+str); */
		return str;

	}

	// 获取所有站点
	public void mapSite() {
		String sql = null, sql_count = null;

		if (LoginUserZone2().equals("")) {
			sql = "select a.*,b.siteId as alarmId from site as a left join view_alarm_site as b on a.siteId=b.siteId";
			sql_count = "select count(id) from site";
		} else {
			sql = "select b.*,a.siteId as alarmId from site as b left join view_alarm_site as a on a.siteId=b.siteId where "
					+ LoginUserZone();
			sql_count = "select count(id) from site as b where "
					+ LoginUserZone();
		}

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

	// 地阻树形菜单
	public void rTree() throws SQLException {
		Connection conn = mysql.getConn();
		Statement stmt = conn.createStatement();
		String sql = "select * from site_lr  where siteId=" + siteId;
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList list = new ArrayList();
		Map rowData;
		while (rs.next()) {
			rowData = new HashMap();
			rowData.put("id", rs.getString("deviceId"));
			rowData.put(
					"name",
					"编号：" + rs.getString("deviceId") + "  名称："
							+ rs.getString("name"));
			list.add(rowData);
		}
		HashMap result = new HashMap();
		result.put("items", list);
		stmt.close();
		conn.close();
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

	// 雷电流检测仪累计电流超过阀值10次以上统计
	public void i_value_count() {
		int i_value_count = Integer.parseInt(fun.readXml("System","i_value_count"));
		String sql = null, sql2 = null;

		if (LoginUserZone2().equals("")) {
			sql = "select count(id) from site_li where num>='" + i_value_count
					+ "'";
			sql2 = "select * from site_li where num>='" + i_value_count + "'";
		} else {
			sql = "select count(id) from site_li where num>='" + i_value_count
					+ "' and  siteId in(select siteId from site where "
					+ LoginUserZone2() + ")";
			sql2 = "select * from site_li where num>='" + i_value_count
					+ "' and  siteId in(select siteId from site where "
					+ LoginUserZone2() + ")";
		}

		HashMap result = new HashMap();
		result.put("items", mysql.DBList(sql2));
		result.put("totals", mysql.getCount(sql));
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
	
	public void DownExcelFile(HttpServletResponse response,String filePath) throws Exception {
		File file = new File(filePath);
		String fileName="";
		response.setContentType("text/plain;charset=utf-8");
		if (file.exists()) {
			try {   
				// 要用servlet 来打开一个 EXCEL 文档，需要将 response 对象中 header 的 contentType 设置成"application/x-msexcel"。
				response.setContentType("application/x-msexcel");
				// 保存文件名称
				fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				fileName = new String(fileName.getBytes("GB2312"), "iso8859_1");
				//servlet中，要在 header中设置下载方式
				response.setHeader("Content-Disposition","attachment; filename=" + fileName);
				//缓冲流(BufferedStream)可以一次读写一批数据，,缓冲流(Buffered Stream)大大提高了I/O的性能。
				BufferedInputStream  bis = new BufferedInputStream(new FileInputStream(file));
				//OutputStream输出流
				OutputStream bos = response.getOutputStream();
				byte[] buff = new byte[1024];
				int readCount = 0;
				//每次从文件流中读1024个字节到缓冲里。
				readCount = bis.read(buff);
				while (readCount != -1) {
					//把缓冲里的数据写入浏览器
					bos.write(buff, 0, readCount);
					readCount = bis.read(buff);
				}
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
				response.getWriter().close();
			} catch (Exception e) {

			}
		}
	}
	//下载文件
	public void DownExcel() throws Exception {
		String saveDir = ServletActionContext.getServletContext().getRealPath("/upload"); 
		HttpServletResponse response =ServletActionContext.getResponse();
		String filePath=saveDir+"/"+excelName;
		File file = new File(filePath);
		String fileName="";
		response.setContentType("text/plain;charset=utf-8");
		if (file.exists()) {
			try {   
				// 要用servlet 来打开一个 EXCEL 文档，需要将 response 对象中 header 的 contentType 设置成"application/x-msexcel"。
				response.setContentType("application/x-msexcel");
				// 保存文件名称
				fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				fileName = new String(fileName.getBytes("GB2312"), "iso8859_1");
				//servlet中，要在 header中设置下载方式
				response.setHeader("Content-Disposition","attachment; filename=" + fileName);
				//缓冲流(BufferedStream)可以一次读写一批数据，,缓冲流(Buffered Stream)大大提高了I/O的性能。
				BufferedInputStream  bis = new BufferedInputStream(new FileInputStream(file));
				//OutputStream输出流
				OutputStream bos = response.getOutputStream();
				byte[] buff = new byte[1024];
				int readCount = 0;
				//每次从文件流中读1024个字节到缓冲里。
				readCount = bis.read(buff);
				while (readCount != -1) {
					//把缓冲里的数据写入浏览器
					bos.write(buff, 0, readCount);
					readCount = bis.read(buff);
				}
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
				response.getWriter().close();
			} catch (Exception e) {

			}
		}
	}

	// 同步电阻

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

	public String getSpd_name() {
		return spd_name;
	}

	public void setSpd_name(String spd_name) {
		this.spd_name = spd_name;
	}

	public String getSpd_model() {
		return spd_model;
	}

	public void setSpd_model(String spd_model) {
		this.spd_model = spd_model;
	}

	public String getSpd_address() {
		return spd_address;
	}

	public void setSpd_address(String spd_address) {
		this.spd_address = spd_address;
	}

	public int getSpd_level() {
		return spd_level;
	}

	public void setSpd_level(int spd_level) {
		this.spd_level = spd_level;
	}

	public String getR_name() {
		return r_name;
	}

	public void setR_name(String r_name) {
		this.r_name = r_name;
	}

	public String getR_model() {
		return r_model;
	}

	public void setR_model(String r_model) {
		this.r_model = r_model;
	}

	public String getR_address() {
		return r_address;
	}

	public void setR_address(String r_address) {
		this.r_address = r_address;
	}

	public String getI_name() {
		return i_name;
	}

	public void setI_name(String i_name) {
		this.i_name = i_name;
	}

	public String getI_model() {
		return i_model;
	}

	public void setI_model(String i_model) {
		this.i_model = i_model;
	}

	public String getI_address() {
		return i_address;
	}

	public void setI_address(String i_address) {
		this.i_address = i_address;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getSpd_deviceId() {
		return spd_deviceId;
	}

	public void setSpd_deviceId(int spd_deviceId) {
		this.spd_deviceId = spd_deviceId;
	}

	public int getR_deviceId() {
		return r_deviceId;
	}

	public void setR_deviceId(int r_deviceId) {
		this.r_deviceId = r_deviceId;
	}

	public int getI_deviceId() {
		return i_deviceId;
	}

	public void setI_deviceId(int i_deviceId) {
		this.i_deviceId = i_deviceId;
	}

	public String getTimeS() {
		return timeS;
	}

	public void setTimeS(String timeS) {
		this.timeS = timeS;
	}

	public String getTimeE() {
		return timeE;
	}

	public void setTimeE(String timeE) {
		this.timeE = timeE;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getR_maxValues() {
		return r_maxValues;
	}

	public void setR_maxValues(String r_maxValues) {
		this.r_maxValues = r_maxValues;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String geti_maxValues() {
		return i_maxValues;
	}

	public void seti_maxValues(String i_maxValues) {
		this.i_maxValues = i_maxValues;
	}

	public String getI_maxNum() {
		return i_maxNum;
	}

	public void setI_maxNum(String i_maxNum) {
		this.i_maxNum = i_maxNum;
	}

	public String getSpd_md44id() {
		return spd_md44id;
	}

	public void setSpd_md44id(String spd_md44id) {
		this.spd_md44id = spd_md44id;
	}

	public int getMd44_deviceId() {
		return md44_deviceId;
	}

	public void setMd44_deviceId(int md44_deviceId) {
		this.md44_deviceId = md44_deviceId;
	}

	public String getMd44_name() {
		return md44_name;
	}

	public void setMd44_name(String md44_name) {
		this.md44_name = md44_name;
	}

	public String getMd44_type() {
		return md44_type;
	}

	public void setMd44_type(String md44_type) {
		this.md44_type = md44_type;
	}

	public String getMd44_address() {
		return md44_address;
	}

	public void setMd44_address(String md44_address) {
		this.md44_address = md44_address;
	}

	public String getR_modelName() {
		return r_modelName;
	}

	public void setR_modelName(String r_modelName) {
		this.r_modelName = r_modelName;
	}

	public String getSpd_type() {
		return spd_type;
	}

	public void setSpd_type(String spd_type) {
		this.spd_type = spd_type;
	}

	public String getR_type() {
		return r_type;
	}

	public void setR_type(String r_type) {
		this.r_type = r_type;
	}

	public String getI_maxValues() {
		return i_maxValues;
	}

	public void setI_maxValues(String i_maxValues) {
		this.i_maxValues = i_maxValues;
	}

	public int getR_md44id() {
		return r_md44id;
	}

	public void setR_md44id(int r_md44id) {
		this.r_md44id = r_md44id;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getCountyId() {
		return countyId;
	}

	public void setCountyId(int countyId) {
		this.countyId = countyId;
	}
	public String getExcelName() {
		return excelName;
	}
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getR1() {
		return r1;
	}
	public void setR1(String r1) {
		this.r1 = r1;
	}
	public String getR2() {
		return r2;
	}
	public void setR2(String r2) {
		this.r2 = r2;
	}

}
