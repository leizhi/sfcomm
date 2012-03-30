package es.deusto.smartlab.rfid.iccrf;

import java.io.UnsupportedEncodingException;
import java.util.*;
import es.deusto.smartlab.rfid.*;

/**
 * @author Xabier Echevarría Espinosa
 */
public class ImplementationIccrf implements InterfaceRFID {
	private String port;
	private SerialManager sm;
	boolean opened = false;

	private static Hashtable<Integer,String> errorCodes;
	static {
		errorCodes = new Hashtable<Integer,String>();
		errorCodes.put(new Integer(0), "ERROR_NONE");
		errorCodes.put(new Integer(1), "ERROR_TOKEN_NOT_PRESENT");
		errorCodes.put(new Integer(5), "ERROR_INVALID_RF_FORMAT");
		errorCodes.put(new Integer(14), "ERROR_DEVICE_ID_INVALID");
		errorCodes.put(new Integer(16), "ERROR_ILLEGAL_ACTION");
		errorCodes.put(new Integer(17), "ERROR_WRONG_DOWNLOAD_STATE");
		errorCodes.put(new Integer(18), "ERROR_WRITE_FAILED");
		errorCodes.put(new Integer(19), "ERROR_INVALID_ADDRESS");
		errorCodes.put(new Integer(20), "ERROR_INVALID_BAUD");
		errorCodes.put(new Integer(21), "ERROR_INVALID_CHECK_DIGITS");
		errorCodes.put(new Integer(22), "ERROR_NO_TIMER_AVAILABLE");
		errorCodes.put(new Integer(23), "ERROR_INVALID_ENTITY_ID");
		errorCodes.put(new Integer(24), "ERROR_DATA_TRUNCATED");
		errorCodes.put(new Integer(25), "ERROR_NO_DATA_READ");
		errorCodes.put(new Integer(26), "ERROR_INVALID_START_BYTE");
		errorCodes.put(new Integer(27), "ERROR_INVALID_CRC");
		errorCodes.put(new Integer(28), "ERROR_CMD_REPLY_MISMATCH");
		errorCodes.put(new Integer(32), "ERROR_14443_A_DATA_INCORRECT");
		errorCodes.put(new Integer(33), "ERROR_14443_B_DATA_INCORRECT");
		errorCodes.put(new Integer(34), "ERROR_14443_B_TOKEN_NOT_FOUND");
		errorCodes.put(new Integer(35), "ERROR_HF_ASIC_RECEIVE_TIMEOUT");
		errorCodes.put(new Integer(36), "ERROR_HF_ASIC_ABORTED");
		errorCodes.put(new Integer(37), "ERROR_HF_ASIC_ATQB_ERR1");
		errorCodes.put(new Integer(38), "ERROR_HF_ASIC_ATQB_PROT_TYPE");
		errorCodes.put(new Integer(39), "ERROR_HF_ASIC_INVALID_CID");
		errorCodes.put(new Integer(40), "ERROR_HF_ASIC_INVALID_NAD");
		errorCodes.put(new Integer(41), "ERROR_HF_ASIC_CID_LOW_POWER");
		errorCodes.put(new Integer(42), "ERROR_14443_B_HLTB_ERROR");
		errorCodes.put(new Integer(43), "ERROR_14443_B_INVALID_BLK_TYPE");
		errorCodes.put(new Integer(44), "ERROR_HF_ASIC_NOT_IBLOCK");
		errorCodes.put(new Integer(45), "ERROR_HF_ASIC_NOT_RBLOCK");
		errorCodes.put(new Integer(46), "ERROR_14443_B_SDESELECT");
		errorCodes.put(new Integer(47), "ERROR_14443_DATA_INCORRECT");
		errorCodes.put(new Integer(48), "ERROR_MANY_CID_NO_SUPRT_TRANSPONDERS");
		errorCodes.put(new Integer(49), "ERROR_COLISN_BPSK_AND_OR_CID");
		errorCodes.put(new Integer(50), "ERROR_COLISN_BPSK_DECODE");
		errorCodes.put(new Integer(51), "ERROR_14443_ABORTED");
		errorCodes.put(new Integer(52), "ERROR_TOKEN_BUFFER_NULL");
		errorCodes.put(new Integer(53), "ERROR_14443_A_UPLINK_PARITY");
		errorCodes.put(new Integer(54), "ERROR_14443_A_ATS");
		errorCodes.put(new Integer(55), "ERROR_14443_A_PPS");
		errorCodes.put(new Integer(56), "ERROR_14443_A_CASCADE_LEVEL");
		errorCodes.put(new Integer(57), "ERROR_14443_A_SAK_CRC");
		errorCodes.put(new Integer(64), "ERROR_BPSK_NO_ERROR");
		errorCodes.put(new Integer(65), "ERROR_BPSK_BAD_FRAME_WAIT");
		errorCodes.put(new Integer(66), "ERROR_BPSK_BAD_VAL_BAUD");
		errorCodes.put(new Integer(67), "ERROR_BPSK_CANCELLED");
		errorCodes.put(new Integer(68), "ERROR_BPSK_SUBCARRIER");
		errorCodes.put(new Integer(69), "ERROR_BPSK_TR0_TIMEOUT");
		errorCodes.put(new Integer(70), "ERROR_BPSK_RCV_OVERFLOW");
		errorCodes.put(new Integer(71), "ERROR_BPSK_NO_SOF");
		errorCodes.put(new Integer(72), "ERROR_BPSK_NO_EOF");
		errorCodes.put(new Integer(73), "ERROR_BPSK_TR1_TIMEOUT");
		errorCodes.put(new Integer(74), "ERROR_BPSK_CRC_ERROR");
		errorCodes.put(new Integer(75), "ERROR_BPSK_FRAME");
		errorCodes.put(new Integer(76), "ERROR_MODULE_ABORTED");
		errorCodes.put(new Integer(77), "ERROR_PARAMETER_ERROR");
		errorCodes.put(new Integer(87), "ERROR_COLLISION_DETECTED");
		errorCodes.put(new Integer(96), "ERROR_APOLLO_LIFE_CYCLE");
		errorCodes.put(new Integer(97), "ERROR_APOLLO_DATA_INCORRECT");
	}

	/**
	 * Constructor.
	 */
	public ImplementationIccrf() {
		sm = new SerialManager();
	}

	public boolean isOpened() {
		return opened;
	}
	
	/**
	 * Initializes the Texas Instruments's kit, testing the connection with the
	 * reader/interrogator and opening the serial port.
	 * 
	 * @return Returns success if a RFID kit was successfully initialized.
	 */
	public boolean init() {
//		boolean opened = false;
		if (port != null) {
			if (sm.openPort(port, 9600)) {
				if (testConnection())
					opened = true;
				else
					sm.closePort();
			}
		} else {
			ArrayList<String> portsAvailable = new ArrayList<String>();
			portsAvailable = sm.getPorts();
			for (int i = 0; i < portsAvailable.size(); i++) {
				if (sm.openPort(portsAvailable.get(i).toString(), 9600)) {
					if (testConnection()) {
						opened = true;
						break;
					} else
						sm.closePort();
				}
			}
		}
		System.out.println(opened);
		return opened;
	}

	public void setPort(String port) {
		this.port = port;
	}
	public void destroy() {
		sm.closePort();
	}
	private boolean testConnection() {
		
    	byte [] command = CommandsIccrf.versionRequest().getBytes();
		testCommand(command);
		sm.send(command);

		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));

		if (response.length == 0) {
			return false;
		} else if (response[0] == compar[0]) {
			return true;
		} else {
			return false;
		}
	}

	public long findId() {
		
    	byte [] command = CommandsIccrf.findId().getBytes();
		testCommand(command);
		sm.send(command);

		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));

		if (response ==null || response.length == 0) {
			return 0;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
			return byteToInt2(subBytes(response,4,4));
		} else {
			return 0;
		}
	}
	
	public String initval(byte address,byte[] value) {
		byte[] command = CommandsIccrf.initval(address, value).getBytes();

		testCommand(command);
		System.out.println("initval");
		
		sm.send(command);
		
		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));
	
		if (response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
	    	 return bytesToHexString(response);
		}
		return null;
	}
	
	
	public String loadKey(byte address,byte[] password){
		byte[] command = CommandsIccrf.loadKey(address, password).getBytes();

		testCommand(command);
		System.out.println("loadKey");
		
		sm.send(command);
		
		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));
	
		if (response==null || response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
	    	 return bytesToHexString(response);
		}
		return null;
	}
	
	public String authentication(byte address){
		byte[] command = CommandsIccrf.authentication(address).getBytes();

		testCommand(command);
		System.out.println("authentication");

		sm.send(command);
		
		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};
		
   	 	System.out.println(bytesToHexString(response));

		if (response==null || response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
	    	 return bytesToHexString(response);
		}
		return null;
	}
	
	
	public byte[] read(byte address){
		byte[] command = CommandsIccrf.read(address).getBytes();
		testCommand(command);
		
		System.out.println("read");

		sm.send(command);
		
		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));
		
		if (response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
	    	 return response;
		}
		return null;
	}
	
	public String getGBK(byte[] respnse){
		if(respnse == null || respnse.length < 6){
			return null;
		}
		
		try {
			return new String(respnse,4,16,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String write(byte address,byte[] data){

		byte[] command = CommandsIccrf.write(address,data).getBytes();

		testCommand(command);
		System.out.println("write");

		sm.send(command);
		
		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));
		
		if (response==null || response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
	    	 return bytesToHexString(response);
		}
		return null;
	}
	
	public String beep(int msec){
		testCommand(CommandsIccrf.beep(msec).getBytes());
		System.out.println("beep");

		sm.send(CommandsIccrf.beep(msec).getBytes());
		
		byte[] response = sm.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(bytesToHexString(response));
		
		if (response==null || response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && crcByte(response) && response[2]==0) {
	    	 return bytesToHexString(response);
		}
		return null;
	}
	
	public boolean crcByte(byte[] data){
		byte clc = 0x00;
		if(data == null || data.length==1){
			return false;
		}
		
		int length = data.length;
		
		for(int i=0;i<length-1;i++){
			clc = (byte) (clc ^ data[i]);
		}
		
		if(clc == data[length-1]){
			return true;
		}else{
			return false;
		}
	}
	
    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString().toUpperCase();  
    }
    
	public static byte[] subBytes(byte[] src,int begin,int offset) {
		byte[] subBytes = new byte[offset];
		for(int i=0;i<offset;i++){
			subBytes[i] = src[begin+i];
		}
		return subBytes;
	}
	
	public static long byteToInt2(byte[] b) {
		int mask = 0xff;
		int temp = 0;
		long n = 0;
		for (int i = 4; i > 0 ; i--) {
			n <<= 8;
			temp = b[i-1] & mask;
			n |= temp;
		}
		return n;
	}
	
    public static void testCommand(byte[] command){
        for(int i=0;i<command.length;i++){
        	byte bt = command[i];
            System.out.printf("\t 0x%02X 0d%d \n", bt,bt);
        }
    }
    
	@Override
	public Tag[] findTokens() {
		return null;
	}

	@Override
	public Tag readAllBlocksMemory(byte[] id) throws RFIDException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag readMultipleBlocksMemory(byte[] id, int startBlockNumber,
			int endBlockNumber) throws RFIDException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeTokens(byte[] id, int startBlockNumber, byte[] data)
			throws RFIDException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tag[] readAllBlocksMemory() throws RFIDException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag readSingleBlockMemory(byte[] id, int blockNumber)
			throws RFIDException {
		// TODO Auto-generated method stub
		return null;
	}

}
