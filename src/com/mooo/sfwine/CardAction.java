package com.mooo.sfwine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
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

public class CardAction {
	private static Log log = LogFactory.getLog(CardAction.class);

//	public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
	public static SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	
	private Color fg = Color.WHITE;
	
	private Color bg = Color.GREEN;

	private JLabel disLabel;
	
	private JTextField zipCodeText;
	private JTextField wineryName;
	private JTextField wineryAddress;
	private JTextField wineJarKey;
	private JTextField wineJarVolume;
	private JTextField wineVolume;
	
	private JComboBox jobType;
	
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
		int execWidth = 60;
		int p=0;
		
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

		disLabel = new JLabel("操作:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		jobType = new JComboBox();
		jobType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jobType.addItem("封酒");
		jobType.addItem("加酒");
		jobType.addItem("取酒");
		jobType.addItem("加&取 酒");
		jobType.addItem("取完酒");
		jobType.addItem("巡检");
		bodyPanel.add(jobType);
		
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
		
		y += hight;
		disLabel = new JLabel("酒厂名:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		if(wineryName==null){
			wineryName = new JTextField();
			wineryName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		}
		bodyPanel.add(wineryName);
		
		
		y += hight;
		disLabel = new JLabel("酒厂地址:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);
		
		if(wineryAddress==null){
			wineryAddress = new JTextField();
			wineryAddress.setBounds(x+width,y,width_1,hight);//一个字符9 point
		}
		bodyPanel.add(wineryAddress);
		
		y += hight;
		
		disLabel = new JLabel("酒罐号:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		if(wineJarKey==null){
			wineJarKey = new JTextField();
			wineJarKey.setBounds(x+width,y,32,hight);//一个字符9 point
			wineJarKey.setDocument(new LimitDocument(4));
		}
		bodyPanel.add(wineJarKey);

		display = " * 4位数字编号";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);

		y += hight;

		disLabel = new JLabel("罐体容量:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		p=64;
		
		if(wineJarVolume==null){
			wineJarVolume = new JTextField();
			wineJarVolume.setBounds(x+width,y,p,hight);//一个字符9 point
			wineJarVolume.setDocument(new LimitDocument(8));
		}
		bodyPanel.add(wineJarVolume);

		display = "(吨)";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+p,y,8*display.length(),hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		display = " 例: 10000.00";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("原酒容量:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		p=64;
		
		if(wineVolume==null){
			wineVolume = new JTextField();
			wineVolume.setBounds(x+width,y,p,hight);//一个字符9 point
			wineVolume.setDocument(new LimitDocument(8));
		}
		bodyPanel.add(wineVolume);

		display = "(吨)";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+p,y,8*display.length(),hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		display = " 例: 10000.00";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.WHITE);
		bodyPanel.add(disLabel);
		
		if(!StringUtils.isNull(message)){
			y += hight;
			
			disLabel = new JLabel(message);
			disLabel.setForeground(Color.RED);
			disLabel.setBounds(x,y,message.length()*20,hight);
			bodyPanel.add(disLabel);
		}
		
		y += hight;
		
		JButton confirm = new JButton("确定");
		confirm.setBackground(new Color(105,177,35));
		confirm.setForeground(fg);
		confirm.requestFocus();
		confirm.setBounds(x+width,y,execWidth,hight);//一个字符9 point

		confirm.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					processNewWineCard();
				}
			});
		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAction());
		
		bodyPanel.add(confirm);

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

	public void processNewWineCard() {
		ISO14443AAction cardRFID = new ISO14443AAction();
		
		try {
			card = new Card();
			fillCard();
			
			cardRFID.initialize();

			// 初始化检查
			if (!cardRFID.isOpened())
				throw new NullPointerException("请正确连接发卡器");

			String serialNumber = cardRFID.findSerialNumber();
			
			if (serialNumber == null)
				throw new NullPointerException("请放人电子标签或者电子卡");

			card.setRfidcode(sixMD5(serialNumber));
			
			CardDBObject dbObjcet = new CardDBObject();

			if (dbObjcet.isRegistr(card))
				throw new NullPointerException("卡片已经使用,请换新卡!");

			cardRFID.save(card);
			
			dbObjcet.save(card);
			
			cardRFID.beep(10);
			message = "发卡成功";
		} catch (CardException e) {
			if (log.isErrorEnabled()) log.error("CardException:" + e.getMessage());
			message = e.getMessage();
		} catch (NullPointerException e) {
			if (log.isErrorEnabled()) log.error("NullPointerException:" + e.getMessage());
			message = e.getMessage();
		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error("Exception:" + e.getMessage());
			message = e.getMessage();
		} finally {
			cardRFID.destroy();
		}
		
		promptNewWineCard();
		if (log.isDebugEnabled()) log.debug("flinsh");
	}
	
	
	public String sixMD5(String plainText){
		
		 String sixteen="";
		 try {
			   MessageDigest md = MessageDigest.getInstance("MD5");   
			   md.update(plainText.getBytes());
			   byte b[] = md.digest();

			   int i;
			   
			   StringBuffer buf = new StringBuffer(""); 
			   for (int offset = 0; offset < b.length; offset++) {
			    i = b[offset];
			    if(i<0) i+= 256;
			    if(i<16)
			     buf.append("0");
			    buf.append(Integer.toHexString(i));
			   }
			   sixteen= buf.toString().substring(8,24);//16位的加密
			  // System.out.println("result: " + buf.toString());//32位的加密
			  // System.out.println("result: " + buf.toString().substring(8,24));//16位的加密

			  } catch (NoSuchAlgorithmException e) {
			   e.printStackTrace();
			  }

		 return sixteen;
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
		card.setJobTypeName(jobType.getSelectedItem().toString());

		card.setZipCode(zipCodeText.getText());
		card.setWineryName(wineryName.getText());
		card.setWineryAddress(wineryAddress.getText());
		card.setWineJarKey(wineJarKey.getText());
		card.setWineJarVolume(wineJarVolume.getText());
		card.setWineVolume(wineVolume.getText());
		
		card.setOrgId(LoginSession.user.getOrgId());
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
}