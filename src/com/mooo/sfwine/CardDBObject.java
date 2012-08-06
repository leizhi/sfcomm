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
	private static final String ADD_CARD="INSERT INTO Card(id,rfidcode,uuid,wineryId) VALUES(?,?,?,?)";
	private static final String ADD_CARD_TRACK="INSERT INTO CardTrack(id,cardId,userId,trackDate,statusId,isLast) VALUES(?,?,?,?,1,'Yes')";

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
			pstmt.setString(2, card.getRfidcode());
			pstmt.setString(3, card.getUuid());

			int wineryId = IDGenerator.getId("Winery", "definition", card.getWinery());
			pstmt.setInt(4, wineryId);
			pstmt.execute();
			
			pstmt = conn.prepareStatement(ADD_CARD_TRACK);
			pstmt.setLong(1, IDGenerator.getNextID(conn,"CardTrack"));
			pstmt.setLong(2, cardId);
			pstmt.setLong(3, LoginSession.user.getId());
			pstmt.setTimestamp(4,new Timestamp(new Date().getTime()));
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
