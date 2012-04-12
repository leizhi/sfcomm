package com.mooo.sfwine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

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
	private JTextField wineJarKey;
	private JTextField wineName;
	private JTextField operator;
	private JTextField alcohol;
	private JTextField supervisorName;
	private JTextField supervisorCompanyKey;
	private JTextField wineJarVolume;
	private JTextField wineVolume;
	private JTextField material;
	
	private JComboBox jobType;
	
	private JComboBox wineLevel;
	private JComboBox wineType;
	
	private JTextField brewingDate;
	private JCalendarButton brewingCalendar;
	
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
			
		card = new Card();
		
		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		int execWidth = 60;

		x = 340;
		y = 140;
		
		width = 80;
		hight = 20;
		width_1 = 160;
		
		x += 10;
		y += 10;
		
		String display = "操作员:"+LoginSession.user.getName();
		
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,20*display.length(),hight);
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

		zipCodeText = new JTextField();
		zipCodeText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(zipCodeText);

		y += hight;
		disLabel = new JLabel("酒厂名:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineryName = new JTextField();
		wineryName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineryName);
		
		y += hight;
		
		disLabel = new JLabel("酒名:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineName = new JTextField();
		wineName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineName);
		y += hight;
		
		disLabel = new JLabel("酒罐号:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarKey = new JTextField();
		wineJarKey.setBounds(x+width,y,40,hight);//一个字符9 point
		wineJarKey.setDocument(new LimitDocument(4));
		bodyPanel.add(wineJarKey);

		display = " * 4位数字编号";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("酒香型:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineType = new JComboBox();
		wineType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		wineType.addItem("酱香型");
		wineType.addItem("浓香型");
		wineType.addItem("兼香型");
		wineType.addItem("米香型");
		wineType.addItem("凤香型");
		wineType.addItem("芝麻香型");
		wineType.addItem("豉香型");
		wineType.addItem("清香型");
		wineType.addItem("特香型");
		wineType.addItem("药香型");
		wineType.addItem("老白干香型");
		wineType.addItem("馥郁香型白酒");
		bodyPanel.add(wineType);

		y += hight;

		disLabel = new JLabel("品质:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineLevel = new JComboBox();
		wineLevel.setBounds(x+width,y,width_1,hight);//一个字符9 point

		wineLevel.addItem("特级");
		wineLevel.addItem("特优");
		wineLevel.addItem("优级");
		wineLevel.addItem("一级");
		wineLevel.addItem("二级");
		bodyPanel.add(wineLevel);

		y += hight;

		disLabel = new JLabel("酒精度:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		alcohol = new JTextField();
		alcohol.setBounds(x+width,y,40,hight);//一个字符9 point
		alcohol.setDocument(new LimitDocument(5));
		bodyPanel.add(alcohol);

		display = " 度(%) 45.13";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("操作人:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		operator = new JTextField();
		operator.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(operator);

		y += hight;

		disLabel = new JLabel("监管公司:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorCompanyKey = new JTextField();
		supervisorCompanyKey.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorCompanyKey);

		y += hight;

		disLabel = new JLabel("监管人:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorName = new JTextField();
		supervisorName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorName);

		y += hight;

		disLabel = new JLabel("酿造日期:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		brewingDate = new JTextField();
		brewingDate.setBounds(x+width,y,100,hight);//一个字符9 point
		brewingDate.setDocument(new LimitDocument(10));
		bodyPanel.add(brewingDate);

		brewingCalendar = new JCalendarButton();
		brewingCalendar.setLocale(new Locale("zh","CN"));
		
		brewingCalendar.setBounds(x+width+100,y,20,hight);
		brewingCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				dateOnlyPopupChanged(evt);
			}
		});
		
		bodyPanel.add(brewingCalendar);
		
		y += hight;

		disLabel = new JLabel("罐体容量:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarVolume = new JTextField();
		wineJarVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineJarVolume);

		y += hight;

		disLabel = new JLabel("源酒容量:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineVolume = new JTextField();
		wineVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineVolume);

		y += hight;

		disLabel = new JLabel("原料:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		material = new JTextField();
		material.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(material);
		
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
				@Override
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
			
			new CardLoginAction(bodyPanel);
		}
	}

	public void processNewWineCard() {
		ISO14443AAction cardRFID = new ISO14443AAction();
		
		try {
			fillCard();
			cardRFID.init();

			// 初始化检查
			if (!cardRFID.isOpened())
				throw new NullPointerException("请正确连接发卡器");

			String serialNumber = cardRFID.findSerialNumber();
			
			if (serialNumber == null)
				throw new NullPointerException("请放人电子标签或者电子卡");

			card.setUuid(serialNumber);
			card.setRfidcode(sixMD5(serialNumber));
			
			CardDBObject dbObjcet = new CardDBObject();

			if (dbObjcet.isRegistr(card))
				throw new NullPointerException("卡片已经使用,请换新卡!");

			cardRFID.save(card);
			
			dbObjcet.save(card);

			JOptionPane.showMessageDialog(null, "发卡成功", "系统提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (NullPointerException e) {
			if (log.isErrorEnabled())
				log.error("NullPointerException:" + e.getMessage());
			message = e.getMessage();
			
			promptNewWineCard();
		} catch (Exception se) {
			if (log.isErrorEnabled())
				log.error("Exception:" + se.getMessage());
			message = se.getMessage();
			promptNewWineCard();
			se.printStackTrace();
		} finally {
			cardRFID.beep(10);
			cardRFID.destroy();
		}
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
			
		card = new Card();
		
		//clean view
		if (bodyPanel.isShowing()) {
			bodyPanel.removeAll();
		}
		
		int x, y,width,hight,width_1;
		int execWidth = 60;

		x = 340;
		y = 140;
		
		width = 80;
		hight = 20;
		width_1 = 160;
		
		x += 10;
		y += 10;
		
		String display = "操作员:"+LoginSession.user.getName();
		
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,20*display.length(),hight);
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

		zipCodeText = new JTextField();
		zipCodeText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(zipCodeText);

		y += hight;
		disLabel = new JLabel("酒厂名:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineryName = new JTextField();
		wineryName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineryName);

		y += hight;
		
		disLabel = new JLabel("酒罐号:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarKey = new JTextField();
		wineJarKey.setBounds(x+width,y,40,hight);//一个字符9 point
		wineJarKey.setDocument(new LimitDocument(4));
		bodyPanel.add(wineJarKey);

		display = " * 4位数字编号";
		disLabel = new JLabel(display);
		disLabel.setForeground(fg);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("酒香型:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineType = new JComboBox();
		wineType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		wineType.addItem("酱香型");
		wineType.addItem("浓香型");
		wineType.addItem("兼香型");
		wineType.addItem("米香型");
		wineType.addItem("凤香型");
		wineType.addItem("芝麻香型");
		wineType.addItem("豉香型");
		wineType.addItem("清香型");
		wineType.addItem("特香型");
		wineType.addItem("药香型");
		wineType.addItem("老白干香型");
		wineType.addItem("馥郁香型白酒");
		bodyPanel.add(wineType);

		y += hight;

		disLabel = new JLabel("品质:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineLevel = new JComboBox();
		wineLevel.setBounds(x+width,y,width_1,hight);//一个字符9 point

		wineLevel.addItem("特级");
		wineLevel.addItem("特优");
		wineLevel.addItem("优级");
		wineLevel.addItem("一级");
		wineLevel.addItem("二级");
		bodyPanel.add(wineLevel);

		y += hight;

		disLabel = new JLabel("酒精度:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		alcohol = new JTextField();
		alcohol.setBounds(x+width,y,40,hight);//一个字符9 point
		alcohol.setDocument(new LimitDocument(5));
		bodyPanel.add(alcohol);

		display = " 度(%) 45.13";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("操作人:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		operator = new JTextField();
		operator.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(operator);

		y += hight;

		disLabel = new JLabel("监管公司:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorCompanyKey = new JTextField();
		supervisorCompanyKey.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorCompanyKey);

		y += hight;

		disLabel = new JLabel("监管人:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorName = new JTextField();
		supervisorName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorName);

		y += hight;

		disLabel = new JLabel("酿造日期:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		brewingDate = new JTextField();
		brewingDate.setBounds(x+width,y,100,hight);//一个字符9 point
		brewingDate.setDocument(new LimitDocument(10));
		bodyPanel.add(brewingDate);

		brewingCalendar = new JCalendarButton();
		brewingCalendar.setLocale(new Locale("zh","CN"));
		
		brewingCalendar.setBounds(x+width+100,y,20,hight);
		brewingCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				dateOnlyPopupChanged(evt);
			}
		});
		
		bodyPanel.add(brewingCalendar);
		
		y += hight;

		disLabel = new JLabel("罐体容量:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarVolume = new JTextField();
		wineJarVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineJarVolume);

		y += hight;

		disLabel = new JLabel("源酒容量:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineVolume = new JTextField();
		wineVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineVolume);

		y += hight;

		disLabel = new JLabel("原料:");
		disLabel.setForeground(fg);
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		material = new JTextField();
		material.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(material);
		
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
				@Override
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
			
			new CardLoginAction(bodyPanel);
		}
	}
	/**
	 * Validate and set the datetime field on the screen given a date.
	 * 
	 * @param dateTime
	 *            The datetime object
	 */
	public void setDate(Date date) {
		String dateString = "";
		if (date != null)
			dateString = dformat.format(date);
		
		brewingDate.setText(dateString);
		brewingCalendar.setTargetDate(date);
	}
	
	private void dateOnlyPopupChanged(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof Date)
			setDate((Date) evt.getNewValue());
	}

	public void fillCard() {
		
		card.setJobTypeName(jobType.getSelectedItem().toString());
		card.setWineType(wineType.getSelectedItem().toString());
		card.setWineLevel(wineLevel.getSelectedItem().toString());

		card.setZipCode(zipCodeText.getText());
		card.setWineryName(wineryName.getText());
		card.setWineJarKey(wineJarKey.getText());
		card.setAlcohol(alcohol.getText());
		card.setOperator(operator.getText());
		card.setSupervisorCompanyKey(supervisorCompanyKey.getText());
		card.setSupervisorName(supervisorName.getText());
		card.setBrewingDate(brewingDate.getText());
		card.setWineJarVolume(wineJarVolume.getText());
		card.setWineVolume(wineVolume.getText());
		
		card.setMaterial(material.getText());
		card.setOrgId(LoginSession.user.getOrgId());
		card.setUserId(LoginSession.user.getId());
		card.setWineName(wineName.getText());
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