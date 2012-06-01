package com.mooo.sfwine;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.mooo.mycoz.common.StringUtils;

public class LoginSession {

	public static User user = new User();
	
	public static boolean allow = false;
	
	public static boolean staffSignal = false;

	public static boolean isAllow() {
		UserAction userAction = new UserAction();

		if(!StringUtils.isNull(user.getName()))
			user.setName(user.getName().trim());
		
		if(!StringUtils.isNull(user.getPassword()))
			user.setPassword(user.getPassword().trim());

		allow = userAction.processLogin();
		
		return allow;
	}
	
	public static boolean isOpenNetwork() {
		try{
			Socket socket = new Socket("122.225.88.84", 3306);
			socket.setSoTimeout(50);
			socket.close();
			
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		}catch (BindException e) {
			e.printStackTrace();
		}catch (ConnectException e) {
			e.printStackTrace();
		}catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}