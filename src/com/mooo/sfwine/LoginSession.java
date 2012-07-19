package com.mooo.sfwine;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.mycoz.db.pool.DbConnectionManager;

public class LoginSession {

	private static final String EXISTS_USER="SELECT count(*) FROM User WHERE name=?";

	private static final String LOGIN="SELECT id  FROM  User where   name=? AND password=?";
	
	public static User user = new User();
	
	public static boolean allow = false;
	
	public static boolean staffSignal = false;

	public static boolean isAllow() {

		Connection connection=null;
        PreparedStatement pstmt = null;
        int count=0;
        try {

    		if(StringUtils.isNull(user.getName()))
    			throw new NullPointerException("请输入用户名");
    		
    		if(StringUtils.isNull(user.getPassword()))
    			throw new NullPointerException("请输入密码");
    		
			connection = DbConnectionManager.getConnection();
			pstmt = connection.prepareStatement(EXISTS_USER);
            pstmt.setString(1, user.getName());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
            if(count < 1)
    			throw new NullPointerException("无此用户");
            
            count=0;
            
            pstmt = connection.prepareStatement(LOGIN);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, StringUtils.hash(user.getPassword()));

            rs = pstmt.executeQuery();
            while (rs.next()) {
            	user.setId(rs.getInt(1));
            	
            	count=1;
            }
            
            if(count < 1)
    			throw new NullPointerException("用户和密码不匹配");
            
            //成功登录
            LoginSession.allow = true;
        	
		}catch (Exception e) {
			e.printStackTrace();
	   }finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
        
		if(allow == true)
			staffSignal=false;
		
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