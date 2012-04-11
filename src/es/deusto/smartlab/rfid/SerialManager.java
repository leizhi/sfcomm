package es.deusto.smartlab.rfid;

import gnu.io.*;
import java.io.*;
import java.util.*;

/**
 * The SerialManager class manages the serial port.
 * 
 * @author Xabier Echevarría Espinosa
 */
public class SerialManager {
	private SerialPort mySerialPort = null;
	private InputStream in;
	private OutputStream out;
	private boolean portOpen;

	/**
	 * Constructor. Sets status of the serial port.
	 */
	public SerialManager() {
		portOpen = false;
	}

	/**
	 * Opens a serial port.
	 * 
	 * @param whichPort
	 *            Sets serial port device.
	 * @param whichSpeed
	 *            Sets serial port baud rate.
	 * @return Returns success if a port was successfully opened.
	 */
	public boolean openPort(String whichPort, int whichSpeed) {
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(whichPort);
			mySerialPort = (SerialPort) portId.open("Serial Port" + whichPort,9600);
			try {
				mySerialPort.setSerialPortParams(whichSpeed,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				System.out.println("Comm parameter error. Probably an unsupported speed");
			}

			try {
				in = mySerialPort.getInputStream();
				out = mySerialPort.getOutputStream();
			} catch (IOException e) {
				System.out.println("Couldn't establish streams for serial I/O");
			}

			portOpen = true;
			return portOpen;

		} catch (Exception e) {
			System.out.println("Port in Use");
			return portOpen;
		}
	}

	/**
	 * Closes the serial port and streams.
	 * 
	 * @return Returns success if the port was closed.
	 */
	public boolean closePort() {
		in = null;
		out = null;
		
		if(mySerialPort!=null)
			mySerialPort.close();
		
		portOpen = false;
		return !portOpen;
	}

	/**
	 * Gets a list of available serial ports.
	 * 
	 * @return Returns a list of the serial ports
	 */
	public ArrayList<String> getPorts() {
		ArrayList<String> portsAvailable = new ArrayList<String>();
		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portsAvailable.add(portId.getName());
			}
		}
		return portsAvailable;
	}

	/**
	 * Reads the last bytes of the input buffer.
	 * 
	 * @return Returns the last bytes of the input buffer.
	 */
	public byte[] read() {
		if(in==null)
			return null;
		
		Vector<Byte> temporal = new Vector<Byte>();

		try {
			Thread.sleep(50);
			
			while (in.available() > 0) {
				int newData = 0;
				byte[] newDatas = new byte[512];
				try {
					newData = in.read(newDatas);
					for (int i = 0; i < newData; i++)
						temporal.addElement(newDatas[i]);
					
//					Thread.sleep(250);
				} catch (IOException ex) {
					System.out.println("Error:"+ex.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println("Error:"+e.getMessage());
		}

		byte[] response = new byte[temporal.size()];
		for (int i = 0; i < temporal.size(); i++)
			response[i] = temporal.get(i).byteValue();
		return response;
	}

	/**
	 * Sends bytes to the serial port.
	 * 
	 * @param outputByte
	 *            Bytes to send.
	 */
	public void send(byte[] outputByte) {
		try {
			if(out != null)
				out.write(outputByte);
		} catch (IOException e) {
			System.out.println("Couldn't send byte");
		}
	}

	/**
	 * Checks the serial port.
	 * 
	 * @return Returns success if the port is opened.
	 */
	protected boolean isPortOpen() {
		return portOpen;
	}

}
