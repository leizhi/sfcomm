package es.deusto.smartlab.rfid;

/**
 * An RFIDException is thrown if an exception occurs while making an operation
 * to the reader/interrogator for a remote function call.
 * 
 * @author Xabier Etxebarría Espinosa
 */
public class RFIDException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2665354360800328878L;

	/**
	 * Generates the RFIDException.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public RFIDException(String message) {
		super(message);
	}
}
