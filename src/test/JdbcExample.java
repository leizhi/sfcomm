package test;


import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mooo.mycoz.db.pool.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;

public class JdbcExample {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
//		Statement stmt = null;
		ResultSet result = null;
		ResultSetMetaData rsmd = null;
		String sql = "";
		try {
/*
				 Class.forName("org.gjt.mm.mysql.Driver");
					// use proxool
                 con = DriverManager.getConnection("jdbc:mysql://localhost/test?useUnicode=true&amp;characterEncoding=utf8");
                 System.out.println("打开�?个连�?-------------");
                 System.out.println(con);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			sql = "show tables";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println("Name=" + rs.getString(1));
			}
			con.commit();
*/

			conn = DbConnectionManager.getConnection();
            System.out.println("打开连接-------------");
            System.out.println(conn);
            conn.setAutoCommit(false);
			//con.setCatalog("xpcBranch");
            pstmt = conn.prepareStatement("show tables");
			//sql = "select * from ClientJob";
			result = pstmt.executeQuery();
			rsmd = result.getMetaData();

			for(int i=0;i<rsmd.getColumnCount();i++){
				System.out.print(rsmd.getColumnName(i+1)+"\t");
			}
			System.out.println();

			while (result.next() && result.getRow()>0) {
				for(int i=0;i<rsmd.getColumnCount();i++){
					System.out.print(result.getString(i+1)+"\t");
				}
				System.out.println();
			}
			conn.commit();

/*
			// use mypool
			con = DbConnectionManager.getConnection();
			con.setAutoCommit(false);
			// con.setCatalog("xpcBranch");
			stmt = con.createStatement();
			sql = "SELECT  * FROM dm_xzqh_map_bak";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println("Name=" + rs.getString("XZQH_MC"));
			}
			con.commit();
*/
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			System.out.println("Exception: " + e.getMessage());
		} finally {
			pstmt.close();
			conn.close();
		}
	}

}
