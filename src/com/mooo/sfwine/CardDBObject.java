package com.mooo.sfwine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.db.pool.DbConnectionManager;

public class CardDBObject {
	private static Log log = LogFactory.getLog(CardDBObject.class);
	
	//Card
	private static final String ADD_CARD="INSERT INTO Card(id,operatorId,cardDate,rfidcode,uuid,wineryId,branchId) VALUES(?,?,?,?,?,?,?)";

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
