package com.mooo.sfwine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.swing.LimitDocument;

import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class CardAction {
	private static Log log = LogFactory.getLog(CardAction.class);

	private boolean forever = false;
	
	private Color fg = Color.WHITE;
	
	private Color bg = Color.GREEN;

	private JLabel disLabel;
	
	private JTextField zipCodeText;
	private JComboBox cardType;
	
	private Card card;

	private JPanel bodyPanel;
	private String message;
	
	private Thread loopCard;

	public CardAction(JPanel bodyPanel) {
		this.bodyPanel = bodyPanel;
		this.message = null;
		
		loopCard = new Thread(new CardProcessRegister());
	}
	
	public CardAction(JPanel bodyPanel,String message) {
		this.bodyPanel = bodyPanel;
		this.message = message;
		
		loopCard = new Thread(new CardProcessRegister());
	}
	
	public void promptNewWineCard() {
		try {
			//isAllow
			if(!LoginSession.isAllow())
				throw new Exception("请先登录!");
			
		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		int execWidth = 60;
		
		x = 340;
		y = 180;
		
		width = 64;
		hight = 20;
		width_1 = 160;
		
		x += 10;
		y += 10;
		
		String display;
		
		disLabel = new JLabel("操作员:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		disLabel = new JLabel(LoginSession.user.getName());
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width,y,width_1,hight);
		bodyPanel.add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("酒厂邮编:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		cardType = new JComboBox();
		cardType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		cardType.addItem("铅封标签");
		cardType.addItem("纸质标签");
		bodyPanel.add(cardType);

		y += hight;
		
		disLabel = new JLabel("酒厂邮编:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		if(zipCodeText==null){
			zipCodeText = new JTextField();
			zipCodeText.setBounds(x+width,y,48,hight);//一个字符9 point
			zipCodeText.setDocument(new LimitDocument(6));
		}
		bodyPanel.add(zipCodeText);

		display = " * 6位数字编号";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		if(!StringUtils.isNull(message)){
			y += hight;
			
			disLabel = new JLabel(message);
			disLabel.setForeground(Color.RED);
			disLabel.setBounds(x,y,message.length()*20,hight);
			bodyPanel.add(disLabel);
		}
		
		y += hight;
		
		JButton start = new JButton("启动");
		start.setBackground(new Color(105,177,35));
		start.setForeground(fg);
		start.requestFocus();
		start.setBounds(x+width,y,execWidth,hight);//一个字符9 point

		start.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!loopCard.isAlive() && forever==false){
						forever = true;
						loopCard.start();
					}
				}
			});
		//为按钮添加键盘适配器
		start.addKeyListener(new KeyAction());
		
		bodyPanel.add(start);

		y += hight;
		
		JButton stop = new JButton("停止");
		stop.setBackground(new Color(105,177,35));
		stop.setForeground(fg);
		stop.requestFocus();
		stop.setBounds(x+width,y,execWidth,hight);//一个字符9 point

		stop.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					forever=false;
				}
			});
		bodyPanel.add(stop);
		
		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		//设置背景图片
		URL url = SFWine.class.getResource("bg1.png");
		ImageIcon img = new ImageIcon(url);
		JLabel background = new JLabel(img);
		bodyPanel.add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());
			
			new UserAction(bodyPanel).promptLogin();
		}
	}

	public void viewWineCard() {
		try {
			//isAllow
			if(!LoginSession.isAllow())
				throw new Exception("请先登录!");
			
		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());
			
			new UserAction(bodyPanel).promptLogin();
		}
	}

	public void fillCard() {
		card.setCardType(cardType.getSelectedItem().toString());
		card.setZipCode(zipCodeText.getText().trim());
	}
	
	public void listCard() {
		if(log.isDebugEnabled()) log.debug("listCard start");

		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		JTable cardTable = new JTable();
		CardTableModel caredModel = new CardTableModel();
		
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		
		for(int i=0;i<10;i++){
			Vector<Object> colValue = new Vector<Object>();
			for(int col=0;col<caredModel.getColumnDb().length;col++){
				colValue.add("colValue"+col);
			}
			rows.add(colValue);
		}
		
		caredModel.setContent(rows);
		
		if (caredModel.getContent().size() > 0) {
			RowSorter<CardTableModel> sorter = new TableRowSorter<CardTableModel>(caredModel);
			cardTable.setRowSorter(sorter);
			cardTable.setBackground(bg);
			cardTable.setForeground(fg);
			cardTable.setBorder(BorderFactory.createLineBorder(Color.white));
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(cardTable);

		scrollPane.setPreferredSize(new Dimension(1024, 600));
		scrollPane.validate();//显示
		scrollPane.setVisible(true);//缩放
		
		bodyPanel.add(scrollPane);

		bodyPanel.setVisible(true);
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		//设置背景图片
		  URL url = SFWine.class.getResource("bg1.png");
	        ImageIcon img = new ImageIcon(url);
	        JLabel background = new JLabel(img);
	        bodyPanel.add(background, new Integer(Integer.MIN_VALUE));
	        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		if(log.isDebugEnabled()) log.debug("listCard end");
	}
	
	 class CardProcessRegister implements Runnable {

		 @Override
		public void run(){
				ISO14443AAction cardRFID = new ISO14443AAction();
				try {
					//初始化
					cardRFID.initialize();
					
					//初始化检查
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
						if(!cardRFID.isOpened()){
							message = "请正确连接发卡器!";
							promptNewWineCard();
							
							Thread.sleep(100);
							continue;
						}
						
						if (log.isDebugEnabled()) log.debug("连接发卡器 okay:");

						//请放人电子标签或者电子卡
						String serialNumber = cardRFID.findSerialNumber();
						if(serialNumber == null){
							if (log.isDebugEnabled()) log.debug("请放人电子标签或者电子卡");
							message = "请放人电子标签或者电子卡!";
							promptNewWineCard();
							
							Thread.sleep(37);
							continue;
						}
						if (log.isDebugEnabled()) log.debug("卡片 okay:");
						
						int cardType = cardRFID.findCardType();
						if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
						if (log.isDebugEnabled()) log.debug("falt card:"+CommandsISO14443A.CARD_14443A_M1);
						
						if(cardType!=CommandsISO14443A.CARD_14443A_UL){
            				message = "此卡非标签卡!";
							promptNewWineCard();
							
							Thread.sleep(100);
							continue;
						}
						
						card = new Card();
						fillCard();
						card.setUuid(StringUtils.hash(serialNumber));
						
						CardDBObject dbObjcet = new CardDBObject();

						if (dbObjcet.isRegistr(card)){
							message = "卡片已经使用,请换新卡!";
							promptNewWineCard();
							
							Thread.sleep(100);
							continue;
						}
						
						card.setRfidcode(dbObjcet.nextId(card.getZipCode()));
						
						try{
						cardRFID.save(card);
						
						dbObjcet.save(card);
						
						cardRFID.beep(10);
						} catch (CardException e) {
							if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
							e.printStackTrace();
							
							message = e.getMessage();
							promptNewWineCard();
							
							Thread.sleep(100);
							continue;
						}
						
						message = "发卡成功";
						
						promptNewWineCard();
						
						Thread.sleep(500);
//						break;
					}
				} catch (Exception e) {
					if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
					message = e.getMessage();
					e.printStackTrace();
				}
				
				//check database
				if(LoginSession.staffSignal){
					if(LoginSession.allow)
						new CardAction(bodyPanel).promptNewWineCard();
				}
				
				cardRFID.beep(10);
				cardRFID.destroy();
				if (log.isDebugEnabled()) log.debug("run finlsh!"+LoginSession.staffSignal);
			}
		}
}