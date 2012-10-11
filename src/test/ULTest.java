package test;

import com.mooo.sfwine.ISO14443AAction;
import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;
import es.deusto.smartlab.rfid.iso14443a.ImplementationISO14443A;


/**
 * @author Xabier Echevarría Espinosa
 */
public class ULTest {

	public static void main(String args[]) throws InterruptedException {
		byte[] password = {(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};

		ISO14443AAction.whichPort="COM3";

		ISO14443AAction iccrf = new ISO14443AAction();
		iccrf.initSerial();
		iccrf.initCard();
		
		/*
		int page = 4;//0x00->0x0F:0x04
		int offset = 0;
		int pageSize = 4;
		
		String str = "读卡模块通讯协议读卡模块通讯协议读卡模块通讯协议卡模块通讯协议";
		byte[] buffer = new byte[4];
		byte[] wbyte;
		try {
			wbyte = str.getBytes("GBK");
			if(wbyte.length < 4){
				for(int i=0;i<wbyte.length;i++){
					buffer[i] = wbyte[i];
				}
			}else{
				for(int i=0;i<4;i++){
					buffer[i] = wbyte[i];
				}
			}
			
//			iccrf.write((byte)(page*pageSize+offset),wbyte);
			iccrf.write((byte) 0x04,wbyte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] respnse = iccrf.read((byte) 0x04);
		System.out.println(respnse);
*/
		String buf = "LZZJ001209030023";
		buf = "ZZZZ001209030039";
		String reponse;
		try {
			//request card
			reponse = iccrf.getSerialNumber();
//			System.out.println("SerialNumber:"+reponse);
			iccrf.cleanAll();
			//write UL
			iccrf.save(buf,4,0,16);
			reponse = iccrf.read(4,0);
			System.out.println("reponse:"+reponse);

			//write M1
//			iccrf.loadKey((byte)0x01, password);
//			iccrf.authentication((byte)0x01);
//			iccrf.write((byte)(1*4 + 1),buf.getBytes("gb2312"));
//			iccrf.cleanAll();
			
//			buf = "赵杰1";
//			iccrf.save(buf, 1, 1, 16);
//			buf = "000000";
//			iccrf.save(buf, 1, 2, 16);
//
//			reponse = iccrf.read(1,1);
//			System.out.println("1,1:"+reponse);
//
//			reponse = iccrf.read(1,2);
//			System.out.println("1,2:"+reponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		iccrf.beep(10);
		iccrf.destroy();
	}

}
