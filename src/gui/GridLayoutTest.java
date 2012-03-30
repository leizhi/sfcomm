package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GridLayoutTest extends JFrame {
	JButton ok;
	JButton canel;
	JTextField text = new JTextField(15);
	JPasswordField pwd = new JPasswordField(15);
	JTextArea ta = new JTextArea(4, 15);
	JLabel username = new JLabel("用户名:");
	JLabel password = new JLabel("密  码:");
	JLabel area = new JLabel("正  文:");

	GridLayoutTest() {
		super("提交表单");
		this.setSize(250, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridLayout layout = new GridLayout(4, 2, 10, 10);
		JPanel p = new JPanel();
		ok = new JButton("确认");
		canel = new JButton("取消");
		// 密码符号
		pwd.setEchoChar('#');
		// 文本域自动换行。
		ta.setLineWrap(true);
		p.setLayout(layout);
		p.add(username);
		p.add(text);
		p.add(password);
		p.add(pwd);
		p.add(area);
		p.add(ta);
		p.add(ok);
		p.add(canel);
		this.setContentPane(p);
	}

	public static void main(String[] args) {
		GridLayoutTest g = new GridLayoutTest();
		g.show();
	}
}
