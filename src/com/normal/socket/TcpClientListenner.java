package com.normal.socket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TcpClientListenner implements ServletContextListener{
	private TcpClient tcpClient;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		if (tcpClient.isInterrupted()||tcpClient!=null) {
			tcpClient.interrupt();
		}
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("TCP");
		if (tcpClient==null) {
			tcpClient=new TcpClient();
			tcpClient.start();
		}
	}

}
