package com.mooo.sfwine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.common.StringUtils;

public class SFClient {
	private static Log log = LogFactory.getLog(SFClient.class);

	private static Object initLock = new Object();
	private static SFClient factory = null;

	public static String host="118.123.244.109";
	public static Integer port=8000;
	
	private static Socket socket = null;

	private static InputStream in = null;
	private static OutputStream out = null;

	private static BufferedReader read = null;
	private static PrintStream print = null;
	
	public static void connect(){
		try {
			socket = new Socket();
	//		socket.getChannel().open();
	//		Connects this socket to the server.
			socket.connect(new InetSocketAddress(host, port),1000);//建立连接最多等待6s
			socket.setKeepAlive(true);
			socket.setSoTimeout(1000*60*60*12);//time out 3s
	//		socket.setSoLinger(true, 1000);
//				socket.sendUrgentData(0xFF);

			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			read = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));  
			print = new PrintStream(socket.getOutputStream(),true,"GBK");
			
			if(log.isDebugEnabled())log.debug("\thost:" + host + "\t port:" + port + "\t 连接成功!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			if(log.isDebugEnabled())log.debug("host:" + host + "\t port:" + port + "主机未找到!");
		} catch (SocketException e) {
			e.printStackTrace();
			if(log.isDebugEnabled())log.debug("host:" + host + "\t port:" + port + "建立连接失败!");
		} catch (Exception e) {
			e.printStackTrace();
			if(log.isDebugEnabled())log.debug("IOException"+e.getMessage());
		} 
	}
	
	public static SFClient getInstance() {
		synchronized (initLock) {
			if(factory==null){
				factory = new SFClient();
			}
			return factory;
		}
	}
	
	public static String request(String url){
			String buffer = null;
			try {
				if(log.isDebugEnabled())log.debug("command:"+url);
				print.println(url);//request to server
				
				buffer=read.readLine();
				if(buffer!=null) buffer = buffer.trim();//response from server
				if(log.isDebugEnabled())log.debug("response:"+buffer);
			} catch (Exception e) {
				e.printStackTrace();
				if(log.isDebugEnabled())log.debug("Exception"+e.getMessage());
			}
			return buffer;
	}
	
	public void end(){
		try {
			if(out!=null)
				out.close();
			if(in!=null)
				in.close();
			if(socket!=null)
				socket.close();
			
			factory = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getWineryValues(){
		//send command
		String REQ;
		String reponse;
		
		REQ = "*10";
		REQ += ";"+user.getId();
		REQ += "#";
		
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(!reponse.startsWith("*")||!reponse.endsWith("#")){
//			response = "数据格式不正确";
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		
		if(ret!=0) System.out.println("返回错误");
		
		if(args.length > 2) {
			reponse = doRequest.substring(doRequest.indexOf(";", 2)+1);
		}
		
	    String[] winerys=reponse.split(",");
		for(int i=0;i<winerys.length;i++){
			winerys[i]=winerys[i].trim();
		}
		return winerys;
	}
	
	public static String[] getCardTypes(){
		//send command
		String REQ;
		String reponse;
		
		REQ = "*11#";
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(!reponse.startsWith("*")||!reponse.endsWith("#")){
//			response = "数据格式不正确";
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		
		if(ret!=0) System.out.println("返回错误");
		
		if(args.length > 2) {
			reponse = doRequest.substring(doRequest.indexOf(";", 2)+1);
		}
		
	    String[] winerys=reponse.split(",");
		for(int i=0;i<winerys.length;i++){
			winerys[i]=winerys[i].trim();
		}
		return winerys;
	}
	
	public static boolean existCard(String uuid){
		//send command
		String REQ;
		String reponse;
		
		REQ = "*12;"+uuid+"#";
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(reponse==null || !reponse.startsWith("*")||!reponse.endsWith("#")){
//			response = "数据格式不正确";
			return true;
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		
		if(ret==0) return false;
		
		return true;
	}
	
	public static String nextRfidCode(String wineryName){
		//send command
		String REQ;
		String reponse;
		
		REQ = "*13;"+wineryName+"#";
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(!reponse.startsWith("*")||!reponse.endsWith("#")){
//			response = "数据格式不正确";
//			return true;
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		
		if(ret!=0) System.out.println("返回错误");
		
		if(args.length > 2) {
			reponse = doRequest.substring(doRequest.indexOf(";", 2)+1);
		}
		
		return reponse;
	}
	
	public static void saveCard(String rfidcode,String uuid,String wineryName,String cardTypeName) throws CardException {
		//send command
		String REQ;
		String reponse;
		
		REQ = "*14";
		REQ += ";"+user.getId();
		REQ += ";"+rfidcode;
		REQ += ";"+uuid;
		REQ += ";"+wineryName;
		REQ += ";"+cardTypeName;
		REQ += "#";
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(reponse==null || !reponse.startsWith("*")||!reponse.endsWith("#")){
			throw new CardException("标签注册异常");
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		if(ret!=0) throw new CardException(args[1]);
	}
	//StringUtils.hash(serialNumber)
	public static void saveUser(String userName,String userPassWord,String uuid,String mobile) throws CardException {
		//send command
		String REQ;
		String reponse;
		
		REQ = "*15";
		REQ += ";"+user.getId();
		REQ += ";"+userName;
		REQ += ";"+StringUtils.hash(userPassWord);
		REQ += ";"+uuid;
		REQ += ";"+mobile;
		REQ += "#";
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(reponse==null || !reponse.startsWith("*")||!reponse.endsWith("#")){
			throw new CardException("用户注册异常");
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		if(ret!=0) throw new CardException(args[1]);
	}

	public static Integer processLogin(String userName,String userPassWord){
		//send command
		String REQ;
		String reponse;
		
		REQ = "*80";
		REQ += ";"+userName;
		REQ += ";"+userPassWord;
		REQ += "#";
		
		reponse = request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		if(reponse==null || !reponse.startsWith("*")||!reponse.endsWith("#")){
			return -1;
		}
		
		String doRequest=reponse.substring(reponse.indexOf("*")+1,
				reponse.lastIndexOf("#"));

	    String[] args=doRequest.split(";");
	    
	    if(log.isDebugEnabled()) log.debug("length:"+args.length);
	    
		for(int i=0;i<args.length;i++){
			args[i]=args[i].trim();
			if(log.isDebugEnabled()) log.debug(args[i]);
		}
		
		int ret = new Integer(args[0]);
		
		if(ret==0 && args.length > 1)
			reponse = doRequest.substring(doRequest.lastIndexOf(";")+1);
		else
			reponse="-1";
		
		return  new Integer(reponse);
	}
	
	public static User user = new User();
	
	public static boolean allow = false;
	
	public static boolean staffSignal = false;

	public static boolean isAllow() {
		try{
			int userId =processLogin(user.getName(),StringUtils.hash(user.getPassword()));
			if(userId>0){
				user.setId(userId);
				allow = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return allow;
	}
	
	public static boolean isOpenNetwork() {
		try{
			if(socket==null || socket.isClosed())
				return false;
			
			socket.sendUrgentData(0xFF);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	/*
	public static void main(String[] args) {
		//send command
		String REQ;
		String reponse;
		
		SFClient sf = new SFClient("127.0.0.1",8000);
		
		REQ = "*80;admin;123456#";
		reponse = sf.request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);
		
		REQ = "*12;aaa53ba6c81d6698572472ec11544b94#";
		reponse = sf.request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);
		
		REQ = "*12;aaa53ba6c81d6698572472ec11544b22#";
		reponse = sf.request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);

		REQ = "*13;泸州酒业#";
		reponse = sf.request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);
		
		REQ = "*10;3#";
		reponse = sf.request(REQ);//0 no limit
		System.out.println("reponse:"+reponse);
		
		String[] str = sf.getWineryValues();
		System.out.println("reponse:"+str[0]);
		
		System.out.println("reponse:"+sf.existCard("aaa53ba6c81d6698572472ec11544b22"));
		System.out.println("reponse:"+sf.nextRfidCode("泸州酒业"));
		
		sf.end();
//		new SFClient("122.225.88.84",8000).connection(REQ);//0 no limit
	}
	*/
}