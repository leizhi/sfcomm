package com.mooo.swing;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
 
/**
 * 限制JtextField的最大输入
 * @author nortan
 *
 */
public class LimitDocument extends PlainDocument {
	//只充许输入maxLength 个字符
	private int maxLength = 8;
 
	public LimitDocument(int maxLength) {
		super();
		this.maxLength = maxLength;
	}
 
	public LimitDocument(Content c, int maxLength) {
		super(c);
		this.maxLength = maxLength;
	}
	private static final long serialVersionUID = 1L;
 
	public void insertString(int offset, String s, AttributeSet a)
			throws BadLocationException {
		if (s == null || offset < 0) {  
            return;  
        }  
        for (int i = 0; i < s.length(); i++) {  
            if (getLength() > maxLength - 1) {  
                break;  
            }  
            super.insertString(offset + i, s.substring(i, i + 1),  
                    a); 
        }  
        return;
	} 
}