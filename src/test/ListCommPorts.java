package test;

import java.util.Enumeration;

import gnu.io.*;

public class ListCommPorts {

	static void listPorts() {
		try {
			Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
			while (en.hasMoreElements()) {
				CommPortIdentifier portIdentifier = (CommPortIdentifier) en.nextElement();
				System.out.println(portIdentifier.getName() + " - "
						+ getPortTypeName(portIdentifier.getPortType()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String getPortTypeName(int portType) {
		switch (portType) {
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		default:
			return "unknown type";
		}
	}

	public static void main(String[] args) {
		listPorts();
	}
}
