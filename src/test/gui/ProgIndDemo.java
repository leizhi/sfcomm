package test.gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProgIndDemo extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2926885043928398508L;

	public static void main(String arg[]) {
		new ProgIndDemo();
	}

	public ProgIndDemo() {
		int height;
		int width;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setLocation(200, 150);
		Container pane = getContentPane();
		pane.setLayout(null);
		Insets insets = pane.getInsets();

		JProgressBar bar = new JProgressBar(0, 100);// 创建进度条
		bar.setIndeterminate(true);// 值为 true 意味着不确定，而值为 false 则意味着普通或者确定。
		bar.setStringPainted(true); // 描绘文字
		bar.setString("等待刷卡验证"); // 设置显示文字

		Dimension dim = bar.getPreferredSize();
		int x = insets.left + 20;
		int y = insets.top + 20;
		dim.width += 100;
		bar.setBounds(x, y, dim.width, dim.height);
		pane.add(bar);

		width = x + dim.width + 20 + insets.left;
		height = y + dim.height + 40 + insets.bottom;

		setSize(width, height);
		setVisible(true);
	}
}
