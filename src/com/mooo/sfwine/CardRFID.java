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
		return iccrf.findSerialNumber(iccrf.findCardType());
	}
	
	public void beep() {
		iccrf.beep(10);//响10ms
	}
	
	//清除所有数据
	public void cleanAll() {
		byte[] buffer = new byte[16];
		for(int i=1;i<16;i++){
			buffer[i] = (byte) 0x00;
		}
		
		for(int i=1;i<16;i++){
			ready(i);
			iccrf.write((byte)(i*block+0),buffer);
			iccrf.write((byte)(i*block+1),buffer);
			iccrf.write((byte)(i*block+2),buffer);
		}
	}
	//标签分类,标志位前1字节,剩下16位字节预留
	public void saveType(byte flag,int secnr) {
		try {
			ready(secnr);
			
			byte[] buffer = new byte[16];
			buffer[0] = flag;
			
			for(int i=1;i<16;i++){
				buffer[i] = (byte) 0xEE;
			}
			
			iccrf.write((byte)(secnr*block),buffer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public byte[] read(int secnr,int offset) {
		byte[] buffer=null;
		try {
			ready(secnr);
			buffer = iccrf.getBytes((byte)(secnr*block + offset));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	public String readGBK(int secnr,int offset) {
		String buffer=null;
		try {
			ready(secnr);
			buffer = iccrf.readGBK((byte)(secnr*block + offset));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
}