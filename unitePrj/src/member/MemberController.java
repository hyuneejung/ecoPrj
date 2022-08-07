package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


import static util.InputUtil.*;
import util.MiniConn;

public class MemberController {

	public int join() {

		EcoDto dto = showJoinView();

		if (dto == null) {
			return 0;
		}

		Connection conn = MiniConn.getConnection();

		String sql = "INSERT INTO ECO_MEMBER (NO, NAME, NICK, GENDER, ID, PWD, PHONE, BANKNO, ACCOUNT) VALUES (SEQ_ECO_MEMBER_NO.NEXTVAL,?,?,?,?,?,?,?,?)";

		int result = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getNick());
			pstmt.setString(3, dto.getGender());
			pstmt.setString(4, dto.getId());
			pstmt.setString(5, dto.getPwd());
			pstmt.setString(6, dto.getPhone());
			pstmt.setInt(7, dto.getBankNo());
			pstmt.setString(8, dto.getAccount());

			result = pstmt.executeUpdate();

			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			MiniConn.rollback(conn);
			System.out.println("회원가입 중 예외 발생 !");
			e.printStackTrace();

		} finally {
			MiniConn.close(conn);
			MiniConn.close(pstmt);
		}

		return result;

	}

	private EcoDto showJoinView() {

		EcoDto memberdto = new EcoDto();

		Scanner sc = new Scanner(System.in);

		System.out.println("\n가입하실 정보를 입력해 주세요.");
		System.out.print("\n이름 : ");
		String name = sc.nextLine();
		System.out.print("닉네임 : ");
		String nick = sc.nextLine();
		System.out.print("성별(F/M) : ");
		String gender = toUpperCase(getString());

		// 아이디 중복 확인
		boolean isFinish = false;
		String id = null;

		while (!isFinish) {
			System.out.print("아이디 : ");
			id = sc.nextLine();
			uniqueCheck(id);
			if (id.equals(uniqueCheck(id))) {
				System.out.println("증복 된 아이디가 있습니다.");
				System.out.print("재시도 하시겠습니까 ? ( Y / N ) : ");
				String user = toUpperCase(getString());
				isFinish = request(user);
				if (user.equals("N")) {
					return null;
				}

			} else {
				isFinish = true;
			}

		}

		System.out.print("비밀번호 : ");
		String pwd = sc.nextLine();

		// 핸드폰 번호 중복 확인
		isFinish = false;
		String phone = null;
		while (!isFinish) {
			System.out.print("핸드폰번호 : ");
			phone = getString();
			uniqueCheck(phone);
			if (phone.equals(uniqueCheckPhone(phone))) {
				System.out.println("중복된 핸드폰 번호가 있습니다.");
				System.out.println("재시도 하시겠습니까 ? ( Y / N ) : ");
				String user = toUpperCase(getString());
				isFinish = request(user);
				if (user.equals("N")) {
					return null;
				}
			}else {
				isFinish = true;
			}
		}

		System.out.println("----- 은행 목록 -----");
		System.out.println("1.국민\n2.농협\n3.신한\n4.신협\n5.카카오");
		System.out.print("\n은행 : ");
		int bank = getInt();
		System.out.print("계좌번호 : ");
		String account = getString();

		memberdto.setName(name);
		memberdto.setNick(nick);
		memberdto.setGender(gender);
		memberdto.setId(id);
		memberdto.setPwd(pwd);
		memberdto.setPhone(phone);
		memberdto.setBankNo(bank);
		memberdto.setAccount(account);

		return memberdto;

	}

	public boolean request(String user) { 
		boolean isFinish = false;
		if (user.equals("Y")) { 
		} else if (user.equals("N")) {
			isFinish = true; 
		}
		return isFinish;
	}

	public String uniqueCheck(String id) {
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM ECO_MEMBER WHERE ID = ?";
		ResultSet result = null;
		String checkId = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();
			while (result.next()) {
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

	public String uniqueCheckPhone(String phone) {
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM ECO_MEMBER WHERE PHONE = ?";
		ResultSet result = null;
		String checkPhone = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			result = pstmt.executeQuery();
			while (result.next()) {
				checkPhone = result.getString("PHONE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		return checkPhone;
	}

	
}
