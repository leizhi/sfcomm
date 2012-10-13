package test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketTest {

	public static void main(String[] args) {
		String host="192.168.0.191";

		String port="8000";
		
		try {
			Socket socket = new Socket();
	//		socket.getChannel().open();
	//		Connects this socket to the server.
			socket.connect(new InetSocketAddress(host, new Integer(port)),1000);//建立连接最多等待6s
			
			socket.close();
			
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, new Integer(port)),1000);//建立连接最多等待6s

			socket.setKeepAlive(true);
			socket.setSoTimeout(1000*60*60*12);//time out 3s
	//		socket.setSoLinger(true, 1000);
//				socket.sendUrgentData(0xFF);

			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));  
			PrintStream print = new PrintStream(socket.getOutputStream(),true,"GBK");
			
			System.out.println("\thost:" + host + "\t port:" + port + "\t 连接成功!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("host:" + host + "\t port:" + port + "主机未找到!");
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("host:" + host + "\t port:" + port + "建立连接失败!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IOException"+e.getMessage());
		} 
	}
}
