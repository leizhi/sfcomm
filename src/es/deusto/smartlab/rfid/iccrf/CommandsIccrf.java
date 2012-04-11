/**
 * @author Xabier Echevarría Espinosa
 *
 * The CommandsTexas class contains the request packets of Texas Instruments's Kit.
 */

package es.deusto.smartlab.rfid.iccrf;

class CommandsIccrf {
	private final static byte START_SYMBOL = (byte)0xA7;//起始符
	
	private final static byte MODE_ONE = (byte)0x00;//一张卡
	
//	private final static byte MODE_MORE = (byte)0x01;//多张卡
	
	private final static byte MODE_UL = (byte)0x26;

	private final static short CARD_14443A_M1 = (byte)0x0004;

	private final static short CARD_14443A_UL = (byte)0x0044;

	private int length;//数据长度
	private byte command;//命令字
	private byte data[];//数据
	private final byte CRC = (byte)0x00;;//异或校验值

	public CommandsIccrf(byte command, byte data[]) {
		this.command = command;
		this.data = data;
		calculateFields();
	}

	private void calculateFields() {
		setLength(1 + (getData() == null ? 0 : getData().length));
	}
	//定义命令
	
	static CommandsIccrf initialize() {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) 0xC6;
		buffer[1] = (byte) 0x0E;
		return new CommandsIccrf((byte) 0x12,buffer);
	}
	
	static CommandsIccrf shakeHands() {
		byte[] buffer = new byte[1];
		buffer[0] = 0x30;
		
		return new CommandsIccrf((byte) 0x13,buffer);
	}
	
	static CommandsIccrf getVersion() {
		return new CommandsIccrf((byte) 0x11,null);
	}
	
	static CommandsIccrf findCardType() {
		byte[] data = new byte[1];
		data[0] = MODE_ONE;
		return new CommandsIccrf((byte) 0x02, data);
	}
	
	static CommandsIccrf findSerialNumber(short cardType) {
		byte[] data = new byte[1];

		if(cardType==CARD_14443A_M1){
			System.out.println("CARD_14443A_M1");

			data[0] = MODE_ONE;
			return new CommandsIccrf((byte) 0x01, data);
		}else if(cardType==CARD_14443A_UL){
			
			System.out.println("MODE_UL");

			data[0] = MODE_UL;
			return new CommandsIccrf((byte) 0x38, data);
		}else
			return null;
	}
	
	static CommandsIccrf initval(byte address,byte[] value) {
		byte[] data = new byte[5];
		data[0] = address;

		for(int i=0;i<4;i++)
			data[i+1] = value[i];
		
		return new CommandsIccrf((byte) 0x16, data);
	}
	
	static CommandsIccrf loadKey(byte address,byte[] password) {
		byte[] data = new byte[8];
		data[0] = 0x00;
		data[1] = address;

		for(int i=0;i<6;i++)
			data[2+i] = password[i];
		
		return new CommandsIccrf((byte) 0x05, data);
	}
	
	static CommandsIccrf authentication(byte address) {
		byte[] data = new byte[2];
		data[0] = 0x00;//密码验证模式
		data[1] = address;//要验证密码的扇区号（0～15）
		
		return new CommandsIccrf((byte) 0x09, data);
	}
	
	
	static CommandsIccrf read(byte address) {
		byte[] data = new byte[1];
		data[0] = address;//M1卡块地址(0～63),ML卡页地址(0～11)
		
		return new CommandsIccrf((byte) 0x07, data);
	}
	
	static CommandsIccrf write(byte address,byte[] buffer) {
		
		byte[] request = new byte[buffer.length+1];
		
		request[0] = address;//M1卡块地址(0～63),ML卡页地址(0～11)
		
		for(int i=0;i<buffer.length;i++)
			request[1+i] = buffer[i];
		
		return new CommandsIccrf((byte) 0x08, request);
	}
	
	static CommandsIccrf halt() {
		return new CommandsIccrf((byte) 0x06, null);
	}
	
	static CommandsIccrf beep(int msec) {
		byte[] data = new byte[2];
		data[0] = (byte) (msec%256);//蜂鸣时限，单位是10毫秒，为2个字节
		data[1] = (byte) (msec/256);
		return new CommandsIccrf((byte) 0x14, data);
	}
	
//	static CommandsIccrf writeSingleBlockISO15693Request(byte[] id,
//			int blockNumber, byte blockData[]) {
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream(16);
//		bytes.write(0);
//		bytes.write(1);
//		bytes.write((byte) (blockNumber & 0xff));
//		bytes.write(4);
//		try {
//			bytes.write(encodeBytes(blockData, 4));
//			bytes.write(id);
//		} catch (IOException ex) {
//		}
//		return new CommandsIccrf((byte) 4, (byte) 102, bytes.toByteArray());
//	}

//	static CommandsIccrf writeSingleBlockTagitRequest(byte[] id,
//			int blockNumber, byte blockData[]) {
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream(10);
//		bytes.write((byte) (blockNumber & 0xff));
//		bytes.write((byte) 31);
//		try {
//			bytes.write(encodeBytes(blockData, 4));
//			bytes.write(id);
//		} catch (IOException ex) {
//		}
//		return new CommandsIccrf((byte) 5, (byte) 99, bytes.toByteArray());
//	}

	private byte[] getData() {
		return data;
	}
	private void setLength(int length) {
		this.length = length;
	}

	public byte[] getBytes() {

		int rLength=4;
		
		if(data!=null) 
			rLength += data.length;
		
		byte[] request = new byte[rLength];
		request[0] = START_SYMBOL;
		request[1] = (byte) (length & 0xff);
		request[2] = command;
		
		if(data!=null)
			for(int i=3;i<rLength-1;i++){
				request[i] = data[i-3];
			}
		
		request[rLength-1] = CRC;
		
		for(int i=0;i<rLength-1;i++){
			request[rLength-1] = (byte) (request[rLength-1] ^ request[i]);
		}
		
		return request;
	}
}
