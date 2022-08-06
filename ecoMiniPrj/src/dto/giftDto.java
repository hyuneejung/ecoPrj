package dto;

public class giftDto {

	private int no;
	private String name;
	private int ecoPrice;
	
	public giftDto() {
		
	}
	
	public giftDto(int no, String name, int ecoPrice) {
		super();
		this.no = no;
		this.name = name;
		this.ecoPrice = ecoPrice;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEcoPrice() {
		return ecoPrice;
	}

	public void setEcoPrice(int ecoPrice) {
		this.ecoPrice = ecoPrice;
	}

	@Override
	public String toString() {
		return "gitfDto [no=" + no + ", name=" + name + ", ecoPrice=" + ecoPrice + "]";
	}
	
	
}
