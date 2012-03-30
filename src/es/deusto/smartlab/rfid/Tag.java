package es.deusto.smartlab.rfid;

/**
 * The Tag class contains the important information of a tag: the type, the
 * data, etc.
 * 
 * @author Xabier Echevarría Espinosa
 */
public class Tag {

	private String kitRFID;
	private String tagType;
	private byte[] tagID;
	private byte[][] tagData;

	/**
	 * Constructor.
	 */
	public Tag() {
		kitRFID = null;
		tagType = null;
		tagID = null;
		tagData = null;
	}

	/**
	 * Constructor.
	 * 
	 * @param kitRFID
	 *            Sets used RFID kit.
	 * @param tagType
	 *            Sets tag type.
	 */
	public Tag(String kitRFID, String tagType) {
		this.kitRFID = kitRFID;
		this.tagType = tagType;
		tagID = null;
		tagData = null;
	}

	/**
	 * Sets tag type.
	 * 
	 * @param tagType
	 *            Tag Type.
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	/**
	 * Sets tag ID.
	 * 
	 * @param tagID
	 *            Tag ID.
	 */
	public void setTagID(byte[] tagID) {
		this.tagID = tagID;
	}

	/**
	 * Sets tag data.
	 * 
	 * @param tagData
	 *            Tag data.
	 */
	public void setTagData(byte[][] tagData) {
		this.tagData = tagData;
	}

	/**
	 * Gets used RFID kit.
	 * 
	 * @return Returns used RFID kit.
	 */
	public String getKitRFID() {
		return kitRFID;
	}

	/**
	 * Gets tag type.
	 * 
	 * @return Returns tag type.
	 */
	public String getTagType() {
		return tagType;
	}

	/**
	 * Gets tag ID.
	 * 
	 * @return Returns tag ID.
	 */
	public byte[] getTagID() {
		return tagID;
	}

	/**
	 * Gets tag ID.
	 * 
	 * @return Returns tag ID as String.
	 */
	public String getTagIDAsString() {
		if (this.getTagType().equals("ISO 15693")
				|| this.getTagType().equals("Tag-it")) {
			StringBuffer id = new StringBuffer();
			for (int i = 0; i < this.tagID.length;) {
				String byteStr = Integer.toHexString(this.tagID[i++] & 0xff)
						.toUpperCase();
				if (byteStr.length() < 2)
					byteStr = "0" + byteStr;
				id.insert(0, byteStr);
			}
			return id.toString();
		} else {
			StringBuffer id = new StringBuffer();
			for (int i = this.tagID.length - 1; i >= 0;) {
				String byteStr = Integer.toHexString(this.tagID[i--] & 0xff)
						.toUpperCase();
				if (byteStr.length() < 2)
					byteStr = "0" + byteStr;
				id.insert(0, byteStr);
			}
			return id.toString();
		}
	}

	/**
	 * Gets tag data.
	 * 
	 * @return Returns tag data.
	 */
	public byte[][] getTagData() {
		return tagData;
	}

}
