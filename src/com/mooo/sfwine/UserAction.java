package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.mycoz.db.pool.DbConnectionManager;

import es.deusto.smartlab.rfid.SerialManager;
import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class UserAction {
	
	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(UserAction.class);
	

	private static final String ADD_USER_INFO="INSERT INTO UserInfo(id,uuid) VALUES(?,?)";

	private static final String ADD_USER="INSERT INTO User(id,name,password,userInfoId) VALUES(?,?,?,?)";

	private static final String EXISTS_USER="SELECT count(*) FROM User WHERE name=?";

	private static final String EXISTS_CARD="SELECT count(*) FROM UserInfo WHERE uuid=?";

	private static final String LOGIN="SELECT id  FROM  User where   name=? AND password=?";
	
	private JLabel disLabel;
	private JTextField userNameText;
	private JPasswordField passwordText;
	private JComboBox whichPort;
	
	private JPanel bodyPanel;
	private String message;

	public UserAction() {
		
	}
	
	public UserAction(JPanel bodyPanel) {
			this.bodyPanel=bodyPanel;
		}
	
	public void promptLogin() {
		LoginSession.staffSignal = false;
		
		if (log.isDebugEnabled()) log.debug("staffSignal:"+LoginSession.staffSignal);

		//clean view
		if (bodyPanel!=null && bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		int execWidth = 60;

		x = 340;
		y = 250;
		
		width = 80;
		hight = 20;
		width_1 = 120;
		
		x += 10;
		y += 10;
		
		disLabel = new JLabel("用户名:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		userNameText = new JTextField();
		userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(userNameText);
		
		y += hight;
		disLabel = new JLabel("密码:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(passwordText);
		
		y += hight;
		whichPort = new JComboBox();
		whichPort.setBounds(x+width,y,width,hight);//一个字符9 point
		whichPort.setSelectedItem(whichPort.getSelectedItem());
		
		List<String> ports = new SerialManager().getPorts();
		for(String value:ports){
			whichPort.addItem(value);
		}
		bodyPanel.add(whichPort);
		
		whichPort.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				ISO14443AAction.whichPort=e.getItem().toString();
			}
		});
		
		y += hight;
		if(LoginSession.isOpenNetwork()){
			disLabel = new JLabel("网络正常");
			disLabel.setForeground(Color.GREEN);
		}else{
			disLabel = new JLabel("网络不通");
			disLabel.setForeground(Color.RED);
		}
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		if(!StringUtils.isNull(message)){
			y += hight;
			disLabel = new JLabel(message);
			disLabel.setForeground(Color.RED);
			disLabel.setBounds(x,y,width,hight);
			bodyPanel.add(disLabel);
		}
		
		y += hight;
		
		JButton confirm = new JButton("登录");
		confirm.requestFocus();
//		confirm.addActionListener(new LoginAction(bodyPanel,execPanel,getUser()));
		confirm.setBounds(x+width,y,execWidth,hight);//一个字符9 point
		confirm.setBackground(new Color(105,177,35));
		confirm.setForeground(Color.WHITE);

		confirm.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(log.isDebugEnabled()) log.debug("getName->:"+userNameText.getText());	
					if(log.isDebugEnabled()) log.debug("getPassword->:"+String.valueOf(passwordText.getPassword()));	
					LoginSession.user.setName(userNameText.getText());
					LoginSession.user.setPassword(String.valueOf(passwordText.getPassword()));
					if(processLogin()){
						new CardAction(bodyPanel).promptNewWineCard();
					}else{
						message = "请再次登录.";
						promptLogin();
					}
				}
			});
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAction());
		
		bodyPanel.add(confirm);

		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
		ImageIcon img = new ImageIcon(url);
		JLabel background = new JLabel(img);
		bodyPanel.add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
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
            
            count=0;
            
            pstmt = connection.prepareStatement(LOGIN);
            pstmt.setString(1, LoginSession.user.getName());
            pstmt.setString(2, StringUtils.hash(LoginSession.user.getPassword()));

            rs = pstmt.executeQuery();
            while (rs.next()) {
            	LoginSession.user.setId(rs.getInt(1));
            	
            	count=1;
            }
            
    		if (log.isDebugEnabled()) log.debug("count:"+count);

            if(count == 1){
            	LoginSession.allow = true;
            	return true;
            }else{
    			throw new NullPointerException("用户和密码不匹配");
            }
		}catch (NullPointerException e) {
			message = e.getMessage();
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}catch (SQLException e) {
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
			
		}
		return false;
	}
	
	public void promptRegister() {
		LoginSession.staffSignal = false;

		//clean view
		if (bodyPanel!=null && bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		try {
		//isAllow
		if(!LoginSession.isAllow())
			throw new Exception("请先登录!");
			
		int x, y,width,hight,width_1;
		int execWidth = 60;

		x = 340;
		y = 240;
		
		width = 80;
		hight = 20;
		width_1 = 120;
		
		x += 10;
		y += 10;
		
		String display = "操作员:"+LoginSession.user.getName();
		
		disLabel = new JLabel(display);
		disLabel.setBounds(x,y,20*display.length(),hight);
		bodyPanel.add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("用户名:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		 
		userNameText = new JTextField();
		userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(userNameText);
		
		display = " * 4位中文";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,20*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;
		disLabel = new JLabel("密码:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(x+width,y,width_1,hight);//最大8位密码
		bodyPanel.add(passwordText);
		
		display = " * 8位数字字母组合";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,20*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;
		
		if(LoginSession.isOpenNetwork()){
			disLabel = new JLabel("网络正常");
			disLabel.setForeground(Color.GREEN);
		}else{
			disLabel = new JLabel("网络不通");
			disLabel.setForeground(Color.RED);
		}
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		if(!StringUtils.isNull(message)){
			y += hight;
			disLabel = new JLabel(message);
			disLabel.setForeground(Color.RED);
			disLabel.setBounds(x,y,20*message.length(),hight);
			bodyPanel.add(disLabel);
		}
		
		y += hight;
		
		JButton confirm = new JButton("注册");
		confirm.requestFocus();
		confirm.setBounds(x+width,y,execWidth,hight);
		confirm.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(log.isDebugEnabled()) log.debug("getName->:"+userNameText.getText());	
					if(log.isDebugEnabled()) log.debug("getPassword->:"+String.valueOf(passwordText.getPassword()));	
					
//					LoginSession.user.setName(userNameText.getText());
//					LoginSession.user.setPassword(String.valueOf(passwordText.getPassword()));
					
					processRegister();
				}
			});
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAction());
		
		bodyPanel.add(confirm);

		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();

		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
        ImageIcon img = new ImageIcon(url);
        JLabel background = new JLabel(img);
        bodyPanel.add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error("Exception:"+e.getMessage());
			
			promptLogin();
		}
	}
	
	public void processRegister(){
		if(log.isDebugEnabled()) log.debug("processLogin");	

		Connection connection=null;
        PreparedStatement pstmt = null;
        long count=0;
		ISO14443AAction cardRFID = new ISO14443AAction();
        try {
    		cardRFID.initialize();
    		
    		if(log.isDebugEnabled()) log.debug("processLogin getName:"+userNameText.getText());	
    		if(log.isDebugEnabled()) log.debug("processLogin getPassword:"+String.valueOf(passwordText.getPassword()));	
    		
			// 初始化检查
			if (!cardRFID.isOpened())
				throw new NullPointerException("请正确连接发卡器");

			String serialNumber = cardRFID.findSerialNumber();
			
			if (serialNumber == null)
				throw new NullPointerException("请放人电子标签或者电子卡");
			
			int cardType = cardRFID.findCardType();
			if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
			if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
			
			if(cardType!=CommandsISO14443A.CARD_14443A_M1)
				throw new NullPointerException("此卡非员工卡");
			
    		if(StringUtils.isNull(String.valueOf(userNameText.getText())))
    			throw new NullPointerException("请输入用户名");
    		
    		if(StringUtils.isNull(String.valueOf(passwordText.getPassword())))
    			throw new NullPointerException("请输入密码");
    		
    		cardRFID.cleanAll();
    		
			if (log.isDebugEnabled()) log.debug("userNameText:"+String.valueOf(userNameText.getText()));
			if (log.isDebugEnabled()) log.debug("passwordText:"+String.valueOf(passwordText.getPassword()));

			cardRFID.saveM1(String.valueOf(userNameText.getText()), 1, 1, 16);
			cardRFID.saveM1(String.valueOf(passwordText.getPassword()), 1, 2, 16);

			connection = DbConnectionManager.getConnection();
			connection.setAutoCommit(false);
			
			pstmt = connection.prepareStatement(EXISTS_USER);
            pstmt.setString(1, String.valueOf(userNameText.getText()));
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
    		if(log.isDebugEnabled()) log.debug("count:"+count);	

            if(count > 0)
    			throw new NullPointerException("此用户已注册");
            
            pstmt = connection.prepareStatement(EXISTS_CARD);
            pstmt.setString(1, serialNumber);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	count = rs.getInt(1);
            }
            
    		if(log.isDebugEnabled()) log.debug("count:"+count);	

            if(count > 0)
    			throw new NullPointerException("此卡已注册");
            
            
            
            pstmt = connection.prepareStatement(ADD_USER_INFO);
            int userInfoId = IDGenerator.getNextID(connection,"UserInfo");
            pstmt.setLong(1, userInfoId);
            pstmt.setString(2, StringUtils.hash(serialNumber));
            pstmt.execute();
            
            pstmt = connection.prepareStatement(ADD_USER);
            pstmt.setLong(1, IDGenerator.getNextID(connection,"UserInfo"));
            pstmt.setString(2, String.valueOf(userNameText.getText()));
            pstmt.setString(3, StringUtils.hash(String.valueOf(passwordText.getPassword())));
            pstmt.setInt(4, userInfoId);
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
		}catch (Exception e) {
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
			
			cardRFID.beep(10);
			cardRFID.destroy();
		}
        promptRegister();
	}
}