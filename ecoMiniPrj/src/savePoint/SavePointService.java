package savePoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cafe.CafeDto;
import util.InputUtil;
import util.MiniConn;

public class SavePointService {
	
	public void point() {//~~~~적립하기~~~~~
		boolean isFinish = false;
		while(!isFinish) {
			System.out.println("적립하시겠습니까?(Y/N)");
			String user = InputUtil.toUpperCase(InputUtil.getString());
			if(user.equals("Y")) {
				System.out.println("1. 일회용 컵 반환하기 2. 텀블러 사용하기");
				int userChoice = InputUtil.getInt();
				if(userChoice==1 || userChoice == 2) {
					cafeEmp(userChoice);
					return;
				}else {
					System.out.println("잘못입력하셨습니다.");
					isFinish =request();
				}
			}else if (user.equals("N")){
				return;
			}else {
				System.out.println("잘못 입력하셨습니다");
				isFinish =request();
			}
		}
	}//point
	

	private void cafeEmp(int userChoice) { // ~~~카페직원 확인~~~
		boolean isFinish = false;
		while (!isFinish) {

			System.out.print("고유번호 입력해주세요 ::: ");
			int cafeNo = InputUtil.getInt(); // 고유넘버 입력

			CafeDto dto = setCafeMember(cafeNo); // 카페객체 생성
			
			if (dto != null) { // 고유번호가 정확하게 들어갔는지 체크 ~~

				boolean num = authentication(); //인증번호 발생해서 맞는지 체크~

				if (num == true) { // 인증번호가 맞다면 실행~

					if (userChoice == 1) {// 1. 일회용 컵 반환하기
						int cupPoint = dbAllTableAddCP(dto); 
						if (cupPoint == 2) { 
							System.out.println(dto.getName() + "에서 " + dto.getTumPoint() + "ECO 적립되었습니다.");
							return; //======================적립성공하면 종료====================================
						} else {
							System.out.println("일회용 컵 반납 ECO 적립 실패");
						}
					} else if (userChoice == 2) {// 2. 텀블러 사용하기
						int tumPoint = dbAllTableAddTP(dto);
						if (tumPoint == 2) {
							System.out.println(dto.getName() + "에서 " + dto.getTumPoint() + "ECO 적립되었습니다.");
							return; //======================적립성공하면 종료====================================
						} else {
							System.out.println("텁블러 사용 ECO 적립 실패");
						}
					}
				} else {
					System.out.println("인증번호가 틀렸습니다."); //인증번호 틀림~
					isFinish = request(); //다시 시도 할건지 되물어봄~
				}
			}else {
				System.out.println("제휴 까페가 아닙니다."); //고유번호 틀림~~ 객체생성 안됬다는 뜻~
				isFinish = request(); //다시 시도 할건지 되물어봄~
			}
		}
	}// cafeEmp
	
	public boolean authentication() { //인증번호 발송 ~
		boolean result = false;
		String rdn = InputUtil.randomNum(6, 1); // 랜덤숫자 증복있게 6자리 출력
		System.out.println("인증번호 ::: " + rdn);
		System.out.print("입력번호 ::: ");
		String user = InputUtil.getString();
		if(rdn.equals(user)) {
			result = true; //인증번호 맞으면 true ~
		}
		return result; //인증번호 틀리면 false~
	}
	
	public boolean request() { //재시도 메시지 날림~~ 반복문 실행시 !ISFINSH 여야 반복문 진행 되니까 FALSE~로 리턴~ 
		boolean isFinish = false;
		System.out.println("재시도 하시겠습니까 ? ( Y / N )");
		String user = InputUtil.toUpperCase(InputUtil.getString());
		if(user.equals("Y")) { //맞으면 FALSE 리턴~
		}else if(user.equals("N")) {
			isFinish = true; //아니면 TRUE 리턴 ~
		}
		return isFinish;
	}
	
	public int dbAllTableAddCP(CafeDto dto) { //일회용 컵 반환 적립 시 회원테이블에 포인트 added포인트 업데이트
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE ECO_MEMBER \r\n"
				+ "SET \r\n"
				+ "POINT = POINT + (SELECT CUPPOINT FROM CAFE WHERE NAME = ?)\r\n"
				+ ", ADDEDPOINT = ADDEDPOINT + (SELECT CUPPOINT FROM CAFE WHERE NAME = ?)\r\n"
				+ "WHERE ID = 'TAEWON'"; //아이디 부분 로그인 부분에서 받아올수 있게하기 
		String sql2 = "INSERT INTO HISTORY(NO, ID, POINT, REPORT) \r\n"
				+ "VALUES (SEQ_HISTORY_NO.NEXTVAL, \r\n"
				+ "'TAEWON'" //아이디 부분 로그인 부분에서 받아올 수 있게하기
				+ ", (SELECT CUPPOINT FROM CAFE WHERE NAME = ? ), '텀블러 사용' )";
		int result = 0;
		int result2 = 0;
		try {
			conn = MiniConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getName());
			
			result = pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, dto.getName());
			
			result2 = pstmt.executeUpdate();
			
			if(result > 0 && result2 > 0) {
				MiniConn.commit(conn);
				return result+result2;
			} else {
				MiniConn.rollback(conn);
			}
		} catch (SQLException e) {
			System.out.println("[ERROR] 일회용 컵 반환 ECO적립 실패");
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
		return result+result2;
	}
	
	
	public int dbAllTableAddTP(CafeDto dto) { //텀블러포인트 적립 시 회원테이블에 포인트 added포인트 업데이트
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE ECO_MEMBER \r\n"
				+ "SET \r\n"
				+ "POINT = POINT + (SELECT TUMPOINT FROM CAFE WHERE NAME = ?)\r\n"
				+ ", ADDEDPOINT = ADDEDPOINT + (SELECT TUMPOINT FROM CAFE WHERE NAME = ?)\r\n"
				+ "WHERE ID = 'TAEWON'"; //아이디 부분 로그인 부분에서 받아올수 있게하기 
		String sql2 = "INSERT INTO HISTORY(NO, ID, POINT, REPORT) \r\n"
				+ "VALUES (SEQ_HISTORY_NO.NEXTVAL, \r\n"
				+ "'TAEWON'" //아이디 부분 로그인 부분에서 받아올 수 있게하기
				+ ", (SELECT TUMPOINT FROM CAFE WHERE NAME = ? ), '텀블러 사용' )";
		int result = 0;
		int result2 = 0;
		try {
			conn = MiniConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getName());
			
			result = pstmt.executeUpdate();
			
			pstmt.setString(1, dto.getName());
			result2 = pstmt.executeUpdate();
			
			if(result > 0 && result2 > 0) {
				System.out.println(result+result2);
				MiniConn.commit(conn);
				return result+result2;
			} else {
				MiniConn.rollback(conn);
			}
		} catch (SQLException e) {
			System.out.println("[ERROR] 텁블러 사용 ECO적립 실패");
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
		return result+result2;
	}

	 
	
	private CafeDto setCafeMember(int cafeNo) { //카페고유번호 받아서 맞으면 객체 생성 틀리면 실패
		CafeDto dto = null;
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM CAFE WHERE SECRETCODE = ?";
		ResultSet result = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cafeNo);
			result = pstmt.executeQuery();
			if(result.next()) {
				dto = new CafeDto();
				dto.setNo(result.getInt("NO"));
				dto.setName(result.getString("NAME"));
				dto.setCupPoint(result.getInt("CUPPOINT"));
				dto.setTumPoint(result.getInt("TUMPOINT"));
				dto.setSecretCode(cafeNo);
				return dto;
			}
		} catch (SQLException e) {
			System.out.println("제휴 까페가 아닙니다.");
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		return dto;
	}
	
	public void cafeView() { ///~~~까페목록~~~

		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM CAFE";
		ResultSet result = null;
		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeQuery();
			System.out.println("순번\t카페이름\t\t컵반환금\t텀블러사용");
			while(result.next()) {
				System.out.println();
				System.out.println(result.getInt(1)+"\t"+result.getString(2)+"\t"+result.getInt(3)+"\t"+result.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, result, conn);
		}

	}// cafeView

	
	public void mySelfJoin() { //임의로 만든 회원가입 테스트~
		boolean isFinish = false;
		while(!isFinish) {
			System.out.println("아이디 ::: "); //회원이 회원가입할때 아이디 입력 받는곳임~
			String id = InputUtil.getString();
			uniqueCheck(id);
			if(id.equals(uniqueCheck(id))){
				System.out.println("증복 된 아이디가 있습니다.");
				isFinish = request();
			}
			System.out.println("폰번호 ::: "); //회원이 회원가입할때 아이디 입력 받는곳임~
			String PHONE = InputUtil.getString();
			uniqueCheck(PHONE); //<<쿼리문 수정해야함~>
			if(id.equals(uniqueCheck(PHONE))){
				System.out.println("증복 된 핸드폰 번호가 있습니다.");
				isFinish = request();
			}
		}
	}
	public String uniqueCheck(String id) { //쿼리문으로 증복검사할거임~~ 입력한 아이디가있다면 DB 아이디 불러옴~
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM ECO_MEMBER WHERE ID = ?";
		ResultSet result = null;
		String checkId = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();
			while(result.next()) {
				checkId = result.getString("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		return checkId;
	}
	

}// class
