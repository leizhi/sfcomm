package test;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.sfwine.IDGenerator;

public class Test {

	public static void main(String[] args) {
		byte[] bytes = new byte[2];
		bytes[0] = 0x00;
		bytes[1] = 0x44;
		
		byte[] obytes = StringUtils.swapBytes(bytes);
		
		System.out.printf("orderByDESC:0x%x\n",obytes[0]);
		
		System.out.printf("orderByDESC:0x%x\n",obytes[1]);

		System.out.println("orderByDESC:"+obytes[0]);
		int wineryId = IDGenerator.getId("Winery", "definition", "金川酒厂");

		System.out.println("wineryId:"+wineryId);
//010F001000040100
	}
}
