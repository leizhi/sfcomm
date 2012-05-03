package com.mooo.sfwine;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.db.pool.DbConnectionManager;

public class CardDBObject {
	private static Log log = LogFactory.getLog(CardDBObject.class);
	
	//commons SQL
	private static final String FIND_CARD="SELECT count(*) FROM Card WHERE id=?";

	private static final String SELECT_MAX_BY_TABLE="SELECT MAX(ID) maxid FROM ";
	
	//Card
	private static final String ADD_CARD="INSERT INTO Card(id,operationDate,jobTypeId,wineJarId,rfidcode,operatorId,org_id,mode) VALUES(?,?,?,?,?,?,?,'有效')";

	//JobType
	private static final String ADD_JOB_TYPE="INSERT INTO JobType(id,definition) VALUES(?,?)";
	
	//Winery
	private static final String ADD_WINERY="INSERT INTO Winery(id,definition,address) VALUES(?,?,?)";
	
	//WineJar
	private static final String ADD_WINEJAR="INSERT INTO WineJar(id,wineryId,abbreviation,volume,volumeUnit) VALUES(?,?,?,?,?)";
	private static final String ADD_WINE_JAR="INSERT INTO WINE_JAR(orgId,wineJarKey,volume,wineJarVolume,volumeUnitCode) VALUES(?,?,?,?,?)";
	
	//volumeUnit
	private static final String  SELECT_UNIT="SELECT * FROM T_PUBLIC_CODE WHERE KIND='3'";
	
	private static final String FIND_UNIT ="SELECT *FROM T_PUBLIC_CODE  WHERE KIND='3' AND NAME=?";
	
	public static int getNextID(String table) {
		
		Connection connection=null;
        PreparedStatement pstmt = null;
        int nextId=0;
        try {
			connection = DbConnectionManager.getConnection();
            pstmt = connection.prepareStatement(SELECT_MAX_BY_TABLE+table);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	nextId = rs.getInt("maxid");
            }
            
            nextId ++;
		}catch (SQLException e) {
			e.printStackTrace();
	   }finally {
			try {
				if(pstmt != null)
					pstmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return nextId;
	}
	
	public int getId(String table,String fieldName,String fieldValue){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int id = 0;
		String sql = "SELECT id FROM "+table+" WHERE "+fieldName+"=?";
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fieldValue);
			
			ResultSet result = pstmt.executeQuery();
			if(result.next()){
				id = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	public int getOrgId(String table,String fieldName,String fieldValue){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int id = 0;
		String sql = "SELECT ORG_ID FROM "+table+" WHERE "+fieldName+"=?";
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fieldValue);
			
			ResultSet result = pstmt.executeQuery();
			if(result.next()){
				id = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	public boolean find(String table,String fieldName,String fieldValue){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		String sql = "SELECT count(*) FROM "+table+" WHERE "+fieldName+"=?";
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fieldValue);
			
			ResultSet result = pstmt.executeQuery();
			if(result.next()){
				count = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(count > 0)
			return true;
		
		return false;
	}
	
	public boolean isRegistr(Card card){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(FIND_CARD);
			pstmt.setLong(1, card.getId());
			
			ResultSet result = pstmt.executeQuery();
			if(result.next()){
				count = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(count > 0)
			return true;
		
		return false;
	}
	
	public void save(Card card) throws CardException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = DbConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			//JobType
			boolean exists = find("JobType","definition",card.getJobTypeName());
			
		
			int jobTypeId = 0;
			if(exists){
				jobTypeId = getId("JobType","definition",card.getJobTypeName());
			}else{
				pstmt = conn.prepareStatement(ADD_JOB_TYPE);
				jobTypeId = getNextID("JobType");
				pstmt.setInt(1, jobTypeId);
				pstmt.setString(2, card.getJobTypeName());
				pstmt.execute();
			}
			//volumeUnit  名字转编码
			Card c= findUnit(card.getVolumeUnit());
			if(c.getVolumeUnit()!=null){
				card.setVolumeUnit(c.getVolumeUnit());
			}
			
			//WineJar
			/*exists = find("WineJar","abbreviation",card.getWineJarKey());

			int wineJarId = 0;

			if(exists){
				wineJarId = getId("WineJar","abbreviation",card.getWineJarKey());
			}else{
				//Winery
				exists = find("Winery","definition",card.getWineryName());
				
				int wineryId = 0;
				if(exists){
					wineryId = getId("Winery","definition",card.getWineryName());
				}else{
					pstmt = conn.prepareStatement(ADD_WINERY);
					wineryId = getNextID("Winery");
					pstmt.setInt(1, wineryId);
					pstmt.setString(2, card.getWineryName());
					pstmt.setString(2, card.getWineryAddress());
					pstmt.execute();
				}
				
				//WineJar
				pstmt = conn.prepareStatement(ADD_WINEJAR);
				wineJarId = getNextID("WineJar");
				pstmt.setInt(1, wineJarId);
				pstmt.setInt(2, wineryId);
				pstmt.setString(3, card.getWineJarKey());
				pstmt.setString(4, card.getWineJarVolume());
				pstmt.setString(5, card.getVolumeUnit());
				pstmt.execute();
				
				}*/
			
			//T_ORGANIZATION
			exists = find("T_ORGANIZATION","name",card.getWineryName());
			
			int orgId = 0;
			if(exists){
				orgId = getOrgId("T_ORGANIZATION","name",card.getWineryName());
				
			}
			//WineJar
			exists = find("WINE_JAR","wineJarKey",card.getWineJarKey());

			int wineJarId = 0;
			if(exists){
				wineJarId = getId("WINE_JAR","wineJarKey",card.getWineJarKey());
			}else{
				

				//WineJar
			
				pstmt = conn.prepareStatement(ADD_WINE_JAR);
	
				pstmt.setInt(1, orgId);
				pstmt.setString(2, card.getWineJarKey());
				pstmt.setString(3, card.getWineVolume());
				pstmt.setString(4, card.getWineJarVolume());
				pstmt.setString(5, card.getVolumeUnit());
				pstmt.execute();
			}
			
			pstmt = conn.prepareStatement(ADD_CARD);
			long cardId = getNextID("Card");
			pstmt.setLong(1, cardId);
			pstmt.setTimestamp(2,new Timestamp(new Date().getTime()));
			pstmt.setInt(3, jobTypeId);
			pstmt.setInt(4, wineJarId);
			pstmt.setString(5, card.getRfidcode());
			pstmt.setLong(6, LoginSession.user.getId());
			pstmt.setInt(7, orgId);
			pstmt.execute();
			
			conn.commit();
			
			if(log.isDebugEnabled()) log.debug("save finlsh");
		} catch (Exception e) {
			System.out.println("CardDBObject Exception="+e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			
			throw new CardException(e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
	public String sixMD5(String plainText){
		
		 String sixteen="";
		 try {
			   MessageDigest md = MessageDigest.getInstance("MD5");   
			   md.update(plainText.getBytes());
			   byte b[] = md.digest();

			   int i;
			   
			   StringBuffer buf = new StringBuffer(""); 
			   for (int offset = 0; offset < b.length; offset++) {
			    i = b[offset];
			    if(i<0) i+= 256;
			    if(i<16)
			     buf.append("0");
			    buf.append(Integer.toHexString(i));
			   }
			   sixteen= buf.toString().substring(8,24);//16位的加密
			  // System.out.println("result: " + buf.toString());//32位的加密
			  // System.out.println("result: " + buf.toString().substring(8,24));//16位的加密

			  } catch (NoSuchAlgorithmException e) {
			   e.printStackTrace();
			  }

		 return sixteen;
	}
	
	
	
	public List findUnitList(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		List list=new ArrayList();
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(SELECT_UNIT); 
			ResultSet result = pstmt.executeQuery();
			Card car=null;
			while(result.next()){
				car=new Card();
				car.setVolumeUnit(result.getString(4));
				list.add(car);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return list;
	}
	public Card findUnit(String name){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Card car=new Card();
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(FIND_UNIT); 
			pstmt.setString(1, name);
			ResultSet result = pstmt.executeQuery();
			if(result.next()){
				car.setVolumeUnit(result.getString(3));
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return car;
	}
	public List findOrgList(int orgId){
		Connection conn = null;
		CallableStatement cs = null;
		List orgList=new ArrayList();
		try{
			conn = DbConnectionManager.getConnection();
			cs = conn.prepareCall("{call dt_organizationTree(?)}"); 
			cs.setInt(1, orgId);
		
			ResultSet result = cs.executeQuery();
			Card car=null;
			while(result.next()){
				car=new Card();
				car.setOrgId(result.getInt(1));
				car.setOrgName(result.getString(3));
				orgList.add(car);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				cs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orgList;
	}
}
