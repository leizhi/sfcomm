package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.swing.LimitDocument;

import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class UserAction {
	
	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(UserAction.class);
	
	private JLabel disLabel;
	private JLabel messageLabel;

	private JTextField userNameText;
	private JPasswordField passwordText;
	private JTextField mobileText;

	public void promptLogin() {
		SFClient.staffSignal = false;
		
		if (log.isDebugEnabled()) log.debug("staffSignal:"+SFClient.staffSignal);

		//clean view
		if (SFWine.global.getBodyPanel()!=null && SFWine.global.getBodyPanel().isShowing()) {
			SFWine.global.getBodyPanel().removeAll();
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
		Document hostNameDoc = hostName.getDocument();
		hostNameDoc.addDocumentListener(new javax.swing.event.DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				SFClient.host = hostName.getText();
				SFClient.start();
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				SFClient.host = hostName.getText();
				SFClient.start();
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				SFClient.host = hostName.getText();
				SFClient.start();
			}
		});
        */
		
		disLabel = new JLabel("用户名:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		SFWine.global.getBodyPanel().add(disLabel);
		
		userNameText = new JTextField();
		userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		SFWine.global.getBodyPanel().add(userNameText);
		
		y += hight;
		disLabel = new JLabel("密码:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		SFWine.global.getBodyPanel().add(disLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		SFWine.global.getBodyPanel().add(passwordText);
		
		y += hight;
		
		messageLabel= new JLabel();
		messageLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(messageLabel);
		
		y += hight;
		JButton confirm = new JButton("登录");
		confirm.requestFocus();
		confirm.setBounds(x+width,y,execWidth,hight);//一个字符9 point
		confirm.setBackground(new Color(105,177,35));
		confirm.setForeground(Color.WHITE);
		
		confirm.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(log.isDebugEnabled()) log.debug("getName->:"+userNameText.getText());	
					if(log.isDebugEnabled()) log.debug("getPassword->:"+String.valueOf(passwordText.getPassword()));	
					SFClient.user.setName(userNameText.getText());
					SFClient.user.setPassword(String.valueOf(passwordText.getPassword()));
					
					processLogin();
				}
			});
		
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(log.isDebugEnabled()) log.debug("getName->:"+userNameText.getText());	
					if(log.isDebugEnabled()) log.debug("getPassword->:"+String.valueOf(passwordText.getPassword()));	
					SFClient.user.setName(userNameText.getText());
					SFClient.user.setPassword(String.valueOf(passwordText.getPassword()));
					
					processLogin();
		        }
			}
		});
		
		SFWine.global.getBodyPanel().add(confirm);

		SFWine.global.getBodyPanel().setVisible(true);
		SFWine.global.getBodyPanel().validate();//显示
		SFWine.global.getBodyPanel().repaint();
		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
		ImageIcon img = new ImageIcon(url);
		JLabel background = new JLabel(img);
		SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
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
    		SFWine.global.getCardAction().promptNewWineCard();
		}catch (Exception e) {
			Global.message = e.getMessage();
			messageLabel.setText(Global.message);
			messageLabel.setForeground(Color.RED);
			
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}
	}
	
	public void promptRegister() {
		SFClient.staffSignal = false;

		//clean view
		if (SFWine.global.getBodyPanel()!=null && SFWine.global.getBodyPanel().isShowing()) {
			SFWine.global.getBodyPanel().removeAll();
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
		SFWine.global.getBodyPanel().add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("用户名:");
		disLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		 
		userNameText = new JTextField();
		userNameText.setDocument(new LimitDocument(8));
		userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		SFWine.global.getBodyPanel().add(userNameText);
		
		display = " * 8位中文";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,20*display.length(),hight);
		disLabel.setForeground(Color.RED);
		SFWine.global.getBodyPanel().add(disLabel);
		
		y += hight;
		disLabel = new JLabel("密码:");
		disLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(x+width,y,width_1,hight);//最大8位密码
		SFWine.global.getBodyPanel().add(passwordText);
		
		display = " * 8位数字字母组合";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,20*display.length(),hight);
		disLabel.setForeground(Color.RED);
		SFWine.global.getBodyPanel().add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("手机号码:");
		disLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		
		mobileText = new JTextField();
		mobileText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		SFWine.global.getBodyPanel().add(mobileText);
		
		y += hight;
		
		messageLabel= new JLabel();
		if(SFClient.isOpenNetwork()){
			Global.message="网络正常";
			messageLabel.setText(Global.message);
			messageLabel.setForeground(Color.GREEN);
		}else{
			Global.message="网络不通";
			messageLabel.setText(Global.message);
			messageLabel.setForeground(Color.RED);
		}
		messageLabel.setBounds(x,y,200,hight);
		SFWine.global.getBodyPanel().add(messageLabel);
		
		y += hight;
		
		JButton confirm = new JButton("注册");
		confirm.requestFocus();
		confirm.setBounds(x+width,y,execWidth,hight);
		confirm.isDefaultButton();

		confirm.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveUser();
				}
			});
		
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        }
			}
		});
		
		SFWine.global.getBodyPanel().add(confirm);

		SFWine.global.getBodyPanel().setVisible(true);
		SFWine.global.getBodyPanel().validate();//显示
		SFWine.global.getBodyPanel().repaint();

		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
        ImageIcon img = new ImageIcon(url);
        JLabel background = new JLabel(img);
        SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error("Exception:"+e.getMessage());
//			promptLogin();
			Global.message = e.getMessage();
			messageLabel.setText(Global.message);
		}
	}
	
	public void saveUser(){
		ISO14443AAction cardRFID = new ISO14443AAction();
		//初始化
		cardRFID.initSerial();
		if(!SFClient.isOpenNetwork()) SFClient.connect();

		try {
	    		if (log.isDebugEnabled()) log.debug(ISO14443AAction.whichPort +"/" + ISO14443AAction.whichSpeed);

				String userName = String.valueOf(userNameText.getText());
				String password = String.valueOf(passwordText.getPassword());
				String mobile = String.valueOf(mobileText.getText());

				StringUtils.notEmpty(userName);
	    		StringUtils.notEmpty(password);

	    		cardRFID.initCard();
				// 初始化检查
				if (!cardRFID.isOpened())
					throw new NullPointerException("请正确连接发卡器");
				
				String serialNumber = cardRFID.getSerialNumber();
				
				if (serialNumber == null)
					throw new NullPointerException("请放人电子标签或者电子卡");
				
				int cardType = cardRFID.getCardType();
				if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
				if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
				
				if(cardType!=CommandsISO14443A.CARD_14443A_M1)
					throw new NullPointerException("此卡非员工卡");
				
	    		if(StringUtils.isNull(userName))
	    			throw new NullPointerException("请输入用户名");
	    		
	    		if(StringUtils.isNull(password))
	    			throw new NullPointerException("请输入密码");
	    		
	    		cardRFID.cleanAll();
	    		cardRFID.save(userName, 1, 1, 16);
				cardRFID.save(password, 1, 2, 16);
				
	    		String uuid = StringUtils.hash(serialNumber);
				SFClient.saveUser(userName, password, uuid,mobile);

	            Global.message = "注册成功";
				messageLabel.setText(Global.message);
		 }catch (Exception e) {
				Global.message = "Exception:"+e.getMessage();
				messageLabel.setText(Global.message);
				messageLabel.setForeground(Color.RED);
				if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}finally {
				cardRFID.beep(10);
				cardRFID.destroy();
		}
	}
	
	public void promptRestStaff() {
		try {
//			if(!SFClient.isAllow()) throw new Exception("请先登录");
	
			//clean view
			if (SFWine.global.getBodyPanel()!=null && SFWine.global.getBodyPanel().isShowing()) {
				SFWine.global.getBodyPanel().removeAll();
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
			SFWine.global.getBodyPanel().add(disLabel);
			
			userNameText = new JTextField();
			userNameText.setDocument(new LimitDocument(8));
			userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
			SFWine.global.getBodyPanel().add(userNameText);
			
			y += hight;
			disLabel = new JLabel("密码:");
			disLabel.setBounds(x,y,width,hight);
			disLabel.setForeground(Color.WHITE);
			SFWine.global.getBodyPanel().add(disLabel);
			
			passwordText = new JPasswordField();
			passwordText.setBounds(x+width,y,width_1,hight);//一个字符9 point
			SFWine.global.getBodyPanel().add(passwordText);
			
			y += hight;
			
			messageLabel= new JLabel();
			if(SFClient.isOpenNetwork()){
				Global.message="网络正常";
				messageLabel.setText(Global.message);
				messageLabel.setForeground(Color.GREEN);
			}else{
				Global.message="网络不通";
				messageLabel.setText(Global.message);
				messageLabel.setForeground(Color.RED);
			}
			messageLabel.setBounds(x,y,200,hight);
			SFWine.global.getBodyPanel().add(messageLabel);
			
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
			confirm.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
			        if(e.getKeyCode() == KeyEvent.VK_ENTER){
			        }
				}
			});
			
			SFWine.global.getBodyPanel().add(confirm);
	
			SFWine.global.getBodyPanel().setVisible(true);
			SFWine.global.getBodyPanel().validate();//显示
			SFWine.global.getBodyPanel().repaint();
			//设置背景图片
			URL url = SFWine.class.getResource("bg.png");
			ImageIcon img = new ImageIcon(url);
			JLabel background = new JLabel(img);
			SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
			background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
			if(log.isDebugEnabled()) log.debug("initializeGUI end");
			
		}catch (Exception e) {
			Global.message = e.getMessage();
			messageLabel.setText(Global.message);
			messageLabel.setForeground(Color.RED);
			if(log.isErrorEnabled()) log.error("Exception:"+e.getMessage());	
		}
	}
	
	public void processRestStaff(){
		ISO14443AAction cardRFID = new ISO14443AAction();
		cardRFID.initSerial();
		 try {
	    		if (log.isDebugEnabled()) log.debug(ISO14443AAction.whichPort +"/" + ISO14443AAction.whichSpeed);

				String userName = String.valueOf(userNameText.getText());
				String password = String.valueOf(passwordText.getPassword());
	    		
				StringUtils.notEmpty(userName);
	    		StringUtils.notEmpty(password);

	    		cardRFID.initCard();
				// 初始化检查
				if (!cardRFID.isOpened())
					throw new NullPointerException("请正确连接发卡器");
				
				String serialNumber = cardRFID.getSerialNumber();
				
				if (serialNumber == null)
					throw new NullPointerException("请放人电子标签或者电子卡");
				
				int cardType = cardRFID.getCardType();
				if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
				if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
				
				if(cardType!=CommandsISO14443A.CARD_14443A_M1)
					throw new NullPointerException("此卡非员工卡");
				
	    		if(StringUtils.isNull(userName))
	    			throw new NullPointerException("请输入用户名");
	    		
	    		if(StringUtils.isNull(password))
	    			throw new NullPointerException("请输入密码");
	    		
	    		cardRFID.cleanAll();
	    		cardRFID.save(userName, 1, 1, 16);
				cardRFID.save(password, 1, 2, 16);
				
				Global.message = "重置完成!";
				messageLabel.setText(Global.message);
				messageLabel.setForeground(Color.GRAY);
		 }catch (Exception e) {
				Global.message = "Exception:"+e.getMessage();
				messageLabel.setText(Global.message);
				messageLabel.setForeground(Color.RED);
				if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
		}finally {
				cardRFID.beep(10);
				cardRFID.destroy();
		}
	}
}