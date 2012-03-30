package test;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReadSerialPort {

	public ReadSerialPort() {
		super();
	}
	
    void connect (String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ){
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.FLOWCONTROL_NONE);
                
                InputStream in = serialPort.getInputStream();
                (new Thread(new SerialReader(in))).start();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }

    
    /** */
    public static class SerialReader implements Runnable {
        InputStream in;
        BufferedReader reader;
        
        public SerialReader ( InputStream in ) {
            this.in = in;
            this.reader = new BufferedReader(new InputStreamReader(in));
        }
        
        public void run () {
            String line = null;
            try{
               while ((line = reader.readLine()) != null) {
            	   System.out.println("Read line with " + line.length() + " characters: \"" + line + "\"");
               }
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }            
        }
    }

	public static void main(String[] args) {
        try {
            (new ReadSerialPort()).connect("/dev/ttyUSB1");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
	}
}

