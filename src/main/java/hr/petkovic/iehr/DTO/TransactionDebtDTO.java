package hr.petkovic.iehr.DTO;

import hr.petkovic.iehr.entity.Transaction;

public class TransactionDebtDTO {

	private Transaction trans;
	private Float debtRepay = 0F;

	public TransactionDebtDTO() {
		trans = new Transaction();
	}

	public TransactionDebtDTO(Transaction trans, Float debtRepay) {
		super();
		this.trans = trans;
		this.debtRepay = debtRepay;
	}

	public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction trans) {
		this.trans = trans;
	}

	public Float getDebtRepay() {
		return debtRepay;
	}

	public void setDebtRepay(Float debtRepay) {
		this.debtRepay = debtRepay;
	}

}
