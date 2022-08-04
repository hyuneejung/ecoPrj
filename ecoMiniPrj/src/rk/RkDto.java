package rk;

public class RkDto {

	public RkDto() {
	}
	
	public RkDto(String rankName, int bonus) {
		this.rankName = rankName;
		this.bonus = bonus;
	}

	private String rankName;
	private int bonus;
	
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public int getBonus() {
		return bonus;
	}
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	@Override
	public String toString() {
		return "RkDto [rankName=" + rankName + ", bonus=" + bonus + "]";
	}
	
	
	
}
