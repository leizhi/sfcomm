package test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressSample {
	static class BarThread extends Thread {
		private static int DELAY = 500;
		JProgressBar progressBar;

		public BarThread(JProgressBar bar) {
			progressBar = bar;
		}

		public void run() {
			int minimum = progressBar.getMinimum();
			int maximum = progressBar.getMaximum();
			Runnable runner = new Runnable() {
				public void run() {
					int value = progressBar.getValue();
					progressBar.setValue(value + 1);
				}
			};
			for (int i = minimum; i < maximum; i++) {
				try {
					SwingUtilities.invokeAndWait(runner);
					// Our task for each step is to just sleep
					Thread.sleep(DELAY);
				} catch (InterruptedException ignoredException) {
				} catch (InvocationTargetException ignoredException) {
				}
			}
		}
	}

	public static void main(String args[]) {
		// Initialize
		final JProgressBar aJProgressBar = new JProgressBar(0, 100);
		final JButton aJButton = new JButton("Start");
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aJButton.setEnabled(false);
				
				Thread stepper = new BarThread(aJProgressBar);
				stepper.start();

				//whileThread wt = new whileThread();
				//wt.start();
				// try
				// {
				// wt.join();
				// } catch (InterruptedException e1)
				// {
				// // TODO 自动生成 catch 块
				// e1.printStackTrace();
				// }
			}
		};
		aJButton.addActionListener(actionListener);
		JFrame theFrame = new JFrame("Progress Bars");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = theFrame.getContentPane();
		contentPane.add(aJProgressBar, BorderLayout.NORTH);
		contentPane.add(aJButton, BorderLayout.SOUTH);
		theFrame.setLocation(500, 200);
		theFrame.setSize(300, 100);

		theFrame.setVisible(true);// 显示
		theFrame.repaint();
		theFrame.validate();
	}

}

class whileThread extends Thread {
	public void run() {
		int i = 0;
		while (i < 1000000) {
			System.out.println(i++);
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JFrame newFrame = new JFrame("&&&&&&&&&&&&&&&&&&&&&&");
				newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				newFrame.setLocation(500, 400);
				newFrame.setSize(300, 100);

				newFrame.setVisible(true);// 显示
				newFrame.repaint();
				newFrame.validate();
			}
		});
	}
}
