package hr.petkovic.iehr.DTO.report;

public class InOutMonthReportDTO extends InOutYearReportDTO {

	Integer month;

	public InOutMonthReportDTO() {
		super();
	}

	public InOutMonthReportDTO(String type, Double total, Integer year, Integer month) {
		super(type, total, year);
		this.month = month;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

}
