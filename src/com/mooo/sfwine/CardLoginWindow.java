package com.mooo.sfwine;

import java.awt.Color;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

import es.deusto.smartlab.rfid.iccrf.ImplementationIccrf;

public class CardLoginWindow {
	private static Log log = LogFactory.getLog(CardLoginWindow.class);

	private JLabel disLabel;
	private JProgressBar progressBar;
	
	private boolean forever = true;
	
	private JPanel bodyPanel;

	public CardLoginWindow(final JPanel bodyPanel) {
		initializeGUI(bodyPanel,null);
	}
	
	public CardLoginWindow(final JPanel bodyPanel,String error) {
		initializeGUI(bodyPanel,error);
	}
	
	public void initializeGUI(final JPanel bodyPanel,String error) {
		LoginSession.staffSignal = false;
		this.bodyPanel = bodyPanel;

		//clean view
		if (bodyPanel!=null && bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight;

		x = 400;
		y = 250;
		
		width = 80;
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
		
		Thread loopCard = new Thread(new CardLoginAction());
		loopCard.start();
		
		if(log.isDebugEnabled()) log.debug("unLock end");
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
	
	 class CardLoginAction implements Runnable {

		 @Override
		public void run(){
				CardRFID cardRFID = new CardRFID();
				try {
					// 初始化检查
					LoginSession.staffSignal = true;
					
					//do while
					forever = true;
					while(forever){
						if (log.isDebugEnabled()) log.debug("staffSignal:"+LoginSession.staffSignal);

						if(LoginSession.staffSignal==false){
							forever = false;
							continue;
						}
						//请正确连接发卡器
						if(!cardRFID.getIccrf().isOpened()){
							Thread.sleep(100);
							continue;
						}
						
						if (log.isDebugEnabled()) log.debug("连接发卡器 okay:");

						//请放人电子标签或者电子卡
						if(cardRFID.getCardId()==0){
							Thread.sleep(37);
							continue;
						}
						
						if (log.isDebugEnabled()) log.debug("卡片 okay:");

						cardRFID.beep();
						
						byte[] buffer = cardRFID.read(1, 0);

						if (log.isDebugEnabled()) log.debug("falt card:"+buffer[0]);
						ImplementationIccrf.testCommand(buffer);
						
						if (log.isDebugEnabled()) log.debug("falt card:"+CardAction.CARD_STAFF);

						if(buffer[0]!=CardAction.CARD_STAFF){
            				JOptionPane.showMessageDialog(SFWine.frame, "此卡非员工卡");
            				
							Thread.sleep(100);
							continue;
						}
						
						String userName = cardRFID.readGBK(1, 1);
						if (log.isDebugEnabled()) log.debug("userName:"+userName);
						LoginSession.user.setName(userName);
						
						String password = cardRFID.readGBK(1, 2);
						if (log.isDebugEnabled()) log.debug("password:"+password);
						LoginSession.user.setPassword(password);

						if(LoginSession.isAllow())
							forever = false;
//						break;
					}
				} catch (Exception e) {
					if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
					e.printStackTrace();
				} finally {
					cardRFID.beep();
					cardRFID.destroy();
				}
				
				//check database
				if(LoginSession.staffSignal){
					if(LoginSession.allow)
						new CardAction(bodyPanel).promptNewWineCard();
				}
				if (log.isDebugEnabled()) log.debug("run finlsh!"+LoginSession.staffSignal);
			}
		}
}