package gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class TestTable extends JFrame {

	JSplitPane split;
	
	public TestTable() {
		init();
	}
	
	private void init() {
		JTextArea info = new JTextArea();
		JScrollPane jspInfo = new JScrollPane();
		jspInfo.getViewport().add(info);
		JTextArea send = new JTextArea();
		JScrollPane jspSend = new JScrollPane();
		jspSend.getViewport().add(send);
		split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, jspInfo, jspSend);
		add(split);
		
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setVisible(true);
		split.setDividerLocation(0.7);
		this.addComponentListener(new ComponentAdapter(){
            @Override
			public void componentResized(ComponentEvent e) {
            	split.setDividerLocation(0.7);
            }
        });
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestTable();
	}
}
