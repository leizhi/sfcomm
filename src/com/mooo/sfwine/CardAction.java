package com.mooo.sfwine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CardAction {
	
	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(CardAction.class);
	private static String error;

	private CardRFID cardRFID;
	
	private SimpleDateFormat dformat = new SimpleDateFormat("yyy-MM-dd");

	public CardAction(final JPanel bodyPanel,final JPanel execPanel,Card card) {
		try{
			cardRFID = new CardRFID();
			cardRFID.getIccrf().isOpened();
			//初始化检查
			if(!cardRFID.getIccrf().isOpened())
				throw new NullPointerException("请正确连接发卡器");
			
			Long cardId = cardRFID.getCardId();
			if(cardId==0)
				throw new NullPointerException("请放人电子标签或者电子卡");
			
			card.setId(cardId);
			
			CardDBObject dbObjcet = new CardDBObject();

			if(dbObjcet.isRegistr(card))
				throw new NullPointerException("卡片已经使用,请换新卡!");
			
			cardRFID.save(card.getZipCode(), 1,0,6);
			
			cardRFID.saveGBK(card.getWineryName(), 1,1,16);
			
			cardRFID.saveGBK(card.getWineJarKey(), 1,2,16);
			
			cardRFID.saveGBK(card.getWineType(), 2,0,16);
			
			cardRFID.saveGBK(card.getWineLevel(), 2,1,16);
			
			cardRFID.saveGBK(card.getAlcohol(), 2,2,16);
			
			cardRFID.saveGBK(card.getOperator(), 3,0,16);
			
			cardRFID.saveGBK(card.getSupervisorCompanyKey(), 3,1,16);
			
			cardRFID.saveGBK(card.getSupervisorName(), 3,2,16);
			
			cardRFID.saveGBK(card.getBrewingDate(), 4,0,16);
			
			Calendar rightNow = Calendar.getInstance();
			String value = dformat.format(rightNow.getTime());
			
			cardRFID.saveGBK(value, 4,1,16);
			
			cardRFID.saveGBK(card.getWineJarVolume(), 4,2,16);
			
			cardRFID.saveGBK(card.getWineVolume(), 5,0,16);
			
			cardRFID.saveGBK(card.getMaterial(), 6,0,16);
			
			cardRFID.beep();
			
			dbObjcet.save(card);
			
			
			JOptionPane.showMessageDialog(null, "发卡成功","系统提示",
					JOptionPane.INFORMATION_MESSAGE);
			}catch (NullPointerException e) {
				error = e.getMessage();
				new CardWindow(bodyPanel,execPanel,error);
				if(log.isErrorEnabled()) log.error("NullPointerException:"+e.getMessage());	
			} catch (Exception se) {
				if(log.isErrorEnabled()) log.error("Exception:"+se.getMessage());
				se.printStackTrace();
			} finally {
				cardRFID.destroy();
			}

		if(log.isDebugEnabled()) log.debug("flinsh");	}
	}