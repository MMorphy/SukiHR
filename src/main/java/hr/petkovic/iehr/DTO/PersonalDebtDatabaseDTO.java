package hr.petkovic.iehr.DTO;

public class PersonalDebtDatabaseDTO {

	Long debtId;
	Double paidAmount;

	public PersonalDebtDatabaseDTO() {
	}

	public PersonalDebtDatabaseDTO(Long debtId, Double paidAmount) {
		super();
		this.debtId = debtId;
		this.paidAmount = paidAmount;
	}
	public Long getId() {
		return debtId;
	}
	public void setId(Long debt) {
		this.debtId = debt;
	}
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

}
