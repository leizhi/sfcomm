package com.mooo.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;
import com.mooo.serial.SerialUtil;

import es.deusto.smartlab.rfid.SerialManager;

public class Chip485 {
	private static Log log = LogFactory.getLog(Chip485.class);

	public String portName;
	public int speed;
	
	public SerialManager serialManager;
	boolean opened = false;
	
	public Chip485(){
		serialManager = new SerialManager();
		
		List<String> ports = new SerialManager().getPorts();
		
		for(String activityPort:ports){
			portName=activityPort;
		}
		
		speed=9600;
	}
	
	public void exec() {
//		byte[] command={0x01,0x05,0x00,0x10,(byte) 0xFF,0x00};
		byte[] command={0x01,0x05,0x00,0x10,(byte) 0x00,0x00};
		serialManager.openPort(portName, speed);
		
		System.out.println("exec request:");
		SerialUtil.testCommand(SerialUtil.crcw(command));
		serialManager.send(SerialUtil.crcw(command));
		
		byte[] response = serialManager.read();
		System.out.println("write response:"+StringUtils.toHex(response));
		
		serialManager.closePort();
	}
	
	private String host;
	private int port;
	
	private long createTime;
	
	private int maxConnMSec;

	public void start(double maxConnTime) throws IOException {
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		BufferedReader read = null;
		PrintStream print = null;
		
		String buffer = null;
		
		long finishTime = 0l;
		long hours = 0l;
		long minutes = 0l;
		long seconds = 0l;
		
		long expendsTime = 0l;
		
		long startTime = System.currentTimeMillis();
			try {
				socket = new Socket();
//				socket.getChannel().open();
//				Connects this socket to the server.
				socket.connect(new InetSocketAddress(host, port),1000*60*60*12);//建立连接最多等待6s
//				socket.setKeepAlive(true);
				socket.setSoTimeout(1000*60*60*12);//time out 3s
//				socket.setSoLinger(true, 1000);
				
				in = socket.getInputStream();
				out = socket.getOutputStream();
				read = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));  
				print = new PrintStream(socket.getOutputStream(),true,"GBK");
				
				finishTime = System.currentTimeMillis();
				expendsTime = finishTime - startTime;
				if(log.isDebugEnabled())log.debug("\thost:" + host + "\t port:" + port + "\t expendsTime:"+expendsTime+"\t 连接成功!");
				
				boolean forever = true;
				while (forever) {
				
				// write do
				finishTime = System.currentTimeMillis();
				expendsTime = finishTime - startTime;
				if(log.isDebugEnabled())log.debug("write begin expendsTime:"+expendsTime);
				
//				print.println(COMMAND_TEST);

				finishTime = System.currentTimeMillis();
				expendsTime = finishTime - startTime;
				if(log.isDebugEnabled())log.debug("write end/read begin expendsTime:"+expendsTime+"\t in.available():"+in.available());

				// read do
//				if(buffer!=null)
				buffer=read.readLine().trim();
				
				print.println(buffer);

				finishTime = System.currentTimeMillis();
				hours = (finishTime - startTime) / 1000 / 60 / 60;
				minutes = (finishTime - startTime) / 1000 / 60 - hours * 60;
				seconds = (finishTime - startTime) / 1000 - hours * 60 * 60 - minutes * 60;
				
				expendsTime = finishTime - startTime;
				
				if(log.isDebugEnabled())log.debug("expendsTime:"+expendsTime);
				
				if(log.isDebugEnabled())log.debug("host:" + host + "\t port:" 
						+ port +"\t expends:   " + hours + ":" + minutes + ":"	
						+ seconds+"\t expendsTime:"+expendsTime);
				
				// do end for timeout
				long age = System.currentTimeMillis() - createTime;
				if(log.isDebugEnabled())log.debug("age:"+age+"\tmaxConnMSec:"+maxConnMSec);

				Thread.sleep(500);
				
				if (age > maxConnMSec && maxConnMSec > 0) { // Force a reset at the max
					System.out.println("===超时 退出=====");
					break;
				}

			}//loop while
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
				if(log.isDebugEnabled())log.debug("host:" + host + "\t port:" + port + "主机未找到!");
			} catch (SocketException e) {
				e.printStackTrace();
				if(log.isDebugEnabled())log.debug("host:" + host + "\t port:" + port + "建立连接失败!");
			} catch (IOException e) {
				e.printStackTrace();
				if(log.isDebugEnabled())log.debug("IOException"+e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				if(log.isDebugEnabled())log.debug("Exception"+e.getMessage());
			} finally {
				
				try {
					if(out!=null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					if(in!=null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					if(socket!=null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//clear 
				buffer = null;
				
				socket = null;
				out = null;
				in = null;
			}//end try
			
			if(log.isDebugEnabled())log.debug("host:" + host + "\t port:" + port + "连接完成!");
	}

	public static void main(String args[]){
		Chip485 chip = new Chip485();
		chip.exec();
	}
}
