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

	private static final String FIND_CARD="SELECT count(*) FROM Card WHERE uuid=?";

	private static final String SELECT_MAX_BY_LIKE="SELECT MAX(rfidcode) nowCode FROM Card WHERE rfidcode LIKE ?";
	
	//Card
	private static final String ADD_CARD="INSERT INTO Card(id,operatorId,cardDate,rfidcode,uuid,wineryId,branchId) VALUES(?,?,?,?,?,?,?)";

	public String nextId(String winery) {
		String nextCode=null;
		
		String wineryCode=null;
		wineryCode = IDGenerator.getKey(winery);
		if(wineryCode==null || wineryCode.length()>6){
			wineryCode="000000";
		}else if(wineryCode.length()>0 && wineryCode.length()<6){
			for(int i=wineryCode.length();i<6;i++){
				wineryCode +="0";
			}
		}
		
		String nowDate = dformat.format(Calendar.getInstance().getTime());
		
		if(nowDate==null || nowDate.length()!=6)
			nowDate="000000";
		
		String prefix = wineryCode+nowDate;
		
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
	
	public boolean isRegistr(Card card){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try{
			conn = DbConnectionManager.getConnection();
			pstmt = conn.prepareStatement(FIND_CARD);
			pstmt.setString(1, card.getUuid());
			
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
		if(log.isDebugEnabled()) log.debug("save Card start");

		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = DbConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(ADD_CARD);
			long cardId = IDGenerator.getNextID(conn,"Card");
			pstmt.setLong(1, cardId);
			pstmt.setLong(2, LoginSession.user.getId());
			pstmt.setTimestamp(3,new Timestamp(new Date().getTime()));
			pstmt.setString(4, card.getRfidcode());
			pstmt.setString(5, card.getUuid());
			
			if(log.isDebugEnabled()) log.debug("Winery="+card.getWinery());

			int wineryId = IDGenerator.getId("Winery", "definition", card.getWinery());
			
			pstmt.setInt(6, wineryId);
			if(log.isDebugEnabled()) log.debug("okay");

			pstmt.setInt(7, IDGenerator.getBranchId("Winery", wineryId));//branchId
			if(log.isDebugEnabled()) log.debug("okay");

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
