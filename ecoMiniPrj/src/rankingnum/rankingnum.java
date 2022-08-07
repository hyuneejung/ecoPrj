package rankingnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import member.EcoDto;
import util.MiniConn;

public class rankingnum {

	public void RankNum() {

//		EcoDto dto = null;
		// 드라이버 등록
		Connection conn = MiniConn.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// SQL 실행
    	String sql = "SELECT NICK , ADDEDPOINT FROM ECO_MEMBER ORDER BY ADDEDPOINT";
		// SQL 객체에담기 
		try {
			pstmt = conn.prepareStatement(sql);
			// SQL실행
			rs = pstmt.executeQuery();

//			if(rs.next()) {
				for(int e = 1; e<6 ; e++) {
					rs.next();

					String nick = rs.getString("NICK");
					int point = rs.getInt("ADDEDPOINT");
					System.out.println( e +" | " + nick+ " | " +point );	
//					e = e+1;
				}

		} catch (Exception e) {
//			System.out.println(rs);
			e.printStackTrace();
		} finally {
			MiniConn.close(conn);
			MiniConn.close(pstmt);
			MiniConn.close(rs);
		}
	}
}