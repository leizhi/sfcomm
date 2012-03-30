package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

public class SFWine {
	private static Log log = LogFactory.getLog(SFWine.class);

	public static JFrame frame;

	private JPanel bottomPanel;

	private Card card;
	private CardRFID cardRFID;
	
	private JTextField wineryName;
	private JComboBox jobType;
	private JTextField brewingDate;
	private JCalendarButton brewingCalendar;
	
//	public static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
	public static SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	
	public SFWine() {
		initComponent();
		
		cardRFID = new CardRFID();
		
		card = new Card();
		
		// init
		frame = new JFrame("源酒产业联盟发卡系统");
		
		frame.setFont(new Font("MS Song", 0, 25));

		frame.setJMenuBar(createJMenuBar());
//
//		JScrollPane leftPanel = new JScrollPane();
//		leftPanel.setPreferredSize(new Dimension(400, 100));

		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(400, 100));
		jp.setBorder(BorderFactory.createLineBorder(null));

//		jp.setLayout(new GridLayout(14, 2,4,5));
//		FlowLayout.LEADING
		jp.setLayout(null);

		int x, y,width,hight,width_1;
		x = 60;
		y = 0;
		
		width = 80;
		hight = 20;
		width_1 = 120;
		
		x += 10;
		y += 10;
		
		JLabel srcLabel = new JLabel("操作:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		jobType = new JComboBox();
		jobType.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jobType.addItem("封酒");
		jobType.addItem("加酒");
		jobType.addItem("取酒");
		jobType.addItem("加&取 酒");
		jobType.addItem("取完酒");
		jobType.addItem("巡检");
		jp.add(jobType);
		
		srcLabel = new JLabel("酒厂邮编:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField zipCodeText = new JTextField();
		zipCodeText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(zipCodeText);

		y += hight;
		srcLabel = new JLabel("酒厂名:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		wineryName = new JTextField();
		wineryName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(wineryName);

		y += hight;
		
		srcLabel = new JLabel("酒罐号:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField wineJarKeyText = new JTextField();
		wineJarKeyText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(wineJarKeyText);

		y += hight;

		srcLabel = new JLabel("酒香型:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JComboBox wineType = new JComboBox();
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
		jp.add(wineType);

		y += hight;

		srcLabel = new JLabel("品质:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JComboBox wineLevel = new JComboBox();
		wineLevel.setBounds(x+width,y,width_1,hight);//一个字符9 point

		wineLevel.addItem("特级");
		wineLevel.addItem("特优");
		wineLevel.addItem("优级");
		wineLevel.addItem("一级");
		wineLevel.addItem("二级");
		jp.add(wineLevel);

		y += hight;

		srcLabel = new JLabel("酒精度:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField alcohol = new JTextField();
		alcohol.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(alcohol);

		srcLabel = new JLabel("操作人:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField operatorName = new JTextField();
		operatorName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(operatorName);

		y += hight;

		srcLabel = new JLabel("监管公司:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField supervisorCompanyKey = new JTextField();
		supervisorCompanyKey.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(supervisorCompanyKey);

		y += hight;

		srcLabel = new JLabel("监管人:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField supervisorName = new JTextField();
		supervisorName.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(supervisorName);

		y += hight;

		srcLabel = new JLabel("酿造日期:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

//		final JTextField brewingDate = new JTextField();
		brewingDate = new JTextField();
		brewingDate.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(brewingDate);

		brewingCalendar = new JCalendarButton();
		brewingCalendar.setLocale(new Locale("zh","CN"));
		
		brewingCalendar.setBounds(x+width+width_1,y,20,hight);
		brewingCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				dateOnlyPopupChanged(evt);
			}
		});
		
		jp.add(brewingCalendar);
		
		y += hight;

		srcLabel = new JLabel("罐体容量:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField wineJarVolume = new JTextField();
		wineJarVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(wineJarVolume);

		y += hight;

		srcLabel = new JLabel("源酒容量:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField wineVolume = new JTextField();
		wineVolume.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(wineVolume);

		y += hight;

		srcLabel = new JLabel("原料:");
		srcLabel.setBounds(x,y,width,hight);
		jp.add(srcLabel);

		final JTextField rawMaterialText = new JTextField();
		rawMaterialText.setBounds(x+width,y,width_1,hight);//一个字符9 point
		jp.add(rawMaterialText);
		
//		leftPanel.setViewportView(jp);
//
//		leftPanel.validate();//显示
//		leftPanel.setVisible(true);//缩放

		// right Panel
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(80, 100));
		rightPanel.setBorder(BorderFactory.createLineBorder(null));
//		rightPanel.addKeyListener(new KeyManager());

		JButton confirm = new JButton("确定");
		confirm.requestFocus();
//		confirm.setMnemonic(KeyEvent.VK_ENTER);
//		confirm.registerKeyboardAction(anAction, aKeyStroke, aCondition);
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
				card.setId(cardRFID.getCardId());
				CardDBObject dbObjcet = new CardDBObject();

				if(dbObjcet.isRegistr(card))
					throw new Exception("卡片已经使用,请换新卡!");
				
				card.setJobTypeName(jobType.getSelectedItem().toString());
				
				card.setZipCode(zipCodeText.getText());
				card.setWineryName(wineryName.getText());
				
				cardRFID.save(card.getZipCode(), 1,0,6);
				cardRFID.saveGBK(card.getWineryName(), 1,1,16);

				cardRFID.saveGBK(wineJarKeyText.getText(), 1,2,16);
				
				cardRFID.saveGBK(wineType.getSelectedItem().toString(), 2,0,16);
				
				cardRFID.saveGBK(wineLevel.getSelectedItem().toString(), 2,1,16);
				
				cardRFID.saveGBK(alcohol.getText(), 2,2,16);
				cardRFID.saveGBK(operatorName.getText(), 3,0,16);
				cardRFID.saveGBK(supervisorCompanyKey.getText(), 3,1,16);
				cardRFID.saveGBK(supervisorName.getText(), 3,2,16);
				cardRFID.saveGBK(brewingDate.getText(), 4,0,16);
				
				SimpleDateFormat dformat = new SimpleDateFormat("yyy-MM-dd");
				Calendar rightNow = Calendar.getInstance();
				String value = dformat.format(rightNow.getTime());
				
				cardRFID.saveGBK(value, 4,1,16);
				
				cardRFID.saveGBK(wineJarVolume.getText(), 4,2,16);
				cardRFID.saveGBK(wineVolume.getText(), 5,0,16);
				
				cardRFID.saveGBK(rawMaterialText.getText(), 6,0,16);
				cardRFID.beep();
				
				dbObjcet.save(card);
				
				JOptionPane.showMessageDialog(frame, "发卡成功");
				
				} catch (Exception se) {
					JOptionPane.showMessageDialog(null, se.getMessage(),"系统提示",
							JOptionPane.INFORMATION_MESSAGE);
					
					if(log.isErrorEnabled()) log.error("Exception:"+se.getMessage());
//					se.printStackTrace();
				}
			}
		});

		//为按钮添加键盘适配器
		confirm.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
            	System.out.println("KeyEvent:"+e.getKeyCode());
            	
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                            //输入正确
                            JOptionPane.showMessageDialog(null, "输入正确","系统提示",
                            		JOptionPane.INFORMATION_MESSAGE);
                    }
                }
        });
		
		rightPanel.add(confirm);
		
		rightPanel.setVisible(true);
		rightPanel.validate();//显示

//		frame.getContentPane().add(leftPanel, BorderLayout.WEST);
		frame.getContentPane().add(jp, BorderLayout.WEST);
		frame.getContentPane().add(rightPanel, BorderLayout.EAST);

		frame.getContentPane().validate(); // 显示

		bottomPanel = new JPanel();
		JLabel bottomLabel = new JLabel();
		bottomLabel.setText("Copyright@mooo.com leizhifesker@gmail.com");
		bottomPanel.add(bottomLabel);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		frame.setSize(480, 360);
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2, (Toolkit
				.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2);

		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getRootPane().setDefaultButton(confirm);
		
		//初始化检查
		if(!cardRFID.getIccrf().isOpened())
			JOptionPane.showMessageDialog(frame, "请正确连接发卡器");
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
	
	/**
	 * 初始化组件
	 */
	private void initComponent() {
		System.out.println("");
	}
		
	private JMenuBar createJMenuBar() {
		JMenuBar menubar = new JMenuBar();

		JMenu menuFile = new JMenu("文件");
		JMenu menuEdit = new JMenu("发卡");
		JMenu readCard = new JMenu("读卡");
		JMenu menuPrint = new JMenu("打印");
		JMenu menuHelp = new JMenu("帮助");

		JMenuItem menuFileExit = new JMenuItem("退出");
		menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					cardRFID.destroy();
				} catch (Exception ce) {
					ce.printStackTrace();
				}
				System.exit(0);
			}
		});

		menuFile.add(menuFileExit);

		JMenuItem popWine = new JMenuItem("取酒");
		JMenuItem pushWine = new JMenuItem("加酒");
		JMenuItem ppWine = new JMenuItem("加/取酒");
		JMenuItem popAllWine = new JMenuItem("取完酒");
		JMenuItem inspection = new JMenuItem("巡检");
		//		//名单
		//		listMain.add(new JMenuItem());//班级名单
		//		listMain.add(new JMenuItem());//课程名单
		//		listMain.add(new JMenuItem());//教师名单
		//		listMain.add(new JMenuItem());//教室名单
		//		listMain.addSeparator();//划一横线

		menuEdit.add(popWine);
		menuEdit.add(pushWine);
		menuEdit.add(ppWine);
		menuEdit.add(popAllWine);
		menuEdit.add(inspection);

		JMenuItem viewCard = new JMenuItem("查看");
		JMenuItem printCard = new JMenuItem("打印");
		readCard.add(viewCard);
		readCard.add(printCard);

		menuPrint.addSeparator();
		JMenuItem printInfo = new JMenuItem("发票");
		menuPrint.add(printInfo);

		JMenuItem menuHelpAbout = new JMenuItem("关于");
		menuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "发卡系统 V1.0");
			}
		});

		menuHelp.add(menuHelpAbout);

		menubar.add(menuFile);
		menubar.add(menuEdit);
		menubar.add(readCard);
		menubar.add(menuPrint);
		menubar.add(menuHelp);
		return menubar;
	}

	public static void main(String args[]) {
		new SFWine();
	}

	
}