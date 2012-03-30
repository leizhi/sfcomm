package gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KeyEventDemo extends JFrame {
	private Keyboardpanel p = new Keyboardpanel();

	public KeyEventDemo() {
		add(p);
		p.setFocusable(true);

	}

	static class Keyboardpanel extends JPanel {
		private int x = 100;
		private int y = 100;
		private char keychar = 'A';

		public Keyboardpanel() {

			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						y += 10;
						break;
					case KeyEvent.VK_UP:
						y -= 10;
						break;
					case KeyEvent.VK_LEFT:
						x -= 10;
						break;
					case KeyEvent.VK_RIGHT:
						x += 10;
						break;
					default:
						keychar = e.getKeyChar();

					}

					repaint();

				}

			});

		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));

			g.drawString(String.valueOf(keychar), x, y);

		}

	}

	public static void main(String[] args) {

		KeyEventDemo f = new KeyEventDemo();

		f.setTitle("KeyEvent");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.setLocationRelativeTo(null);

		f.setSize(400, 300);

		f.setVisible(true);

	}
}
