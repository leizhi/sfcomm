package test;

import com.mooo.mycoz.common.StringUtils;

public class Test {

	public static void main(String[] args) {
		byte[] bytes = new byte[2];
		bytes[0] = 0x00;
		bytes[1] = 0x44;
		
		byte[] obytes = StringUtils.orderByDESC(bytes);
		
		System.out.printf("orderByDESC:0x%x\n",obytes[0]);
		
		System.out.printf("orderByDESC:0x%x\n",obytes[1]);

		//System.out.println("orderByDESC:"+obytes[0]);
		
	}
}
