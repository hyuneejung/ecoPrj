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
		RkDto rd = new RkController().rankBonus();
		/*
		 * 업데이트전 랭크받아오기
		 * 
		 * 업데이트후 등급업 되면 등급업 축하메시지 로직
		 * 
		 * 
		 * */
		String userRank = ed.getRank();
		String updateRank = rd.getRankName();
		
		new RankUpdateService().rankUpdate(ed);
		
		if(!userRank.equals(updateRank)) {
			String cr = new RankUpdateService().changeRank(ed);
			ed.setRank(cr);
			System.out.println(ed.getNick()+"님 "+ed.getRank()+ "로 등업 축하드립니다!!");
		}
		
	}
}
