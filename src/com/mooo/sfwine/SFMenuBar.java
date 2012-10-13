package com.mooo.sfwine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class SFMenuBar  extends  JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3248560207642843767L;

    public SFMenuBar(){
        	setBackground(Global.bg);
        	setBorder(BorderFactory.createLineBorder(Global.fg));

        	//开始
    		JMenu menuFile = new JMenu("开始");
    		menuFile.setBackground(Global.bg);
    		menuFile.setForeground(Global.fg);
    		
    		//主菜单
    		JMenuItem loginSystem = new JMenuItem("刷卡登录");
    		loginSystem.setBackground(Global.bg);
    		loginSystem.setForeground(Global.fg);
    		loginSystem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getCardLoginAction().promptCardLogin();
    			}
    		});
    		menuFile.add(loginSystem);
    		
    		JMenuItem admin = new JMenuItem("登录");
    		admin.setBackground(Global.bg);
    		admin.setForeground(Global.fg);

    		admin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getUserAction().promptLogin();
    			}
    		});
    		menuFile.add(admin);
    		
    		JMenuItem menuFileExit = new JMenuItem("退出");
    		menuFileExit.setBackground(Global.bg);
    		menuFileExit.setForeground(Global.fg);

    		menuFileExit.addActionListener(new ActionListener() {
    			@Override
				public void actionPerformed(ActionEvent e) {
    				System.exit(0);
    			}
    		});
    		menuFile.add(menuFileExit);
    		
    		add(menuFile);

    		//发卡
    		JMenu menuEdit = new JMenu("发卡");
    		menuEdit.setBackground(Global.bg);
    		menuEdit.setForeground(Global.fg);
    		//
    		JMenuItem staff = new JMenuItem("员工卡");
    		staff.setBackground(Global.bg);
    		staff.setForeground(Global.fg);

    		staff.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getUserAction().promptRegister();
				}
			});
    		menuEdit.add(staff);

    		JMenuItem cardId = new JMenuItem("标识卡");
    		cardId.setBackground(Global.bg);
    		cardId.setForeground(Global.fg);

    		cardId.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getCardAction().promptNewWineCard();
				}
			});
    		menuEdit.add(cardId);
    		
    		add(menuEdit);

    		JMenuItem restStaff = new JMenuItem("重置员工卡");
    		restStaff.setBackground(Global.bg);
    		restStaff.setForeground(Global.fg);

    		restStaff.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getUserAction().promptRestStaff();
				}
			});
    		menuEdit.add(restStaff);
    		
    		add(menuEdit);
    		/*
    		JMenu readCard = new JMenu("查询");
    		readCard.setBackground(Global.bg);
    		readCard.setForeground(Global.fg);

    		JMenuItem viewStaff = new JMenuItem("员工卡");
    		viewStaff.setBackground(Global.bg);
    		viewStaff.setForeground(Global.fg);
    		viewStaff.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getCardAction().listCard();
				}
			});
    		readCard.add(viewStaff);

    		JMenuItem viewCardId = new JMenuItem("标识卡");
    		viewCardId.setBackground(Global.bg);
    		viewCardId.setForeground(Global.fg);
    		viewCardId.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SFWine.global.getCardAction().viewWineCard();
				}
			});
    		readCard.add(viewCardId);

    		add(readCard);

    		JMenu menuPrint = new JMenu("打印");
    		menuPrint.setBackground(Global.bg);
    		menuPrint.setForeground(Global.fg);
    		
    		JMenuItem printStaff = new JMenuItem("员工卡");
    		printStaff.setBackground(Global.bg);
    		printStaff.setForeground(Global.fg);
    		menuPrint.add(printStaff);

//          menuPrint.addSeparator();

    		JMenuItem printCardId = new JMenuItem("标识卡");
    		printCardId.setBackground(Global.bg);
    		printCardId.setForeground(Global.fg);
    		menuPrint.add(printCardId);
    		
    		add(menuPrint);
*/
    		JMenu menuHelp = new JMenu("帮助");
    		menuHelp.setBackground(Global.bg);
    		menuHelp.setForeground(Global.fg);

    		JMenuItem menuHelpAbout = new JMenuItem("关于");
    		menuHelpAbout.setBackground(Global.bg);
    		menuHelpAbout.setForeground(Global.fg);
    		menuHelpAbout.addActionListener(new ActionListener() {
    			@Override
				public void actionPerformed(ActionEvent e) {
    				JOptionPane.showMessageDialog(SFWine.frame, "发卡系统 V1.1");
    			}
    		});

    		menuHelp.add(menuHelpAbout);

    		add(menuHelp);
            }
    }