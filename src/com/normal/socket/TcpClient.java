package com.normal.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.normal.action.NetDataTypeTransform;
import org.normal.action.RtuController;
import org.normal.action.Site;
import org.normal.dwr.TcpDwr;
import org.normal.function.Func;
import org.normal.function.JSON;
import org.normal.javabean.IalarmBean;
import org.normal.sql.MysqlConnection;

import com.google.protobuf.InvalidProtocolBufferException;
import com.normal.protobuf.XhRtu;

public class TcpClient extends Thread {
	protected final Log log = LogFactory.getLog(TcpClient.class);
	private String ip;
	private int port;
	private static Socket socket;
	private static int timeout = 1 * 80 * 1000;
	private boolean connected = false;

	private static int dataLen = 0;
	private static byte[] writeBuf = {};
	private static byte[] result;
	private static byte[] readBuf;
	private static int comID = -1;
	private static int bufLen = -1;

	private static String callId;
	private MysqlConnection mysql = new MysqlConnection();
	private Func fun = new Func();
	private NetDataTypeTransform dd = new NetDataTypeTransform();
	private String xmlPath = TcpClient.class.getResource("/config.xml")
			.getPath();
	private RtuController rtu = new RtuController();
	private JSON json = new JSON();

	private static HashMap<String, IalarmBean> m_ialarmMap = new HashMap<String, IalarmBean>();
	private static HashMap<String, Float> r_map = new HashMap<String, Float>();
	private static float r_value;

	public void run() {

		receive();
	}

	// 接收数据
	public void receive() {
		InputStream input = null;
		NetDataTypeTransform dd = new NetDataTypeTransform();

		while (!connected) {

			try {
				System.setProperty("java.net.preferIPv4Stack", "true");
				connect();
				input = socket.getInputStream();
				if (socket.isConnected()) {
					connected = true;
					log.info("TCP Connected success!!");
					TcpDwr.centerStatus(1);

					
					 Timer timer = new Timer(); try { timer.schedule(new
					 HeartBeat(socket), 2000, 1 * 60* 1000); 
					 
					 SendData.OnOfflineREQ(-1);
					 
					  } catch (IOException e) {
					 }
					

				}
				// read body
				byte[] buf = new byte[1024];// 收到的包字节数组
				byte[] bufH = new byte[2];// 收到的包字节数组
				byte[] realBuf = new byte[10240];

				while (connected) {
					int len = input.read(buf);
					int recvLen = len;
					if (len > 0 && len + writeBuf.length >= 4) {
						readBuf = new byte[len + writeBuf.length];
						System.arraycopy(writeBuf, 0, readBuf, 0,
								writeBuf.length);
						System.arraycopy(buf, 0, readBuf, writeBuf.length, len);
						len = len + writeBuf.length;

						System.arraycopy(readBuf, 0, bufH, 0, 2);
						String packageHeader = HexString(bufH);
						if (!packageHeader.equals("c4d5")) {
							log.error("SocketError1111:>>!c4d7");
							log.info(packageHeader);
							/* writeBuf=null; */
						} else {
							int length = dd.BigByteArrayToShort(readBuf, 2);
							if (length + 4 > len) {
								byte[] temp = new byte[writeBuf.length];
								System.arraycopy(writeBuf, 0, temp, 0,
										writeBuf.length);
								writeBuf = new byte[recvLen + temp.length];
								System.arraycopy(temp, 0, writeBuf, 0,
										temp.length);
								System.arraycopy(buf, 0, writeBuf, temp.length,
										recvLen);
								// break;
							} else if (length + 4 == len) {
								int comId = dd.BigByteArrayToShort(readBuf, 4);
								String callid = dd.ByteArraytoString(readBuf,
										6, 8);
								comID = comId;

								callId = dd.ByteArraytoString(readBuf, 6, 8);
								handler(comId, length + 4, callid, readBuf, len);
							} else if (len > length + 4) {

								for (int i = 0; i <= len;) {

									System.arraycopy(readBuf, i, realBuf, 0,
											len - i);
									length = dd.BigByteArrayToShort(realBuf, 2);
									dataLen = length + 4;
									System.arraycopy(realBuf, 0, bufH, 0, 2);
									packageHeader = HexString(bufH);
									if (!packageHeader.equals("c4d5")) {
										log.error("SocketError2:>>!c4d7");
									}
									if (dataLen > len - i) {
										writeBuf = new byte[len - i];
										System.arraycopy(realBuf, 0, writeBuf,
												0, len - i);
										break;
									} else {
										int comId = dd.BigByteArrayToShort(
												realBuf, 4);
										String callid = dd.ByteArraytoString(
												realBuf, 6, 8);
										length = dd.BigByteArrayToShort(
												realBuf, 2);
										comID = comId;

										callId = dd.ByteArraytoString(realBuf,
												6, 8);
										handler(comId, dataLen, callid,
												realBuf, len);
										i += length + 4;
										writeBuf = null;
									}

								}
							}
						}

					} else {
						socket.close();
						log.debug("====TCP connection is closed!!====");
						log.debug("====reconnection now!!====");

						connected = false;
					}
				}

			} catch (SocketException e) {
				log.info("TCP connection trying");
				TcpDwr.centerStatus(0);
				if (socket.isConnected() || socket != null) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					connected = false;
				}

			} catch (UnknownHostException e) {
				System.out.println("UnknownHostException");
			} catch (IOException e) {
				log.info("recvData timeout 60s,socket is closed and reconnecting!");
				TcpDwr.centerStatus(2);
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				connected = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void connect() {
		if (socket == null || socket.isClosed() || !socket.isConnected()) {
			socket = new Socket();
			ip = fun.readXml("centerNet", "center_ip");
			port = Integer.parseInt(fun.readXml("centerNet", "center_port"));
			InetSocketAddress addr = new InetSocketAddress(ip, port);
			try {
				socket.connect(addr, timeout);
				socket.setTcpNoDelay(true);
			} catch (IOException e) {

			}
			try {
				socket.setKeepAlive(true);
			} catch (SocketException e) {
				/* log.info("KeepAlive SocketException"); */
			}
			try {
				socket.setSoTimeout(timeout);
			} catch (SocketException e) {

				try {
					socket.close();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				connected = false;
			}

		}
	}

	// 转16进制
	public String HexString(byte[] src) {
		String str = "";
		int v1 = src[0] & 0xFF;
		int v2 = src[1] & 0xFF;
		str = Integer.toHexString(v1) + Integer.toHexString(v2);
		return str;
	}

	public void handler(int comId, int len, String callid, byte[] buf,
			int length) throws Exception {
		
		/*log.info(fun.BytesToHexS(buf));*/
		try {
			switch (comId) {
			/*case 1:
				HeartBeatOut(len, buf);
				break;*/
			case 3:
				CmdRES(len, buf);
				break;
			case 5:
				OnOfflineRES(len, buf);
				break;

			default:
				break;
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		} catch (StringIndexOutOfBoundsException e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		} catch (ConcurrentModificationException e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodError e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		}

	}

	// RTU状态
	public void OnOfflineRES(int len, byte[] buf) {
		result = new byte[len - 26];
		System.arraycopy(buf, 24, result, 0, len - 26);
		
		try {
			XhRtu.OnOfflineRES res = XhRtu.OnOfflineRES.parseFrom(result);
			log.info("==========[OnOfflineRES]=======");
			log.info("[OnOfflineRES]----RTUCount>>" + res.getRtustatCount());
			for (int i = 0; i < res.getRtustatCount(); i++) {
				XhRtu.OnOfflineRES.OnOffStatus offStatus = res.getRtustatList()
						.get(i);
				String sql = "update site set status='" + offStatus.getStatus()
						+ "' where siteId=" + offStatus.getRtuid();
				mysql.Update(sql);
				
				log.info("[OnOfflineRES]----RtuID>>" + offStatus.getRtuid());
				log.info("[OnOfflineRES]----RtuStatus>>"+ offStatus.getStatus());
				String sql2 = "select a.*,b.siteId as alarmId from site as a left join view_alarm_site as b on a.siteId=b.siteId where a.siteId="+offStatus.getRtuid();
				HashMap result = new HashMap();
				result.put("items", mysql.DBList(sql2));
				String jsonstr = json.Encode(result);
				TcpDwr.RtuStatus(jsonstr);
			}
			log.info("----------------------------");
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 心跳
	public void HeartBeatOut(int len, byte[] buf) {
		result = new byte[len - 26];
		System.arraycopy(buf, 24, result, 0, len - 26);
		// log.info(Arrays.toString(result));
		try {
			XhRtu.HeartBeatOUT res = XhRtu.HeartBeatOUT.parseFrom(result);
			log.info("==========[HeartBeat]=======");
			log.info("[HeartBeat]----RTU>>" + res.getWorkstatus());
			log.info("----------------------------");
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 命令应答
	public void CmdRES(int len, byte[] buf) {
		result = new byte[len - 26];
		System.arraycopy(buf, 24, result, 0, len - 26);
		System.out.println(fun.BytesToHexS(result));
		
		try {
			XhRtu.CmdREQ res = XhRtu.CmdREQ.parseFrom(result);
			int rtuId = res.getRtuid();
			int deviceId = res.getDeviceid();
			int model = res.getModle();
			int md44 = res.getMd44Id();
			float value = res.getValue();
			String key = rtuId + "-" + md44 + "-" + deviceId + "-" + model;
			log.info("============================================");
			log.info("siteId=" + rtuId);
			log.info("md44Id=" + md44);
			log.info("deviceId=" + deviceId);
			log.info("model=" + model);
			log.info("测试地阻值=" + value);
			log.info("============================================");
			TcpClient.getR_map().put(key, value);
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();

	}

	

	// 取位数
	public int byte_value(int value, int index) {
		/* int a=(index+31)%32; */
		if (index > 31) {
			return -1;
		}
		return (value >> index) & 0x01;
	}

	

	public String timeFormat(int str) {
		String a = "";
		if (str < 10) {
			a = "0" + str;
		} else {
			a = String.valueOf(str);
		}
		return a;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		TcpClient.socket = socket;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		TcpClient.timeout = timeout;
	}

	public static byte[] getReadBuf() {
		return readBuf;
	}

	public static void setReadBuf(byte[] readBuf) {
		TcpClient.readBuf = readBuf;
	}

	public static int getDataLen() {
		return dataLen;
	}

	public static void setDataLen(int dataLen) {
		TcpClient.dataLen = dataLen;
	}

	public static byte[] getWriteBuf() {
		return writeBuf;
	}

	public static void setWriteBuf(byte[] writeBuf) {
		TcpClient.writeBuf = writeBuf;
	}

	public static int getComID() {
		return comID;
	}

	public static void setComID(int comID) {
		TcpClient.comID = comID;
	}

	public static int getBufLen() {
		return bufLen;
	}

	public static void setBufLen(int bufLen) {
		TcpClient.bufLen = bufLen;
	}

	public static String getCallId() {
		return callId;
	}

	public static void setCallId(String callId) {
		TcpClient.callId = callId;
	}

	public static HashMap<String, IalarmBean> getM_ialarmMap() {
		return m_ialarmMap;
	}

	public static void setM_ialarmMap(HashMap<String, IalarmBean> m_ialarmMap) {
		TcpClient.m_ialarmMap = m_ialarmMap;
	}

	public static float getR_value() {
		return r_value;
	}

	public static void setR_value(float r_value) {
		TcpClient.r_value = r_value;
	}

	public static HashMap<String, Float> getR_map() {
		return r_map;
	}

	public static void setR_map(HashMap<String, Float> r_map) {
		TcpClient.r_map = r_map;
	}

}

class HeartBeat extends TimerTask {
	private Socket socket = null;
	private TcpClient tcp;

	protected final Log log = LogFactory.getLog(HeartBeat.class);

	public HeartBeat(Socket socket) throws Exception {
		this.socket = socket;

	}

	public void run() {
		NetDataTypeTransform dd = new NetDataTypeTransform();
		// ===============================protoco buf 数据
		XhRtu.HeartBeatIN.Builder builder = XhRtu.HeartBeatIN.newBuilder();

		builder.setWorkstatus(1);
		builder.setRtuid(0);
		XhRtu.HeartBeatIN dsReq = builder.build();
		byte[] buffer = dsReq.toByteArray();

		// ====================================
		// 发送数据，应该获取Socket流中的输出流。
		MessageStruct mStruct = new MessageStruct();
		if (socket.isConnected()) {
			try {
				OutputStream out = socket.getOutputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);

				dos.writeShort(mStruct.getCMDHeader()); // commandHeader 2
				// 命令开始字段
				dos.writeShort(mStruct.getLength() + buffer.length);// length 2
				// 后接数据长度
				dos.writeShort(0);// commandId 2 命令ID
				dos.write(dd.LongData(mStruct.getCallID(), 8));// businessSN 8
				// 业务流水号
				dos.writeShort(mStruct.getSeqNum());
				// segNum 2 分片总数
				dos.write(dd.LongData(mStruct.getReserved(), 8));
				// **************** content ***********************//*
				dos.write(buffer);
				// **************** content ***********************//*
				dos.writeShort(mStruct.getCheckSum());

				byte[] info = bos.toByteArray();
				if (TcpClient.getSocket().isConnected()) {

					out.write(info);
				}

			} catch (IOException e) {
			}
		} else {
			log.info("====Timer:socket closed!!====");
		}
	}
}
