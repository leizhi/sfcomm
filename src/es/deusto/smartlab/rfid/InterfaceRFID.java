package es.deusto.smartlab.rfid;

/**
 * @author Xabier Echevarría Espinosa
 */
public interface InterfaceRFID {

	/**
	 * Initializes the corresponding RFID kit, the Texas Instruments's or
	 * Microchip's kit, testing the connection with the reader/interrogator and
	 * opening the serial port.
	 * 
	 * @return Returns success if an RFID kit was successfully initialized.
	 */
	public boolean init();

	/**
	 * Sets the specific serial port to connect with the reader/interrogator.
	 * 
	 * @param port
	 *            Serial port to use.
	 */
	public void setPort(String port);

	/**
	 * Checks if any tag is present, the reader/interrogator searchs for a
	 * variety of tags.
	 * 
	 * @return Returns a response with the tags found.
	 */
	public Tag[] findTokens();

	/**
	 * Reads a single memory block. Gets the data from one memory block.
	 * 
	 * @param id
	 *            Unique ID of the tag.
	 * @param blockNumber
	 *            Specifies the block to read.
	 * @return Returns a response with the tag read. The tag contains the kit
	 *         used, the type of tag, its unique ID and the data of the memory
	 *         block.
	 */
	public Tag readSingleBlockMemory(byte[] id, int blockNumber)
			throws RFIDException;

	/**
	 * Reads all the memory blocks. Gets the data from all the memory blocks.
	 * 
	 * @return Returns a response with the tags read. Each tag contains the kit
	 *         used, the type of tag, its unique ID and the data of all the
	 *         memory blocks.
	 */
	public Tag[] readAllBlocksMemory() throws RFIDException;

	/**
	 * Reads all the memory blocks. Gets the data from all the memory blocks.
	 * 
	 * @param id
	 *            Unique ID of the tag.
	 * @return Returns a response with the tag read. The tag contains the kit
	 *         used, the type of tag, its unique ID and the data of all the
	 *         memory blocks.
	 */
	public Tag readAllBlocksMemory(byte[] id) throws RFIDException;

	/**
	 * Reads multiple memory blocks. Gets the data from multiple memory blocks.
	 * 
	 * @param id
	 *            Unique ID of the tag.
	 * @param startBlockNumber
	 *            Specifies the first block to read.
	 * @param endBlockNumber
	 *            Specifies the end block to read.
	 * @return Returns a response with the tag read. The tag contains the kit
	 *         used, the type of tag, its unique ID and the data of multiple
	 *         memory blocks.
	 */
	public Tag readMultipleBlocksMemory(byte[] id, int startBlockNumber,
			int endBlockNumber) throws RFIDException;

	/**
	 * Writes memory blocks. Writes data to memory blocks of the addressed tag.
	 * 
	 * @param id
	 *            Unique ID of the tag.
	 * @param startBlockNumber
	 *            Specifies the first block to write.
	 * @param data
	 *            Data to be written to specified blocks.
	 */
	public void writeTokens(byte[] id, int startBlockNumber, byte[] data)
			throws RFIDException;

	/**
	 * Destroys the communication with the reader/interrogator, and closes the
	 * serial port.
	 */
	public void destroy();

}
