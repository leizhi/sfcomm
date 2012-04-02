package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
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
	
	public SFWine() {
		frame.setFont(new Font("MS Song", 0, 25));

		//initialize LoginGUI
		
		bodyPanel = new JPanel();
		bodyPanel.setPreferredSize(new Dimension(1024, 580));
		bodyPanel.setBorder(BorderFactory.createLineBorder(null));
		bodyPanel.setLayout(null);
		frame.getContentPane().add(bodyPanel,BorderLayout.NORTH);

		//start Login
//		new LoginWindow(bodyPanel);

		frame.setJMenuBar(JMenuBarFactory.buildJMenuBar(bodyPanel));

		bottomPanel = new JPanel();
//		bodyPanel.setPreferredSize(new Dimension(1024, 12));
		
		JLabel bottomLabel = new JLabel();
//		bottomLabel.setText("Copyright@mooo.com leizhifesker@gmail.com");
//		bottomLabel.setText("版权所有 违者必究");
		bottomLabel.setText("Copyright@原酒商贸 leizhifesker@gmail.com");
		bottomPanel.add(bottomLabel,BorderLayout.CENTER);
		
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		bottomPanel.validate();
		bottomPanel.repaint();
		
//		int block = 103;
//		
//		int WidthBlock = Toolkit.getDefaultToolkit().getScreenSize().width/block;
//		int heightBlock = Toolkit.getDefaultToolkit().getScreenSize().height/block;
//
//		frame.setSize(block*WidthBlock, heightBlock*block);
		
		frame.setSize(1024, 680);

//		frame.setClientSize(fullWidth*(1-0.618), fullheight*(1-0.618));
//103 618/6 618/1000
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2, (Toolkit
				.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getRootPane().setDefaultButton(confirm);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int sCommand = JOptionPane.showConfirmDialog(null, "确定关闭吗？", "温馨提示",
						JOptionPane.YES_NO_OPTION);
				if (sCommand == 0) {
					System.exit(0); // 关闭
				}
			}
		});
//		frame.setResizable(false);
		frame.setResizable(true);

		frame.setVisible(true);//显示
		frame.repaint();
		
		if(log.isErrorEnabled()) log.debug("init end");
	}
	
	public static void main(String args[]) {
		new SFWine();
	}
	
}