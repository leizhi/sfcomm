package es.deusto.smartlab.rfid.iso14443a;

import java.util.ArrayList;

import com.mooo.mycoz.common.StringUtils;
import es.deusto.smartlab.rfid.SerialManager;
import es.deusto.smartlab.rfid.ISO14443A;
/**
 * @author Xabier Echevarría Espinosa
 */
public class ImplementationISO14443A implements ISO14443A{
	
	public String port;
	public SerialManager serialManager;
	boolean opened = false;

	/**
	 * Constructor.
	 */
	public ImplementationISO14443A() {
		serialManager = new SerialManager();
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
	public boolean initialize() {
//		boolean opened = false;
		if (port != null) {
			if (serialManager.openPort(port, 9600)) {
				if (testConnection())
					opened = true;
				else
					serialManager.closePort();
			}
		} else {
			ArrayList<String> portsAvailable = new ArrayList<String>();
			portsAvailable = serialManager.getPorts();
			for (int i = 0; i < portsAvailable.size(); i++) {
				if (serialManager.openPort(portsAvailable.get(i).toString(), 9600)) {
					if (testConnection()) {
						opened = true;
						break;
					} else
						serialManager.closePort();
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
		if(serialManager!=null)
			serialManager.closePort();
	}
	
	private boolean testConnection() {
		
    	byte [] command = CommandsISO14443A.shakeHands().getBytes();
		
		System.out.println("testConnection:");
    	testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println("testConnection response:"+StringUtils.toHex(response));

		if (response.length == 0) {
			return false;
		} else if (response[0] == compar[0]) {
			return true;
		} else {
			return false;
		}
	}
	
	public String findSerialNumber() {
		
		CommandsISO14443A execCmd = CommandsISO14443A.findM1();
		if(execCmd==null)
			return null;
		
    	byte [] command = execCmd.getBytes();
		System.out.println("findSerialNumber:");

		testCommand(command);
		serialManager.send(command);

		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println("findSerialNumber response:"+StringUtils.toHex(response));
		boolean isOK = false;
		
		if (response ==null || response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
			isOK = true;
			return StringUtils.toHex(StringUtils.subBytes(response,4,4));
		}	
	
		if(isOK) {
			System.out.println("The Card is CARD_14443A_M1");
		}else{
			execCmd = CommandsISO14443A.findUL();
			if(execCmd==null)
				return null;
			
	    	command = execCmd.getBytes();
			System.out.println("findSerialNumber:");

			testCommand(command);
			serialManager.send(command);

			response = serialManager.read();

			System.out.println("findSerialNumber response:"+StringUtils.toHex(response));
			
			if (response ==null || response.length == 0) {
				return null;
			} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
				isOK = true;
				return StringUtils.toHex(StringUtils.subBytes(response,4,4));
			}	
		}
		
		if(isOK) 
			System.out.println("The Card is CARD_14443A_UL");
		else 
			System.out.println("The Card is 15693 or other Type");
		
		return null;
	}
	
	public boolean driveVersion() {
		
    	byte [] command = CommandsISO14443A.getVersion().getBytes();
		
		System.out.println("driveVersion:");
    	testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println("driveVersion response:"+StringUtils.toHex(response));

		if (response.length == 0) {
			return false;
		} else if (response[0] == compar[0]) {
			return true;
		} else {
			return false;
		}
	}

	public short findCardType() {
    	byte [] command = CommandsISO14443A.findCardType().getBytes();
		System.out.println("findCardType:");
    	testCommand(command);
		serialManager.send(command);

		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println("findCardType response:"+StringUtils.toHex(response));

		if (response ==null || response.length == 0) {
			return -1;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
			return StringUtils.toShort(StringUtils.swapBytes(StringUtils.subBytes(response,4,2)));
		} else {
			return -2;
		}
	}
	
	public boolean loadKey(byte address,byte[] password){
		byte[] command = CommandsISO14443A.loadKey(address, password).getBytes();

		testCommand(command);
		System.out.println("loadKey");
		
		serialManager.send(command);
		
		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(StringUtils.toHex(response));
	
		if (response==null || response.length == 0) {
			return false;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
			System.out.println("loadKey response:"+StringUtils.toHex(response));
			return true;
		}
		return false;
	}
	
	public boolean authentication(byte address){
		byte[] command = CommandsISO14443A.authentication(address).getBytes();

		testCommand(command);
		System.out.println("authentication");

		serialManager.send(command);
		
		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};
		
   	 	System.out.println(StringUtils.toHex(response));

		if (response==null || response.length == 0) {
			return false;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
			System.out.println("authentication response:"+StringUtils.toHex(response));
	    	 return true;
		}
		return false;
	}
	
	public boolean halt(){
		byte[] command = CommandsISO14443A.halt().getBytes();
		
		System.out.println("halt:");
		testCommand(command);

		serialManager.send(command);
		
		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println(StringUtils.toHex(response));
		
		if (response.length == 0) {
			return false;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
	    	 return true;
		}
		return false;
	}
	
	public boolean write(byte address,byte[] data){
		byte[] command = CommandsISO14443A.write(address,data).getBytes();
		
		System.out.println("write:");
		testCommand(command);

		serialManager.send(command);
		
		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println("write response:"+StringUtils.toHex(response));
		
		if (response==null || response.length == 0) {
			return false;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
	    	  return true;
		}
		return false;
	}
	
	public byte[] read(byte address){
		byte[] command = CommandsISO14443A.read(address).getBytes();
		
		System.out.println("read:");
		testCommand(command);

		serialManager.send(command);
		
		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

		System.out.println("read response:"+StringUtils.toHex(response));
		
		if (response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
	    	 return response;
		}
		return null;
	}
	
	public String beep(int msec){
//		findSerialNumber();
		
		byte[] command =CommandsISO14443A.beep(msec).getBytes();
		
		System.out.println("beep:");
		testCommand(command);
		serialManager.send(command);
		
		byte[] response = serialManager.read();
		byte[] compar = {(byte) 0xA7};

//		System.out.println("beep respnse:"+StringUtils.toHex(response));
		
		if (response==null || response.length == 0) {
			return null;
		} else if (response[0] == compar[0] && StringUtils.rightLRC(response) && response[2]==0) {
	    	 return StringUtils.toHex(response);
		}
		return null;
	}

    public static void testCommand(byte[] command){
    	if(command!=null)
        for(int i=0;i<command.length;i++){
        	byte bt = command[i];
            System.out.printf("\t 0x%02X 0d%d \n", bt,bt);
        }
    }
}
