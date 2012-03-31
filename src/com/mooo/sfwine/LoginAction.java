package com.mooo.sfwine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.mycoz.db.pool.DbConnectionManager;

public class LoginAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7168200530660156025L;
	
	private static Log log = LogFactory.getLog(LoginAction.class);
	private static String error;

	private static final String LOGIN="SELECT count(*) FROM User WHERE name=? AND password=?";
	private static final String EXISTS_USER="SELECT count(*) FROM User WHERE name=?";

	public LoginAction(JPanel bodyPanel,JPanel execPanel,User user) {
			
			if(processLogin(user))
				new CardWindow(bodyPanel,execPanel);
			else
				new LoginWindow(bodyPanel,execPanel,error); // login again
		}

	public boolean processLogin(User user){
		if(log.isDebugEnabled()) log.debug("processLogin");	

		Connection connection=null;
        PreparedStatement pstmt = null;
        int count=0;
        try {
    		if(log.isDebugEnabled()) log.debug("processLogin getName:"+user.getName());	
    		if(log.isDebugEnabled()) log.debug("processLogin getPassword:"+user.getPassword());	

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
            
            pstmt = connection.prepareStatement(LOGIN);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, StringUtils.hash(user.getPassword()));

            rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
            if(count == 1)
            	return true;
            else
    			throw new NullPointerException("用户和密码不匹配");

		}catch (NullPointerException e) {
			error = e.getMessage();
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}catch (SQLException e) {
			if(log.isErrorEnabled()) log.error("SQLException:"+e.getMessage());	
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
		return false;
	}
	
}