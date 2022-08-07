package sendMoney;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import main.Main;
import member.EcoDto;
import dto.HistoryDto;
import util.InputUtil;
import util.MiniConn;
import sendMoney.ShowPoint;

public class MemberSendMoney {

	Scanner sc = new Scanner(System.in);

	// 이체하기
	public boolean memberSendMoney() {

		// 현재 나의 ECO포인트 보여주기
		ShowPoint.showPoint(Main.LoginUser);

		// ECO가 부족하면 이체 불가, 메인으로 돌아감
		if (Main.LoginUser.getPoint() < 10000) {
			System.out.println("이체는 10,000ECO 이상부터 가능합니다.");
			return false;
		} else {
			// 회원 은행명 + 계좌번호 보여주기
			bankInfo(Main.LoginUser);

			int eco = 0;
			String result = "N";
			boolean isFinish = true;

			// Y면 while문 통과
			while (isFinish) {
				// 이체할 ECO 입력, 리턴 (int eco = 0에 대입됨)
				eco = selectEco(Main.LoginUser);
				// 선택에 따른 결과 확인
				result = checkSendMoney(Main.LoginUser, eco);

				switch (result) {
				case "Y":
					isFinish = false;
					break;
				case "N":
					System.out.println("다시 입력하세요.");
					break;
				default:
					System.out.println("잘못 입력하셨습니다. 다시 입력하세요.");
					break;
				}
			}
			
			// 이체한 ECO만큼 회원ECO 차감하고, 차감 후 ECO 보여주기
			updatePoint(Main.LoginUser, eco);
			
			// HISTORY테이블에 ECO 사용내역 추가
			addHistory(Main.LoginUser, eco);
			return true;
		}

	}//memberSendMoney

	// 회원 닉네임 + ECO + 은행명 + 계좌번호 보여주기
	public void bankInfo(EcoDto dto) {
		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 쿼리작성
		String bankInfo = "SELECT NICK, POINT, BANKNO, ACCOUNT FROM ECO_MEMBER WHERE ID = ?";
		// DB전달
		try {
			pstmt = conn.prepareStatement(bankInfo);
			pstmt.setString(1, dto.getId());
			// 결과얻기
			rs = pstmt.executeQuery();
			System.out.println("현재 나의 ECO : " + dto.getPoint() + "\n");
			System.out.println("[" + dto.getNick() + "님의 계좌이체 정보]");
			System.out.println(dto.getBankName() + "은행 " + dto.getAccount());
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}

	//이체할 ECO금액 입력, 리턴
	public int selectEco(EcoDto dto) {
		int eco = 0;
		while (true) {
			System.out.println("이체하실 ECO를 입력하세요.");
			eco = sc.nextInt();
			if (eco < 10000) {
				System.out.println("이체는 10,000ECO 이상부터 가능합니다.");
			} else if (eco > dto.getPoint()) {
				System.out.println("ECO가 부족합니다.");
			} else {
				break;
			}
		}
		return eco;
	}//selectEco

	// 선택에 따른 결과 확인, Y/N 리턴
	public String checkSendMoney(EcoDto dto, int eco) {

		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 로그인 된 회원의 ID값이 필요해서 임시로 입력,
		// 로그인 연동하면 아래 코드 삭제하기
		//dto.setId("TAEWON");
		
		// 쿼리작성
		String sql = "SELECT BANKNO, ACCOUNT, NAME FROM ECO_MEMBER WHERE ID = ?";
		// DB전달
		try {
			String a = dto.getId();
			System.out.println(a);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("[" +dto.getBankName() + "은행 /" + rs.getString("ACCOUNT") +" / " +rs.getString("NAME") + "]으로 " + eco + "ECO를 이체합니다. (Y / N)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		String result = InputUtil.sc.next();
		result = InputUtil.toUpperCase(result);

		return result;
		
	}//checkSendMoney

	// 이체한 ECO만큼 회원ECO 차감하고, 차감 후 ECO 보여주기
	public void updatePoint(EcoDto dto, int eco) {

		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 로그인 된 회원의 ID값이 필요해서 임시로 입력,
		// 로그인 연동하면 아래 코드 삭제하기
		//dto.setId("TAEWON");

		String sql1 = "UPDATE ECO_MEMBER SET POINT = ? WHERE ID = ?";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, dto.getPoint());
			pstmt.setInt(1, dto.getPoint() - eco);
			pstmt.setString(2, dto.getId());
			int result = pstmt.executeUpdate();
			if (result == 1)
				MiniConn.commit(conn);
			else
				MiniConn.rollback(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql2 = "SELECT POINT FROM ECO_MEMBER WHERE ID = ?";
		try {
			// update 후 select
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, dto.getId());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				System.out.println("이체가 완료되었습니다. 회원페이지로 돌아갑니다.");
				// 메인에서 memberDonation 메소드 전체를 for문 안에 넣어야함
				System.out.println("현재 나의 ECO : " + rs.getString("POINT"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}//updatePoint
		
	// HISTORY테이블에 ECO 사용내역 추가
	public void addHistory(EcoDto eDto, int eco) {
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HistoryDto hDto = new HistoryDto();

		hDto.setId(eDto.getId()); // 현재 로그인 아이디
		hDto.setPoint(-eco); // 계좌이체 한 금액
		hDto.setReportNo(3); // 레포트타입 3(계좌이체) 라는 내역으로 히스토리에 저장
		hDto.setPlaceNo(34); // 플레이스넘버3(계좌이체) 라는 내역으로 히스토리에 저장
		String sql = "INSERT INTO HISTORY VALUES(SEQ_HISTORY_NO.NEXTVAL, ?, SYSDATE, ?, ?, ?)";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hDto.getId());
			pstmt.setInt(2, hDto.getPoint());
			pstmt.setInt(3, hDto.getReportNo());
			pstmt.setInt(4, hDto.getPlaceNo());
			int result = pstmt.executeUpdate();
			if (result == 1)
				MiniConn.commit(conn);
			else
				MiniConn.rollback(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
	}
	
}// class
