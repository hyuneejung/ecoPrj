package dto;

import java.sql.Timestamp;

public class HistoryDto {

	private int no;
	private String id;
	private Timestamp timeReport;
	private int point;
	private int reportNo;
	private int placeNo;

	public HistoryDto() {

	}

	public HistoryDto(int no, String id, Timestamp timeReport, int point, int reportNo, int placeNo) {
		super();
		this.no = no;
		this.id = id;
		this.timeReport = timeReport;
		this.point = point;
		this.reportNo = reportNo;
		this.placeNo = placeNo;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getTimeReport() {
		return timeReport;
	}

	public void setTimeReport(Timestamp timeReport) {
		this.timeReport = timeReport;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getReportNo() {
		return reportNo;
	}

	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}

	public int getPlaceNo() {
		return placeNo;
	}

	public void setPlaceNo(int placeNo) {
		this.placeNo = placeNo;
	}

	@Override
	public String toString() {
		return "HistoryDto [no=" + no + ", id=" + id + ", timeReport=" + timeReport + ", point=" + point + ", reportNo="
				+ reportNo + ", placeNo=" + placeNo + "]";
	}

}