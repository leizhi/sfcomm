package test;

import es.deusto.smartlab.rfid.*;

/**
 * @author Xabier Echevarría Espinosa
 */
public class IccrfCall {

	public static void main(String args[]) {

		InterfaceRFID iRFID = null;
		try {
			iRFID = LoaderRFID.load("es.deusto.smartlab.rfid.iccrf.ImplementationIccrf");
		} catch (InvalidDriverException ex) {
			ex.printStackTrace();
			System.out.println("iRFID " + ex.getMessage());
		}
		System.out.println("iRFID " + iRFID);
		if (iRFID != null) {
			
			LoaderRFID.unload(iRFID);
		}

	}

}
