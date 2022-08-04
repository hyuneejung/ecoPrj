package rankUpdate;

import static util.MiniConn.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import member.EcoDto;

public class RankUpdateDao {
	/*
	 * 현재객체 added포인트 가져오기
	 * 
	 * ranksys테이블 기준치 확인
	 * 
	 *ranksys테이블 기준치 맞다면 자동 업데이트
	 * */
	public int rankUpdate(EcoDto ed, Connection conn) throws Exception {
		String sql = "UPDATE ECO_MEMBER SET RANKNO = (SELECT MAX(NO) FROM RANKSYS WHERE (SELECT ADDEDPOINT FROM ECO_MEMBER WHERE ID = ?) > MINPOINT) WHERE ID = ?";
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ed.getId());
			pstmt.setString(2, ed.getId());
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	public String changeRank(EcoDto ed, Connection conn) throws Exception {
		String sql = "SELECT R.RANK FROM ECO_MEMBER E JOIN RANKSYS R ON E.RANKNO = R.NO WHERE E.ID = ?";
		PreparedStatement pstmt = null;
		String rank = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ed.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				rank = rs.getString("RANK");
			}
		} finally {
			close(pstmt);
			close(rs);
		}
		return rank;
	}
}
