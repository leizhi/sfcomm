package com.mooo.sfwine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Global {

	public static Color fg = Color.WHITE;
	public static Color bg = Color.GREEN;

	private Font font;
	
	public static String message;
	
	private JPanel bottomPanel;
	private JPanel bodyPanel;
	
	private SFMenuBar menuBar;
	
	private CardAction cardAction;
	private UserAction userAction;
	private CardLoginAction cardLoginAction;
    
	public Global(){
		font = new Font("MS Song", 255, 12);
//		UIManager.getDefaults().put("TextField.inactiveForeground", Color.darkGray);
//		UIManager.getDefaults().put("Button.font",defFont);
//		UIManager.getDefaults().put("ComboBox.font",defFont);
//		UIManager.getDefaults().put("CheckBox.font",defFont);
//		UIManager.getDefaults().put("Label.font", defFont);
//		UIManager.getDefaults().put("Menu.font", defFont);
//		UIManager.getDefaults().put("MenuBar.font", defFont);
//		UIManager.getDefaults().put("MenuItem.font", defFont);
//		UIManager.getDefaults().put("RadioButtonMenuItem.font", defFont);
//		UIManager.getDefaults().put("TabbedPane.font",defFont);
//		UIManager.getDefaults().put("ToggleButton.font",defFont);
//		UIManager.getDefaults().put("TitledBorder.font",defFont);
//		UIManager.getDefaults().put("List.font",defFont);
		
		bodyPanel = new JPanel();
		bodyPanel.setPreferredSize(new Dimension(800, 568));
		bodyPanel.setLayout(null);
		bodyPanel.setBackground(bg);
		bodyPanel.setForeground(fg);
		bodyPanel.setBorder(BorderFactory.createLineBorder(bg));
		
		bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(1024, 60));
		bottomPanel.setLayout(null);
		bottomPanel.setBackground(bg);
		bottomPanel.setForeground(fg);
		
		JLabel bottomLabel = new JLabel();
		bottomLabel.setText("Copyright@原酒商贸 leizhifesker@gmail.com");

		bottomPanel.add(bottomLabel,BorderLayout.CENTER);
		bottomPanel.setVisible(true);
		bottomPanel.validate();
		bottomPanel.repaint();
		
		menuBar = new SFMenuBar();
		
		cardAction = new CardAction();
		userAction = new UserAction();
		cardLoginAction = new CardLoginAction();
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}

	public JPanel getBottomPanel() {
		return bottomPanel;
	}
	public void setBottomPanel(JPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}
	public JPanel getBodyPanel() {
		return bodyPanel;
	}
	public void setBodyPanel(JPanel bodyPanel) {
		this.bodyPanel = bodyPanel;
	}

	public SFMenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(SFMenuBar menuBar) {
		this.menuBar = menuBar;
	}
	
	public CardAction getCardAction() {
		return cardAction;
	}

	public void setCardAction(CardAction cardAction) {
		this.cardAction = cardAction;
	}

	public UserAction getUserAction() {
		return userAction;
	}

	public void setUserAction(UserAction userAction) {
		this.userAction = userAction;
	}

	public CardLoginAction getCardLoginAction() {
		return cardLoginAction;
	}

	public void setCardLoginAction(CardLoginAction cardLoginAction) {
		this.cardLoginAction = cardLoginAction;
	}
}
