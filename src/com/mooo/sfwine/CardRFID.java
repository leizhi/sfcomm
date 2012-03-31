package com.mooo.sfwine;

import es.deusto.smartlab.rfid.iccrf.ImplementationIccrf;

public class CardRFID {
	private final int block = 4;

	private byte[] password = new byte[6];

	private ImplementationIccrf iccrf = new ImplementationIccrf();

	public CardRFID(){
		password[0] = (byte) 0xFF;
		password[1] = (byte) 0xFF;
		password[2] = (byte) 0xFF;
		password[3] = (byte) 0xFF;
		password[4] = (byte) 0xFF;
		password[5] = (byte) 0xFF;
		
		iccrf.init();
	}
	
	public ImplementationIccrf getIccrf() {
		return iccrf;
	}

	public void ready(int secnr) {
		iccrf.loadKey((byte)secnr, password);
		iccrf.authentication((byte)secnr);
	}
	
	public void destroy() {
		iccrf.destroy();
	}
	
	public Long getCardId() {
		return iccrf.findId();
	}
	
	public void beep() {
		iccrf.beep(10);//响10ms
	}
	
	public void save(String str,int secnr,int offset,int maxLength) {
		ready(secnr);
		
		byte[] buffer = new byte[16];
		for(int i=0;i<16;i++){
			buffer[i] = 0x20;
		}
		
		byte[] wbyte;
		try {
			wbyte = str.getBytes();
			
			if(maxLength < 1)
				maxLength = wbyte.length;
			
			if( wbyte.length < maxLength)
				maxLength = wbyte.length;
			
			if(maxLength < 16){
				for(int i=0;i<maxLength;i++){
					buffer[i] = wbyte[i];
				}
			}else{
				for(int i=0;i<16;i++){
					buffer[i] = wbyte[i];
				}
			}
			
			iccrf.write((byte)(secnr*block + offset),buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveGBK(String str,int secnr,int offset,int maxLength) {
		
		ready(secnr);
		
		byte[] buffer = new byte[16];
		for(int i=0;i<16;i++){
			buffer[i] = 0x20;
		}
		
		byte[] wbyte;
		try {
			wbyte = str.getBytes("gb2312");
			
			if(maxLength < 1)
				maxLength = wbyte.length;
			
			if( wbyte.length < maxLength)
				maxLength = wbyte.length;
			
			System.out.println("maxLength:"+maxLength);
			if(maxLength < 16){
				for(int i=0;i<maxLength;i++){
					buffer[i] = wbyte[i];
				}
			}else{
				for(int i=0;i<16;i++){
					buffer[i] = wbyte[i];
				}
			}
			
			iccrf.write((byte)(secnr*block + offset),buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}