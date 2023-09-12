package hr.petkovic.iehr.DTO.report;

import hr.petkovic.iehr.entity.Debt;

public class DebtDisplayDTO {
	Debt debt;
	Float currentamount;

	public DebtDisplayDTO() {
		super();
	}

	public DebtDisplayDTO(Debt debt, Float currentamount) {
		super();
		this.debt = debt;
		this.currentamount = currentamount;
	}

	public Debt getDebt() {
		return debt;
	}

	public void setDebt(Debt debt) {
		this.debt = debt;
	}

	public Float getCurrentamount() {
		return currentamount;
	}

	public void setCurrentamount(Float currentamount) {
		this.currentamount = currentamount;
	}

}
