package com.mooo.sfwine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JMenuBarFactory {

    private static JMenuBar menuBar = null;
    private static Object menuBarLock = new Object();
    
    public static JMenuBar buildJMenuBar(final JPanel bodyPanel){
        if (menuBar == null) {
            synchronized(menuBarLock) {
                if (menuBar == null) {
                	menuBar = new JMenuBar();
                	
            		JMenu menuFile = new JMenu("开始");
            		JMenu menuEdit = new JMenu("发卡");
            		JMenu readCard = new JMenu("查询");
            		JMenu menuPrint = new JMenu("打印");
            		JMenu menuHelp = new JMenu("帮助");

            		//主菜单
            		JMenuItem loginSystem = new JMenuItem("刷卡登录");
            		loginSystem.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent e) {
        					new CardLoginWindow(bodyPanel);
            			}
            		});
            		menuFile.add(loginSystem);
            		
            		JMenuItem admin = new JMenuItem("登录");
            		admin.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent e) {
        					new LoginWindow(bodyPanel);
            			}
            		});
            		menuFile.add(admin);
            		
            		JMenuItem menuFileExit = new JMenuItem("退出");
            		menuFileExit.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent e) {
            				System.exit(0);
            			}
            		});
            		menuFile.add(menuFileExit);

            		//发卡
            		JMenuItem staff = new JMenuItem("员工卡");
            		staff.addActionListener( new ActionListener() {
        				public void actionPerformed(ActionEvent e) {
        					new StaffCardWindow(bodyPanel);
        				}
        			});
            		menuEdit.add(staff);

            		JMenuItem cardId = new JMenuItem("标识卡");
            		cardId.addActionListener( new ActionListener() {
        				public void actionPerformed(ActionEvent e) {
        					new CardWindow(bodyPanel);
        				}
        			});
            		menuEdit.add(cardId);

            		JMenuItem viewStaff = new JMenuItem("员工卡");
            		JMenuItem viewCardId = new JMenuItem("标识卡");
            		readCard.add(viewStaff);
            		readCard.add(viewCardId);

            		menuPrint.addSeparator();
            		JMenuItem printStaff = new JMenuItem("员工卡");
            		JMenuItem printCardId = new JMenuItem("标识卡");
            		menuPrint.add(printStaff);
            		menuPrint.add(printCardId);

            		JMenuItem menuHelpAbout = new JMenuItem("关于");
            		menuHelpAbout.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent e) {
            				JOptionPane.showMessageDialog(SFWine.frame, "发卡系统 V1.0");
            			}
            		});

            		menuHelp.add(menuHelpAbout);

            		menuBar.add(menuFile);
            		menuBar.add(menuEdit);
            		menuBar.add(readCard);
            		menuBar.add(menuPrint);
            		menuBar.add(menuHelp);
                }
            }
        }
        return menuBar;
    }
}
