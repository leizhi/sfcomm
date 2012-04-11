package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SFWine {
	private static Log log = LogFactory.getLog(SFWine.class);

	public final static JFrame frame= new JFrame("中国原酒产业联盟发卡系统");

	private JPanel bottomPanel;
	
	private JPanel bodyPanel;
	
//	private DisplayMode displayMode;
	public SFWine() {
		Font defFont = new Font("MS Song", 255, 12);
//		UIManager.getDefaults().put("TextField.inactiveForeground", Color.darkGray);
//		UIManager.getDefaults().put("Button.font",defFont);
//		UIManager.getDefaults().put("ComboBox.font",defFont);
//		UIManager.getDefaults().put("CheckBox.font",defFont);
//		UIManager.getDefaults().put("Label.font", defFont);
//		UIManager.getDefaults().put("Menu.font", defFont);
//		UIManager.getDefaults().put("MenuBar.font", defFont);
//		UIManager.getDefaults().put("MenuItem.font", defFont);
//		UIManager.getDefaults().put("RadioButtonMenuItem.font", defFont);
//		UIManager.getDefaults().put("TabbedPane.font",defFont);
//		UIManager.getDefaults().put("ToggleButton.font",defFont);
//		UIManager.getDefaults().put("TitledBorder.font",defFont);
//		UIManager.getDefaults().put("List.font",defFont);
		frame.setFont(defFont);

//		displayMode=new DisplayMode(1024,580,32,75);
//		GraphicsDevice device=GraphicsEnvironment.
//		getLocalGraphicsEnvironment().
//		getDefaultScreenDevice();
		
		//JFrame至全屏
//		device.setFullScreenWindow(frame);
		//改变显示方式
//		device.setDisplayMode(displayMode);
		//退出全屏
//		device.setFullScreenWindow(null);
		//initialize LoginGUI
		
		Color bg = new Color(71,124,171);
		
		Color fg = Color.WHITE;
		
		bodyPanel = new JPanel();
		bodyPanel.setPreferredSize(new Dimension(1003, 568));
		bodyPanel.setLayout(null);
		bodyPanel.setBackground(bg);
		bodyPanel.setForeground(fg);
		bodyPanel.setBorder(BorderFactory.createLineBorder(bg));
		//设置背景图片
        URL url = SFWine.class.getResource("bg.png");
        ImageIcon img = new ImageIcon(url);
        JLabel background = new JLabel(img);
        bodyPanel.add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		frame.getContentPane().add(bodyPanel,BorderLayout.NORTH);

		//start Login
//		new LoginWindow(bodyPanel);

		frame.setJMenuBar(JMenuBarFactory.buildJMenuBar(bodyPanel));
		
		bottomPanel = new JPanel();
//		bodyPanel.setPreferredSize(new Dimension(1024, 60));
		
		JLabel bottomLabel = new JLabel();
//		bottomLabel.setText("Copyright@mooo.com leizhifesker@gmail.com");
//		bottomLabel.setText("版权所有 违者必究");
		bottomLabel.setText("Copyright@原酒商贸 leizhifesker@gmail.com");
		bottomPanel.add(bottomLabel,BorderLayout.CENTER);
		bottomPanel.setBackground(new Color(29,177,238));
		
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		bottomPanel.validate();
		bottomPanel.repaint();
		
//		int block = 103;
//		
//		int WidthBlock = Toolkit.getDefaultToolkit().getScreenSize().width/block;
//		int heightBlock = Toolkit.getDefaultToolkit().getScreenSize().height/block;
//
//		frame.setSize(block*WidthBlock, heightBlock*block);
		
		frame.setSize(1003, 600);
//		frame.setBackground(bg);
//		frame.setForeground(fg);

//		frame.setClientSize(fullWidth*(1-0.618), fullheight*(1-0.618));
//103 618/6 618/1000
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2, (Toolkit
				.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getRootPane().setDefaultButton(confirm);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int sCommand = JOptionPane.showConfirmDialog(null, "确定关闭吗？", "温馨提示",
						JOptionPane.YES_NO_OPTION);
				if (sCommand == 0) {
					System.exit(0); // 关闭
				}
			}
		});
		
		frame.setResizable(false);//缩放
		frame.setVisible(true);//显示
		frame.repaint();
		
		if(log.isErrorEnabled()) log.debug("init end");
	}
	
	public static void main(String args[]) {
		new SFWine();
	}
	
}