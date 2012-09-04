package com.mooo.serial;

public class SerialUtil {

	public static byte[] toByte(int intValue) {
		byte[] ibytes = new byte[4];
		
        for (int i = 0; i < 4; i++) {  
        	ibytes[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
            System.out.printf("ibytes["+i+"]="+"\t 0x%02X 0d%d \n", ibytes[i],ibytes[i]);
        }
        return ibytes;  
	}
	
	public static byte[] crcw(byte[] bytes) {
		int value=0x0000FFFF;
		int CRC=0x0000FFFF;
		int rlenth = bytes.length;
		
		for(int i = 0; i < rlenth; i++){
			value = bytes[i] & 0x00FF;
			CRC = CRC^value;
			for(int j = 0;j < 8; j ++){
				if ((CRC & 0x0001)>0){
					CRC>>=1;
					CRC^=0xA001;
				}else {
					CRC>>=1;
				}
			}
		}
		
		byte[] cbytes = new byte[rlenth+2];
		
		for(int i = 0; i < rlenth; i++){
			cbytes[i]=bytes[i];
		}
		
		byte[] ibytes = toByte(CRC);
		
		cbytes[rlenth] = ibytes[3];
		cbytes[rlenth+1] = ibytes[2];
		
		return cbytes;
	}
	
	public static byte[] crcb(byte[] bytes) {
		byte CRC = (byte)0x00;
		int rlenth = bytes.length;
		for(int i=0;i<rlenth;i++){
			CRC = (byte) (CRC ^ bytes[i]);
		}
		
		byte[] cbytes = new byte[rlenth+1];
		
		for(int i = 0; i < rlenth; i++){
			cbytes[i]=bytes[i];
		}
		
		cbytes[rlenth] = CRC;
		
		return cbytes;
	}
	
    public static void testCommand(byte[] command){
    	if(command!=null)
        for(int i=0;i<command.length;i++){
        	byte bt = command[i];
            System.out.printf("\t 0x%02X 0d%d \n", bt,bt);
        }
    }
}
