package com.mooo.sfwine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.mooo.mycoz.db.pool.DbConnectionManager;

public class CardDBObject {

	private static final String ADD_CARD="INSERT INTO Card(id,createDate,jobTypeId,wineJarId) VALUES(?,?,?,?)";
	
	private static final String ADD_JOB_TYPE="INSERT INTO JobType(id,typeName) VALUES(?,?)";

	private static final String ADD_WINERY="INSERT INTO Winery(id,wineryName) VALUES(?,?)";

	private static final String ADD_WINEJAR="INSERT INTO WineJar(id,wineryName) VALUES(?,?)";
	
	private static final String FIND_CARD="SELECT count(*) FROM Card WHERE id=?";

	private static final String SELECT_MAX_BY_TABLE="SELECT MAX(ID) maxid FROM ";

//	public boolean find(String table,String category,String fieldName,String fieldValue){
	public int getNextID(String table) {
		
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
			
			boolean exists = find("JobType","typeName",card.getJobTypeName());
			
			int jobTypeId = 0;
			if(exists){
				jobTypeId = getId("JobType","typeName",card.getJobTypeName());
			}else{
				pstmt = conn.prepareStatement(ADD_JOB_TYPE);
				jobTypeId = getNextID("JobType");
				pstmt.setInt(1, jobTypeId);
				pstmt.setString(2, card.getJobTypeName());
				pstmt.execute();
			}
			
			exists = find("Winery","wineryName",card.getWineryName());

			int wineryJarId = 0;

			if(exists){
				wineryJarId = getId("Winery","wineryName",card.getWineryName());
			}else{
				
				exists = find("Winery","wineryName",card.getWineryName());
				
				int wineryId = 0;
				if(exists){
					wineryId = getId("Winery","wineryName",card.getWineryName());
				}else{
					pstmt = conn.prepareStatement(ADD_WINERY);
					wineryId = getNextID("Winery");
					pstmt.setInt(1, wineryId);
					pstmt.setString(2, card.getWineryName());
					pstmt.execute();
				}
				
				pstmt = conn.prepareStatement(ADD_WINEJAR);
				wineryJarId = getNextID("WineJar");
				pstmt.setInt(1, wineryJarId);
				pstmt.setString(2, card.getWineryName());
				pstmt.execute();
			}
			
			pstmt = conn.prepareStatement(ADD_CARD);
			pstmt.setLong(1, card.getId());
			pstmt.setTimestamp(2,new Timestamp(new Date().getTime()));
			pstmt.setInt(3, jobTypeId);
			pstmt.setInt(4, wineryJarId);
			pstmt.execute();
			
			conn.commit();
		} catch (Exception e) {
			System.out.println("CardDBObject Exception="+e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
