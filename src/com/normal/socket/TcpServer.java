package com.normal.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {  
    public static void main(String[] args) throws Exception {  
       /* if (args.length == 0) {  
            System.out.println("未输入端口号，格式：java TcoServer 端口号");  
            return;  
        } */ 
        System.out.println("服务器已开启，等待客户机响应。。。");  
        ServerSocket serverSocket = new ServerSocket(6666);  
        while (true) {  
            Socket socket = serverSocket.accept();  
            System.out.println(socket.getInetAddress().getHostAddress() + "："  
                    + socket.getPort() + " 已连接！");  
            new Thread(new Send(socket)) {  
            }.start();  
        }  
    }  
}  
// 发送数据  
class Send implements Runnable {  
    public Send(Socket socket) {  
        this.socket = socket;  
    }  
    private Socket socket;  
    private InputStream in;  
    private OutputStream out;  
    private BufferedReader br;  
    private BufferedWriter bw;  
    public void run() {  
        try {  
            in = socket.getInputStream();  
            out = socket.getOutputStream();  
            br = new BufferedReader(new InputStreamReader(in));  
            bw = new BufferedWriter(new OutputStreamWriter(out));  
            while (true) {  
                String msg = br.readLine();  
                if ("exit".equals(msg)) {  
                    System.out.println(socket.getInetAddress().getHostAddress()  
                            + "：" + socket.getPort() + "已断开！");  
                    break;  
                }  
                StringBuffer sb = new StringBuffer(msg);  
                msg = sb.reverse().toString();  
                bw.write("服务器响应：-->" + msg + "/r/n");  
                bw.flush();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        try {  
            br.close();  
            bw.close();  
            socket.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  