package gui;

import java.awt.event.*;
import javax.swing.*;

public class TestKey extends JFrame implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5622523995784515302L;

	public TestKey() {
		JPanel j = new JPanel();
		JButton b = new JButton("button ");
		b.addKeyListener(this); // <----------------here
		j.add(b);
		
		getContentPane().add(j);
		addKeyListener(this);

		setSize(200, 100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
		JOptionPane.showMessageDialog(null, "succeed ", "Show ",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void main(String args[]) {
		new TestKey();
	}
}
