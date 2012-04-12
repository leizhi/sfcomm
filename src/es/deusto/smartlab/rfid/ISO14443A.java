package es.deusto.smartlab.rfid;

public interface ISO14443A {
	
	boolean driveVersion();
	
	String findSerialNumber();
	
	short findCardType();
	
	boolean initialize();
	
	void destroy();
	
	boolean loadKey(byte address,byte[] password);
	
	byte[] read(byte address);
	
	boolean write(byte address,byte[] buffer);
	
	boolean authentication(byte address);
	
//	boolean shakeHands();
	
	boolean halt();
}