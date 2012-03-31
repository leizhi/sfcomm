package com.mooo.sfwine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class JMenuBarFactory {

    private static JMenuBar menuBar = null;
    private static Object menuBarLock = new Object();
    
    public static JMenuBar buildJMenuBar(){
        if (menuBar == null) {
            synchronized(menuBarLock) {
                if (menuBar == null) {
                	menuBar = new JMenuBar();
                	
            		JMenu menuFile = new JMenu("文件");
            		JMenu menuEdit = new JMenu("发卡");
            		JMenu readCard = new JMenu("读卡");
            		JMenu menuPrint = new JMenu("打印");
            		JMenu menuHelp = new JMenu("帮助");

            		JMenuItem menuFileExit = new JMenuItem("退出");
            		menuFileExit.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent e) {
            				try{

            				} catch (Exception ce) {
            					ce.printStackTrace();
            				}
            				System.exit(0);
            			}
            		});

            		menuFile.add(menuFileExit);

            		JMenuItem popWine = new JMenuItem("取酒");
            		JMenuItem pushWine = new JMenuItem("加酒");
            		JMenuItem ppWine = new JMenuItem("加/取酒");
            		JMenuItem popAllWine = new JMenuItem("取完酒");
            		JMenuItem inspection = new JMenuItem("巡检");

            		menuEdit.add(popWine);
            		menuEdit.add(pushWine);
            		menuEdit.add(ppWine);
            		menuEdit.add(popAllWine);
            		menuEdit.add(inspection);

            		JMenuItem viewCard = new JMenuItem("查看");
            		JMenuItem printCard = new JMenuItem("打印");
            		readCard.add(viewCard);
            		readCard.add(printCard);

            		menuPrint.addSeparator();
            		JMenuItem printInfo = new JMenuItem("发票");
            		menuPrint.add(printInfo);

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
