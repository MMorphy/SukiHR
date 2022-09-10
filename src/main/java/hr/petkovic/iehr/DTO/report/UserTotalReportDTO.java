package hr.petkovic.iehr.DTO.report;

public class UserTotalReportDTO extends ReportingBaseDTO {

	String username;

	public UserTotalReportDTO() {
		super();
	}

	public UserTotalReportDTO(String type, Double total, String username) {
		super(type, total);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
