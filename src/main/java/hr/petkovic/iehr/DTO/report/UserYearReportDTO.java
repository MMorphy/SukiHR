package hr.petkovic.iehr.DTO.report;

public class UserYearReportDTO extends UserTotalReportDTO {

	Integer year;

	public UserYearReportDTO() {
		super();
	}

	public UserYearReportDTO(String type, Double total, String username, Integer year) {
		super(type, total, username);
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
