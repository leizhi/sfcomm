package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

import es.deusto.smartlab.rfid.SerialManager;
import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class CardLoginAction {
	private static Log log = LogFactory.getLog(CardLoginAction.class);

	private JLabel disLabel;
	
	private JLabel messageLabel;

	private JProgressBar progressBar;
	
	private JComboBox whichPort;
	
	private boolean runEnable = false;
	
	private JPanel bodyPanel;
	private String message;

	public CardLoginAction(JPanel bodyPanel) {
		this.bodyPanel = bodyPanel;
	}
	
	public CardLoginAction(JPanel bodyPanel,String message) {
		this.bodyPanel = bodyPanel;
		this.message = message;

	}
	
	public void promptCardLogin() {
		LoginSession.staffSignal = false;

		//clean view
		if (bodyPanel!=null && bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight;

		x = 400;
		y = 250;
		
		width = 90;
		hight = 20;
		
		x += 10;
		y += 10;
		
		String display = "等待请用员刷卡登录";
		progressBar = new JProgressBar(0, 100);// 创建进度条
		progressBar.setIndeterminate(true);// 值为 true 意味着不确定，而值为 false 则意味着普通或者确定。
		progressBar.setStringPainted(true); // 描绘文字
		progressBar.setString(display); // 设置显示文字
		progressBar.setBounds(x, y, display.length()*20, hight);
		bodyPanel.add(progressBar);
		
		y += hight;
		
		disLabel = new JLabel("串口端口:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
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
		JButton login = new JButton("登录/退出");
		login.setBounds(x+width,y,width,hight);
		login.setForeground(Color.WHITE);
		login.setBackground(new Color(105,177,35));
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
		bodyPanel.add(login);
		
		y += hight;
		
		messageLabel= new JLabel();
		
		if(LoginSession.isOpenNetwork()){
			messageLabel.setText("网络正常");
			messageLabel.setForeground(Color.GREEN);
		}else{
			messageLabel.setText("网络不通");
			messageLabel.setForeground(Color.RED);
		}
		messageLabel.setBounds(x,y,200,hight);
		bodyPanel.add(messageLabel);
		
		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		
		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
        ImageIcon img = new ImageIcon(url);
        JLabel background = new JLabel(img);
        bodyPanel.add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	}
	
	 class CardProcessLogin implements Runnable {
		private JLabel messageLabel;
		
		CardProcessLogin(JLabel messageLabel){
			this.messageLabel=messageLabel;
		}
		
		public void run(){
			 Runnable runner = new Runnable() {
				public void run() {
					ISO14443AAction cardRFID = new ISO14443AAction();
					try {
						//初始化
						cardRFID.initialize();
						
						//请正确连接发卡器
						if(!cardRFID.isOpened()){
							throw new NullPointerException("发卡器未连接或者端口选择错误!");
						}
						
						if (log.isDebugEnabled()) log.debug("连接发卡器 okay:");

						//请放人电子标签或者电子卡
						String serialNumber = cardRFID.findSerialNumber();
						if(serialNumber == null){
							if (log.isDebugEnabled()) log.debug("请放人电子标签或者电子卡");
							throw new NullPointerException("请放人电子标签或者电子卡!");
						}
						if (log.isDebugEnabled()) log.debug("卡片 okay:");
						
						int cardType = cardRFID.findCardType();
						if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
						if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
						
						if(cardType!=CommandsISO14443A.CARD_14443A_M1){
							throw new NullPointerException("此卡非标签卡!");
						}
						
						cardRFID.findSerialNumber();
						String userName = cardRFID.read(1, 1);
						if (log.isDebugEnabled()) log.debug("userName:"+userName);
						LoginSession.user.setName(userName.trim());
						
						cardRFID.findSerialNumber();
						String password = cardRFID.read(1, 2);
						if (log.isDebugEnabled()) log.debug("password:"+password);
						LoginSession.user.setPassword(StringUtils.hash(password.trim()));
						
						//check database
						if(LoginSession.isAllow()){
							runEnable = false;
							new CardAction(bodyPanel).promptNewWineCard();
						}else{
							message = "登录失败!";
							messageLabel.setText(message);
							messageLabel.setForeground(Color.RED);
						}
					}catch (NullPointerException e){
							if (log.isErrorEnabled()) log.error("NullPointerException:" + e.getMessage());
	
							message = e.getMessage();
							messageLabel.setText(message);
							messageLabel.setForeground(Color.RED);
							
							e.printStackTrace();
					} catch (Exception e) {
							if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
							message = e.getMessage();
							messageLabel.setText(message);
							messageLabel.setForeground(Color.RED);
							
							e.printStackTrace();
					} finally {
							cardRFID.beep(10);
							cardRFID.destroy();
					}
					if (log.isDebugEnabled()) log.debug("run finlsh!"+LoginSession.staffSignal);
				}
			 };
			

			//do while
			runEnable = true;
			LoginSession.staffSignal = true;
			 if (log.isDebugEnabled()) log.debug("LoginSession.staffSignal:"+LoginSession.staffSignal);

			while(runEnable && LoginSession.staffSignal){
				try {
					message = "开始登录";
					messageLabel.setText(message);
					messageLabel.setForeground(Color.GREEN);
					
					SwingUtilities.invokeAndWait(runner);
					
					 if (log.isDebugEnabled()) log.debug("Lookup:");

					// Our task for each step is to just sleep
					//Sleep 500 ms
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	 }
}