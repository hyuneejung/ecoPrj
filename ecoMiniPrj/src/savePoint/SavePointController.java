package savePoint;

import cafe.CafeDto;
import main.Main;
import member.EcoDto;
import util.InputUtil;

public class SavePointController {
	
	public void point() {// ~~~~적립하기~~~~~
		if(Main.LoginUser == null) {
			return;
		}
		
		boolean isFinish = false;
		while (!isFinish) {
			System.out.println("적립하시겠습니까?(Y/N)");
			String user = InputUtil.toUpperCase(InputUtil.getString());
			if(user.equals("Y")) {
				System.out.println("1. 일회용 컵 반환하기 2. 텀블러 사용하기");
				int userChoice = InputUtil.getInt();
				if(userChoice == 1 || userChoice == 2) {
					cafeEmp(userChoice,Main.LoginUser);
					return;
				}else {
					System.out.println("잘못 입력 하셨습니다.");
					isFinish = request();
				}
			}else {
				System.out.println("잘못 입력 하셨습니다.");
				isFinish = request();
			}
		}
	}// point

	private void cafeEmp(int userChoice, EcoDto ed) { // ~~~카페직원 확인~~~
		boolean isFinish = false;
		while (!isFinish) {

			System.out.print("고유번호 입력해주세요 ::: ");
			int cafeNo = InputUtil.getInt(); // 고유넘버 입력

			CafeDto dto = new SavePointDao().setCafeMember(cafeNo); // 카페객체 생성

			if (dto != null) { // 고유번호가 정확하게 들어갔는지 체크 ~~

				boolean num = authentication(); // 인증번호 발생해서 맞는지 체크~

				if (num == true) { // 인증번호가 맞다면 실행~

					if (userChoice == 1) {// 1. 일회용 컵 반환하기
						int cupPoint = new SavePointDao().dbAllTableAddCP(dto,ed);
						if (cupPoint == 2) {
							System.out.println(dto.getName() + "에서 " + dto.getCupPoint() + "ECO 적립되었습니다.");
							return; // ======================적립성공하면 종료====================================
						} else {
							System.out.println("일회용 컵 반납 ECO 적립 실패");
						}
					} else if (userChoice == 2) {// 2. 텀블러 사용하기
						int tumPoint = new SavePointDao().dbAllTableAddTP(dto,ed);
						if (tumPoint == 2) {
							System.out.println(dto.getName() + "에서 " + dto.getTumPoint() + "ECO 적립되었습니다.");
							return; // ======================적립성공하면 종료====================================
						} else {
							System.out.println("텁블러 사용 ECO 적립 실패");
						}
					}
				} else {
					System.out.println("인증번호가 틀렸습니다."); // 인증번호 틀림~
					isFinish = request(); // 다시 시도 할건지 되물어봄~
				}
			} else {
				System.out.println("제휴 까페가 아닙니다."); // 고유번호 틀림~~ 객체생성 안됬다는 뜻~
				isFinish = request(); // 다시 시도 할건지 되물어봄~
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
	

}// class
