package test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.*;

public class LPT // create a class called "lpt"
{
	public static void main(String[] argv) {
		try {
//			FileOutputStream os = new FileOutputStream("LPT1");
			FileOutputStream os = new FileOutputStream("COM1");

			PrintStream ps = new PrintStream(os);

			// ZPL goes here
			ps.println("^XA^FO50,50^A050,50^FDTech Support^FS^XZ");

			// flush buffer and close
			ps.close();
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e);
		}
	}
}