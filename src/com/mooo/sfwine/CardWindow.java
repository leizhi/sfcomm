package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.swing.LimitDocument;

public class CardWindow {
	private static Log log = LogFactory.getLog(CardWindow.class);

//	public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
	public static SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	
	private JLabel disLabel;
	
	private JTextField zipCodeText;
	private JTextField wineryName;
	private JTextField wineJarKey;
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
	
	public CardWindow(final JPanel bodyPanel,final JPanel execPanel) {
		card = new Card();
		
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
		
		disLabel = new JLabel("操作:");
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
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		zipCodeText = new JTextField();
		zipCodeText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(zipCodeText);

		y += hight;
		disLabel = new JLabel("酒厂名:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineryName = new JTextField();
		wineryName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineryName);

		y += hight;
		
		disLabel = new JLabel("酒罐号:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarKey = new JTextField();
		wineJarKey.setBounds(x+width,y,40,hight);//一个字符9 point
		wineJarKey.setDocument(new LimitDocument(4));
		bodyPanel.add(wineJarKey);

		String display = " * 4位数字编号";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("酒香型:");
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
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		alcohol = new JTextField();
		alcohol.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(alcohol);

		disLabel = new JLabel("操作人:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		operator = new JTextField();
		operator.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(operator);

		y += hight;

		disLabel = new JLabel("监管公司:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorCompanyKey = new JTextField();
		supervisorCompanyKey.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorCompanyKey);

		y += hight;

		disLabel = new JLabel("监管人:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorName = new JTextField();
		supervisorName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorName);

		y += hight;

		disLabel = new JLabel("酿造日期:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		brewingDate = new JTextField();
		brewingDate.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(brewingDate);

		brewingCalendar = new JCalendarButton();
		brewingCalendar.setLocale(new Locale("zh","CN"));
		
		brewingCalendar.setBounds(x+width+width_1,y,20,hight);
		brewingCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				dateOnlyPopupChanged(evt);
			}
		});
		
		bodyPanel.add(brewingCalendar);
		
		y += hight;

		disLabel = new JLabel("罐体容量:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarVolume = new JTextField();
		wineJarVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineJarVolume);

		y += hight;

		disLabel = new JLabel("源酒容量:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineVolume = new JTextField();
		wineVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineVolume);

		y += hight;

		disLabel = new JLabel("原料:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		material = new JTextField();
		material.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(material);
		
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		
		// bodyPanel
		JButton confirm = new JButton("确定");
		confirm.requestFocus();
//		confirm.setMnemonic(KeyEvent.VK_ENTER);
//		confirm.registerKeyboardAction(anAction, aKeyStroke, aCondition);
		confirm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CardAction(bodyPanel,execPanel,getCard());
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
	
	public CardWindow(final JPanel bodyPanel,final JPanel execPanel,String error) {
		card = new Card();
		
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
		
		disLabel = new JLabel("操作:");
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
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		zipCodeText = new JTextField();
		zipCodeText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(zipCodeText);

		y += hight;
		disLabel = new JLabel("酒厂名:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineryName = new JTextField();
		wineryName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineryName);

		y += hight;
		
		disLabel = new JLabel("酒罐号:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarKey = new JTextField();
		wineJarKey.setBounds(x+width,y,40,hight);//一个字符9 point
		wineJarKey.setDocument(new LimitDocument(4));
		bodyPanel.add(wineJarKey);

		String display = " * 4位数字编号";
		disLabel = new JLabel(display);
		disLabel.setBounds(x+width+width_1,y,10*display.length(),hight);
		disLabel.setForeground(Color.RED);
		bodyPanel.add(disLabel);
		
		y += hight;

		disLabel = new JLabel("酒香型:");
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
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		alcohol = new JTextField();
		alcohol.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(alcohol);

		disLabel = new JLabel("操作人:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		operator = new JTextField();
		operator.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(operator);

		y += hight;

		disLabel = new JLabel("监管公司:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorCompanyKey = new JTextField();
		supervisorCompanyKey.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorCompanyKey);

		y += hight;

		disLabel = new JLabel("监管人:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		supervisorName = new JTextField();
		supervisorName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(supervisorName);

		y += hight;

		disLabel = new JLabel("酿造日期:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		brewingDate = new JTextField();
		brewingDate.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(brewingDate);

		brewingCalendar = new JCalendarButton();
		brewingCalendar.setLocale(new Locale("zh","CN"));
		
		brewingCalendar.setBounds(x+width+width_1,y,20,hight);
		brewingCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				dateOnlyPopupChanged(evt);
			}
		});
		
		bodyPanel.add(brewingCalendar);
		
		y += hight;

		disLabel = new JLabel("罐体容量:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineJarVolume = new JTextField();
		wineJarVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineJarVolume);

		y += hight;

		disLabel = new JLabel("源酒容量:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		wineVolume = new JTextField();
		wineVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(wineVolume);

		y += hight;

		disLabel = new JLabel("原料:");
		disLabel.setBounds(x,y,width,hight);
		bodyPanel.add(disLabel);

		material = new JTextField();
		material.setBounds(x+width,y,width_1,hight);//一个字符9 point
		bodyPanel.add(material);
		
		if(!StringUtils.isNull(error)){
			y += hight;
			disLabel = new JLabel(error);
			disLabel.setForeground(Color.RED);
			disLabel.setBounds(x,y,error.length()*20,hight);
			bodyPanel.add(disLabel);
		}
		
		bodyPanel.validate();//显示
		bodyPanel.repaint();
		
		// bodyPanel
		JButton confirm = new JButton("确定");
		confirm.requestFocus();
//		confirm.setMnemonic(KeyEvent.VK_ENTER);
//		confirm.registerKeyboardAction(anAction, aKeyStroke, aCondition);
		confirm.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CardAction(bodyPanel,execPanel,getCard());
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
	
	private void dateOnlyPopupChanged(PropertyChangeEvent evt) {// GEN-FIRST:event_dateOnlyPopupChanged
		if (evt.getNewValue() instanceof Date)
			setDate((Date) evt.getNewValue());
	}// GEN-LAST:event_dateOnlyPopupChanged

	public Card getCard() {
		
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
		
		return card;
	}
}