package sendMoney;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import member.EcoDto;
import util.MiniConn;

public class ShowPoint {

	// 현재 나의 ECO포인트 보여주기
	public static void showPoint(EcoDto dto) {
		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 쿼리작성
		String sql = "SELECT POINT FROM ECO_MEMBER WHERE ID = ?";
		// DB전달
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			// 결과얻기
			rs = pstmt.executeQuery();
			System.out.println("현재 나의 ECO : " + dto.getPoint());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}// showPoint

}
