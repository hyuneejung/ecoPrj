package rankUpdate;

import main.Main;
import member.EcoDto;
import rk.RkController;
import rk.RkDto;

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
		String cr = new RankUpdateService().changeRank(ed);

		String userRank = ed.getRankName();
		System.out.println(userRank);
		System.out.println(cr);
		if(!userRank.equals(cr)) {
			ed.setRankName(cr);
			System.out.println(ed.getNick()+"님 "+ed.getRankName()+ "로 등업 축하드립니다!!");
		}
		
	}
}
