package es.deusto.smartlab.rfid;

/**
 * An InvalidDriverClassException is thrown if an exception occurs while loading
 * a RFID kit.
 * 
 * @author Xabier Etxebarría Espinosa
 */
public class InvalidDriverException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6564776566962590881L;

	/**
	 * Generates the InvalidDriverClassException.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public InvalidDriverException(String message) {
		super(message);
	}
}
