package hr.petkovic.iehr.DTO;

import hr.petkovic.iehr.entity.PersonalDebt;

public class PersonalDebtUIDTO {

	PersonalDebt debt;
	Double outstandingAmount;

	public PersonalDebtUIDTO() {
	}

	public PersonalDebtUIDTO(PersonalDebt debt, Double outstandingAmount) {
		super();
		this.debt = debt;
		this.outstandingAmount = outstandingAmount;
	}

	public PersonalDebt getDebt() {
		return debt;
	}

	public void setDebt(PersonalDebt debt) {
		this.debt = debt;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
}
