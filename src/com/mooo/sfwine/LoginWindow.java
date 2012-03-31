package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

public class LoginWindow {
	private static Log log = LogFactory.getLog(LoginWindow.class);

	private JLabel disLabel;
	private JTextField userNameText;
	private JPasswordField passwordText;
	
	private User user;
	
	public LoginWindow(final JPanel bodyPanel,final JPanel execPanel) {
		user = new User();
		
		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		if (execPanel.isShowing()) {
			execPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		x = 60;
		y = 0;
		
		width = 80;
		hight = 20;
		width_1 = 120;
		
		x += 10;
		y += 10;
		
		disLabel = new JLabel("用户名:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		userNameText = new JTextField();
		userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(userNameText);
		
		y += hight;
		disLabel = new JLabel("密码:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(passwordText);
		
		y += hight;
		if(isOpenNetwork()){
			disLabel = new JLabel("网络正常");
			disLabel.setForeground(Color.GREEN);
		}else{
			disLabel = new JLabel("网络不通");
			disLabel.setForeground(Color.RED);
		}
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
//		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		//
		JButton confirm = new JButton("登录");
		confirm.requestFocus();
//		confirm.addActionListener(new LoginAction(bodyPanel,execPanel,getUser()));

		confirm.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					getUser();
					new LoginAction(bodyPanel,execPanel,getUser());
				}
			});
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAction());
		
		execPanel.add(confirm);
		
		execPanel.setVisible(true);
		execPanel.validate();//显示
		execPanel.repaint();

		if(log.isDebugEnabled()) log.debug("init end");	}
	
	public LoginWindow(final JPanel bodyPanel,final JPanel execPanel,String error) {
		user = new User();
		
		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		if (execPanel.isShowing()) {
			execPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		x = 60;
		y = 0;
		
		width = 80;
		hight = 20;
		width_1 = 120;
		
		x += 10;
		y += 10;
		
		disLabel = new JLabel("用户名:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		userNameText = new JTextField();
		userNameText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(userNameText);
		
		y += hight;
		disLabel = new JLabel("密码:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(passwordText);
		
		y += hight;
		if(isOpenNetwork()){
			disLabel = new JLabel("网络正常");
			disLabel.setForeground(Color.GREEN);
		}else{
			disLabel = new JLabel("网络不通");
			disLabel.setForeground(Color.RED);
		}
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		if(!StringUtils.isNull(error)){
			y += hight;
			disLabel = new JLabel(error);
			disLabel.setForeground(Color.RED);
			disLabel.setBounds(x,y,width,hight);
			bodyPanel.add(disLabel);
		}
//		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		//
		JButton confirm = new JButton("登录");
		confirm.requestFocus();
		confirm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginAction(bodyPanel,execPanel,getUser());
			}
		});
		
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAction());
		
		execPanel.add(confirm);
		
		execPanel.setVisible(true);
		execPanel.validate();//显示
		execPanel.repaint();

		if(log.isDebugEnabled()) log.debug("init end");

	}
	
	public boolean isOpenNetwork(){
		try{
			Socket socket = new Socket("122.225.88.86", 3306);
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

	public User getUser() {
		if(log.isDebugEnabled()) log.debug("getName->:"+userNameText.getText());	
		if(log.isDebugEnabled()) log.debug("getPassword->:"+String.valueOf(passwordText.getPassword()));	
		
		user.setName(userNameText.getText());
		user.setPassword(String.valueOf(passwordText.getPassword()));
		return user;
	}
}