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
	
	public LoginWindow(final JPanel bodyPanel) {
		initializeGUI(bodyPanel,null);
	}
	
	public LoginWindow(final JPanel bodyPanel,String error) {
		initializeGUI(bodyPanel,error);
	}
	
	public void initializeGUI(final JPanel bodyPanel,String error) {
		LoginSession.staffSignal = false;
		
		if (log.isDebugEnabled()) log.debug("staffSignal:"+LoginSession.staffSignal);

		//clean view
		if (bodyPanel!=null && bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		int execWidth = 60;

		x = 340;
		y = 100;
		
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
		
		y += hight;
		
		JButton confirm = new JButton("登录");
		confirm.requestFocus();
//		confirm.addActionListener(new LoginAction(bodyPanel,execPanel,getUser()));
		confirm.setBounds(x+width,y,execWidth,hight);//一个字符9 point
		confirm.setBackground(new Color(105,177,35));
		confirm.setForeground(Color.WHITE);

		confirm.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(log.isDebugEnabled()) log.debug("getName->:"+userNameText.getText());	
					if(log.isDebugEnabled()) log.debug("getPassword->:"+String.valueOf(passwordText.getPassword()));	
					LoginSession.user.setName(userNameText.getText());
					LoginSession.user.setPassword(String.valueOf(passwordText.getPassword()));
					new UserAction(bodyPanel).promptLogin();
				}
			});
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAction());
		
		bodyPanel.add(confirm);

		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();

		if(log.isDebugEnabled()) log.debug("initializeGUI end");
	}
	
	public boolean isOpenNetwork(){
		try{
			Socket socket = new Socket("122.225.88.83", 1433);
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