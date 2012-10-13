package es.deusto.smartlab.rfid.iso14443a;

import java.util.List;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.sfwine.CardException;
import com.mooo.sfwine.ISO14443AAction;

import es.deusto.smartlab.rfid.SerialManager;
import es.deusto.smartlab.rfid.ISO14443A;
/**
 * @author Xabier Echevarría Espinosa
 */
public class ImplementationISO14443A implements ISO14443A{
	private final static Object initLock = new Object();

	public String port;
	public SerialManager serialManager;
	boolean opened = false;

	private int cardType = 4;
	private String serialNumber;

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
	public boolean initSerial() {
		if (port != null) {
			if (serialManager.openPort(port, 9600)) {
				if (shakeHands())
					opened = true;
				else
					serialManager.closePort();
			}
		} else {
			List<String> ports = new SerialManager().getPorts();
			
			for(String commPort:ports){
				ISO14443AAction.whichPort=commPort;
			}
			
			if (serialManager.openPort(ISO14443AAction.whichPort, ISO14443AAction.whichSpeed)) {
				if (shakeHands())
					opened = true;
				else
					serialManager.closePort();
			}
		}
		System.out.println(opened);
		return opened;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public int getCardType() {
		return cardType;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void destroy() {
		if(serialManager!=null)
			serialManager.closePort();
	}
	
	public boolean shakeHands() {
    	byte [] command = CommandsISO14443A.shakeHands().getBytes();
		
		System.out.println("shakeHands:");
    	testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();

		System.out.println("shakeHands response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("shakeHands buffer:"+StringUtils.toHex(buf));
		return true;
	}
	
	public void initCard()  throws CardException{
		synchronized (initLock) {
			cardType = request();
			
			if(cardType<0){
				throw new CardException("请放入标签");
			}
			
			byte[] s1 = anticoll();
			select(s1);
			
			if(cardType==CommandsISO14443A.CARD_14443A_UL){
				byte[] s2 = anticoll2();
				select2(s2);
				serialNumber = StringUtils.toHex(s2);
			}
			serialNumber += StringUtils.toHex(s1);
		}
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
	
	public short request() {
		byte [] command = CommandsISO14443A.request().getBytes();
		System.out.println("request:");
		testCommand(command);
		
		serialManager.send(command);
		byte[] response = serialManager.read();

		if(response==null) return -1;;
		
		System.out.println("request response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		int respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return -1;
		} 
		System.out.println("request buffer:"+StringUtils.toHex(buf));
		return StringUtils.toShort(StringUtils.swapBytes(StringUtils.subBytes(response,4,2)));
	}

	public byte[] anticoll() {
		byte[] command = CommandsISO14443A.anticoll().getBytes();

		System.out.println("anticoll:");
		testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();

		System.out.println("anticoll response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return null;
		} 
		
		System.out.println("anticoll buffer:"+StringUtils.toHex(buf));
		byte[] serialAddr = StringUtils.subBytes(buf,2,4);
//		serialAddr = StringUtils.swapBytes(serialAddr);
		
		return serialAddr;
	}
	
	public boolean select(byte[] serialAddr) {
		byte [] command = CommandsISO14443A.select(serialAddr).getBytes();

		System.out.println("select:");
		testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();

		System.out.println("select response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("select buffer:"+StringUtils.toHex(buf));
		return true;
	}
	
	public byte[] anticoll2() {
		byte [] command = CommandsISO14443A.anticoll2().getBytes();

		System.out.println("anticoll2:");
		testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();

		System.out.println("anticoll2 response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return null;
		} 
		System.out.println("anticoll2 buffer:"+StringUtils.toHex(buf));
		byte[] serialAddr = StringUtils.subBytes(buf,2,4);
		System.out.println("serialAddr 2:"+StringUtils.toHex(StringUtils.swapBytes(serialAddr)));
		return serialAddr;
	}
	
	public boolean select2(byte[] serialAddr) {
		byte[] command = CommandsISO14443A.select2(serialAddr).getBytes();
		System.out.println("select2:");
		testCommand(command);
		
		serialManager.send(command);

		byte[] response = serialManager.read();

		System.out.println("select2 response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("select2 buffer:"+StringUtils.toHex(buf));
		return true;
	}

	public boolean loadKey(byte address,byte[] password){
		byte[] command = CommandsISO14443A.loadKey(address, password).getBytes();

		testCommand(command);
		System.out.println("loadKey");
		
		serialManager.send(command);
		
		byte[] response = serialManager.read();

		System.out.println("loadKey response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("loadKey buffer:"+StringUtils.toHex(buf));
		return true;
	}
	
	public boolean authentication(byte address){
		byte[] command = CommandsISO14443A.authentication(address).getBytes();

		testCommand(command);
		System.out.println("authentication");

		serialManager.send(command);
		
		byte[] response = serialManager.read();

		System.out.println("authentication response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("authentication buffer:"+StringUtils.toHex(buf));
		return true;
	}
	
	public boolean halt(){
		byte[] command = CommandsISO14443A.halt().getBytes();
		
		System.out.println("halt:");
		testCommand(command);

		serialManager.send(command);
		
		byte[] response = serialManager.read();

		System.out.println("halt response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("halt buffer:"+StringUtils.toHex(buf));
		return true;
	}
	
	public boolean write(byte address,byte[] data){
		byte[] command = CommandsISO14443A.write(address,data).getBytes();
		System.out.println("write:");
		testCommand(command);
		
		serialManager.send(command);
		
		byte[] response = serialManager.read();

		System.out.println("write response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("write buffer:"+StringUtils.toHex(buf));
		return true;
	}
	
	public byte[] read(byte address){
		byte[] command = CommandsISO14443A.read(address).getBytes();
		
		System.out.println("read:");
		testCommand(command);

		serialManager.send(command);
		
		byte[] response = serialManager.read();

		System.out.println("read response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return null;
		} 
		System.out.println("read buffer:"+StringUtils.toHex(response));
		
		return response;
	}
	
	public boolean beep(int msec){
		byte[] command =CommandsISO14443A.beep(msec).getBytes();
		
		System.out.println("beep:");
		testCommand(command);
		serialManager.send(command);
		
		byte[] response = serialManager.read();

		System.out.println("beep response:"+StringUtils.toHex(response));
		byte LRC = 0x00;

		byte respS  = (byte)response[0];
		LRC = respS;

		byte respE  = (byte)response[response.length-1];

		byte respL = (byte)response[1];
		LRC = (byte) (LRC^respL);
		
		byte[] buf = new byte[respL];
		for(int i=0;i<respL;i++){
			buf[i]=response[i+2];
			LRC = (byte) (LRC^buf[i]);
		}
		if ( (byte)respS!=(byte)0xA7 || LRC!=respE || buf[0]!= 0) {
			return false;
		} 
		System.out.println("beep buffer:"+StringUtils.toHex(buf));
		return true;
	}

    public static void testCommand(byte[] command){
    	if(command!=null)
        for(int i=0;i<command.length;i++){
        	byte bt = command[i];
            System.out.printf("\t 0x%02X 0d%d \n", bt,bt);
        }
    }
}
