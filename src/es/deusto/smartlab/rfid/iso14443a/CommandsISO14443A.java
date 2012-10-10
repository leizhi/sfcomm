/**
 * @author Xabier Echevarría Espinosa
 *
 * The CommandsTexas class contains the request packets of Texas Instruments's Kit.
 */

package es.deusto.smartlab.rfid.iso14443a;

public class CommandsISO14443A {
	public final static byte MODE_ONE = (byte)0x00;//一张卡
	
//	private final static byte MODE_MORE = (byte)0x01;//多张卡
	
	public final static byte MODE_UL = (byte)0x26;

	public final static short CARD_14443A_M1 = (byte)0x0004;

	public final static short CARD_14443A_UL = (byte)0x0044;
	
	private final static byte START_SYMBOL = (byte)0xA7;//起始符
	private int length;//数据长度
	private byte command;//命令字
	private byte parameters[];//数据
	private final byte CRC = (byte)0x00;//异或校验值

	public CommandsISO14443A(byte command, byte parameters[]) {
		this.command = command;
		this.parameters = parameters;
		calculateFields();
	}

	private void calculateFields() {
		setLength(1 + (getParameters() == null ? 0 : getParameters().length));
	}
	
	private byte[] getParameters() {
		return parameters;
	}
	
	private void setLength(int length) {
		this.length = length;
	}

	public byte[] getBytes() {

		int rLength=4;
		
		if(parameters!=null) 
			rLength += parameters.length;
		
		byte[] request = new byte[rLength];
		request[0] = START_SYMBOL;
		request[1] = (byte) (length & 0xff);
		request[2] = command;
		
		if(parameters!=null)
			for(int i=3;i<rLength-1;i++){
				request[i] = parameters[i-3];
			}
		
		request[rLength-1] = CRC;
		
		for(int i=0;i<rLength-1;i++){
			request[rLength-1] = (byte) (request[rLength-1] ^ request[i]);
		}
		
		return request;
	}
	
	//定义命令
		static CommandsISO14443A initialize() {
			byte[] buffer = new byte[2];
			buffer[0] = (byte) 0xC6;
			buffer[1] = (byte) 0x0E;
			return new CommandsISO14443A((byte) 0x12,buffer);
		}
		
		static CommandsISO14443A shakeHands() {
			byte[] buffer = new byte[1];
			buffer[0] = 0x30;
			
			return new CommandsISO14443A((byte) 0x13,buffer);
		}
		
		static CommandsISO14443A getVersion() {
			return new CommandsISO14443A((byte) 0x11,null);
		}
		/*
		static CommandsISO14443A findM1() {
			byte[] data = new byte[1];
			data[0] = MODE_ONE;
			return new CommandsISO14443A((byte) 0x01, data);
		}
		
		static CommandsISO14443A requestUL() {
			byte[] data = new byte[1];
			data[0] = MODE_UL;
			return new CommandsISO14443A((byte) 0x38, data);
		}
		*/
		static CommandsISO14443A loadKey(byte address,byte[] password) {
			byte[] data = new byte[8];
			data[0] = 0x00;
			data[1] = address;

			for(int i=0;i<6;i++)
				data[2+i] = password[i];
			
			return new CommandsISO14443A((byte) 0x05, data);
		}
		
		static CommandsISO14443A authentication(byte address) {
			byte[] data = new byte[2];
			data[0] = 0x00;//密码验证模式
			data[1] = address;//要验证密码的扇区号（0～15）
			
			return new CommandsISO14443A((byte) 0x09, data);
		}
		
		
		static CommandsISO14443A read(byte address) {
			byte[] data = new byte[1];
			data[0] = address;//M1卡块地址(0～63),ML卡页地址(0～11)
			
			return new CommandsISO14443A((byte) 0x07, data);
		}
		
		static CommandsISO14443A write(byte address,byte[] buffer) {
			
			byte[] request = new byte[buffer.length+1];
			
			request[0] = address;//M1卡块地址(0～63),ML卡页地址(0～11)
			
			for(int i=0;i<buffer.length;i++)
				request[1+i] = buffer[i];
			
			return new CommandsISO14443A((byte) 0x08, request);
		}
		
		static CommandsISO14443A halt() {
			return new CommandsISO14443A((byte) 0x06, null);
		}
		
		static CommandsISO14443A beep(int msec) {
			byte[] data = new byte[2];
			data[0] = (byte) (msec%256);//蜂鸣时限，单位是10毫秒，为2个字节
			data[1] = (byte) (msec/256);
			return new CommandsISO14443A((byte) 0x14, data);
		}

		//是否有卡
		static CommandsISO14443A request() {
			byte[] data = new byte[1];
			data[0] = MODE_ONE;
			return new CommandsISO14443A((byte) 0x02, data);
		}
		
		//返回卡的序列号 防止卡冲突
		static CommandsISO14443A anticoll() {
			byte[] data = new byte[1];
			data[0] = 0x00;//预选卡所用的位数，标准值为0
			return new CommandsISO14443A((byte) 0x03, data);
		}
		//选取指定序列号的卡
		static CommandsISO14443A select(byte[] serialAddr) {
			byte[] data = new byte[4];
			data[0] = serialAddr[0];
			data[1] = serialAddr[1];
			data[2] = serialAddr[2];
			data[3] = serialAddr[3];
			return new CommandsISO14443A((byte) 0x04, data);
		}
		
		static CommandsISO14443A anticoll2() {
			byte[] data = new byte[1];
			data[0] = 0x00;//预选卡所用的位数，标准值为0
			return new CommandsISO14443A((byte) 0x70, data);
		}
		
		static CommandsISO14443A select2(byte[] serialAddr) {
			byte[] data = new byte[4];
			data[0] = serialAddr[0];
			data[1] = serialAddr[1];
			data[2] = serialAddr[2];
			data[3] = serialAddr[3];
			return new CommandsISO14443A((byte) 0x71, data);
		}
		/*
		public static byte[] writeUL(byte address,byte[] buffer) {
			byte[] request = new byte[30];
			request[0] = (byte) 0xa7;
			request[1] = 0x12;
			request[2] = 0x08;
			request[3] = 0x04;
			
			//要写入的数据
			int maxLen = 16;
			
			if(buffer.length < 16) 
				maxLen = buffer.length;
			
			for (int i=0;i<maxLen;i++)
				request[4+i]=buffer[i];
			
			request[20] = 0;
			
			//求发送数据包的异或校验值
			for(int i=0;i<2+request[1];i++)
				request[20]=(byte) (request[20]^request[i]);
			
			return request;
//			Sleep(10);
//			for (int i=0;i<21;i++)
//		        WriteChar(icdevice,sendbuf[i]);
//			
//			if(ReceiveST(icdevice,&t))
//			{
//				return (-132);//超时错误
//			}
//		    LRC=t;
//			
//			ReadChar(icdevice,&len);
//			LRC=LRC^len;
//			for(i=0;i<=len;i++)
//			{
//				ReadChar(icdevice,&rebuf[i]);
//		   	}
//			for(i=0;i<len;i++)
//			{
//		    	LRC=LRC^rebuf[i]; 
//		    }
		}
		*/
}
