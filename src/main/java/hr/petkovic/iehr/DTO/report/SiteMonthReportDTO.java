package hr.petkovic.iehr.DTO.report;

public class SiteMonthReportDTO extends SiteYearReportDTO {

	Integer month;

	public SiteMonthReportDTO() {
		super();
	}

	public SiteMonthReportDTO(String type, Double total, String siteName, String location, Integer year,
			Integer month) {
		super(type, total, siteName, location, year);
		this.month = month;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

}
