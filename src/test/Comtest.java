package test;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;

public class Comtest {

	public static void main(String[] args) {
		try {
			
			Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier portId = null;
			while (en.hasMoreElements()) {
				portId = (CommPortIdentifier) en.nextElement();
				System.out.println("coms :" + portId.getName());
				
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					System.out.println(portId.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}