package hr.petkovic.iehr.DTO.report;

public class UserMonthReportDTO extends UserYearReportDTO {

	Integer month;

	public UserMonthReportDTO() {
		super();
	}

	public UserMonthReportDTO(String type, Double total, String username, Integer year, Integer month) {
		super(type, total, username, year);
		this.month = month;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

}
