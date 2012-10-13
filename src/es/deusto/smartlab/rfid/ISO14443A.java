package es.deusto.smartlab.rfid;

import com.mooo.sfwine.CardException;

public interface ISO14443A {
	
	boolean driveVersion();
	
	short request();
	
	boolean initSerial();
	
	void initCard() throws CardException;

	void destroy();
	
	boolean loadKey(byte address,byte[] password);
	
	byte[] read(byte address);
	
	boolean write(byte address,byte[] buffer);
	
	boolean authentication(byte address);
	
	boolean shakeHands();
	
	boolean halt();
}