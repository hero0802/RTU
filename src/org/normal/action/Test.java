package org.normal.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import org.normal.function.Crc;
import org.normal.function.Func;
import org.normal.javabean.Rbean;

public class Test {
	public static int num=0;
	private static ArrayList<Rbean> m_rlist=new ArrayList<Rbean>();
	private static Func fun = new Func();

	public static int getNum() {
		return num;
	}
	public static void setNum(int num) {
		Test.num = num;
	}
	
	@SuppressWarnings("null")
	public static void main(String[] args) {
		//01 34 00 02
		/*String h="00000001 0011 0011 0000000000000010";
		String[] num = new String[32];*/
	/*	num[0]="0";
		System.out.println( R_value(h));*/
		  // 0x02 05 00 03 FF 00 , crc16=7C 09  
        int crc = Crc.calcCrc16(new byte[] { 0x06, 0x03, 0x00, 0x01,0x00, 0x02});  
        System.out.println(String.format("0x%04x", crc)); 

	}
	
	public static float R_value(String str){
		if (str.length()!=32) {
			return -1;
		}else {
			String[] num = new String[32];
			for (int i = 0; i < str.length(); i++) {
				num[i]=String.valueOf(str.charAt(i));
				
			}
			System.out.println(Arrays.toString(num));
			int power=0,r1=0;
		    float r2=0,r=0;
			StringBuffer b_power=new StringBuffer();
			StringBuffer b_r1=new StringBuffer();
			StringBuffer b_r2=new StringBuffer();
			b_r1.append(1);
			
			for (int i = 1; i <9; i++) {
			 b_power.append(num[i]);
			}
			
			power=fun.hex_2_10(b_power.toString())-127;
			System.out.println(power);
			for (int i = 9; i <9+power; i++) {
				b_r1.append(num[i]);
			}
			for (int i =9+power; i < num.length; i++) {
				b_r2.append(num[i]);
			}
			r1=fun.hex_2_10(b_r1.toString());
			for (int i = 0; i < b_r2.toString().length(); i++) {
				r2+=fun.StringToInt(String.valueOf(b_r2.charAt(i)))*Math.pow(2, -(i+1));
			}
			r=r1+r2;
			return r;
			
		}
		
	}
	
	public static void dd(){
		long a=0x40a66666;
		int power=(int) ((a>>23)-127);
		boolean b;
		String h="101010";
		int c=(int) ((a>>21)|0x00000040);
		System.out.println(a>>31);
		System.out.println(power);
		System.out.println(h.charAt(1));
		
		
		       
	}
	
	
	public static void Server(){
		ServerSocket serverSocket = null;
		System.out.println("服务端就绪........");
		try {
			serverSocket = new ServerSocket(9999);
			//使用循环方式一直等待客户端的连接
			while(true){
				//num ++;
				Socket accept = serverSocket.accept();
				//启动一个新的线程，接管与当前客户端的交互会话
				if (num<=3) {
					new Thread(new ServerThread(accept),"Client "+num).start();
				}else {
					System.out.println("超过最大连接数");
					accept.close();
				
					
				}
				
			}
	} catch (IOException e) {
		System.out.println("服务器故障断开连接");
		//e.printStackTrace();
	}
	finally{
		try {
			serverSocket.close();
			System.out.println("---->  serverSocket closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
}


/**
 * @author JCC
 * 服务器处理客户端会话的线程
 */
class ServerThread implements Runnable {
	
	
	
	Socket socket = null;
	boolean conn_flg=true;
	public ServerThread(Socket socket){
		System.out.println("==》建立一个新的连接");
		Test.num++;
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out  = socket.getOutputStream();
			String clientIp = socket.getInetAddress().getHostAddress();
			System.out.println("开始处理来自\""+clientIp+":"+socket.getPort()+"\"的请求。");
			//使用循环的方式，不停的与客户端交互会话
			boolean done=socket.isConnected();//判断客户端Socket是否连接
		    while(done){
				try {
					Thread.sleep(10);
					//处理客户端发来的数据
					doRead(in);
					//发送数据回客户端
					doWrite(out);
					System.out.println("send Message to client."+socket.getPort());
					//socket.close();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
					
				}
				try {
					socket.sendUrgentData(0); 
				} catch (Exception e) {
					// TODO: handle exception
					Test.setNum(Test.getNum()-1);
					System.out.println(socket.getPort()+"断开"+Test.num);
					done= false; //如果抛出了异常，那么就是断开连接了 跳出无限循环
				}	
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取数据
	 * @param in
	 * @return
	 */
	public boolean doRead(InputStream in){
		//引用关系，不要在此处关闭流
		byte[] buf = new byte[1024];// 收到的包字节数组
		try {
			int len = in.read(buf);
			System.out.println("line:"+len);
		} catch (IOException e) {
			try {
				socket.close();
				System.out.println("客户端主动断开");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * 写入数据
	 * @param out
	 * @return
	 */
	public boolean doWrite(OutputStream out){
		//引用关系，不要在此处关闭流
		try {
			out.write("welcome you client.".getBytes());
			out.flush();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			/*
			 * conn_flg=false; System.out.println("断开");
			 */
		}
		return true;
	}
}
