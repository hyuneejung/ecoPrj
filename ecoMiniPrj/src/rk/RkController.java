package rk;

import main.Main;
import member.EcoDto;

public class RkController {
	
	public RkDto rankBonus() {
		RkDto rd = null;
		if(Main.LoginUser == null) {
			return rd;
		}
		EcoDto ed = Main.LoginUser;
		rd = new RkService().rankBonus(ed);
		return rd;
	}
}
