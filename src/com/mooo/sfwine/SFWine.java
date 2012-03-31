package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SFWine {
	private static Log log = LogFactory.getLog(SFWine.class);

	public final static JFrame frame= new JFrame("源酒产业联盟发卡系统");

	private JPanel bottomPanel;
	
	private JPanel bodyPanel;
	private JPanel execPanel;

	public SFWine() {
		frame.setFont(new Font("MS Song", 0, 25));

		frame.setJMenuBar(JMenuBarFactory.buildJMenuBar());
		
		//init Login
		bodyPanel = new JPanel();
		bodyPanel.setPreferredSize(new Dimension(400, 100));
		bodyPanel.setBorder(BorderFactory.createLineBorder(null));
		bodyPanel.setLayout(null);
		
		execPanel = new JPanel();
		execPanel.setPreferredSize(new Dimension(80, 100));
		execPanel.setBorder(BorderFactory.createLineBorder(null));
		
		frame.getContentPane().add(bodyPanel, BorderLayout.WEST);
		frame.getContentPane().add(execPanel, BorderLayout.EAST);
		
		//start Login
		new LoginWindow(bodyPanel,execPanel);

		bottomPanel = new JPanel();
		JLabel bottomLabel = new JLabel();
//		bottomLabel.setText("Copyright@mooo.com leizhifesker@gmail.com");
//		bottomLabel.setText("版权所有 违者必究");
		bottomLabel.setText("Copyright@源酒商贸 leizhifesker@gmail.com");
		bottomPanel.add(bottomLabel);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		bottomPanel.validate();
		bottomPanel.repaint();
		
		frame.setSize(480, 400);
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2, (Toolkit
				.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getRootPane().setDefaultButton(confirm);
		
		frame.setResizable(false);
		frame.setVisible(true);//显示
		frame.repaint();
		
		if(log.isErrorEnabled()) log.debug("init end");
	}
	
	public static void main(String args[]) {
		new SFWine();
	}
	
}