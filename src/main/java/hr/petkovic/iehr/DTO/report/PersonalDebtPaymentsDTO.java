package hr.petkovic.iehr.DTO.report;

import hr.petkovic.iehr.entity.PersonalDebtPayments;

public class PersonalDebtPaymentsDTO {
	PersonalDebtPayments pay;
	Float currentDebt;

	public PersonalDebtPaymentsDTO() {
		super();
	}

	public PersonalDebtPaymentsDTO(PersonalDebtPayments pay, Float currentDebt) {
		super();
		this.pay = pay;
		this.currentDebt = currentDebt;
	}

	public PersonalDebtPayments getPay() {
		return pay;
	}

	public void setPay(PersonalDebtPayments pay) {
		this.pay = pay;
	}

	public Float getCurrentDebt() {
		return currentDebt;
	}

	public void setCurrentDebt(Float currentDebt) {
		this.currentDebt = currentDebt;
	}

}
