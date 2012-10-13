package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class CardLoginAction {
	private static Log log = LogFactory.getLog(CardLoginAction.class);
	
	private final static Object initLock = new Object();

	private JLabel messageLabel;

	private JProgressBar progressBar;
	
	private boolean runEnable = false;
	
	public void promptCardLogin() {
		SFClient.staffSignal = false;

		//clean view
		if (SFWine.global.getBodyPanel()!=null && SFWine.global.getBodyPanel().isShowing()) {
			SFWine.global.getBodyPanel().removeAll();
		}
		
		int x, y,wm,wt,hight;

		x = 400;
		y = 250;
		
		wm = 30;
		wt = 100;
		hight = 20;
		
		x += 10;
		y += 10;
		
		String display = "等待请用员刷卡登录";
		progressBar = new JProgressBar(0, 100);// 创建进度条
		progressBar.setIndeterminate(true);// 值为 true 意味着不确定，而值为 false 则意味着普通或者确定。
		progressBar.setStringPainted(true); // 描绘文字
		progressBar.setString(display); // 设置显示文字
		progressBar.setBounds(x, y, display.length()*20, hight);
		SFWine.global.getBodyPanel().add(progressBar);
		
		y += hight;
		
		messageLabel= new JLabel();
		messageLabel.setBounds(x,y,wt+wm,hight);
		
		if(SFClient.isOpenNetwork()){
			messageLabel.setText("网络正常");
			messageLabel.setForeground(Color.GREEN);
		}else{
			messageLabel.setText("网络不通");
			messageLabel.setForeground(Color.RED);
		}
		SFWine.global.getBodyPanel().add(messageLabel);
		
		y += hight;
		JButton login = new JButton("登录/退出");
		login.setBounds(x,y,wt,hight);
		login.setForeground(Color.WHITE);
		login.setBackground(Color.GREEN);
		login.requestFocus();

		login.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(runEnable){
						runEnable = false;
						messageLabel.setText("停止登录");
						messageLabel.setForeground(Color.GREEN);
					}else{
						new Thread (new CardProcessLogin(messageLabel)).start();
					}
				}
			});
		SFWine.global.getBodyPanel().add(login);
		

		
		SFWine.global.getBodyPanel().setVisible(true);
		SFWine.global.getBodyPanel().validate();//显示
		SFWine.global.getBodyPanel().repaint();
		
		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
        ImageIcon img = new ImageIcon(url);
        JLabel background = new JLabel(img);
        SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	
	 class CardProcessLogin implements Runnable {
		private JLabel messageLabel;
		private ISO14443AAction cardRFID;

		CardProcessLogin(JLabel messageLabel){
			this.messageLabel=messageLabel;
			
			cardRFID = new ISO14443AAction();
			//初始化
			cardRFID.initSerial();
		}
		
		public void run(){
			 Runnable runner = new Runnable() {
				public void run() {
					synchronized (initLock) {
						try {
							cardRFID.initCard();
							
							//请正确连接发卡器
							if(!cardRFID.isOpened()){
								throw new NullPointerException("发卡器未连接或者端口选择错误!");
							}
							
							if (log.isDebugEnabled()) log.debug("连接发卡器 okay:");
	
							//请放人电子标签或者电子卡
							String serialNumber = cardRFID.getSerialNumber();
							if(serialNumber == null){
								if (log.isDebugEnabled()) log.debug("请放人电子标签或者电子卡");
								throw new NullPointerException("请放人电子标签或者电子卡!");
							}
							if (log.isDebugEnabled()) log.debug("卡片 okay:");
							
							int cardType = cardRFID.getCardType();
							if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
							if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
							
							if(cardType!=CommandsISO14443A.CARD_14443A_M1){
								throw new NullPointerException("此卡非标签卡!");
							}
							
							String userName = cardRFID.read(1, 1);
							if (log.isDebugEnabled()) log.debug("userName:"+userName);
							SFClient.user.setName(userName.trim());
							
							String password = cardRFID.read(1, 2);
							if (log.isDebugEnabled()) log.debug("password:"+password);
							SFClient.user.setPassword(password.trim());
							
							//check database
							if(SFClient.isAllow()){
								runEnable = false;
								new CardAction().promptNewWineCard();
							}else{
								Global.message = "登录失败!";
								messageLabel.setText(Global.message);
								messageLabel.setForeground(Color.RED);
							}
						} catch (Exception e) {
								if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
								Global.message = e.getMessage();
								messageLabel.setText(Global.message);
								messageLabel.setForeground(Color.RED);
								
								e.printStackTrace();
						} finally {
							cardRFID.beep(10);
	//						SFWine.global.getMessage() = "finally";
							messageLabel.setText(Global.message);
						}
					}
					if (log.isDebugEnabled()) log.debug("run finlsh!"+SFClient.staffSignal);
				}
			 };
			

			//do while
			runEnable = true;
			SFClient.staffSignal = true;
			 if (log.isDebugEnabled()) log.debug("LoginSession.staffSignal:"+SFClient.staffSignal);

			while(runEnable && SFClient.staffSignal){
				try {
					Global.message = "开始登录";
					messageLabel.setText(Global.message);
					messageLabel.setForeground(Color.GREEN);
					
					SwingUtilities.invokeAndWait(runner);
					
					 if (log.isDebugEnabled()) log.debug("Lookup:");

					// Our task for each step is to just sleep
					//Sleep 2400 ms
					Thread.sleep(2400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			cardRFID.beep(10);
			cardRFID.destroy();
		}
	 }
}