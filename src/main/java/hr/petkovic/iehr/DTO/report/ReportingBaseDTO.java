package hr.petkovic.iehr.DTO.report;

public class ReportingBaseDTO {

	String type;

	Double total;

	public ReportingBaseDTO() {
	}

	public ReportingBaseDTO(String type, Double total) {
		super();
		this.type = type;
		this.total = total;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
