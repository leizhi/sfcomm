package gui;
/*
 * @(#)SimpleWrite.java	1.12 98/06/25 SMI
 * 
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license
 * to use, modify and redistribute this software in source and binary
 * code form, provided that i) this copyright notice and license appear
 * on all copies of the software; and ii) Licensee does not utilize the
 * software in a manner which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS
 * BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING
 * OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control
 * of aircraft, air traffic, aircraft navigation or aircraft
 * communications; or in the design, construction, operation or
 * maintenance of any nuclear facility. Licensee represents and
 * warrants that it will not use or redistribute the Software for such
 * purposes.
 */
import java.io.*;
import java.util.*;
import gnu.io.*;

/**
 * Class declaration
 *
 *
 * @author
 * @version 1.10, 08/04/00
 */
public class SimpleWrite {
	static byte LRC = 0x00;
	
    static Enumeration	      portList;
    static CommPortIdentifier portId;
    static String	      messageString = "Hello, world!";
    static SerialPort	      serialPort;
    static OutputStream       outputStream;
    static InputStream       inputStream;
    static boolean	      outputBufferEmptyFlag = false;
    /**
     * Method declaration
     *
     *
     * @param args
     *
     * @see
     */
    public static void main(String[] args) {
	boolean portFound = false;
	String  defaultPort = "/dev/term/a";
	defaultPort = "COM4";
	defaultPort = "/dev/ttyUSB0";
	if (args.length > 0) {
	    defaultPort = args[0];
	} 

	portList = CommPortIdentifier.getPortIdentifiers();

	while (portList.hasMoreElements()) {
	    portId = (CommPortIdentifier) portList.nextElement();

	    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

		if (portId.getName().equals(defaultPort)) {
		    System.out.println("Found port " + defaultPort);

		    portFound = true;

		    try {
			serialPort = 
			    (SerialPort) portId.open("SimpleWrite", 2000);
		    } catch (PortInUseException e) {
			System.out.println("Port in use.");

			continue;
		    } 

		    try {
			outputStream = serialPort.getOutputStream();
		    } catch (IOException e) {}

		    try {
			serialPort.setSerialPortParams(9600, 
						       SerialPort.DATABITS_8, 
						       SerialPort.STOPBITS_1, 
						       SerialPort.PARITY_NONE);
		    } catch (UnsupportedCommOperationException e) {}
	

		    try {
		    	serialPort.notifyOnOutputEmpty(true);
		    } catch (Exception e) {
			System.out.println("Error setting event notification");
			System.out.println(e.toString());
			System.exit(-1);
		    }
		    
		    System.out.println("Writing \""+messageString+"\" to "+serialPort.getName());

		    try {
			outputStream.write(messageString.getBytes());
			
	    	byte [] command = new byte [5];
	    	command[0] = (byte)0xA7;//起始符
	    	command[1] = (byte)0x02;//数据长度
	    	command[2] = (byte)0x01;//命令字
	    	command[3] = (byte)0x00;//寻卡模式
	    	command[4] = (byte)0x00;//求异或校验值
	        for(int i=0;i<2+command[1];i++){
	        	command[4]=(byte)(command[4]^command[i]);
	        }
	        
	        for(int i=0;i<command.length;i++){
	        	byte bt = command[i];
	            System.out.printf("\t 0x%02X 0d%d \n", bt,bt);
	        }
			outputStream.write(command);

			try {
				Thread.sleep(500);
				inputStream = serialPort.getInputStream();
				System.out.println("inputStream.available():"+ inputStream.available());
				while (inputStream.available() > 0) {
					int len = 0;
					byte CLRC = 0x00;
					
					byte[] buffer = new byte[400];
					try {
						len = inputStream.read(buffer);
						System.out.println("Data len:"+ len);
						LRC = buffer[len];
						
						for (int i = 0; i < len; i++) {
							System.out.printf("buffer["+i+"]\t 0x%02X \n",buffer[i]);
						}

						for (int i = 0; i <len; i++) {
							CLRC = (byte) (CLRC ^ buffer[i]);
						}
			            
			            System.out.println("LRC:"+LRC);
			            System.out.println("CLRC:"+CLRC);
			            
						if(((buffer[0]^(byte)0xA7)!=0) || (LRC!=CLRC) || buffer[2]!=0){
							throw new Exception("接收数据错误...");
						}
						
						byte[] cardId = new byte[4];
					     for(int i=0;i<4;i++){
					    	 cardId[i]=buffer[i+4]; //返回卡的序列号
					    	 System.out.printf("0x%02X \t",cardId[i]);
					     }
				    	 System.out.println();

				    	 System.out.println(bytesToHexString(cardId));

						Thread.sleep(250);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
		        } catch (Exception e) {
		            System.out.println("Error"+e.getMessage());
		        }
		    } catch (IOException e) {}

		    try {
		       Thread.sleep(2000);  // Be sure data is xferred before closing
		    } catch (Exception e) {}
		    serialPort.close();
		    System.exit(1);
		} 
	    } 
	} 

	if (!portFound) {
	    System.out.println("port " + defaultPort + " not found.");
	} 
    } 

//	 0xA7 
//	 0x06 
//	 0x00 
//	 0x01 
//	 0xEE 
//	 0xF9 
//	 0xFA 
//	 0xB9 
//	 0xF4 
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
}