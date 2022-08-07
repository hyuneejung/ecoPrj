package main;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import member.EcoDto;
import myPackage.EcoMoney;
import myPackage.Join;
import myPackage.Login;
import myPackage.MenuPanel;
import util.MiniConn;

public class Main {

	public static EcoDto LoginUser = null;
	public static final int SCREEN_WIDTH = 616;
	public static final int SCREEN_HEIGHT = 839;
	public static Font font = new Font("AppleSDGothicNeoL00", Font.PLAIN, 15);
	public static Font fontB = new Font("AppleSDGothicNeoR00", Font.PLAIN, 15);

	public static void main(String[] args) {
		setUIFont(new FontUIResource(font));

		new EcoMoney();
		
		UIManager.put("OptionPane.messageFont", fontB);
		UIManager.put("OptionPane.buttonFont", fontB);
	}

	public static void setUIFont(FontUIResource f) {
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				FontUIResource orig = (FontUIResource) value;
				Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}
	
	public static void updateUser() {
		
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		int beforeRankNo = LoginUser.getRank();
		
		String updateSql = "UPDATE ECO_MEMBER SET RANKNO = (SELECT MAX(NO) FROM RANKSYS WHERE (SELECT ADDEDPOINT FROM ECO_MEMBER WHERE ID = ?) >= MINPOINT) WHERE ID = ?";

		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setString(1, LoginUser.getId());
			pstmt.setString(2, LoginUser.getId());
			int result = pstmt.executeUpdate();
			if (result == 1)
				MiniConn.commit(conn);
			else {
				MiniConn.rollback(conn);
			}
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String selectSql = "SELECT * FROM ECO_MEMBER WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setString(1, LoginUser.getId());
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				LoginUser.setId(resultSet.getString("ID"));
				LoginUser.setPwd(resultSet.getString("PWD"));
				LoginUser.setName(resultSet.getString("NAME"));
				LoginUser.setNick(resultSet.getString("NICK"));
				LoginUser.setGender(resultSet.getString("GENDER"));
				LoginUser.setPhone(resultSet.getString("PHONE"));
				LoginUser.setBankNo(resultSet.getInt("BANKNO"));
				LoginUser.setAccount(resultSet.getString("ACCOUNT"));
				LoginUser.setPoint(resultSet.getInt("POINT"));
				LoginUser.setAddedPoint(resultSet.getInt("ADDEDPOINT"));
				LoginUser.setEnroll_date(resultSet.getTimestamp("ENROLL_DATE"));
				LoginUser.setRank(resultSet.getInt("RANKNO"));
				LoginUser.setQuit_yn(resultSet.getString("QUIT_YN").charAt(0));
			}
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int afterRankNo = LoginUser.getRank();
		String afterRankName = null;
		
		selectSql = "SELECT RANK FROM RANKSYS WHERE NO = ?";
		try {
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setInt(1, LoginUser.getRank());
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				afterRankName = resultSet.getString("RANK");
			}
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(beforeRankNo < afterRankNo) {
			JOptionPane.showMessageDialog(null, LoginUser.getNick()+"님, "+ afterRankName+ "(으)로 승급하신 것을 축하드립니다!!");
		}
	}
	
	

}