package dto;

public class DonationDto {
	private int no;
	private String name;
	private String object;
	private int addedPoint;
	
	public DonationDto() {
		
	}
	
	public DonationDto(int no, String name, String object, int addedPoint) {
		super();
		this.no = no;
		this.name = name;
		this.object = object;
		this.addedPoint = addedPoint;
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

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public int getAddedPoint() {
		return addedPoint;
	}

	public void setAddedPoint(int addedPoint) {
		this.addedPoint = addedPoint;
	}

	@Override
	public String toString() {
		return "Donation [no=" + no + ", name=" + name + ", object=" + object + ", addedPoint=" + addedPoint + "]";
	}
	
}
