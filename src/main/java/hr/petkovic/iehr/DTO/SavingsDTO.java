package hr.petkovic.iehr.DTO;

public class SavingsDTO {

	private Float amountInHRK;

	private Float amountInCurrency;

	private String note;

	public SavingsDTO() {
		// TODO Auto-generated constructor stub
	}

	public Float getAmountInHRK() {
		return amountInHRK;
	}

	public void setAmountInHRK(Float amountInHRK) {
		this.amountInHRK = amountInHRK;
	}

	public Float getAmountInCurrency() {
		return amountInCurrency;
	}

	public void setAmountInCurrency(Float amountInCurrency) {
		this.amountInCurrency = amountInCurrency;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
