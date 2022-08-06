package gift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.Main;
import member.EcoDto;
import dto.HistoryDto;
import dto.giftDto;
import util.InputUtil;
import util.MiniConn;

public class MemberGift {

	// 교환하기
	public void memberGift() {

		// 로그인 객체 정보 받아오기
		EcoDto dto = Main.LoginUser;

		// 교환하기 목록 선택 후 정보를 담을 객체
		giftDto gDto = new giftDto();

		// 현재 나의 ECO포인트 보여주기
		System.out.println("\n현재 나의 ECO : " + dto.getPoint() + "\n");

		// 교환목록 리스트 보여주기
		showList();

		// 선택한 상품 정보 불러오기
		int num = selectNum();
		gDto = selectGift(gDto, num);

		// 보유ECO가 부족하면 메인으로 돌려보내기
		boolean flag = checkEco(dto, gDto);

		// 선택한 상품으로 교환하기
		if (!flag) {
			System.out.println("처음으로 돌아갑니다.");
		} else {
			//최종 확인 (Y/N)
			String result = choiceGift();

			if (result.equals("Y")) {

				updatePoint(dto, gDto);

				addHistory(dto, gDto, num);
				System.out.println(dto.getNick() + "님의 휴대폰번호로 상품권을 발송하였습니다.");
				System.out.println("휴대폰번호 : " + dto.getPhone());

				System.out.println("현재 나의 ECO : " + dto.getPoint() + "\n");

			} else if (result.equals("N")) {
				System.out.println("처음으로 돌아갑니다.");
			} else {
				System.out.println("잘못 입력하셨습니다. 처음으로 돌아갑니다.");
			}

		}
	}// memberGift

	// 교환목록 리스트 보여주기
	public void showList() {
		System.out.println("\n교환하기 목록");
		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 쿼리작성
		String donationList = "SELECT * FROM GIFT";
		// DB전달
		try {
			pstmt = conn.prepareStatement(donationList);
			// 결과얻기
			rs = pstmt.executeQuery();
			// GIFT 테이블의 모든값 출력
			while (rs.next()) {
				System.out.println("\n번호: " + rs.getInt("NO") + "\n상품명: " + rs.getString("NAME") + "\n필요한 ECO: "
						+ rs.getString("ECOPRICE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}// showList

	// 상품 번호 선택
	public int selectNum() {
		int num = 0;
		while (true) {
			System.out.println("\n교환하실 상품의 번호를 입력해 주세요.");
			num = InputUtil.getInt();
			if (num >= 1 && num <= 6) {
				break;
			} else {
				System.out.println("잘못 입력하셨습니다.");
			}
		}
		return num;
	}// selectNum

	// 선택한 상품 정보를 객체에 담기
	public giftDto selectGift(giftDto gDto, int num) {
		// 연결얻기
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 쿼리작성
		String sql = "SELECT * FROM GIFT WHERE NO = ?";
		// DB전달
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				gDto.setNo(rs.getInt("NO"));
				gDto.setName(rs.getString("NAME"));
				gDto.setEcoPrice(rs.getInt("ECOPRICE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		return gDto;
	}// selectGift

	// 상품 금액과 회원 ECO 비교
	public boolean checkEco(EcoDto dto, giftDto gDto) {

		if (dto.getPoint() < gDto.getEcoPrice()) {
			System.out.println("보유하신 ECO가 부족합니다.");
		} else {
			return true;
		}
		return false;
	}// checkEco

	// 최종 확인
	public String choiceGift() {
		System.out.println("교환하시겠습니까? (Y/N)");
		return InputUtil.toUpperCase(InputUtil.getString());
	}// choiceGift

	// ECOPRICE만큼 회원ECO 차감 + 회원객체POINT에 SET
	public void updatePoint(EcoDto dto, giftDto gDto) {

		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;

		String sql1 = "UPDATE ECO_MEMBER SET POINT = ? WHERE ID = ?";
		try {
			// UPDATE 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, dto.getPoint() - gDto.getEcoPrice());
			pstmt.setString(2, dto.getId());
			result = pstmt.executeUpdate();

			if (result == 1)
				MiniConn.commit(conn);
			else
				MiniConn.rollback(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql2 = "SELECT POINT FROM ECO_MEMBER WHERE ID = ?";
		try {
			// UPDATE 후 SELECT해서 회원객체POINT에 SET
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, dto.getId());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto.setPoint(rs.getInt("POINT"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

	}// updatePoint

	// 회원이 선택한 1~6 번호를 PLACE테이블의 NO로 변환
	public void setHdtoPlaceNo(int num, HistoryDto hDto) {
		switch (num) {
		case 1:
			hDto.setPlaceNo(28);
			break;
		case 2:
			hDto.setPlaceNo(29);
			break;
		case 3:
			hDto.setPlaceNo(30);
			break;
		case 4:
			hDto.setPlaceNo(31);
			break;
		case 5:
			hDto.setPlaceNo(32);
			break;
		case 6:
			hDto.setPlaceNo(33);
			break;
		}
	}

	// HISTORY테이블에 ECO 사용내역 추가
	public void addHistory(EcoDto eDto, giftDto gDto, int num) {
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HistoryDto hDto = new HistoryDto();
		setHdtoPlaceNo(num, hDto);

		hDto.setId(eDto.getId()); // 현재 로그인 아이디
		hDto.setPoint(-gDto.getEcoPrice()); // 교환하기로 사용한 ECO
		hDto.setReportNo(5); // 레포트타입 5(교환하기) 라는 내역으로 히스토리에 저장
		hDto.setPlaceNo(hDto.getPlaceNo()); // 플레이스넘버 28~33 내역으로 히스토리에 저장
		String sql = "INSERT INTO HISTORY VALUES(SEQ_HISTORY_NO.NEXTVAL, ?, SYSDATE, ?, ?, ?)";
		try {
			// UPDATE 실시 후 커밋, 실패면 롤백
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
