package test;

import com.mooo.sfwine.ISO14443AAction;
import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;
import es.deusto.smartlab.rfid.iso14443a.ImplementationISO14443A;


/**
 * @author Xabier Echevarría Espinosa
 */
public class ULTest {

	public void test(){
		ImplementationISO14443A iccrf = new ImplementationISO14443A();
		try {
			iccrf.initialize();
			
			while(true){
				iccrf.findSerialNumber();
				
				short cardType = iccrf.findCardType();
				
				if(cardType!=CommandsISO14443A.CARD_14443A_UL){
    				throw new NullPointerException("此卡非员工卡");
				}
				
				iccrf.beep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iccrf.beep(10);
			iccrf.destroy();
		}
	}
	public static void main(String args[]) throws InterruptedException {
		ISO14443AAction iccrf = new ISO14443AAction();
		iccrf.initialize();
		
//		int page = 4;//0x00->0x0F:0x04
//		int offset = 0;
//		int pageSize = 4;
//		
//		String str = "读卡模块通讯协议读卡模块通讯协议读卡模块通讯协议卡模块通讯协议";
//		byte[] buffer = new byte[4];
//		byte[] wbyte;
//		try {
//			wbyte = str.getBytes("GBK");
//			if(wbyte.length < 4){
//				for(int i=0;i<wbyte.length;i++){
//					buffer[i] = wbyte[i];
//				}
//			}else{
//				for(int i=0;i<4;i++){
//					buffer[i] = wbyte[i];
//				}
//			}
//			
////			iccrf.write((byte)(page*pageSize+offset),wbyte);
//			iccrf.write((byte) 0x04,wbyte);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		byte[] respnse = iccrf.read((byte) 0x04);
//		System.out.println(respnse);
//
//		System.out.println(iccrf.getGBK(respnse));
//		iccrf.findSerialNumber(iccrf.findCardType());
//		iccrf.findSerialNumber(iccrf.findCardType());
		
		String buf = "幸福中国共同富裕";
		buf = "老子不相信";
		buf = "12345678";
		try {
			//
			iccrf.findSerialNumber();

			//write UL
//			iccrf.write((byte)0x04,buffer);
//			iccrf.saveUL(buf1,4,32);
//			byte[] response = iccrf.read((byte)0x04);
			
			//write M1
//			iccrf.loadKey((byte)0x01, password);
//			iccrf.authentication((byte)0x01);
//			
//			iccrf.write((byte)(1*4 + 1),buf.getBytes("gb2312"));
//			
//			iccrf.saveM1(buf, 1, 1, 16);
//			iccrf.saveM1(buf, 1, 2, 16);
//			
			iccrf.findSerialNumber();
//			byte[] response = iccrf.read((byte)(1*4 + 1));
			String reponse = iccrf.read(1,1);
			System.out.println(reponse);

//			String str = iccrf.read(1,1);
//			System.out.println("readM1:"+str);
//			str = iccrf.read(1,2);
//			System.out.println("readM1:"+str);
//			response = iccrf.read((byte)(1*4 + 2));
//			System.out.println(new String(response,4,16,"GBK"));

//			response = iccrf.read((byte)0x08);
			
//			System.out.println(new String(response,4,16,"GBK"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		iccrf.beep(10);
		iccrf.destroy();
	}

}
