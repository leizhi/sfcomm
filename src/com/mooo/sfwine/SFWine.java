package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.deusto.smartlab.rfid.SerialManager;

public class SFWine {
	private static Log log = LogFactory.getLog(SFWine.class);

	public final static JFrame frame= new JFrame("中国原酒产业联盟发卡系统");
	
	public final static Global global= new Global();

	public SFWine(){
		frame.setFont(global.getFont());
	
	//	displayMode=new DisplayMode(1024,580,32,75);
	//	GraphicsDevice device=GraphicsEnvironment.
	//	getLocalGraphicsEnvironment().
	//	getDefaultScreenDevice();
		
		//JFrame至全屏
	//	device.setFullScreenWindow(frame);
		//改变显示方式
	//	device.setDisplayMode(displayMode);
		//退出全屏
	//	device.setFullScreenWindow(null);
		//initialize LoginGUI
	    int x, y,wm,hight,wt;

		x = 340;
		y = 250;
		
		wm = 110;
		hight = 20;
		wt = 120;
		
		x += 10;
		y += 10;
		
	    JLabel hostMsg = new JLabel("服务器地址:");
	    hostMsg.setBounds(x,y,wm,hight);
	    hostMsg.setForeground(Color.WHITE);
	    hostMsg.setOpaque(false);
		SFWine.global.getBodyPanel().add(hostMsg);
		
		final JTextField hostName = new JTextField();
		hostName.setBounds(x+wm,y,wt,hight);//一个字符9 point
		hostName.setText(SFClient.host);
		SFWine.global.getBodyPanel().add(hostName);
		
		y += hight;
		
		JLabel portMsg = new JLabel("服务器端口:");
		portMsg.setBounds(x,y,wm,hight);
		portMsg.setForeground(Color.WHITE);
		SFWine.global.getBodyPanel().add(portMsg);
		
		final JTextField hostPort = new JTextField();
		hostPort.setBounds(x+wm,y,wt,hight);//一个字符9 point
		hostPort.setText(SFClient.port.toString());
		SFWine.global.getBodyPanel().add(hostPort);
		
		y += hight;
		
		JLabel serialMsg = new JLabel("串口端口:");
		serialMsg.setBounds(x,y,wm,hight);
		serialMsg.setForeground(Color.WHITE);
		SFWine.global.getBodyPanel().add(serialMsg);
		
		final JComboBox whichPort = new JComboBox();
		whichPort.setBounds(x+wm,y,wt,hight);//一个字符9 point
		whichPort.setSelectedItem(whichPort.getSelectedItem());
		
		List<String> ports = new SerialManager().getPorts();
		for(String value:ports){
			whichPort.addItem(value);
		}
		SFWine.global.getBodyPanel().add(whichPort);
		
		whichPort.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ISO14443AAction.whichPort=e.getItem().toString();
			}
		});
		
		y += hight;
		final JLabel messageLabel= new JLabel();

		JButton connect = new JButton("连接/断开");
		connect.requestFocus();
		connect.setBounds(x+wm,y,100,hight);//一个字符9 point
		connect.setBackground(new Color(105,177,35));
		connect.setForeground(Color.WHITE);

		connect.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ISO14443AAction.whichPort=whichPort.getSelectedItem().toString();

					if(SFClient.isOpenNetwork()){
						SFClient.getInstance().end();
						messageLabel.setText("断开连接");
						messageLabel.setForeground(Color.GREEN);
					}else{
						SFClient.host = hostName.getText();
						SFClient.port = new Integer(hostPort.getText());
						SFClient.connect();
						if(SFClient.isOpenNetwork()){
							messageLabel.setText("连接成功");
							messageLabel.setForeground(Color.GREEN);
						}else{
							messageLabel.setText("连接失败");
							messageLabel.setForeground(Color.RED);
						}
					}
				}
			});
		SFWine.global.getBodyPanel().add(connect);

		y += hight;
		
		messageLabel.setBounds(x,y,wm+wt,hight);
		SFWine.global.getBodyPanel().add(messageLabel);
		
		//设置背景图片
		URL url = SFWine.class.getResource("bg.png");
		ImageIcon img = new ImageIcon(url);
		JLabel background = new JLabel(img);
		SFWine.global.getBodyPanel().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		
		SFWine.global.getBodyPanel().setVisible(true);
		SFWine.global.getBodyPanel().validate();//显示
		SFWine.global.getBodyPanel().repaint();
		
		frame.getContentPane().add(global.getBodyPanel(),BorderLayout.NORTH);
//		((JComponent) frame.getContentPane()).setOpaque(true);
		//start Login
		frame.setJMenuBar(global.getMenuBar());
		frame.getContentPane().add(global.getBottomPanel(), BorderLayout.SOUTH);
		
		
	//	int block = 103;
	//	
	//	int WidthBlock = Toolkit.getDefaultToolkit().getScreenSize().width/block;
	//	int heightBlock = Toolkit.getDefaultToolkit().getScreenSize().height/block;
	//
	//	frame.setSize(block*WidthBlock, heightBlock*block);
		
		frame.setSize(1003, 600);
	//	frame.setBackground(bg);
	//	frame.setForeground(fg);
	
	//	frame.setClientSize(fullWidth*(1-0.618), fullheight*(1-0.618));
	//103 618/6 618/1000
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2, (Toolkit
				.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2);
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.getRootPane().setDefaultButton(confirm);
		
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
		frame.setVisible(true);// 显示
		frame.repaint();
		frame.validate();
		
		if(log.isErrorEnabled()) log.debug("init end");
	}
	
	public static void main(String args[]) {
		new SFWine();
	}
}
