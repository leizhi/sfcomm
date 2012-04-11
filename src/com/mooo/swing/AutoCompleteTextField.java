package com.mooo.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AutoCompleteTextField {

    private JFrame frame;
    private ArrayList<String> listSomeString = new ArrayList<String>();
    private Java2sAutoTextField someTextField = new Java2sAutoTextField(listSomeString);
    private ArrayList<String> listSomeAnotherString = new ArrayList<String>();
    private Java2sAutoComboBox someComboBox = new Java2sAutoComboBox(listSomeAnotherString);

    public AutoCompleteTextField() {
        listSomeString.add("-");
        listSomeString.add("Snowboarding");
        listSomeString.add("Rowing");
        listSomeString.add("Knitting");
        listSomeString.add("Speed reading");
        listSomeString.add("Pool");
        listSomeString.add("None of the above");
        listSomeString.add("中国1");
        listSomeString.add("中国2");
        listSomeString.add("中国3..");
        listSomeString.add("中国5");
        listSomeString.add("中国4");
//
        listSomeAnotherString.add("-");
        listSomeAnotherString.add("XxxZxx Snowboarding");
        listSomeAnotherString.add("AaaBbb Rowing");
        listSomeAnotherString.add("CccDdd Knitting");
        listSomeAnotherString.add("Eee Fff Speed reading");
        listSomeAnotherString.add("Eee Fff Pool");
        listSomeAnotherString.add("Eee Fff None of the above");
        listSomeAnotherString.add("中国1");
        listSomeAnotherString.add("中国2");
        listSomeAnotherString.add("中国3..");
        listSomeAnotherString.add("中国5");
        listSomeAnotherString.add("中国4");
//
        someTextField.setFont(new Font("Serif", Font.BOLD, 16));
        someTextField.setForeground(Color.black);
        someTextField.setBackground(Color.orange);
        someTextField.setName("someTextField");
        someTextField.setDataList(listSomeString);
//
        someComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        someComboBox.setFont(new Font("Serif", Font.BOLD, 16));
        someComboBox.setForeground(Color.black);
        someComboBox.setBackground(Color.YELLOW);
        someComboBox.getEditor().selectAll();
        someComboBox.getEditor().getEditorComponent().setBackground(Color.YELLOW);
        ((JTextField) someComboBox.getEditor().getEditorComponent()).setDisabledTextColor(Color.black);
        someComboBox.setName("someComboBox");
        someComboBox.setDataList(listSomeAnotherString);
//
        frame = new JFrame();
        frame.setLayout(new GridLayout(0, 1, 10, 10));
        frame.add(someTextField);
        frame.add(someComboBox);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.pack();
        frame.setVisible(true);
//
        SwingUtilities.invokeLater(new Runnable() {

            @Override
			public void run() {
                someTextField.setText("-");
                someComboBox.getEditor().setItem(0);
                someComboBox.getEditor().selectAll();
                someTextField.grabFocus();
                someTextField.requestFocus();
                someTextField.setText(someTextField.getText());
                someTextField.selectAll();
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
//                AutoCompleteTextField aCTF = 
                	new AutoCompleteTextField();
            }
        });
    }
}

