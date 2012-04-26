package com.mooo.sfwine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KeyAction extends KeyAdapter {
	
	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(KeyAction.class);

	@Override
	public void keyPressed(KeyEvent e) {
    	System.out.println("KeyEvent:"+e.getKeyCode());
    	
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
                //输入正确
                JOptionPane.showMessageDialog(null, "输入正确","系统提示",
                		JOptionPane.INFORMATION_MESSAGE);
        }
		if(log.isDebugEnabled()) log.debug("init end");
	}
	
}