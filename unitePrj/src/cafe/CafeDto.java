package cafe;

public class CafeDto {
	
    private int no; 
    private String name;
    private int cupPoint;
    private int tumPoint;
    private int secretCode;
	
    public CafeDto(int no, String name, int cupPoint, int tumPoint, int secretCode) {
		this.no = no;
		this.name = name;
		this.cupPoint = cupPoint;
		this.tumPoint = tumPoint;
		this.secretCode = secretCode;
	}
    
    public CafeDto() {
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

	public int getCupPoint() {
		return cupPoint;
	}

	public void setCupPoint(int cupPoint) {
		this.cupPoint = cupPoint;
	}

	public int getTumPoint() {
		return tumPoint;
	}

	public void setTumPoint(int tumPoint) {
		this.tumPoint = tumPoint;
	}

	public int getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(int secretCode) {
		this.secretCode = secretCode;
	}

	public String toString() {
		return "CafeDto [no=" + no + ", name=" + name + ", cupPoint=" + cupPoint + ", tumPoint=" + tumPoint
				+ ", secretCode=" + secretCode + "]";
	}
    
    



}//class
