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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class CardAction {
	private static Log log = LogFactory.getLog(CardAction.class);

	private JLabel disLabel;
	private JLabel messageLabel;

	private JComboBox winery;
	private JComboBox cardType;

	private Card card;

	public void promptNewWineCard() {
		try {
			//isAllow
			if(!SFClient.isAllow())
				throw new Exception("请先登录!");
			
		//clean view
		if (SFWine.global.getBodyPanel().isShowing()) {
			SFWine.global.getBodyPanel().removeAll();
		}
		
		int x, y,width,hight,width_1;
		int execWidth = 97;
		
		x = 340;
		y = 180;
		
		width = 64;
		hight = 20;
		width_1 = 160;
		
		x += 10;
		y += 10;
		
		String display;
		
		disLabel = new JLabel("操作员:");
		disLabel.setForeground(Global.fg);
		disLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		
		disLabel = new JLabel(SFClient.user.getName());
		disLabel.setForeground(Global.fg);
		disLabel.setBounds(x+width,y,width_1,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("酒厂:");
		disLabel.setForeground(Global.fg);
		disLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		
		winery = new JComboBox();
		winery.setBounds(x+width,y,width_1,hight);//一个字符9 point
		String[] items = SFClient.getWineryValues();
		for(String value:items){
			winery.addItem(value);
		}
		SFWine.global.getBodyPanel().add(winery);
		
		display = " * 发标签到酒厂";
		disLabel = new JLabel(display);
		disLabel.setForeground(Global.fg);
		disLabel.setBounds(x+width+width_1,y,12*display.length(),hight);
		disLabel.setForeground(Color.RED);
		SFWine.global.getBodyPanel().add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("标签类型:");
		disLabel.setForeground(Global.fg);
		disLabel.setBounds(x,y,width,hight);
		SFWine.global.getBodyPanel().add(disLabel);
		
		cardType = new JComboBox();
		cardType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		items = SFClient.getCardTypes();
		for(String value:items){
			cardType.addItem(value);
		}
		SFWine.global.getBodyPanel().add(cardType);
		
		y += hight;
		
		messageLabel= new JLabel();
		messageLabel.setText("请启动发卡");
		messageLabel.setForeground(Color.RED);
		messageLabel.setBounds(x,y,20*20,hight);
		SFWine.global.getBodyPanel().add(messageLabel);
		
		y += hight;
		
		JButton runProcess = new JButton("发卡");
		runProcess.setBackground(new Color(105,177,35));
		runProcess.setForeground(Global.fg);
		runProcess.requestFocus();
		runProcess.setBounds(x+width,y,execWidth,hight);//一个字符9 point
		runProcess.isDefaultButton();

		runProcess.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					String rfidCode=null;
					
					ISO14443AAction cardRFID = new ISO14443AAction();
					//初始化
					cardRFID.initSerial();
					
					if(!SFClient.isOpenNetwork()) SFClient.connect();
					
					try {
						Global.message="发卡开始";
						messageLabel.setText(Global.message);
						
						cardRFID.initCard();
						//请正确连接发卡器
						if(!cardRFID.isOpened()){
							throw new CardException("发卡器未连接或者端口选择错误!");
						}
						if (log.isDebugEnabled()) log.debug("连接发卡器 okay:");

						//请放人电子标签或者电子卡
						String serialNumber = cardRFID.getSerialNumber();
						if(serialNumber == null){
							throw new CardException("请放人电子标签或者电子卡!");
						}
						if (log.isDebugEnabled()) log.debug("卡片 okay:");
						
						int cardType = cardRFID.getCardType();
						if(cardType!=CommandsISO14443A.CARD_14443A_UL){
							throw new CardException("此卡非标签卡!");
						}
							
						card = new Card();
						fillCard();

						card.setUuid(StringUtils.hash(serialNumber));
						if (log.isDebugEnabled()) log.debug("uuid:"+card.getUuid());

						if (SFClient.existCard(card.getUuid())){
							throw new CardException("卡片已经使用,请换新卡!");
						}

						if (log.isDebugEnabled()) log.debug("Winery:"+card.getWinery());
						rfidCode = SFClient.nextRfidCode(card.getWinery());
						card.setRfidcode(rfidCode);
							
						cardRFID.save(card);
						if (log.isDebugEnabled()) log.debug("RFID save card");

						SFClient.saveCard(card.getRfidcode(),card.getUuid(),card.getWinery(),card.getCardTypeName());
						
						Global.message = "发卡成功号为:"+rfidCode;
						
						if (log.isDebugEnabled()) log.debug("DB save card");
					} catch (Exception e1) {
						if (log.isErrorEnabled()) log.error("Exception:" + e1.getMessage());
						Global.message=e1.getMessage();

						e1.printStackTrace();
					}finally {
						cardRFID.beep(10);
						cardRFID.destroy();
						messageLabel.setText(Global.message);
					}
				}
			});
		//为按钮添加键盘适配器
		runProcess.addKeyListener(new KeyAction());
		
		SFWine.global.getBodyPanel().add(runProcess);

		SFWine.global.getBodyPanel().setVisible(true);
		SFWine.global.getBodyPanel().validate();//显示
		SFWine.global.getBodyPanel().repaint();
		
		//设置背景图片
		URL url = SFWine.class.getResource("bg1.png");
		ImageIcon img = new ImageIcon(url);
		JLabel background = new JLabel(img);
		SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());
			
			new UserAction().promptLogin();
		}
	}

	public void viewWineCard() {
		try {
			//isAllow
			if(!SFClient.isAllow())
				throw new Exception("请先登录!");
			
		//clean view
		if (SFWine.global.getBodyPanel().isShowing()) {
			SFWine.global.getBodyPanel().removeAll();
		}
		if(log.isDebugEnabled()) log.debug("initializeGUI end");
		} catch (Exception e) {
			if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());
			
			SFWine.global.getUserAction().promptLogin();
		}
	}

	public void fillCard() {
		card.setCardTypeName(cardType.getSelectedItem().toString());
		card.setWinery(winery.getSelectedItem().toString());
	}
	
	public void listCard() {
		if(log.isDebugEnabled()) log.debug("listCard start");

		//clean view
		if (SFWine.global.getBodyPanel().isShowing()) {
			SFWine.global.getBodyPanel().removeAll();
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
			cardTable.setBackground(Global.bg);
			cardTable.setForeground(Global.fg);
			cardTable.setBorder(BorderFactory.createLineBorder(Color.white));
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(cardTable);

		scrollPane.setPreferredSize(new Dimension(1024, 600));
		scrollPane.validate();//显示
		scrollPane.setVisible(true);//缩放
		
		SFWine.global.getBodyPanel().add(scrollPane);

		SFWine.global.getBodyPanel().setVisible(true);
		SFWine.global.getBodyPanel().validate();//显示
		SFWine.global.getBodyPanel().repaint();
		//设置背景图片
		URL url = SFWine.class.getResource("bg1.png");
		ImageIcon img = new ImageIcon(url);
		JLabel background = new JLabel(img);
		SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		if(log.isDebugEnabled()) log.debug("listCard end");
	}
}