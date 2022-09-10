package hr.petkovic.iehr.DTO.report;

public class TransactionYearReportDTO extends TransactionTotalReportDTO {

	Integer year;

	public TransactionYearReportDTO() {
		super();
	}

	public TransactionYearReportDTO(String type, Double total, String category, Integer year) {
		super(type, total, category);
		this.year = year;
	}

	public TransactionYearReportDTO(String type, Double total, Integer year) {
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
