package org.normal.action;

import org.normal.function.Func;
import org.normal.function.JSON;

import com.normal.socket.SendData;
import com.normal.socket.TcpClient;
import com.opensymphony.xwork2.ActionSupport;

public class Config extends ActionSupport {
	private boolean success;
	private String message;
	
	private int  i_max;
	private int  r_max;
	private int self_time;
	private String ip;
	private int port;
	
	private int r_time;
	private int i_time;
	private int spd_time;
	private Func fun = new Func();
	private JSON json=new JSON();
	private String xmlPath = Config.class.getResource("/config.xml").getPath();
	
	
	public String configData(){
		i_time=Integer.parseInt(fun.readXml("System", "i_time"));
		r_time=Integer.parseInt(fun.readXml("System", "r_time"));
		spd_time=Integer.parseInt(fun.readXml("System", "spd_time"));
	/*	ip=fun.readXml("centerNet","center_ip");
		port=Integer.parseInt(fun.readXml("centerNet","center_port"));*/

		
		this.success=true;
		return SUCCESS;
	}
	public String updateConfig(){
		try {
			
			int i=Integer.parseInt(fun.readXml("System", "i_time"));
			int r=Integer.parseInt(fun.readXml("System", "r_time"));
			int spd=Integer.parseInt(fun.readXml("System", "spd_time"));
			if(TcpClient.getSocket().isConnected()){
				if(i!=i_time){
					fun.updateXML(xmlPath, "System", "i_time",String.valueOf(i_time));
					SendData.SynchronizeITime(i_time);
				}
				if(r!=r_time){
					fun.updateXML(xmlPath, "System", "r_time",String.valueOf(r_time));
					SendData.SynchronizeRTime(r_time);
				}
				if(spd!=spd_time){
					fun.updateXML(xmlPath, "System", "spd_time",String.valueOf(spd_time));
					SendData.SynchronizeSpdTime(spd_time);
				}
				this.success=true;
				/*fun.updateXML(xmlPath, "centerNet","center_ip", ip);
				fun.updateXML(xmlPath, "centerNet","center_port", String.valueOf(port));*/
			}else{
				this.success=false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.success=true;
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
	
	public float getI_max() {
		return i_max;
	}

	public int getR_max() {
		return r_max;
	}
	public void setR_max(int r_max) {
		this.r_max = r_max;
	}
	public int getSelf_time() {
		return self_time;
	}
	public void setSelf_time(int self_time) {
		this.self_time = self_time;
	}
	public void setI_max(int i_max) {
		this.i_max = i_max;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getR_time() {
		return r_time;
	}
	public void setR_time(int r_time) {
		this.r_time = r_time;
	}
	public int getI_time() {
		return i_time;
	}
	public void setI_time(int i_time) {
		this.i_time = i_time;
	}
	public int getSpd_time() {
		return spd_time;
	}
	public void setSpd_time(int spd_time) {
		this.spd_time = spd_time;
	}
	
	

}
