package test;

import es.deusto.smartlab.rfid.iccrf.ImplementationIccrf;


/**
 * @author Xabier Echevarría Espinosa
 */
public class IccrfTest {

	public static void main(String args[]) throws InterruptedException {
		ImplementationIccrf iccrf = new ImplementationIccrf();
		iccrf.init();
		
//		byte[] value = new byte[4];
//		value[0] = (byte) 0xFF;
//		value[1] = (byte) 0xFF;
//		value[2] = (byte) 0xFF;
//		value[3] = (byte) 0xFF;
//		iccrf.initval((byte)0x01, value);
		
		int secnr = 0;//max 15
		int block = 4;
		
		byte[] password = new byte[6];
		password[0] = (byte) 0xFF;
		password[1] = (byte) 0xFF;
		password[2] = (byte) 0xFF;
		password[3] = (byte) 0xFF;
		password[4] = (byte) 0xFF;
		password[5] = (byte) 0xFF;
		iccrf.loadKey((byte)secnr, password);

		iccrf.authentication((byte)secnr);
		
		iccrf.beep(100);
		
		long id = iccrf.findId();
		System.out.println("findId:"+id);

		Thread.sleep(1000);
		
//		byte[] reponse = 
				iccrf.read((byte)(secnr*block));
//		System.out.println(iccrf.bytesToHexString(reponse));

//		byte[] wbyte = new byte[16];
//		for(int i=0;i<16;i++){
//			wbyte[i] = (byte) 0x11;
//		}
//		
//		iccrf.write((byte)(secnr*block),wbyte);
		
//		String str = "读卡模块通讯协议读卡模块通讯协议读卡模块通讯协议卡模块通讯协议";
//		byte[] buffer = new byte[16];
//		byte[] wbyte;
//		try {
//			wbyte = str.getBytes("GBK");
//			if(wbyte.length < 16){
//				for(int i=0;i<wbyte.length;i++){
//					buffer[i] = wbyte[i];
//				}
//			}else{
//				for(int i=0;i<16;i++){
//					buffer[i] = wbyte[i];
//				}
//			}
//			iccrf.write((byte)(secnr*block),buffer);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

		iccrf.destroy();
	}

}
