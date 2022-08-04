package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import main.Main;
import member.EcoDto;
import util.InputUtil;
import util.MiniConn;

public class MyPage {
	
	EcoControll ec = new EcoControll();
	
	public void myPage() {
		
		// 로그인 여부 체크
		if(Main.LoginUser == null) {
			System.out.println("로그인이 되지 않았습니다. 로그인을 먼저 해 주세요 !");
			return;
		}
		
		System.out.println("=======" + Main.LoginUser.getNick() + " 님의 회원 정보 ========");
		System.out.println("현재 등급 : " + getRank());
		System.out.println("현재 ECO : " + getNowSeed());
		System.out.println("누적 ECO : " + getTotalSeed());
		System.out.println("=============================");
		System.out.println("1. 회원 정보 변경하기 (닉네임 / 비밀번호)");
		System.out.println("2. ECO 적립 및 사용 내역 확인하기");
		System.out.println("3. ECO 기부 내역 확인하기");
		System.out.println("4. 회원 탈퇴하기");
		System.out.println("5. 메인으로 돌아가기");
		
		int mp1 = InputUtil.getInt();
		
		switch(mp1) {
		case 1 :
			// 회원 정보 변경하기
			memberUpdate();
			break;
		case 2 :
			// Seed 적립 및 사용 내역 확인
			viewEcoUse();
			break;
		case 3 :
			// Seed 기부 내역 확인
			viewEcoDonate();
			break;
		case 4 :
			// 회원 탈퇴 (QUIT_YN = Y)
			quitMember();
			break;
		case 5 : return;
		default :
			System.out.println("잘못 입력하셨습니다. 처음으로 돌아갑니다.");
			return;
		}

	}
	
	private String getRank() {
		
		String id = Main.LoginUser.getId();
		String rank = "";
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 연결된 정보를 이용해서 SQL 실행
		String sql = "SELECT R.RANK FROM ECO_MEMBER E JOIN RANKSYS R ON E.RANKNO = R.NO WHERE ID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			// 지금 실행하는 쿼리는 SELECT 쿼리 -> 결과 집합 (RESULT SET)
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				rank = rs.getString("RANK");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		
		return rank;
		
	}

	private String getTotalSeed() {

		String id = Main.LoginUser.getId();
		String addedPoint = "";
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 연결된 정보를 이용해서 SQL 실행
		String sql = "SELECT ADDEDPOINT FROM ECO_MEMBER WHERE ID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			// 지금 실행하는 쿼리는 SELECT 쿼리 -> 결과 집합 (RESULT SET)
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				addedPoint = rs.getString("ADDEDPOINT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		
		return addedPoint;

	}

	private String getNowSeed() {
		
		String id = Main.LoginUser.getId();
		String point = "";
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 연결된 정보를 이용해서 SQL 실행
		String sql = "SELECT POINT FROM ECO_MEMBER WHERE ID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			// 지금 실행하는 쿼리는 SELECT 쿼리 -> 결과 집합 (RESULT SET)
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				point = rs.getString("POINT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		
		return point;

	}

	private void quitMember() {
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String id = Main.LoginUser.getId();

		System.out.println("정말 탈퇴하시겠습니까?");
		System.out.println("탈퇴하시려면 현재 비밀번호를 한 번 더 입력해 주세요.");

		if(pwdRight() == false) {
			System.out.println("처음으로 돌아갑니다.");
			return;
		} else {

		// SQL 작성
		String sql = "UPDATE ECO_MEMBER SET QUIT_YN = 'Y' WHERE ID = ?";
		
		// SQL 객체에 담기 (+SQL 완성)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			// SQL 실행 (실행 결과 저장)
			int result = pstmt.executeUpdate();
			
			// 실행 결과에 따른 로직 처리
			if(result == 1) {
				System.out.println("탈퇴에 성공했습니다 !");
				conn.commit();
			} else {
				System.out.println("탈퇴에 실패했습니다 ..");
				conn.rollback();
			}
		} catch(SQLException e) {
			MiniConn.rollback(conn);
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
		
		}
		
	}
	
	// 비밀번호 맞는지 확인
	private boolean pwdRight() {
		
		// 현재 비밀번호 확인
		System.out.println("현재 비밀번호 : ");
		String pwd = InputUtil.sc.nextLine();
		
		// 비번 맞는지 체크
		EcoDto dto = new EcoDto();
		dto.setId(Main.LoginUser.getId());
		dto.setPwd(pwd);
		
		EcoDto pwdCheckResult = ec.login(dto);
		if(pwdCheckResult == null) {
			// 비번 틀림
			System.out.println("비밀번호가 일치하지 않습니다!");
			return false;
		} else {
			return true;
		}
		
	}

	private void viewEcoUse() { // 적립 및 사용 내역 조회
		
		String id = Main.LoginUser.getId();
		String Pwd = Main.LoginUser.getPwd();
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 연결된 정보를 이용해서 SQL 실행
		String sql = "SELECT H.TIME_REPORT, H.POINT, R.TYPE, P.PLACE_NAME FROM HISTORY H JOIN PLACE P ON P.PLACE_NO = H.PLACE_NO JOIN REPORT R ON R.REPORT_NO = H.REPORT_NO WHERE ID = ? AND H.REPORT_NO != 5";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			// 지금 실행하는 쿼리는 SELECT 쿼리 -> 결과 집합 (RESULT SET)
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getTimestamp(1)+"\t"+rs.getInt(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}

	private void viewEcoDonate() { // 기부 내역 조회
		
		String id = Main.LoginUser.getId();
		String Pwd = Main.LoginUser.getPwd();
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 연결된 정보를 이용해서 SQL 실행
		String sql = "SELECT H.TIME_REPORT, H.POINT, R.TYPE, P.PLACE_NAME FROM HISTORY H JOIN PLACE P ON P.PLACE_NO = H.PLACE_NO JOIN REPORT R ON R.REPORT_NO = H.REPORT_NO WHERE ID = ? AND H.REPORT_NO = 5";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			// 지금 실행하는 쿼리는 SELECT 쿼리 -> 결과 집합 (RESULT SET)
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getTimestamp(1)+"\t"+rs.getInt(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		
	}

	private void memberUpdate() {
		
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		
		System.out.println('\n' + "변경하실 항목을 선택하세요.");
		System.out.println("1. 닉네임 변경");
		System.out.println("2. 비밀번호 변경");
		System.out.println("3. 처음으로 돌아가기");
		int n = InputUtil.getInt();
		
		switch(n) {
		case 1 :
			updateNick();
			break;
		case 2 :
			updatePwd();
			break;
		case 3 : return;
		default :
			System.out.println("잘못 입력하셨습니다. 처음으로 돌아갑니다.");
			return;
		}
		
	}
	
	private void updateNick() {
		
		System.out.println("변경할 닉네임을 입력하세요 : ");
		String nick = InputUtil.getString();
		String id = Main.LoginUser.getId();
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		
		// SQL 작성
		String sql = "UPDATE ECO_MEMBER SET NICK = ? WHERE ID = ?";
		
		// SQL 객체에 담기 (+SQL 완성)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setString(2, id);
			
			// SQL 실행 (실행 결과 저장)
			int result = pstmt.executeUpdate();
			
			// 실행 결과에 따른 로직 처리
			if(result == 1) {
				System.out.println("닉네임 변경 성공 !");
				conn.commit();
			} else {
				System.out.println("닉네임 변경 실패 ..");
				conn.rollback();
			}
		} catch(SQLException e) {
			MiniConn.rollback(conn);
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
		
	}
	
	private void updatePwd() {
		// 비밀번호 변경
		
		// 현재 비밀번호 확인
		System.out.println("현재 비밀번호 : ");
		String pwd = InputUtil.getString();

		// 변경할 비밀번호 입력
		System.out.println("변경할 비밀번호 : ");
		String newPwd = InputUtil.getString();
		
		// 변경할 비밀번호 재입력
		System.out.println("변경할 비밀번호 재입력 : ");
		String newPwd2 = InputUtil.getString();
		
		// 비번 맞는지 체크
		EcoDto dto = new EcoDto();
		dto.setId(Main.LoginUser.getId());
		dto.setPwd(pwd);
		
		EcoDto pwdCheckResult = ec.login(dto);
		if(pwdCheckResult == null) {
			// 비번 틀림
			System.out.println("기존 비밀번호와 일치하지 않습니다 !");
			return;
		}
		
		// 신규 비번 2개가 일치하는지
		if(!newPwd.equals(newPwd2)) {
			System.out.println("신규 비밀번호가 일치하지 않습니다 !");
			return;
		}
		
		// 현재 비번과 신규 비번이 같은지
		if(pwd.equals(newPwd)) {
			System.out.println("기존 비밀번호와 신규 비밀번호가 같습니다 !");
			return;
		}
		
	    //입력받은 정보로 update진행
	    updatePwd(Main.LoginUser.getId(), newPwd); //현재 로그인 아이디 + 바꾼 비밀번호
	    
	}

	private void updatePwd(String id, String newPwd) {
		
		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		
		updatePwd();
		
		// SQL 작성
		String sql = "UPDATE ECO_MEMBER SET PWD = ? WHERE ID = ?";
		
		// SQL 객체에 담기 (+SQL 완성)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newPwd);
			pstmt.setString(2, id);
			
			// SQL 실행 (실행 결과 저장)
			int result = pstmt.executeUpdate();
			
			// 실행 결과에 따른 로직 처리
			if(result == 1) {
				System.out.println("비밀번호 변경 성공 !");
				conn.commit();
			} else {
				System.out.println("비밀번호 변경 실패 ..");
				conn.rollback();
			}
		} catch(SQLException e) {
			MiniConn.rollback(conn);
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
	}
	
}
