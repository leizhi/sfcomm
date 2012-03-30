/**
 * @author Xabier Echevarría Espinosa
 *
 * The CommandsTexas class contains the request packets of Texas Instruments's Kit.
 */

package es.deusto.smartlab.rfid.iccrf;

class CommandsIccrf {
	private final byte START_SYMBOL = (byte)0xA7;//起始符
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

//	static CommandsIccrf setDriverRequest(byte deviceMask, byte state) {
//		byte data[] = new byte[2];
//		data[0] = deviceMask;
//		data[1] = state;
//		return new CommandsIccrf((byte) 1, (byte) 67, data);
//	}

//	static CommandsIccrf findTokenRequest() {
//		return new CommandsIccrf((byte) 1, (byte) 65, new byte[] { 10 });
//	}
//
//	static CommandsIccrf findTokenISO15693Request() {
//		return new CommandsIccrf((byte) 4, (byte) 65, new byte[] { 10 });
//	}
//
//	static CommandsIccrf findTokenTagitRequest() {
//		return new CommandsIccrf((byte) 5, (byte) 65, new byte[] { 10 });
//	}

//	static CommandsIccrf readSingleBlockISO15693Request(byte[] id,
//			int blockNumber) throws RFIDException {
//		if (id == null)
//			throw new RFIDException("The tag ID is null");
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream(11);
//		bytes.write(0);
//		bytes.write(1);
//		bytes.write((byte) (blockNumber & 0xff));
//		try {
//			bytes.write(id);
//		} catch (IOException ex) {
//			RFIDException nex = new RFIDException(ex.getMessage());
//			nex.setStackTrace(ex.getStackTrace());
//			throw nex;
//		}
//		return new CommandsIccrf((byte) 4, (byte) 101, bytes.toByteArray());
//	}

//	static CommandsIccrf readSingleBlockTagitRequest(byte[] id, int blockNumber) {
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream(5);
//		bytes.write((byte) (blockNumber & 0xff));
//		try {
//			bytes.write(id);
//		} catch (IOException ex) {
//		}
//		return new CommandsIccrf((byte) 5, (byte) 97, bytes.toByteArray());
//	}

//	static CommandsIccrf readMultipleBlocksISO15693Request(byte[] id,
//			int startBlockNumber, int numberOfBlocks) {
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream(12);
//		bytes.write(0);
//		bytes.write(0);
//		bytes.write((byte) (startBlockNumber & 0xff));
//		bytes.write((byte) (numberOfBlocks & 0xff));
//		try {
//			bytes.write(id);
//		} catch (IOException ex) {
//		}
//		return new CommandsIccrf((byte) 4, (byte) 104, bytes.toByteArray());
//	}
//
//	static CommandsIccrf stayQuietISO15693Request(byte[] uid) {
//		return new CommandsIccrf((byte) 4, (byte) 100, uid);
//	}
//
	static CommandsIccrf versionRequest() {
		byte[] data = new byte[1];
		data[0] = 0x00;
		return new CommandsIccrf((byte) 0x01, data);
	}
	
	static CommandsIccrf findId() {
		byte[] data = new byte[1];
		data[0] = 0x00;
		return new CommandsIccrf((byte) 0x01, data);
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
	
	static CommandsIccrf write(byte address,byte[] newData) {
		byte[] data = new byte[17];
		data[0] = address;//M1卡块地址(0～63),ML卡页地址(0～11)
		
		for(int i=0;i<16;i++)
			data[1+i] = newData[i];
		
		return new CommandsIccrf((byte) 0x08, data);
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

		int rLength=4+data.length;
		
		byte[] request = new byte[rLength];
		request[0] = START_SYMBOL;
		request[1] = (byte) (length & 0xff);
		request[2] = command;
		
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
