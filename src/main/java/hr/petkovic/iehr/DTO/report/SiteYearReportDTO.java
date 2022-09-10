package hr.petkovic.iehr.DTO.report;

public class SiteYearReportDTO extends SiteTotalReportDTO {

	Integer year;

	public SiteYearReportDTO() {
		super();
	}

	public SiteYearReportDTO(String type, Double total, String siteName, String location, Integer year) {
		super(type, total, siteName, location);
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
