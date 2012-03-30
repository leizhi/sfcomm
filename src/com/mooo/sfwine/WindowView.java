package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WindowView extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(WindowView.class);

	public WindowView() {
		super("显示");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(log.isDebugEnabled()) log.debug("haveNode->");
		JDialog dialog = new JDialog(SFWine.frame,"显示设置",true);
		
		JPanel jContentPane = new JPanel();
		
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setPreferredSize(new Dimension(380, 260));
		
		JList jList = new JList();
		jList.setLayoutOrientation(JList.VERTICAL_WRAP);

		DefaultListModel listModel = new DefaultListModel();
		
//		Vector<String> vector = DataManager.getInstance().getValues("data/classes/class", "className");
//		for(String bean:vector){
//			listModel.addElement(bean);
//		}
		
		jList.setModel(listModel);
		jList.setVisibleRowCount(8);
		
		jScrollPane.setViewportView(jList);
		
		jContentPane.add(jScrollPane,BorderLayout.WEST);
		
		JPanel rightPane = new JPanel();
		rightPane.setPreferredSize(new Dimension(100, 260));

		JButton confirm = new JButton("确定");
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
//		confirm.setAlignmentX(Component.LEFT_ALIGNMENT);
		JButton cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
//		cancel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		jContentPane.add(rightPane,BorderLayout.EAST);

		dialog.setContentPane(jContentPane);
		
		dialog.setSize(550, 320);
		dialog.setLocationRelativeTo(SFWine.frame);
		dialog.setVisible(true);
	}

}
