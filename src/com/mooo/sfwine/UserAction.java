package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//import javax.swing.event.DocumentEvent;
//import javax.swing.text.Document;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

import es.deusto.smartlab.rfid.SerialManager;
import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class UserAction {
	
	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(UserAction.class);
	
	private JLabel disLabel;
	private JLabel messageLabel;

//	private JTextField hostName;
//	private JTextField hostPort;

	private JTextField userNameText;
	private JPasswordField passwordText;
//	private JComboBox branch;
	private JComboBox whichPort;
	
	private JPanel bodyPanel;
	private String message;

	public UserAction(JPanel bodyPanel) {
			this.bodyPanel=bodyPanel;
		}
	
	public void promptLogin() {
		SFClient.staffSignal = false;
		
		if (log.isDebugEnabled()) log.debug("staffSignal:"+SFClient.staffSignal);

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
		/*
		disLabel = new JLabel("服务器:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		hostName = new JTextField();
		hostName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		hostName.setText("192.168.1.7");
		bodyPanel.add(hostName);
		
		Document hostNameDoc = hostName.getDocument();
		hostNameDoc.addDocumentListener(new javax.swing.event.DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				SFClient.host = hostName.getText();
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				SFClient.host = hostName.getText();
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				SFClient.host = hostName.getText();
			}
		});
        
		disLabel = new JLabel("端口:");
		disLabel.setBounds(x+width+width_1,y,40,hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		hostPort = new JTextField();
		hostPort.setBounds(x+width+width_1+40,y,40,hight);//一个字符9 point
		hostPort.setText("8000");
		bodyPanel.add(hostPort);
		
		Document hostPortDoc = hostPort.getDocument();
		hostPortDoc.addDocumentListener(new javax.swing.event.DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				SFClient.port = new Integer(hostPort.getText());
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				SFClient.port = new Integer(hostPort.getText());
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				SFClient.port = new Integer(hostPort.getText());
			}
		});
		
		y += hight;
		*/
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
		messageLabel= new JLabel();
		if(SFClient.isOpenNetwork()){
			messageLabel.setText("网络正常");
			messageLabel.setForeground(Color.GREEN);
		}else{
			messageLabel.setText("网络不通");
			messageLabel.setForeground(Color.RED);
		}
		messageLabel.setBounds(x,y,width,hight);
		bodyPanel.add(messageLabel);
		
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
					SFClient.user.setName(userNameText.getText());
					SFClient.user.setPassword(String.valueOf(passwordText.getPassword()));
					
					processLogin();
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

	public void processLogin(){
		if(log.isDebugEnabled()) log.debug("processLogin");	
        try {
    		if(log.isDebugEnabled()) log.debug("processLogin getName:"+SFClient.user.getName());	
    		if(log.isDebugEnabled()) log.debug("processLogin getPassword:"+SFClient.user.getPassword());	

    		if(StringUtils.isNull(SFClient.user.getName()))
    			throw new NullPointerException("请输入用户名");
    		
    		if(StringUtils.isNull(SFClient.user.getPassword()))
    			throw new NullPointerException("请输入密码");
    		
    		if(!SFClient.isAllow())
    			throw new NullPointerException("登录失败!");
            
    		//成功登录
        	new CardAction(bodyPanel).promptNewWineCard();
		}catch (Exception e) {
			message = e.getMessage();
			messageLabel.setText(message);
			messageLabel.setForeground(Color.RED);
			
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}
	}
	
	public void promptRegister() {
		SFClient.staffSignal = false;

		//clean view
		if (bodyPanel!=null && bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		try {
		//isAllow
		if(!SFClient.isAllow())
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
		
		String display = "操作员:"+SFClient.user.getName();
		
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
		
		messageLabel= new JLabel();
		if(SFClient.isOpenNetwork()){
			message="网络正常";
			messageLabel.setText(message);
			messageLabel.setForeground(Color.GREEN);
		}else{
			message="网络不通";
			messageLabel.setText(message);
			messageLabel.setForeground(Color.RED);
		}
		messageLabel.setBounds(x,y,200,hight);
		bodyPanel.add(messageLabel);
		
		y += hight;
		
		JButton confirm = new JButton("注册");
		confirm.requestFocus();
		confirm.setBounds(x+width,y,execWidth,hight);
		confirm.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
//			promptLogin();
			message = e.getMessage();
			messageLabel.setText(message);
		}
	}
	
	public void processRegister(){
		if(log.isDebugEnabled()) log.debug("processRegister");	

        long count=0;
		ISO14443AAction cardRFID = new ISO14443AAction();
        try {
    		cardRFID.initialize();
    		
    		if(log.isDebugEnabled()) log.debug("processLogin getName:"+userNameText.getText());	
    		if(log.isDebugEnabled()) log.debug("processLogin getPassword:"+String.valueOf(passwordText.getPassword()));	
    		
    		StringUtils.notEmpty(userNameText.getText());
    		StringUtils.notEmpty(String.valueOf(passwordText.getPassword()));
    		
			// 初始化检查
			if (!cardRFID.isOpened())
				throw new NullPointerException("请正确连接发卡器");

			String serialNumber = cardRFID.findSerialNumber();
			
			if (serialNumber == null)
				throw new NullPointerException("请放人电子标签或者电子卡");
			
			int cardType = cardRFID.request();
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

            
    		if(log.isDebugEnabled()) log.debug("count:"+count);	

            if(count > 0)
    			throw new NullPointerException("此用户已注册");
            
    		if(log.isDebugEnabled()) log.debug("count:"+count);	

            if(count > 0)
    			throw new NullPointerException("此卡已注册");
            
            message = "注册成功";
			messageLabel.setText(message);

		}catch (Exception e) {
				message = e.getMessage();
				messageLabel.setText(message);
			if(log.isErrorEnabled()) log.error("SQLException:"+e.getMessage());	
		}finally {
			cardRFID.beep(10);
			cardRFID.destroy();
		}
	}
	
	public void promptRestStaff() {
		SFClient.staffSignal = false;
		
		if (log.isDebugEnabled()) log.debug("staffSignal:"+SFClient.staffSignal);

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
		messageLabel= new JLabel();
		if(SFClient.isOpenNetwork()){
			messageLabel.setText("网络正常");
			messageLabel.setForeground(Color.GREEN);
		}else{
			messageLabel.setText("网络不通");
			messageLabel.setForeground(Color.RED);
		}
		messageLabel.setBounds(x,y,260,hight);
		bodyPanel.add(messageLabel);
		
		y += hight;
		
		JButton confirm = new JButton("确认");
		confirm.requestFocus();
		confirm.setBounds(x+width,y,execWidth,hight);//一个字符9 point
		confirm.setBackground(new Color(105,177,35));
		confirm.setForeground(Color.WHITE);

		confirm.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					processRestStaff();
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
	
	public void processRestStaff(){
		ISO14443AAction cardRFID = new ISO14443AAction();
		 try {
	    		cardRFID.initialize();
				
	    		if (log.isDebugEnabled()) log.debug(ISO14443AAction.whichPort +"/" + ISO14443AAction.whichSpeed);

	    		StringUtils.notEmpty(userNameText.getText());
	    		StringUtils.notEmpty(String.valueOf(passwordText.getPassword()));
	    		
				// 初始化检查
				if (!cardRFID.isOpened())
					throw new NullPointerException("请正确连接发卡器");
				
				String serialNumber = cardRFID.findSerialNumber();
				
				if (serialNumber == null)
					throw new NullPointerException("请放人电子标签或者电子卡");
				
				int cardType = cardRFID.request();
				if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
				if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
				
				if(cardType!=CommandsISO14443A.CARD_14443A_M1)
					throw new NullPointerException("此卡非员工卡");
				
	    		if(StringUtils.isNull(String.valueOf(userNameText.getText())))
	    			throw new NullPointerException("请输入用户名");
	    		
	    		if(StringUtils.isNull(String.valueOf(passwordText.getPassword())))
	    			throw new NullPointerException("请输入密码");
	    		
	    		cardRFID.cleanAll();
	    		
	    		cardRFID.saveM1(String.valueOf(userNameText.getText()), 1, 1, 16);
				cardRFID.saveM1(String.valueOf(passwordText.getPassword()), 1, 2, 16);
				
				message = "重置完成!";
				messageLabel.setText(message);
				messageLabel.setForeground(Color.GRAY);
		 }catch (Exception e) {
				message = e.getMessage();
				messageLabel.setText(message);
				messageLabel.setForeground(Color.RED);
				if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}finally {
			cardRFID.beep(10);
			cardRFID.destroy();
		}
	}
}