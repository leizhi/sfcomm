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

public class UserAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7168200530660156025L;
	
	private static Log log = LogFactory.getLog(UserAction.class);
	private static String message;

	private static final String LOGIN_USER="SELECT count(*) FROM User WHERE name=? AND password=?";
	
	private static final String REISTER_USER="INSERT INTO User(id,name,password) VALUES(?,?,?)";

	private static final String EXISTS_USER="SELECT count(*) FROM User WHERE name=?";

	private static final String EXISTS_CARD="SELECT count(*) FROM User WHERE id=?";

	private JPanel bodyPanel;

	public UserAction() {
		
	}
	
	public UserAction(JPanel bodyPanel) {
			this.bodyPanel=bodyPanel;
		}
	
	public void promptLogin(){
		if(processLogin())
			new CardWindow(bodyPanel);
		else
			new LoginWindow(bodyPanel,message); // login again
	}
	
	public boolean processLogin(){
		if(log.isDebugEnabled()) log.debug("processLogin");	

		Connection connection=null;
        PreparedStatement pstmt = null;
        int count=0;
        try {
    		if(log.isDebugEnabled()) log.debug("processLogin getName:"+LoginSession.user.getName());	
    		if(log.isDebugEnabled()) log.debug("processLogin getPassword:"+LoginSession.user.getPassword());	

    		if(StringUtils.isNull(LoginSession.user.getName()))
    			throw new NullPointerException("请输入用户名");
    		
    		if(StringUtils.isNull(LoginSession.user.getPassword()))
    			throw new NullPointerException("请输入密码");
    		
			connection = DbConnectionManager.getConnection();
			pstmt = connection.prepareStatement(EXISTS_USER);
            pstmt.setString(1, LoginSession.user.getName());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
            if(count < 1)
    			throw new NullPointerException("无此用户");
            
            pstmt = connection.prepareStatement(LOGIN_USER);
            pstmt.setString(1, LoginSession.user.getName());
            pstmt.setString(2, StringUtils.hash(LoginSession.user.getPassword()));

            rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
            if(count == 1)
            	return true;
            else
    			throw new NullPointerException("用户和密码不匹配");

		}catch (NullPointerException e) {
			message = e.getMessage();
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
	
	public void processRegister(){
		if(log.isDebugEnabled()) log.debug("processLogin");	

		Connection connection=null;
        PreparedStatement pstmt = null;
        long count=0;
		CardRFID cardRFID = new CardRFID();
        try {
    		if(log.isDebugEnabled()) log.debug("processLogin getName:"+LoginSession.user.getName());	
    		if(log.isDebugEnabled()) log.debug("processLogin getPassword:"+LoginSession.user.getPassword());	

			// 初始化检查
			if (!cardRFID.getIccrf().isOpened())
				throw new NullPointerException("请正确连接发卡器");

			Long cardId = cardRFID.getCardId();
			if (cardId == 0)
				throw new NullPointerException("请放人电子标签或者电子卡");

			LoginSession.user.setId(cardId);
			
    		if(StringUtils.isNull(LoginSession.user.getName()))
    			throw new NullPointerException("请输入用户名");
    		
    		if(StringUtils.isNull(LoginSession.user.getPassword()))
    			throw new NullPointerException("请输入密码");
    		
    		cardRFID.cleanAll();
    		
			cardRFID.saveType(CardAction.CARD_STAFF, 1);//
			
			cardRFID.saveGBK(LoginSession.user.getName(), 1, 1, 16);
			cardRFID.saveGBK(LoginSession.user.getPassword(), 1, 2, 16);

			connection = DbConnectionManager.getConnection();
			connection.setAutoCommit(false);
			
			pstmt = connection.prepareStatement(EXISTS_USER);
            pstmt.setString(1, LoginSession.user.getName());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
    		if(log.isDebugEnabled()) log.debug("count:"+count);	

            if(count > 0)
    			throw new NullPointerException("此用户已注册");
            
            pstmt = connection.prepareStatement(EXISTS_CARD);
            pstmt.setLong(1, LoginSession.user.getId());
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
    		if(log.isDebugEnabled()) log.debug("count:"+count);	

            if(count > 0)
    			throw new NullPointerException("此卡已注册");
            
            pstmt = connection.prepareStatement(REISTER_USER);
            pstmt.setLong(1, LoginSession.user.getId());
            pstmt.setString(2, LoginSession.user.getName());
            pstmt.setString(3, StringUtils.hash(LoginSession.user.getPassword()));
            pstmt.execute();

            connection.commit();
            
            message = "注册成功";
		}catch (NullPointerException e) {
			if(connection != null)
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				message = e.getMessage();
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}catch (SQLException e) {
				if(connection != null)
					try {
						connection.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				message = e.getMessage();
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
			
			cardRFID.beep();
			cardRFID.destroy();
			
			new StaffCardWindow(bodyPanel,message); // foward next View
		}
	}
}