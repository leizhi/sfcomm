package com.mooo.sfwine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JMenuBarFactory {

    private static JMenuBar menuBar = null;
    private static Object menuBarLock = new Object();
    
    private static Color bg = new Color(30,178,239);
    private static Color fg = Color.WHITE;

    public static JMenuBar buildJMenuBar(final JPanel bodyPanel){
        if (menuBar == null) {
            synchronized(menuBarLock) {
                if (menuBar == null) {
                	menuBar = new JMenuBar();
                	menuBar.setBackground(bg);
                	menuBar.setBorder(BorderFactory.createLineBorder(bg));

            		final CardAction cardAction = new CardAction(bodyPanel);
            		final UserAction userAction = new UserAction(bodyPanel);
            		final CardLoginAction cardLoginAction = new CardLoginAction(bodyPanel);

                	//开始
            		JMenu menuFile = new JMenu("开始");
            		menuFile.setBackground(bg);
            		menuFile.setForeground(fg);
            		
            		//主菜单
            		JMenuItem loginSystem = new JMenuItem("刷卡登录");
            		loginSystem.setBackground(bg);
            		loginSystem.setForeground(fg);
            		loginSystem.addActionListener(new ActionListener() {
            			@Override
						public void actionPerformed(ActionEvent e) {
            				cardLoginAction.promptCardLogin();
            			}
            		});
            		menuFile.add(loginSystem);
            		
            		JMenuItem admin = new JMenuItem("登录");
            		admin.setBackground(bg);
            		admin.setForeground(fg);

            		admin.addActionListener(new ActionListener() {
            			@Override
						public void actionPerformed(ActionEvent e) {
            				userAction.promptLogin();
            			}
            		});
            		menuFile.add(admin);
            		
            		JMenuItem menuFileExit = new JMenuItem("退出");
            		menuFileExit.setBackground(bg);
            		menuFileExit.setForeground(fg);

            		menuFileExit.addActionListener(new ActionListener() {
            			@Override
						public void actionPerformed(ActionEvent e) {
            				System.exit(0);
            			}
            		});
            		menuFile.add(menuFileExit);
            		
            		menuBar.add(menuFile);

            		//发卡
            		JMenu menuEdit = new JMenu("发卡");
            		menuEdit.setBackground(bg);
            		menuEdit.setForeground(fg);
            		//
            		JMenuItem staff = new JMenuItem("员工卡");
            		staff.setBackground(bg);
            		staff.setForeground(fg);

            		staff.addActionListener( new ActionListener() {
        				@Override
						public void actionPerformed(ActionEvent e) {
        					userAction.promptRegister();
        				}
        			});
            		menuEdit.add(staff);

            		JMenuItem cardId = new JMenuItem("标识卡");
            		cardId.setBackground(bg);
            		cardId.setForeground(fg);

            		cardId.addActionListener( new ActionListener() {
        				@Override
						public void actionPerformed(ActionEvent e) {
        					cardAction.promptNewWineCard();
        				}
        			});
            		menuEdit.add(cardId);
            		
            		menuBar.add(menuEdit);

            		JMenu readCard = new JMenu("查询");
            		readCard.setBackground(bg);
            		readCard.setForeground(fg);

            		JMenuItem viewStaff = new JMenuItem("员工卡");
            		viewStaff.setBackground(bg);
            		viewStaff.setForeground(fg);
            		viewStaff.addActionListener( new ActionListener() {
        				@Override
						public void actionPerformed(ActionEvent e) {
        					cardAction.listCard();
        				}
        			});
            		readCard.add(viewStaff);

            		JMenuItem viewCardId = new JMenuItem("标识卡");
            		viewCardId.setBackground(bg);
            		viewCardId.setForeground(fg);
            		viewCardId.addActionListener( new ActionListener() {
        				@Override
						public void actionPerformed(ActionEvent e) {
        					cardAction.viewWineCard();
        				}
        			});
            		readCard.add(viewCardId);

            		menuBar.add(readCard);

            		JMenu menuPrint = new JMenu("打印");
            		menuPrint.setBackground(bg);
            		menuPrint.setForeground(fg);
            		
            		JMenuItem printStaff = new JMenuItem("员工卡");
            		printStaff.setBackground(bg);
            		printStaff.setForeground(fg);
            		menuPrint.add(printStaff);

//            		menuPrint.addSeparator();

            		JMenuItem printCardId = new JMenuItem("标识卡");
            		printCardId.setBackground(bg);
            		printCardId.setForeground(fg);
            		menuPrint.add(printCardId);
            		
            		menuBar.add(menuPrint);

            		JMenu menuHelp = new JMenu("帮助");
            		menuHelp.setBackground(bg);
            		menuHelp.setForeground(fg);

            		JMenuItem menuHelpAbout = new JMenuItem("关于");
            		menuHelpAbout.setBackground(bg);
            		menuHelpAbout.setForeground(fg);
            		menuHelpAbout.addActionListener(new ActionListener() {
            			@Override
						public void actionPerformed(ActionEvent e) {
            				JOptionPane.showMessageDialog(SFWine.frame, "发卡系统 V1.0");
            			}
            		});

            		menuHelp.add(menuHelpAbout);

            		menuBar.add(menuHelp);
                }
            }
        }
        return menuBar;
    }
}
