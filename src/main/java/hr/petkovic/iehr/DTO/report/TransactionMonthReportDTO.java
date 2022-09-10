package hr.petkovic.iehr.DTO.report;

public class TransactionMonthReportDTO extends TransactionYearReportDTO {

	Integer month;

	public TransactionMonthReportDTO() {
		super();
	}

	public TransactionMonthReportDTO(String type, Double total, Integer year, Integer month) {
		super(type, total, year);
		this.month = month;
	}

	public TransactionMonthReportDTO(String type, Double total, String category, Integer year, Integer month) {
		super(type, total, category, year);
		this.month = month;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

}
