package rk;

import static util.MiniConn.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import member.EcoDto;
public class RkDao {
	
	public RkDto rankBonus(Connection conn, EcoDto ed) throws Exception {
		
		String sql = "SELECT R.RANK, R.BONUS FROM RANKSYS R JOIN ECO_MEMBER E ON R.NO = E.RANKNO WHERE E.ID = ?";
		PreparedStatement pstmt = null;
		RkDto rd = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ed.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				rd = new RkDto();
				rd.setRankName(rs.getString("RANK"));
				rd.setBonus(rs.getInt("BONUS"));
			}
		} finally {
			close(pstmt);
			close(rs);
		}
		
		return rd;
	}
}
