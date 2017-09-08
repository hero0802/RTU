package com.normal.socket;

public class RTUStruct {
	
	private int rtuId;
	/*private int deviceType; //设备类型  0:地阻测试仪器  1:雷电流在线监测仪
*/	private int deviceId; //modbus地址码;详细请参考具体协议文
	/*private int com; //modbus地址码;详细请参考具体协议文档
*/	
	/*private int addr; //modbus读写地址;详细请参考具体协议文档    1
	private int func;//modbus功能码;详细请参考具体协议文档      1
	private int data;//modbus读数据长度/写数据值;详细请参考具体协议文档     n*1
*/	
   private int md44id;
   private int modle ;
   private int value;
   private int status ;
   private int type;
   private int  crc;//校验码          2
public int getRtuId() {
	return rtuId;
}
public void setRtuId(int rtuId) {
	this.rtuId = rtuId;
}
public int getDeviceId() {
	return deviceId;
}
public void setDeviceId(int deviceId) {
	this.deviceId = deviceId;
}
public int getMd44id() {
	return md44id;
}
public void setMd44id(int md44id) {
	this.md44id = md44id;
}
public int getModle() {
	return modle;
}
public void setModle(int modle) {
	this.modle = modle;
}
public int getValue() {
	return value;
}
public void setValue(int value) {
	this.value = value;
}
public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status = status;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public int getCrc() {
	return crc;
}
public void setCrc(int crc) {
	this.crc = crc;
}
	
	
	
	
	
	

}
