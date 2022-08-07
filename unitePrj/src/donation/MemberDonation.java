package donation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import main.Main;
import dto.DonationDto;
import member.EcoDto;
import sendMoney.ShowPoint;
import dto.HistoryDto;
import util.InputUtil;
import util.MiniConn;

public class MemberDonation {

	Scanner sc = new Scanner(System.in);

	// 기부하기
	public boolean memberDonation() {

		// 현재 나의 ECO포인트 보여주기
		ShowPoint.showPoint(Main.LoginUser);

		// ECO가 부족하면 기부 불가, 메인으로 돌아감
		if (Main.LoginUser.getPoint() < 1000) {
			System.out.println("기부는 1,000ECO 이상부터 가능합니다.");
			return false;
		} else {
			// 환경단체 리스트 보여주기
			showList();

			int eco = 0;
			int num = 0;
			String result = "N";
			boolean isFinish = true;
			HistoryDto hDto = new HistoryDto();
			// Y면 while문 통과
			while (isFinish) {
				// 기부할 환경단체 선택, 리턴 (int num = 0에 대입됨)
				num = selectNum();
				
				//DONATION NO를 PLACE_NO로 변경하는 작업
				setHdtoPlaceNo(num,hDto);
				
				// 기부할 ECO 입력, 리턴 (int eco = 0에 대입됨)
				eco = selectEco(Main.LoginUser);
				// 선택에 따른 결과 확인
				result = checkDonation(Main.LoginUser, eco, num);

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

			// 기부한 ECO만큼 회원ECO 차감하고, 차감 후 ECO 보여주기
			updatePoint(Main.LoginUser, eco);

			// DONATION테이블에 누적 기부내역 추가
			addDonationPoint(eco, num);

			// HISTORY테이블에 ECO 사용내역 추가
			addHistory(Main.LoginUser, eco, hDto);
			return true;
		}

	}// memberDonation

	private void setHdtoPlaceNo(int num, HistoryDto hDto) {
		switch(num){
			case 1 : 
				hDto.setPlaceNo(22);
				break;
			case 2 : 
				hDto.setPlaceNo(23);
				break;
			case 3 : 
				hDto.setPlaceNo(24);
				break;
			case 4 : 
				hDto.setPlaceNo(25);
				break;
			case 5 : 
				hDto.setPlaceNo(26);
				break;
			case 6 : 
				hDto.setPlaceNo(27);
				break;
		}
	}

	// 환경단체 리스트 보여주기
	public void showList() {
		System.out.println("\n환경단체 목록");
		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 쿼리작성
		String donationList = "SELECT * FROM DONATION";
		// DB전달
		try {
			pstmt = conn.prepareStatement(donationList);
			// 결과얻기
			rs = pstmt.executeQuery();
			// DONATION 테이블의 모든값 출력
			while (rs.next()) {
				System.out.println("\n번호: " + rs.getInt(1) + "\n단체명: " + rs.getString(2) + "\n단체 소개: " + rs.getString(3)
						+ "\n현재까지 기부받은 Eco: " + rs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}// DonationList

	// 기부할 환경단체 번호 선택, 리턴
	public int selectNum() {
		int num = 0;
		while (true) {
			System.out.println("\n기부하실 단체의 번호를 입력해 주세요.");
			num = sc.nextInt();
			if (num >= 1 && num <= 6) {			
				break;
			} else {
				System.out.println("잘못 입력하셨습니다.");
			}
		}
		return num;
	}

	// 기부할 ECO금액 입력, 리턴
	public int selectEco(EcoDto dto) {
		int eco = 0;
		while (true) {
			System.out.println("기부하실 ECO를 입력하세요.");
			eco = sc.nextInt();
			if (eco < 1000) {
				System.out.println("기부는 1,000ECO 이상부터 가능합니다.");
			} else if (eco > dto.getPoint()) {
				System.out.println("ECO가 부족합니다.");
			} else {
				break;
			}
		}
		return eco;
	}

	// 선택에 따른 결과 확인 Y/N
	public String checkDonation(EcoDto dto, int eco, int num) {

		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 쿼리작성
		String sql = "SELECT NAME FROM DONATION WHERE NO = ?";
		// DB전달
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString("NAME") + "재단으로 " + eco + "ECO를 기부합니다. (Y / N)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		//String result = InputUtil.sc.next();
		return InputUtil.toUpperCase(InputUtil.getString());

	}

	// 기부한 ECO만큼 회원ECO 차감하고, 차감 후 ECO 보여주기
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

			if (rs.next()) {
				System.out.println("기부가 완료되었습니다. 회원페이지로 돌아갑니다.");
				dto.setPoint(dto.getPoint()-eco);
				// 메인에서 memberDonation 메소드 전체를 for문 안에 넣어야함
				System.out.println("현재 나의 ECO : " + rs.getString("POINT"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}

	// DONATION테이블에 누적 기부내역 추가
	public void addDonationPoint(int eco, int num) {
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DonationDto dto = new DonationDto();

		// 임시로 회원정보 dto에 값을 지정하고 dto값으로 db를 update한다.
		dto.setAddedPoint(dto.getAddedPoint() + eco); // 누적 기부금액 = 기존+기부한eco
		dto.setNo(num); // 선택했던 기부단체 번호

		String sql = "UPDATE DONATION SET ADDEDPOINT = ? WHERE NO = ?";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getAddedPoint());
			pstmt.setInt(2, dto.getNo());
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

	// HISTORY테이블에 ECO 사용내역 추가
	public void addHistory(EcoDto eDto, int eco, HistoryDto hDto) {
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		hDto.setId(eDto.getId()); // 현재 로그인 아이디
		hDto.setPoint(-eco); // 기부한 금액 저장
		hDto.setReportNo(4); // 레포트'기부' 라는 내역으로 내역에 저장
		//hDto.setPlaceNo();
		String sql = "INSERT INTO HISTORY VALUES(SEQ_HISTORY_NO.NEXTVAL, ?, SYSDATE, ?, ?, ?)";
		//String sql = "INSERT INTO HISTORY VALUES(SEQ_HISTORY_NO.NEXTVAL, ?, SYSDATE, ?, ?)";
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
