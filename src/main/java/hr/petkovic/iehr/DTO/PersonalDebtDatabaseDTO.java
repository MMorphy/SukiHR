package hr.petkovic.iehr.DTO;

public class PersonalDebtDatabaseDTO {

	Long debtId;
	Double outstandingAmount;

	public PersonalDebtDatabaseDTO() {
	}

	public PersonalDebtDatabaseDTO(Long debtId, Double outstandingAmount) {
		super();
		this.debtId = debtId;
		this.outstandingAmount = outstandingAmount;
	}
	public Long getId() {
		return debtId;
	}
	public void setId(Long debt) {
		this.debtId = debt;
	}
	public Double getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

}
