package hr.petkovic.iehr.DTO.report;

public class TransactionTotalReportDTO extends ReportingBaseDTO {

	String category;

	public TransactionTotalReportDTO() {
		super();
	}

	public TransactionTotalReportDTO(String type, Double total) {
		super(type, total);
	}

	public TransactionTotalReportDTO(String type, Double total, String category) {
		super(type, total);
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
