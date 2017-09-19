package org.normal.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.normal.function.Func;
import org.normal.sql.MysqlConnection;

import com.normal.socket.RTUStruct;
import com.normal.socket.SendData;
import com.normal.socket.TcpClient;
import com.opensymphony.xwork2.ActionSupport;

public class RtuController extends ActionSupport {
	private boolean success;
	private String message;

	private int rtuId;
	private int deviceType; // 设备类型 0:地阻测试仪器 1:雷电流在线监测仪
	private int deviceId; // modbus地址码;详细请参考具体协议文档
	private int func;// modbus功能码;详细请参考具体协议文档 1
	private int addr; // modbus读写地址;详细请参考具体协议文档 1
	private int data;// modbus读数据长度/写数据值;详细请参考具体协议文档 n*1
	
	private int deviceId2;
	private int model;
	private int md44id;
	
	private int timeM;
	private int timeS;
	
	private int timeD;
	private int timeH;
	
	private int timeYear;
	private int timeMonth;
	
	private int icount;
	
	private int iindex;
	private int databack;
	private int reboot;
	private int clearAlarm;
	
	private float value;
	

	private RTUStruct rtuStruct = new RTUStruct();
	private SendData sendData = new SendData();
	private Func fun = new Func();
	private MysqlConnection mysql = new MysqlConnection();
	protected final Log log = LogFactory.getLog(RtuController.class);

	public String data() {	
		value();
		String sql="select * from site_lr where siteId='"+rtuStruct.getRtuId()+"' "
				+ "and md44id='"+rtuStruct.getMd44id()+"' "
			    + "and deviceId='"+rtuStruct.getDeviceId()+"' "
			    + "and model='"+rtuStruct.getModle()+"'";
		if(!mysql.isExists(sql)){
			this.success=false;
			this.message="监测点不存在，请检查输入是否有误";
			log.error("监测点不存在");
			return SUCCESS;
		}
		try {
			if (TcpClient.getSocket().isConnected()) {
				TcpClient.setR_value(-5);
				log.info("测试地阻");
				
				Thread.sleep(35000);
				String key=rtuStruct.getRtuId()+"-"+rtuStruct.getMd44id()+"-"+rtuStruct.getDeviceId()+"-"+rtuStruct.getModle();
				
				
				value=TcpClient.getR_map().get(key);
				if (value==-5) {
					this.success=false;
					this.message="连接设备超时！，请检查设备配置";
					log.error("rtuId:"+rtuId+";deviceId:"+deviceId+":连接设备超时！，请检查设备配置");
					return SUCCESS;
				}
			}else{
				this.success=false;
				this.message="网络未连接，请检查中心IP，端口是否配置正确";
				log.error("网络未连接，请检查中心IP，端口是否配置正确");
				return SUCCESS;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NullPointerException e){
			
			
		}
		this.success = true;
		this.message = "发送成功";
		return SUCCESS;
	}
	public void value(){
		
		rtuStruct.setRtuId(rtuId);
		rtuStruct.setDeviceId(deviceId);
		rtuStruct.setMd44id(mysql.md44id(rtuId, deviceId, model));
		rtuStruct.setModle(model);
		rtuStruct.setType(1);
		if (TcpClient.getSocket().isConnected()) {
			try {
				sendData.CmdREQ(rtuStruct);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/*	//打开地阻测试开关
	public void open(int id,int dId){
		rtuStruct.setRtuId(id);
		rtuStruct.setDeviceType(mysql.siteId_R_deviceId_com(id, dId));
		rtuStruct.setDeviceId(dId);
		rtuStruct.setFunc(6);
		rtuStruct.setAddr(0x0011);
		rtuStruct.setData(0x0001);
		if (TcpClient.getSocket().isConnected()) {
			try {
				sendData.CmdREQ(rtuStruct);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
	
	

	
	
	
	
	

	public String start() {
		addr = 0x0000;
		data = 0x0009;
		/*rtuStruct.setDeviceType(deviceType);
		rtuStruct.setDeviceId(deviceId);
		rtuStruct.setFunc(func);
		rtuStruct.setAddr(addr);
		rtuStruct.setData(data);*/
		if (TcpClient.getSocket().isConnected()) {
			try {
				sendData.CmdREQ(rtuStruct);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.success = true;
		this.message = "发送成功";
		return SUCCESS;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getAddr() {
		return addr;
	}

	public void setAddr(int addr) {
		this.addr = addr;
	}

	public int getFunc() {
		return func;
	}

	public void setFunc(int func) {
		this.func = func;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getRtuId() {
		return rtuId;
	}

	public void setRtuId(int rtuId) {
		this.rtuId = rtuId;
	}

	public int getDeviceId2() {
		return deviceId2;
	}

	public void setDeviceId2(int deviceId2) {
		this.deviceId2 = deviceId2;
	}

	public int getTimeM() {
		return timeM;
	}

	public void setTimeM(int timeM) {
		this.timeM = timeM;
	}

	public int getTimeS() {
		return timeS;
	}

	public void setTimeS(int timeS) {
		this.timeS = timeS;
	}

	public int getTimeD() {
		return timeD;
	}

	public void setTimeD(int timeD) {
		this.timeD = timeD;
	}

	public int getTimeH() {
		return timeH;
	}

	public void setTimeH(int timeH) {
		this.timeH = timeH;
	}

	public int getTimeYear() {
		return timeYear;
	}

	public void setTimeYear(int timeYear) {
		this.timeYear = timeYear;
	}

	public int getTimeMonth() {
		return timeMonth;
	}

	public void setTimeMonth(int timeMonth) {
		this.timeMonth = timeMonth;
	}

	public int getIcount() {
		return icount;
	}

	public void setIcount(int icount) {
		this.icount = icount;
	}

	public int getIindex() {
		return iindex;
	}

	public void setIindex(int iindex) {
		this.iindex = iindex;
	}

	public int getDataback() {
		return databack;
	}

	public void setDataback(int databack) {
		this.databack = databack;
	}

	public int getReboot() {
		return reboot;
	}

	public void setReboot(int reboot) {
		this.reboot = reboot;
	}

	public int getClearAlarm() {
		return clearAlarm;
	}

	public void setClearAlarm(int clearAlarm) {
		this.clearAlarm = clearAlarm;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	

}
