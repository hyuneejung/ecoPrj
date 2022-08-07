package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.MiniConn;

public class EcoRank {

	public void EcoRank() {
		// 기부왕 랭킹 출력
		
		// 드라이버 등록
		Connection conn = MiniConn.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// SQL 실행
		String sql = "SELECT E.NICK, H.POINT FROM HISTORY H,  ECO_MEMBER E WHERE H.ID = E.ID AND H.POINT <= 0";
		
		// SQL 객체에담기
		try {
			pstmt = conn.prepareStatement(sql);
			
			// SQL실행
			rs = pstmt.executeQuery();
			
			for (int e = 1; e < 6; e++) {
				rs.next();
				
				String nick = rs.getString("NICK");
				int point = rs.getInt("POINT");
				System.out.println(e + "등" + " | " + nick + " | " +"누적 기부액 : " + point*-1);
				
			}
			
		} catch (Exception e) {
			
		} finally {
			MiniConn.close(conn);
			MiniConn.close(pstmt);
			MiniConn.close(rs);
		}
		
		
	}
}
