package com.mooo.sfwine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mooo.mycoz.db.pool.DbConnectionManager;

public class IDGenerator {
	
	//commons SQL

	private static final String SELECT_MAX_BY_TABLE="SELECT MAX(id) maxid FROM ";
	
	public synchronized static int getNextID(Connection connection,String table) {
		boolean notConn = false;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        int nextId=0;
        try {
        	if(connection==null){
        		notConn = true;
        		connection=DbConnectionManager.getConnection();
        	}
        	
			pstmt = connection.prepareStatement(SELECT_MAX_BY_TABLE + table);
			result = pstmt.executeQuery();
			while (result.next()) {
				nextId = result.getInt(1);
			}

			nextId++;
		}catch (SQLException e) {
			e.printStackTrace();
	   }finally {
			try {
				if(result != null)
					result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if(pstmt != null)
					pstmt.close();
				
				if(notConn){
					if(connection != null)
						connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return nextId;
	} // getNextID(String table)
	
	public static int getNextID(String table) {
		return getNextID(null,table);
	}
	
	public synchronized static int getId(Connection connection,String table,String fieldName,String fieldValue){
		boolean notConn = false;
		PreparedStatement pstmt = null;
		int id = -1;
		String sql = "SELECT id FROM "+table+" WHERE "+fieldName+"=?";
		try{
        	if(connection==null){
        		notConn = true;
        		DbConnectionManager.getConnection();
        	}
        	
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, fieldValue);
			
			System.out.println("sql:"+sql);

			ResultSet result = pstmt.executeQuery();
			if(result.next()){
				id = result.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("Exception="+e.getMessage());
		}finally{

			try {
				if(pstmt != null)
					pstmt.close();
				
				if(notConn){
					if(connection != null)
						connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	public static int getId(String table,String fieldName,String fieldValue){
		return getId( table, fieldName, fieldValue);
	}
	
	public synchronized static boolean find(String table,String fieldName,String fieldValue){
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

	/*
	 * getValues
	 */
	public synchronized static List<String> getWineryValues() {
		
		Connection connection=null;
        PreparedStatement pstmt = null;
        List<String> values = new ArrayList<String>();
        try {
        	String sql = "SELECT definition FROM Winery ORDER BY id";
        	
			connection = DbConnectionManager.getConnection();
            pstmt = connection.prepareStatement(sql);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	values.add(rs.getString(1));
            }
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
        return values;
	}
	
	/*
	 * getValues
	 */
	public synchronized static String getKey(String value) {
		
		Connection connection=null;
        PreparedStatement pstmt = null;
        String result = null;
        try {
        	String sql = "SELECT abbreviation FROM Winery WHERE definition=?";
        	
			connection = DbConnectionManager.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, value);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	result = rs.getString(1);
            }
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
        return result;
	}
}
