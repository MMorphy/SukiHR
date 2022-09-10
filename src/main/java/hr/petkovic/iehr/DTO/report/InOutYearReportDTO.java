package hr.petkovic.iehr.DTO.report;

public class InOutYearReportDTO extends ReportingBaseDTO {

	Integer year;

	public InOutYearReportDTO() {
		super();
	}

	public InOutYearReportDTO(String type, Double total, Integer year) {
		super(type, total);
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
}
