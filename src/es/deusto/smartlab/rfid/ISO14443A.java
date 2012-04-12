package es.deusto.smartlab.rfid;

public interface ISO14443A {
	
	boolean driveVersion();
	
	long findSerialNumber(short cardType);
	
	short findCardType();
	
	void initialize();
	
	void destroy();
	
	boolean loadKey(byte address,byte[] password);
	
	byte[] readBytes(byte address);
	
	String read(byte address,String uc);
	
	boolean writeBytes(byte address,byte[] buffer);
	
	boolean write(byte address,String buffer);
	
	boolean authentication(byte address);
	
	boolean shakeHands();
	
	boolean halt();
}