package com.mooo.sfwine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

import es.deusto.smartlab.rfid.SerialManager;
import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;

public class CardAction {
	private static Log log = LogFactory.getLog(CardAction.class);

	private boolean runEnable = false;
	
	private Color fg = Color.WHITE;
	
	private Color bg = Color.GREEN;

	private JLabel disLabel;
	
	private JLabel messageLabel;

	private JComboBox winery;
	private JComboBox cardType;
	private JComboBox whichPort;

	private Card card;

	private JPanel bodyPanel;
	private String message;
	
	public CardAction(JPanel bodyPanel) {
		this.bodyPanel = bodyPanel;
		this.message = null;
	}
	
	public CardAction(JPanel bodyPanel,String message) {
		this.bodyPanel = bodyPanel;
		this.message = message;
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
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		disLabel = new JLabel(LoginSession.user.getName());
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width,y,width_1,hight);
		bodyPanel.add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("酒厂:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		winery = new JComboBox();
		winery.setBounds(x+width,y,width_1,hight);//一个字符9 point
		List<String> items = IDGenerator.getWineryValues();
		for(String value:items){
			winery.addItem(value);
		}
		bodyPanel.add(winery);
		
		display = " * 发标签到酒厂";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;
		
		disLabel = new JLabel("标签类型:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		cardType = new JComboBox();
		cardType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		cardType.addItem("铅封标签");
		cardType.addItem("纸质标签");
		bodyPanel.add(cardType);
		
		y += hight;
		
		disLabel = new JLabel("串口端口:");
		disLabel.setBounds(x,y,width,hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		whichPort = new JComboBox();
		whichPort.setBounds(x+width,y,width_1,hight);//一个字符9 point
		whichPort.setSelectedItem(whichPort.getSelectedItem());
		
		List<String> ports = new SerialManager().getPorts();
		for(String value:ports){
			whichPort.addItem(value);
		}
		bodyPanel.add(whichPort);
		
		whichPort.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("Item=" + e.getItem());
				System.out.println("StateChange=" + e.getStateChange());
				ISO14443AAction.whichPort=e.getItem().toString();
			}
		});
		
		y += hight;
		
		messageLabel= new JLabel();
		messageLabel.setText("请启动发卡");
		messageLabel.setForeground(Color.RED);
		messageLabel.setBounds(x,y,20*20,hight);
		bodyPanel.add(messageLabel);
		
		y += hight;
		
		JButton runProcess = new JButton("启动/停止");
		runProcess.setBackground(new Color(105,177,35));
		runProcess.setForeground(fg);
		runProcess.requestFocus();
		runProcess.setBounds(x+width,y,execWidth,hight);//一个字符9 point

		runProcess.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(runEnable){
						runEnable=false;
						
						message = "发卡停止..";
						messageLabel.setText(message);
					}else{
						new Thread (new CardProcessRegister(messageLabel)).start();
					}
				}
			});
		//为按钮添加键盘适配器
		runProcess.addKeyListener(new KeyAction());
		
		bodyPanel.add(runProcess);

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
		card.setWinery(winery.getSelectedItem().toString());
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
		 
			private JLabel messageLabel;
			
			CardProcessRegister(JLabel messageLabel){
				this.messageLabel=messageLabel;
				ISO14443AAction.whichPort = whichPort.getSelectedItem().toString();
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
//									Thread.sleep(37);
									throw new NullPointerException("请放人电子标签或者电子卡!");
								}
								if (log.isDebugEnabled()) log.debug("卡片 okay:");
								
								int cardType = cardRFID.findCardType();
								if (log.isDebugEnabled()) log.debug("falt card:"+cardType);
								if (log.isDebugEnabled()) log.debug("M1 Card is:"+CommandsISO14443A.CARD_14443A_M1);
								if (log.isDebugEnabled()) log.debug("UL Card is:"+CommandsISO14443A.CARD_14443A_UL);

								if(cardType!=CommandsISO14443A.CARD_14443A_UL){
//									Thread.sleep(100);
									throw new NullPointerException("此卡非标签卡!");
								}
									
								card = new Card();
								fillCard();
								card.setUuid(StringUtils.hash(serialNumber));
								
								if (log.isDebugEnabled()) log.debug("uuid:"+card.getUuid());

								CardDBObject dbObjcet = new CardDBObject();

								if (dbObjcet.isRegistr(card)){
									throw new NullPointerException("卡片已经使用,请换新卡!");
								}
								
								if (log.isDebugEnabled()) log.debug("Winery:"+card.getWinery());

								if (log.isDebugEnabled()) log.debug("Rfidcode:"+dbObjcet.nextId(card.getWinery()));

								card.setRfidcode(dbObjcet.nextId(card.getWinery()));
									
								cardRFID.save(card);
								
								if (log.isDebugEnabled()) log.debug("RFID save card");

								dbObjcet.save(card);
								
								if (log.isDebugEnabled()) log.debug("DB save card");

								message = "发卡成功";
								messageLabel.setText(message);
								
							}  catch (NullPointerException e){
								if (log.isErrorEnabled()) log.error("NullPointerException:" + e.getMessage());

								message = e.getMessage();
								messageLabel.setText(message);
								
								e.printStackTrace();
							}  catch (CardException e){
								if (log.isErrorEnabled()) log.error("CardException:" + e.getMessage());

								message = e.getMessage();
								messageLabel.setText(message);
								
								e.printStackTrace();
							} catch (Exception e) {
								if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
								message = e.getMessage();
								messageLabel.setText(message);
								
								e.printStackTrace();
							} finally {
								cardRFID.beep(10);
								cardRFID.destroy();
							}
						}
					};
					
					//do while
					runEnable = true;
					while(runEnable){
						try {
							message = "发卡处理";
							messageLabel.setText(message);
							
							SwingUtilities.invokeAndWait(runner);
							// Our task for each step is to just sleep
							//Sleep 500 ms
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					/*
					//Authority verification
					if(LoginSession.staffSignal){
						if(LoginSession.allow)
							promptNewWineCard();
					}
					if (log.isDebugEnabled()) log.debug("run finlsh!"+LoginSession.staffSignal);
					
					*/
			}
		}
}