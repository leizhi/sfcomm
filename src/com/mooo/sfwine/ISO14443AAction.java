package com.mooo.sfwine;

import java.io.UnsupportedEncodingException;

import es.deusto.smartlab.rfid.iso14443a.CommandsISO14443A;
import es.deusto.smartlab.rfid.iso14443a.ImplementationISO14443A;

public class ISO14443AAction extends ImplementationISO14443A{
	public static String whichPort="/dev/ttyUSB0";
	
	public static int whichSpeed=9600;
	
	private final int BLOCK_SIZE = 4;

	private byte[] password = {(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};

	//清除所有数据
	public void cleanAll() {
		try {
			
			byte[] buffer = new byte[16];
			for(int i=1;i<16;i++){
				buffer[i] = (byte) 0x00;
			}
			
			for(int i=1;i<16;i++){
				findSerialNumber();
				loadKey((byte)i, password);
				authentication((byte)i);
				
				write((byte)(i*BLOCK_SIZE+0),buffer);
				write((byte)(i*BLOCK_SIZE+1),buffer);
				write((byte)(i*BLOCK_SIZE+2),buffer);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(Card card) throws Exception{
		int choseCard=request();
		
		if(choseCard==CommandsISO14443A.CARD_14443A_M1){
			saveM1(card.getRfidcode(), 1, 0, 16);
		}else if(choseCard==CommandsISO14443A.CARD_14443A_UL){
			saveUL(card.getRfidcode(), 4, 16);
		}
	}

	public void save(User user) throws Exception{
		int choseCard=request();
		
		if(choseCard==CommandsISO14443A.CARD_14443A_M1){
			saveM1(user.getName(), 1, 1, 16);
			saveM1(user.getPassword(), 1, 2, 16);
		}else if(choseCard==CommandsISO14443A.CARD_14443A_UL){
			saveUL(user.getName(), 4, 16);
		}
	}
	public void saveM1(String buf,int page,int offset,int maxLength) throws Exception{
			findSerialNumber();
			
			byte[] bytes = buf.getBytes("gb2312");
			
			if(maxLength < 1 || maxLength>bytes.length)
				maxLength = bytes.length;
			
			if(maxLength>16)
				maxLength=16;
			
			byte[] wbuf = new byte[]{
										0x00,0x00,0x00,0x00,
										0x00,0x00,0x00,0x00,
										0x00,0x00,0x00,0x00,
										0x00,0x00,0x00,0x00};
			
			for(int i=0;i<maxLength;i++){
					wbuf[i] = bytes[i];
			}
			System.out.println("write page:"+page);
			
			loadKey((byte)page, password);
			authentication((byte)page);
			
			write((byte)(page*BLOCK_SIZE + offset),wbuf);//default 16bits
	}
	
	public void saveUL(String buf,int page,int maxLength) throws Exception{
		
			byte[] bbuf = buf.getBytes("gb2312");

			int countPage = 0;
			
			if(maxLength > bbuf.length)
				maxLength = bbuf.length;
			
			if(maxLength > 4*16)
				maxLength = 4;
			
			countPage = (int) Math.floor(maxLength/4);
			
			if(maxLength%4!=0)
				countPage++;

			if(countPage>16)
				countPage=16;
			System.out.println("saveUL>>>>>>>>>>>>>>>>>>>>>>>>>");

			System.out.println("maxLength:"+maxLength);
			System.out.println("countPage:"+countPage);
			System.out.println("page:"+page);

			for(int i=0;i<countPage;i++){
				byte[] wbuf = {0,0,0,0};
				
				if(i*4+0<maxLength)
					wbuf[0] = bbuf[i*4+0];
				
				if(i*4+1<maxLength)
					wbuf[1] = bbuf[i*4+1];
				
				if(i*4+2<maxLength)
					wbuf[2] = bbuf[i*4+2];
				
				if(i*4+3<maxLength)
					wbuf[3] = bbuf[i*4+3];
				
				System.out.println("write page:"+(page+i));
				write((byte)(page+i),wbuf);//default 4 bytes
			}
			System.out.println("saveUL<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	

	public String read(int page,int block) {
		byte[] bytes =null;
		String buffer = null;
		
		try {
			int choseCard=request();
			System.out.println("choseCard:"+choseCard);

			if(choseCard==CommandsISO14443A.CARD_14443A_M1){
				loadKey((byte)page, password);
				authentication((byte)page);
				bytes = read((byte)(page*BLOCK_SIZE + block));
			}else if(choseCard==CommandsISO14443A.CARD_14443A_UL){
				bytes = read((byte)page);
			}
			
			buffer = new String(bytes,4,16,"GBK");
			System.out.println("buffer:"+buffer);

			bytes = buffer.getBytes();
			int length = 0;
			for(int i=0;i<bytes.length;i++){
				if(bytes[i]==0x00){
					length=i;
					break;
				}
			}
			
			if(length==0 && bytes.length>0)
				length=bytes.length;
			
			System.out.println("length:"+length);

			buffer = new String(bytes,0,length);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	public String readUL(int page) throws Exception {
		byte[] bytes =null;
		String buffer = null;
		bytes = read((byte)page);
		buffer = new String(bytes,4,16,"GBK");
		return buffer;
	}
}