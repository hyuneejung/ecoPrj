package main;

public class ShowMenu {

	public int menu() {

		if (Main.LoginUser == null) {
			// 로그인 전 메뉴들
			
			//프로그램 소개
			System.out.println("1. 로그인");
			System.out.println("2. 회원가입");
			System.out.println("3. 아이디 찾기(임시)");
			System.out.println("4. 비번찾고 변경(임시)");
			//랭킹 확인
			//등급제도 안내
			//제휴카페 확인
			
		} else {
			// 로그인 후 메뉴들
			System.out.println("5. 마이페이지");
			System.out.println("6. 적립하기");
			System.out.println("7. 이체하기");
			System.out.println("8. 기부하기");
			// System.out.println("9. 교환하기");
		}

		return InputUtil.getInt();

	}

}
