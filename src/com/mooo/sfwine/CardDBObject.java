package com.mooo.sfwine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mooo.mycoz.db.pool.DbConnectionManager;

public class CardDBObject {
	
	//Card
	private static final String ADD_CARD="INSERT INTO Card(id,operationDate,jobTypeId,wineJarId) VALUES(?,?,?,?)";
	
	//JobType
	private static final String ADD_JOB_TYPE="INSERT INTO JobType(id,definition) VALUES(?,?)";
	
	//Winery
	private static final String ADD_WINERY="INSERT INTO Winery(id,definition) VALUES(?,?)";
	
	//WineType
	private static final String ADD_WINE_TYPE="INSERT INTO WineType(id,definition) VALUES(?,?)";

	//WineLevel
	private static final String ADD_WINE_LEVEL="INSERT INTO WineLevel(id,definition) VALUES(?,?)";

	//SupervisorCompany
	private static final String ADD_SUPERVISOR_COMPANY="INSERT INTO SupervisorCompany(id,definition) VALUES(?,?)";

	//Supervisor
	private static final String ADD_SUPERVISOR="INSERT INTO Supervisor(id,companyId,definition) VALUES(?,?,?)";
	
	//WineJar
	private static final String ADD_WINEJAR="INSERT INTO WineJar(id,wineryId,abbreviation,volume,brewingDate,material,wineTypeId,wineLevelId,alcohol,volumeUnit) VALUES(?,?,?,?,?,?,?,?,?,?)";
	
	private static final String FIND_CARD="SELECT count(*) FROM Card WHERE id=?";

	private static final String SELECT_MAX_BY_TABLE="SELECT MAX(ID) maxid FROM ";

	private static final SimpleDateFormat dformat = new SimpleDateFormat("yyy-MM-dd");

//	public boolean find(String table,String category,String fieldName,String fieldValue){
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
	
	public void save(Card card){
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
			
			//WineJar
			exists = find("WineJar","abbreviation",card.getWineJarKey());

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
					pstmt.execute();
				}
				
				//WineType
				exists = find("WineType","definition",card.getWineType());
				
				int wineTypeId = 0;
				if(exists){
					wineTypeId = getId("WineType","definition",card.getWineType());
				}else{
					pstmt = conn.prepareStatement(ADD_WINE_TYPE);
					wineTypeId = getNextID("WineType");
					pstmt.setInt(1, wineTypeId);
					pstmt.setString(2, card.getWineType());
					pstmt.execute();
				}
				
				//WineLevel
				exists = find("WineLevel","definition",card.getWineLevel());
				
				int wineLevelId = 0;
				if(exists){
					wineLevelId = getId("WineLevel","definition",card.getWineLevel());
				}else{
					pstmt = conn.prepareStatement(ADD_WINE_LEVEL);
					wineLevelId = getNextID("WineLevel");
					pstmt.setInt(1, wineLevelId);
					pstmt.setString(2, card.getWineLevel());
					pstmt.execute();
				}
				
				//SupervisorCompany
				exists = find("SupervisorCompany","definition",card.getSupervisorCompanyKey());
				
				int companyId = 0;
				if(exists){
					wineLevelId = getId("SupervisorCompany","definition",card.getSupervisorCompanyKey());
				}else{
					pstmt = conn.prepareStatement(ADD_SUPERVISOR_COMPANY);
					companyId = getNextID("SupervisorCompany");
					pstmt.setInt(1, companyId);
					pstmt.setString(2, card.getSupervisorCompanyKey());
					pstmt.execute();
				}
				
				//Supervisor
				exists = find("Supervisor","definition",card.getSupervisorName());
				
				int supervisorId = 0;
				if(exists){
					wineLevelId = getId("Supervisor","definition",card.getSupervisorName());
				}else{
					pstmt = conn.prepareStatement(ADD_SUPERVISOR);
					supervisorId = getNextID("Supervisor");
					pstmt.setInt(1, supervisorId);
					pstmt.setInt(2, companyId);
					pstmt.setString(3, card.getSupervisorName());
					pstmt.execute();
				}
				
				//WineJar
				pstmt = conn.prepareStatement(ADD_WINEJAR);
				wineJarId = getNextID("WineJar");
				pstmt.setInt(1, wineJarId);
				pstmt.setInt(2, wineryId);
				pstmt.setString(3, card.getWineJarKey());
				
				pstmt.setString(4, card.getWineJarVolume());
				pstmt.setTimestamp(5,new Timestamp(dformat.parse(card.getBrewingDate()).getTime()));
				pstmt.setString(6, card.getMaterial());
				
				pstmt.setInt(7, wineTypeId);
				pstmt.setInt(8, wineLevelId);
				pstmt.setString(9, card.getAlcohol());
				pstmt.setString(10, card.getVolumeUnit());
				
				pstmt.execute();
			}
			
			pstmt = conn.prepareStatement(ADD_CARD);
			pstmt.setLong(1, card.getId());
			pstmt.setTimestamp(2,new Timestamp(new Date().getTime()));
			pstmt.setInt(3, jobTypeId);
			pstmt.setInt(4, wineJarId);
			pstmt.execute();
			
			conn.commit();
		} catch (Exception e) {
			System.out.println("CardDBObject Exception="+e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
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
}
