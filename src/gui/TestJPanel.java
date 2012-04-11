package gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TestJPanel {

	JLabel Label1;
	JLabel Label2;
	JLabel Label3;
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	JPanel panel4;
	JTextField TextField1;
	JTextField TextField2;
	JTextField TextField3;
	JButton push;

	public TestJPanel() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(4, 0)); // 用GridLayout布局实现
		Label1 = new JLabel("To");
		Label1.setAlignmentX(Component.LEFT_ALIGNMENT);
		Label2 = new JLabel("CC");
		Label3 = new JLabel("Bcc");

		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();

		panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

		TextField1 = new JTextField(30);
		TextField2 = new JTextField(30);
		TextField3 = new JTextField(45);

		push = new JButton("Send");
		panel4.add(push);
		// push.addActionListener(new ButtonListener());

		panel1.add(Label1);
		panel1.add(TextField1);

		panel2.add(Label2);
		panel2.add(TextField2);

		panel3.add(Label3);
		panel3.add(TextField3);
		frame.add(panel1);
		frame.add(panel2);
		frame.add(panel3);
		frame.add(panel4);
		
		frame.setVisible(true);
		frame.setSize(670, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new TestJPanel();
	}

}
