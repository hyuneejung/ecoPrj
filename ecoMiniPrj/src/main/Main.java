package main;

public class Main {

	public static EcoDto LoginUser = null;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		MemberController mc = new MemberController();
		EcoControll ec = new EcoControll();
		MyPage mp = new MyPage();

		System.out.println("\n----- this.eco = money; -----");

		showMenu sm = new showMenu();
		while (true) {
			int input = sm.menu();
			switch (input) {
			case 1:
				ec.login();
				break;
			case 2:
				int result = mc.join();
				if (result > 0)
					System.out.println("\n회원가입 성공!");
				else
					System.out.println("\n회원가입 실패");
				break;
			case 3:
				ec.findUser();
				break;
			case 4:
				ec.changePwd();
				break;
			case 5:
				mp.myPage();
				break;
			case 6:
				//적립
				new SavePointController().point();
				break;
			case 7:
				//이체
				MemberSendMoney ms = new MemberSendMoney();
				ms.memberSendMoney();
				break;
			case 8:
				//기부
				MemberDonation md = new MemberDonation();
				md.memberDonation();
				break;

			}

		}
	}

}
