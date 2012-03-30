package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ListenerTest {
    public ListenerTest(){
        JFrame jframe = new JFrame("监听器测试");
        jframe.setSize(300, 200);
        jframe.setLocation(500, 200);
        final JTextField jtext = new JTextField(10);
        JButton jbutton = new JButton("确定");
        jframe.add(jtext);
        jframe.add(jbutton);
        jframe.setLayout(new GridLayout(2,1));
        
        //为按钮添加行为监听器
        jbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                if(!jtext.getText().equals("123456")){
                    //如果输入错误
                    JOptionPane.showMessageDialog(null, "输入错误",
                            "系统提示", JOptionPane.ERROR_MESSAGE);
                    //提示错误
                    jtext.setText("");
                    //清空输入文本框
                    jtext.requestFocus();
                    //文本框获取焦点
                }else{
                    //如果输入正确
                    JOptionPane.showMessageDialog(null, "输入正确",
                            "系统提示", JOptionPane.INFORMATION_MESSAGE);
                    //提示正确
                }            
            }
            
        });
        
        //为按钮添加键盘适配器
        jtext.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        //如果按下回车键
                        if(!jtext.getText().equals("123456")){
                            //如果输入错误
                            JOptionPane.showMessageDialog(null, "输入错误",
                                    "系统提示", JOptionPane.ERROR_MESSAGE);
                            //提示错误
                            jtext.setText("");
                            //清空输入文本框
                            jtext.requestFocus();
                            //文本框获取焦点
                        }else{
                            //如果输入正确
                            JOptionPane.showMessageDialog(null, "输入正确",
                                    "系统提示", JOptionPane.INFORMATION_MESSAGE);
                            //提示正确
                        }            
                    }
                }
        });
        
        jframe.setVisible(true);
        
    
    }
    public static void main(String[] args) {
        new ListenerTest();
    }
}
