package org.normal.function;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;




public class Func {
	private static String xmlPath=Func.class.getResource("/config.xml").getPath();
	
	public String xmlPath(){
		String str=Func.class.getResource("/config.xml").getPath();
		return str;
	}
	public int StringToInt(String str){
		int value=-1;
		try {
			value= Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}catch (NullPointerException e) {
			// TODO: handle exception
		}
		return value;
	}
	//2进制转10进制
	public int hex_2_10(String str){
		if (str==null) {
			return 0;
		}else {
			return Integer.valueOf(str,2);
		}
	}
	public String ByteToHexS(byte str){
		String string="";
		String c=Integer.toHexString(str& 0xFF);
		if (c.length()==1) {
			c="0"+c;
		}
		string+=c;
	   return string;
		
	}
	//16进制转2进制
	public String  hex_16_2(String  str){
		if (str.equals("0")) {
			return "00000000";
		}
		String a=Integer.toBinaryString(Integer.parseInt(str,16));
		String b="";
		if (a.length()<8) {
			
			for (int i = 0; i <8-a.length(); i++) {
				b+="0";
			}
		}
		a=b+a;
		return a;
	}
	
	public String  hex_10_2(int  str){
		if(str==0) {
			return "00000000";
		}
		String a=Integer.toBinaryString(str);
		String b="";
		if (a.length()<8) {
			
			for (int i = 0; i <8-a.length(); i++) {
				b+="0";
			}
		}
		a=b+a;
		return a;
	}
	
	public String BytesToHexS(byte[] str){
		if (str==null) {
			return "";
		}else {
			String string="";
			for (byte b : str) {
				String c=Integer.toHexString(b& 0xFF);
				if (c.length()==1) {
					c="0"+c;
				}
				string+=c+" ";
			}
			return string;
		}
	}
	/**
	 *  获取随机数
	 * @param length
	 * @return
	 */
	public String RandomWord(int length) {
		String[] str = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B",
				"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
				"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		if (length > str.length) {
			return "";
		}
		List<String> list = Arrays.asList(str);
		// 打乱list顺序
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (String string : list) {
			sb.append(string);
		}
		String afterStr = sb.toString();
		String result = afterStr.substring(0, length);
		return result;
	}
	/**
	 * 设置Cookie
	 * @param name
	 * @param value
	 * @param day
	 * @param domain
	 */
	public void setCookie(String name, String value, int day, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(day * 24 * 60 * 60);
		cookie.setDomain(domain);
		ServletActionContext.getResponse().addCookie(cookie);
	}

	/**
	 * 获取cookie
	 * @param name
	 * @return
	 */
	public String getCookie(String name) {
		HttpServletRequest request = ServletActionContext.getRequest();
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	/**
	 * 设置session
	 * @param name
	 * @param value
	 */
	public static void setSession(String name,String value){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession(); //创建
		session.setAttribute(readXml("Other", "session_prefix")+name, value);
		session.setMaxInactiveInterval(30*60*1000);
	}
	public static  String getSession(String name){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		return session.getAttribute(readXml("Other", "session_prefix")+name).toString();
	}
	public static  void distorySession(String name){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute(readXml("Other", "session_prefix")+name);
	}
	/**
	 * 获取32位MD5加密
	 * @param originstr
	 * @return
	 */
	public String getMd5(String originstr) {
		String result = null;
		char[] hexDigits = {// 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		if (originstr != null) {
			try {
				// 返回实现指定摘要算法的 MessageDigest 对象
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用utf-8编码将originstr字符串编码并保存到source字节数组
				byte[] source = originstr.getBytes("utf-8");
				// 使用指定的 byte 数组更新摘要
				md.update(source);
				// 通过执行诸如填充之类的最终操作完成哈希计算，结果是一个128位的长整数
				byte[] tmp = md.digest();
				// 用16进制数表示需要32位
				char[] str = new char[32];
				for (int i = 0, j = 0; i < 16; i++) {
					// j表示转换结果中对应的字符位置
					// 从第一个字节开始，对 MD5 的每一个字节
					// 转换成 16 进制字符
					byte b = tmp[i];
					// 取字节中高 4 位的数字转换
					// 无符号右移运算符>>> ，它总是在左边补0
					// 0x代表它后面的是十六进制的数字. f转换成十进制就是15
					str[j++] = hexDigits[b >>> 4 & 0xf];
					// 取字节中低 4 位的数字转换
					str[j++] = hexDigits[b & 0xf];
				}
				result = new String(str);// 结果转换成字符串用于返回
			} catch (NoSuchAlgorithmException e) {
				// 当请求特定的加密算法而它在该环境中不可用时抛出此异常
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// 不支持字符编码异常
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 获取客户端IP地址
	 * @return
	 */
	public String getClientIpAddr() {
		HttpServletRequest request=ServletActionContext.getRequest();
		 String ip = request.getHeader("x-forwarded-for");
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getHeader("Proxy-Client-IP");
		    }
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getHeader("WL-Proxy-Client-IP");
		    }
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getHeader("HTTP_CLIENT_IP");
		    }
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		    }
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getRemoteAddr();
		    }
		    return ip;
		}
	/**
	 * 创建文件
	 * @param fileName
	 */
	public void createFile(String fileName){
		String filePath=fileName.substring(0,fileName.lastIndexOf("/"));
		File file=new File(fileName);
		File filePathName=new File(filePath);
		if (!filePathName.isDirectory()&&!filePathName.exists()) {
			filePathName.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 写文件
	 * @param pathname
	 * @param str
	 * @param append
	 */
	public void writeFile(String pathname,String str,boolean append){
		OutputStreamWriter oWriter=null;
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(pathname, append);
			oWriter=new OutputStreamWriter(fos,"UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (oWriter!=null) {
				try {
					oWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 判断文件是否为空
	 * @param filename
	 * @return
	 */
	public boolean fileIsNull(String filename){
		FileReader fReader=null;
		boolean isNull=false;
		try {
			fReader=new FileReader(filename);
			if (fReader.read()==-1) {
				isNull=true;
			}else {
				isNull=false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (fReader!=null) {
				try {
					fReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isNull;
	}
	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	/**
	 * 获取当前时间，并格式化
	 * @return
	 */
	public String nowDate(){
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		Date date=new Date();
		return sFormat.format(date);
	}
	/**
	 * UTF-8转USC2
	 * @param str
	 * @return
	 */
	public byte[] utf8ToUsc2(String str) {

		try {
			ByteArray byteArray = new ByteArray();
			byte[] data = str.getBytes("utf-8");
			for (int i = 0; i < data.length; i++) {
				byte b = data[i];
				if (0 == (b & 0x80)) {
					byteArray.addByte((byte) 0x00);
					byteArray.addByte(b);
				} else if (0 == (b & 0x20)) {
					byte a = data[++i];
					byteArray.addByte((byte) ((b & 0x3F) >> 2));
					byteArray.addByte((byte) (a & 0x3F | b << 6));

				} else if (0 == (b & 0x10)) {
					byte a = data[++i];
					byte c = data[++i];
					byteArray.addByte((byte) (b << 4 | (a & 0x3F) >> 2));
					byteArray.addByte((byte) (a << 6 | c & 0x3F));
				}

			}

			byte[] arr = byteArray.toArray();

			for (int i = 0; i < arr.length; i += 2) {
				byte[] temp1 = new byte[1];
				temp1[0] = arr[i];
				arr[i] = arr[i + 1];
				arr[i + 1] = temp1[0];
			}
			return arr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * USC2转UTF-8
	 * @param arr
	 * @return
	 */
	public byte[] ucs2ToUtf8(byte[] arr) {
		if (arr.length < 1) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		for (int i = 0; i < arr.length; i += 2) {
			char ch = (char) (arr[i] * 256 + arr[i + 1]);
			if (ch <= 0x007F) {
				baos.write(ch);
			} else if (ch <= 0x07FF) {
				byte ub1 = arr[i];
				byte ub2 = arr[i + 1];
				byte b1 = (byte) (0xc0 | (ub1 << 2) | (ub2 >> 6));
				byte b2 = (byte) (0x80 | (ub2 & 0x3F));
				baos.write(b1);
				baos.write(b2);
			} else {
				byte ub1 = arr[i];
				byte ub2 = arr[i + 1];
				byte b1 = (byte) (0xe0 | ((ub1 >> 4) & 0x0F));
				byte b2 = (byte) (0x80 | ((ub1 & 0x0F) << 2) & 0x3c | (ub2 >> 6) & 0x03);
				byte b3 = (byte) (0x80 | (ub2 & 0x3F));
				baos.write(b1);
				baos.write(b2);
				baos.write(b3);
			}
		}
		return baos.toByteArray();

	}
	/**
	 *  读取xml文档
	 * @param fileName
	 * @param str1
	 * @param str2
	 * @return
	 */
		public static String readXml(String str1, String str2) {
			String value = "";
			try {
				File f = new File(xmlPath);
				if (!f.exists()) {
					System.out.println("  Error : Config file doesn't exist!");
					System.exit(1);
				}
				SAXReader reader = new SAXReader();
				Document doc;
				doc = reader.read(f);
				Element root = doc.getRootElement();
				Element data;
				Iterator<?> itr = root.elementIterator(str1);
				data = (Element) itr.next();

				value = data.elementText(str2).trim();

			} catch (Exception ex) {
				System.out.println("Error : " + ex.toString());
				return "";
			}
			return value;

		}
		/**
		 * 修改xml文件
		 * @param filename
		 * @param str1
		 * @param str2
		 * @param value
		 * @throws Exception
		 */
		public void updateXML(String filename,String str1, String str2, String value)
				throws Exception {
			SAXReader sax = new SAXReader();
			Document xmlDoc = sax.read(new File(filename));
			List<Element> eleList = xmlDoc.selectNodes("/config/" + str1 + "/"+ str2);
			Iterator<Element> eleIter = eleList.iterator();
			if (eleIter.hasNext()) {
				Element ownerElement = eleIter.next();
				ownerElement.setText(value);
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 利用格式化类对编码进行设置
			format.setEncoding("UTF-8");
			FileOutputStream output = new FileOutputStream(new File(filename));
			XMLWriter writer = new XMLWriter(output, format);
			writer.write(xmlDoc);
			writer.flush();
			writer.close();
		}
		// 转16进制
		public String HexString(byte[] src) {
			String str = "";
			int v1 = src[0] & 0xFF;
			int v2 = src[1] & 0xFF;
			str = Integer.toHexString(v1) + Integer.toHexString(v2);
			return str;
		}
		//16进制转10进制
		public int hex_16_10(String str){
			if (str==null) {
				return 0;
			}else {
				return Integer.valueOf(str,16);
			}
		}
		//byte to short
		public short toShort(byte[] b) {
		      return (short) (((b[1] << 8) | b[0] & 0xff));
		}
		//16进制转10进制
	/*	public int hex_byte_16_10(byte[] a){
			
		}*/
		public static byte[] hexStringToBytes(String hexString) {  
		    if (hexString == null || hexString.equals("")) {  
		        return null;  
		    }  
		    hexString = hexString.toUpperCase();  
		    int length = hexString.length() / 2;  
		    char[] hexChars = hexString.toCharArray();  
		    byte[] d = new byte[length];  
		    for (int i = 0; i < length; i++) {  
		        int pos = i * 2;  
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		    }  
		    return d;  
		} 
		 private static byte charToByte(char c) {  
			    return (byte) "0123456789ABCDEF".indexOf(c);  
			}


}
