package com.normal.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.normal.action.NetDataTypeTransform;
import org.normal.function.Func;

import com.normal.protobuf.XhRtu;

public class SendData {
	private static Func fun = new Func();
	private static String xmlPath = SendData.class.getResource("/config.xml")
			.getPath();
	private static String IP = fun.readXml("centerNet", "center_ip");
	private static int m_port = Integer.parseInt(fun.readXml("centerNet",
			"center_port"));

	protected final static Log log = LogFactory.getLog(SendData.class);
	private static MessageStruct m_header = new MessageStruct();

	// 请求数据
	public String CmdREQ(RTUStruct rtu) throws IOException {
		// 创建客户端的Socket服务，指定目的主机和端口。
		NetDataTypeTransform dd = new NetDataTypeTransform();
		/* connection(); */
		// ===============================protoco buf 数据

		  /* required uint32	        rtuid 		= 1;	    //RTU ID
			required uint32 		deviceid 	= 2;     	//
			optional uint32 		md44id 		= 3;    	//md44
			optional uint32 		modle 		= 4;    	//挂载通道
			optional float 		    value		= 5;    	//获得的值  r,i
			optional uint32 		status 		= 6;    	//spd状态
			optional uint32 		type 		= 7;    	//0-i;1-i;2-spad
*/		
		  XhRtu.CmdREQ.Builder builder=XhRtu.CmdREQ.newBuilder();
		  builder.setRtuid(rtu.getRtuId());
		  builder.setDeviceid(rtu.getDeviceId());
		  builder.setMd44Id(rtu.getMd44id()); 
		  builder.setModle(rtu.getModle());
		  builder.setType(1);
		  XhRtu.CmdREQ dsReq = builder.build(); 
		  byte[] buffer =dsReq.toByteArray();
		 

		// ====================================
		// 发送数据，应该获取Socket流中的输出流。
		OutputStream out = TcpClient.getSocket().getOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		  
		dos.writeShort(m_header.getCMDHeader()); // commandHeader 2 命令开始字段
		dos.writeShort(m_header.getLength() + buffer.length);// length 2 后接数据长度
		dos.writeShort(2);// commandId 2 命令ID
		dos.write(dd.LongData(m_header.getCallID(), 8));// businessSN 8 业务流水号
		dos.writeShort(m_header.getSeqNum());
		// segNum 2 分片总数
		dos.write(dd.LongData(m_header.getReserved(), 8));
		/**************** content ***********************/
		dos.write(buffer);
		/**************** content ***********************/
		dos.writeShort(m_header.getCheckSum());

		byte[] info = bos.toByteArray();

		if (TcpClient.getSocket().isConnected()) {
			out.write(info);
			log.info("->server:" + rtu.getRtuId() + ":"
					+ rtu.getDeviceId() + ":" + rtu.getMd44id() + ":"
					+ rtu.getModle());
			log.info("->server:"
					+ TcpClient.getSocket().getInetAddress() + ":"
					+ fun.BytesToHexS(info));

		}
		return "OK";
	}

	// 获取RTU状态
	public static String OnOfflineREQ(int rtuId) throws IOException {
		// 创建客户端的Socket服务，指定目的主机和端口。
		NetDataTypeTransform dd = new NetDataTypeTransform();
		XhRtu.OnOfflineREQ.Builder builder = XhRtu.OnOfflineREQ.newBuilder();
		builder.setRtuid(rtuId);
		XhRtu.OnOfflineREQ dsReq = builder.build();
		byte[] buffer = dsReq.toByteArray();
		// ====================================
		// 发送数据，应该获取Socket流中的输出流。
		OutputStream out = TcpClient.getSocket().getOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeShort(m_header.getCMDHeader()); // commandHeader 2 命令开始字段
		dos.writeShort(m_header.getLength() + buffer.length);// length 2 后接数据长度
		dos.writeShort(4);// commandId 2 命令ID
		dos.write(dd.LongData(m_header.getCallID(), 8));// businessSN 8 业务流水号
		dos.writeShort(m_header.getSeqNum());
		// segNum 2 分片总数
		dos.write(dd.LongData(m_header.getReserved(), 8));
		/**************** content ***********************/
		dos.write(buffer);
		/**************** content ***********************/
		dos.writeShort(m_header.getCheckSum());

		byte[] info = bos.toByteArray();

		if (TcpClient.getSocket().isConnected()) {
			out.write(info);
			log.debug("获取RTU状态：send-->["+TcpClient.getSocket().getInetAddress()+ "]rtuId:"+ rtuId);
		}
		return "OK";
	}

	// 同步地阻测试周期
	public static  String SynchronizeRTime(float time) throws IOException {
		// 创建客户端的Socket服务，指定目的主机和端口。
		NetDataTypeTransform dd = new NetDataTypeTransform();
		XhRtu.SynchronizeRTime.Builder builder = XhRtu.SynchronizeRTime
				.newBuilder();
		builder.setTime(time);
		XhRtu.SynchronizeRTime dsReq = builder.build();
		byte[] buffer = dsReq.toByteArray();
		// ====================================
		// 发送数据，应该获取Socket流中的输出流。
		OutputStream out = TcpClient.getSocket().getOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeShort(m_header.getCMDHeader()); // commandHeader 2 命令开始字段
		dos.writeShort(m_header.getLength() + buffer.length);// length 2 后接数据长度
		dos.writeShort(6);// commandId 2 命令ID
		dos.write(dd.LongData(m_header.getCallID(), 8));// businessSN 8 业务流水号
		dos.writeShort(m_header.getSeqNum());
		// segNum 2 分片总数
		dos.write(dd.LongData(m_header.getReserved(), 8));
		/**************** content ***********************/
		dos.write(buffer);
		/**************** content ***********************/
		dos.writeShort(m_header.getCheckSum());

		byte[] info = bos.toByteArray();

		if (TcpClient.getSocket().isConnected()) {
			out.write(info);
			log.info("同步地阻测试周期：send-->time:" + time);
		}
		return "OK";
	}
	// 同步雷电流在线检测仪测试周期 
	public static String SynchronizeITime(float time) throws IOException {
		// 创建客户端的Socket服务，指定目的主机和端口。
		NetDataTypeTransform dd = new NetDataTypeTransform();
		XhRtu.SynchronizeITime.Builder builder = XhRtu.SynchronizeITime
				.newBuilder();
		builder.setTime(time);
		XhRtu.SynchronizeITime dsReq = builder.build();
		byte[] buffer = dsReq.toByteArray();
		// ====================================
		// 发送数据，应该获取Socket流中的输出流。
		OutputStream out = TcpClient.getSocket().getOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeShort(m_header.getCMDHeader()); // commandHeader 2 命令开始字段
		dos.writeShort(m_header.getLength() + buffer.length);// length 2 后接数据长度
		dos.writeShort(7);// commandId 2 命令ID
		dos.write(dd.LongData(m_header.getCallID(), 8));// businessSN 8 业务流水号
		dos.writeShort(m_header.getSeqNum());
		// segNum 2 分片总数
		dos.write(dd.LongData(m_header.getReserved(), 8));
		/**************** content ***********************/
		dos.write(buffer);
		/**************** content ***********************/
		dos.writeShort(m_header.getCheckSum());

		byte[] info = bos.toByteArray();

		if (TcpClient.getSocket().isConnected()) {
			out.write(info);
			log.info("同步雷电流在线检测仪测试周期 ：send-->time:" + time);
		}
		return "OK";
	}
	// 同步SPD检测仪测试周期
	public static String SynchronizeSpdTime(float time) throws IOException {
		// 创建客户端的Socket服务，指定目的主机和端口。
		NetDataTypeTransform dd = new NetDataTypeTransform();
		XhRtu.SynchronizeSpdTime.Builder builder = XhRtu.SynchronizeSpdTime
				.newBuilder();
		builder.setTime(time);
		XhRtu.SynchronizeSpdTime dsReq = builder.build();
		byte[] buffer = dsReq.toByteArray();
		// ====================================
		// 发送数据，应该获取Socket流中的输出流。
		OutputStream out = TcpClient.getSocket().getOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeShort(m_header.getCMDHeader()); // commandHeader 2 命令开始字段
		dos.writeShort(m_header.getLength() + buffer.length);// length 2 后接数据长度
		dos.writeShort(8);// commandId 2 命令ID
		dos.write(dd.LongData(m_header.getCallID(), 8));// businessSN 8 业务流水号
		dos.writeShort(m_header.getSeqNum());
		// segNum 2 分片总数
		dos.write(dd.LongData(m_header.getReserved(), 8));
		/**************** content ***********************/
		dos.write(buffer);
		/**************** content ***********************/
		dos.writeShort(m_header.getCheckSum());

		byte[] info = bos.toByteArray();

		if (TcpClient.getSocket().isConnected()) {
			out.write(info);
			log.info("同步雷Spd检测仪测试周期 ：send-->time:" + time);
		}
		return "OK";
	}
}
