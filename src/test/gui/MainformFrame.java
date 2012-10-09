package test.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainformFrame extends JFrame implements ActionListener {
	JButton sm, cm, scm, um, exitB;

	public MainformFrame() {
		setTitle("学生信息管理系统");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(200, 200);
		sm = new JButton("学生管理");
		cm = new JButton("课程管理");
		scm = new JButton("选课管理");
		um = new JButton("用户管理");
		exitB = new JButton("退出");
		exitB.setToolTipText("安全退出系统");
		JPanel lowerPanel = new JPanel();

		lowerPanel.setLayout(new GridLayout(5, 1));
		Container c = this.getContentPane();
		lowerPanel.add(sm, "1");
		lowerPanel.add(cm, "2");
		lowerPanel.add(scm, "3");
		lowerPanel.add(um, "4");
		lowerPanel.add(exitB);
		c.add(lowerPanel);
		sm.addActionListener(this);
		cm.addActionListener(this);
		scm.addActionListener(this);
		um.addActionListener(this);
		exitB.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sm) {
			sms();
		}
		if (e.getSource() == cm) {
			cms();
		}
		if (e.getSource() == scm) {
			scms();
		}
		if (e.getSource() == um) {
			ums();
		}
		if (e.getSource() == exitB) {
			shutdown();
		}

	}

	private void shutdown() {
	}

	private void ums() {
	}

	private void scms() {
	}

	private void cms() {
	}

	private void sms() {
	}

	public static void main(String[] args) {
		new MainformFrame();
	}

	@SuppressWarnings("unused")
	private class MyPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			super.paintComponent(g);
			Image img = Toolkit.getDefaultToolkit().getImage("hello.jpg");
			g2.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

}
