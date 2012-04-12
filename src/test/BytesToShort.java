package test;

public class BytesToShort {
	public static short bytesToShortWay(byte[] source, int startPos,
			int byteLength) {
		if (startPos < 0 || (byteLength != 2) || source == null
				|| source.length < startPos + byteLength - 1)
			return 0;
		else
			return bytesToshort(source[startPos + 0], source[startPos + 1],
					byteLength);
	}

	public static short bytesToshort(byte b0, byte b1, int byteLength) {
		if (byteLength != 2)
			return 0;
		int i = 0;
		short out = 0;
		if (b0 < 0)
			i = 256 + b0;
		else
			i = b0;
		out += i;
		if (b1 < 0)
			i = 256 + b1;
		else
			i = b1;
		out += (i << 8);
		return out;
	}
	
	public static short toShort(byte[] bytes) {
		short mask = 0xff;
		short temp = 0;
		short n = 0;
		for (int i = 0; i < 2 ; i++) {
			n <<= 8;
			temp = (short) (bytes[i] & mask);
			n |= temp;
		}
		return n;
	}
	
	public static void main(String args[]){
		byte[] bytes = {0x00,0x44};
		System.out.println(toShort(bytes));
	}
}