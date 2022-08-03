package rankUpdate;

import main.Main;
import member.EcoDto;

public class RankUpdateController {
	
	public void rankUpdate() {
		if(Main.LoginUser == null) {
			return;
		}
		EcoDto ed = Main.LoginUser;
		/*
		 * 업데이트전 랭크받아오기
		 * 
		 * 업데이트후 등급업 되면 등급업 축하메시지 로직
		 * 
		 * 
		 * */
		
		new RankUpdateService().rankUpdate(ed);
		
		new RankUpdateService().changeRank(ed);
		
	}
}
