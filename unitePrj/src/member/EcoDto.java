package member;

import java.sql.Timestamp;

public class EcoDto {

	public EcoDto() {

	}
	public EcoDto(String id, String pwd, String name, String nick, String gender, String phone, int bankNo,
			String bankName, String account, int point, int addedPoint, Timestamp enroll_date, int rank,
			String rankName, char quit_yn) {
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.nick = nick;
		this.gender = gender;
		this.phone = phone;
		this.bankNo = bankNo;
		this.bankName = bankName;
		this.account = account;
		this.point = point;
		this.addedPoint = addedPoint;
		this.enroll_date = enroll_date;
		this.rank = rank;
		this.rankName = rankName;
		this.quit_yn = quit_yn;
	}
	
	////////////////////////////////////////////////
	private String id;
	private String pwd;
	private String name;
	private String nick;
	private String gender;
	private String phone;
	private int bankNo;
	private String bankName;
	private String account;
	private int point;
	private int addedPoint;
	private Timestamp enroll_date;
	private int rank;
	private String rankName;
	private char quit_yn;
	private int rankNum;
	
	//////////////////////////////////////////////////////
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getBankNo() {
		return bankNo;
	}
	public void setBankNo(int bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getAddedPoint() {
		return addedPoint;
	}
	public void setAddedPoint(int addedPoint) {
		this.addedPoint = addedPoint;
	}
	public Timestamp getEnroll_date() {
		return enroll_date;
	}
	public void setEnroll_date(Timestamp enroll_date) {
		this.enroll_date = enroll_date;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public char getQuit_yn() {
		return quit_yn;
	}
	public void setQuit_yn(char quit_yn) {
		this.quit_yn = quit_yn;
	}
	public int getRankNum() {
		return rankNum;
	}
	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}
	//////////////////////////////////////////////////
	public String toString() {
		return "EcoDto [id=" + id + ", pwd=" + pwd + ", name=" + name + ", nick=" + nick + ", gender=" + gender
				+ ", phone=" + phone + ", bankNo=" + bankNo + ", bankName=" + bankName + ", account=" + account
				+ ", point=" + point + ", addedPoint=" + addedPoint + ", enroll_date=" + enroll_date + ", rank=" + rank
				+ ", rankName=" + rankName + ", quit_yn=" + quit_yn + "]";
	}

}
	
	
	