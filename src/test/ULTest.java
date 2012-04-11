package test;

import java.io.UnsupportedEncodingException;

import es.deusto.smartlab.rfid.iccrf.ImplementationIccrf;


/**
 * @author Xabier Echevarría Espinosa
 */
public class ULTest {

	public static void main(String args[]) throws InterruptedException {
		ImplementationIccrf iccrf = new ImplementationIccrf();
		iccrf.init();
		
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
		iccrf.findSerialNumber(iccrf.findCardType());

		String str = "读卡模块";
		
		byte[] buffer;
		try {
			buffer = str.getBytes("GBK");
			iccrf.write((byte)0x06,buffer);
			
			byte[] response = iccrf.read((byte)0x06);
			System.out.println(iccrf.getGBK(response));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		iccrf.beep(10);
		iccrf.destroy();
	}

}
