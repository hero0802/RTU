package org.normal.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.normal.socket.SendData;
import com.normal.socket.TcpClient;

public class RtuStatus implements ServletContextListener{
	private SendData sendData = new SendData();
	protected final Log log = LogFactory.getLog(RtuStatus.class);
	
	public void status(){
		try {
			if (TcpClient.getSocket().isConnected()) {
				sendData.OnOfflineREQ(-1);
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		status();
	}

}

