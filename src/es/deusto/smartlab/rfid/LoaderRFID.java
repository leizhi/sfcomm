package es.deusto.smartlab.rfid;

import java.lang.reflect.Constructor;
import es.deusto.smartlab.rfid.InvalidDriverException;

/**
 * The LoaderRFID class loads the corresponding RFID kit.
 * 
 * @author Xabier Etxebarría Espinosa
 */
public class LoaderRFID {

	/**
	 * Loads the corresponding RFID kit, the Texas Instruments's or Microchip's
	 * kit.
	 * 
	 * @param kit
	 *            RFID Kit to load,
	 *            "es.deusto.smartlab.rfid.microchip.ImplementationMicrochip" or
	 *            "es.deusto.smartlab.rfid.texas.ImplementationTexas"
	 * @return InterfaceRFID
	 */
	public static InterfaceRFID load(String kit) throws InvalidDriverException {
		return load(kit, null);
	}

	/**
	 * Loads the corresponding RFID kit, the Texas Instruments's or Microchip's
	 * kit.
	 * 
	 * @param kit
	 *            RFID Kit to load,
	 *            "es.deusto.smartlab.rfid.microchip.ImplementationMicrochip" or
	 *            "es.deusto.smartlab.rfid.texas.ImplementationTexas"
	 * @param port
	 *            Serial port to use. The Texas Instruments's kit configures the
	 *            serial port at 9600 bauds, with 8 bits of data, no parity, and
	 *            one stop bit. The Microchip's kit configures the serial port
	 *            at 19200 bauds, with 8 bits of data, no parity, and one stop
	 *            bit.
	 * @return InterfaceRFID
	 */
	public static InterfaceRFID load(String kit, String port)
			throws InvalidDriverException {
		try {
			boolean found = false;
			Class<?> c = Class.forName(kit);

			for (int i = 0; i < c.getInterfaces().length; i++) {
				Class<?> inter = c.getInterfaces()[i];
				if (inter.getName().equals("es.deusto.smartlab.rfid.InterfaceRFID")) {
					found = true;
					break;
				}
			}

			if (found) {
				Class<?>[] params = new Class[0];
				Constructor<?> cons = c.getConstructor(params);
				InterfaceRFID iRFID = (InterfaceRFID) cons.newInstance();
				iRFID.setPort(port);
				if (iRFID.init())
					return iRFID;
				else
					return null;
			} else {
				throw new InvalidDriverException("Invalid class");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidDriverException("Invalid class");
		}
	}

	/**
	 * Unloads the corresponding RFID kit.
	 * 
	 * @param InterfaceRFID
	 *            RFID kit to unload.
	 */
	public static void unload(InterfaceRFID iRFID) {
		iRFID.destroy();
	}

}
