package com.mooo.sfwine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.db.pool.DbConnectionManager;

public class CardDBObject {
	private static Log log = LogFactory.getLog(CardDBObject.class);
	
	private static SimpleDateFormat dformat = new SimpleDateFormat("yyMMdd");

	//commons SQL
	private static final String FIND_CARD="SELECT count(*) FROM Card WHERE id=?";

	private static final String SELECT_MAX_BY_TABLE="SELECT MAX(ID) maxid FROM ";
	
	private static final String SELECT_MAX_BY_LIKE="SELECT MAX(rfidcode) nowCode FROM Card WHERE rfidcode LIKE ?";
	
	//Card
	private static final String ADD_CARD="INSERT INTO Card(id,operationDate,rfidcode,operatorId,mode,jobTypeId) VALUES(?,?,?,?,'有效',1)";

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
	
	public String nextId(String zipCode) {
		String nextCode=null;
		
		if(zipCode==null || zipCode.length()!=6)
			zipCode="000000";
		
		String nowDate = dformat.format(Calendar.getInstance().getTime());
		
		if(nowDate==null || nowDate.length()!=6)
			nowDate="000000";
		
		String prefix = zipCode+nowDate;
		
		String nextNumber = "0000";
		
		Connection connection=null;
        PreparedStatement pstmt = null;
        try {
			connection = DbConnectionManager.getConnection();
            pstmt = connection.prepareStatement(SELECT_MAX_BY_LIKE);
            pstmt.setString(1, prefix+"%");
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	nextCode = rs.getString(1);
            }
            
            if(nextCode!=null && nextCode.length()==16){
            	nextNumber = nextCode.substring(12);
            	
            	int number = new Integer(nextNumber);
            	
            	number++;
            	
            	if(number<10){
            		nextNumber = "000"+number;
            	}else if(number<100){
            		nextNumber = "00"+number;
            	}else if(number<1000){
            		nextNumber = "0"+number;
            	}else{
            		nextNumber = ""+number;
            	}
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
        
        nextCode = prefix+nextNumber;
        
		return nextCode;
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
	
	public void save(Card card) throws CardException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = DbConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(ADD_CARD);
			long cardId = getNextID("Card");
			pstmt.setLong(1, cardId);
			pstmt.setTimestamp(2,new Timestamp(new Date().getTime()));
			pstmt.setString(3, card.getRfidcode());
			pstmt.setLong(4, LoginSession.user.getId());
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
	
}
