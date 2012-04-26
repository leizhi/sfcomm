package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

public class TestDate {
	public static SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
	
	private JTextField brewingDate;
	private JCalendarButton brewingCalendar;
	
	private JPanel bodyPanel;
	
	public TestDate(){
		int x, y,width,hight;

		x = 340;
		y = 140;
		
		width = 80;
		hight = 20;
		
		x += 10;
		y += 10;
		
		brewingCalendar = new JCalendarButton();
		brewingCalendar.setLocale(new Locale("zh","CN"));
		
		brewingCalendar.setBounds(x+width+100,y,20,hight);
		brewingCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() instanceof Date){
					Date date=(Date) evt.getNewValue();
					String dateString = "";
					if (date != null)
						dateString = dformat.format(date);
					
					brewingDate.setText(dateString);
					brewingCalendar.setTargetDate(date);
				}
			}
		});
		
		bodyPanel.add(brewingCalendar);
	}
}
