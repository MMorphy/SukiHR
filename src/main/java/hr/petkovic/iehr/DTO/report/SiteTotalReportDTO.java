package hr.petkovic.iehr.DTO.report;

public class SiteTotalReportDTO extends ReportingBaseDTO {

	String siteName;

	String location;

	public SiteTotalReportDTO() {
		super();
	}

	public SiteTotalReportDTO(String type, Double total, String siteName, String location) {
		super(type, total);
		this.siteName = siteName;
		this.location = location;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
