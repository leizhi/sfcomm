package test;

import es.deusto.smartlab.rfid.*;

/**
 * @author Xabier Echevarría Espinosa
 */
public class IccrfExample {

	public IccrfExample() {

	}

	public static void main(String args[]) {

		IccrfExample demo = new IccrfExample();
		InterfaceRFID iRFID = null;
		try {
			iRFID = LoaderRFID.load("es.deusto.smartlab.rfid.iccrf.ImplementationIccrf");
		} catch (InvalidDriverException ex) {
			ex.printStackTrace();
			System.out.println("iRFID " + ex.getMessage());
		}
		System.out.println("iRFID " + iRFID);
		if (iRFID != null) {
			Tag[] ids = iRFID.findTokens();
			if (ids != null) {
				System.out.println("Found " + ids.length + " tag(s) in range");

				for (int i = 0; i < ids.length; i++) {
					String asciiData = "Texas Example";
					byte[] dataToWrite = asciiData.getBytes();
					try {
						iRFID.writeTokens(ids[i].getTagID(), 0, dataToWrite);
					} catch (RFIDException ex) {
						ex.printStackTrace();
					}

					Tag read;
					try {
						read = iRFID.readMultipleBlocksMemory(ids[i].getTagID(), 0, 3);
						StringBuffer data = new StringBuffer();
						byte[][] dataRead = read.getTagData();
						for (int j = dataRead.length - 1; j >= 0; j--) {
							for (int k = 3; k >= 0; k--) {
								char oneByte = (char) dataRead[j][k];
								if (oneByte != 0)
									data.insert(0, oneByte);
							}
						}
						System.out.println("Memory BLocks: " + data);
					} catch (RFIDException ex) {
						ex.printStackTrace();
					}
				}
			}
			LoaderRFID.unload(iRFID);
		}

	}

}
