package hr.petkovic.iehr.DTO;

import hr.petkovic.iehr.entity.Saving;

public class SavingsUIDTO {

	private Saving saving;

	private Double localTotal;

	private Double currencyTotal;

	public SavingsUIDTO(Saving saving, Double localTotal, Double currencyTotal) {
		super();
		this.saving = saving;
		this.localTotal = localTotal;
		this.currencyTotal = currencyTotal;
	}

	public Saving getSaving() {
		return saving;
	}

	public void setSaving(Saving saving) {
		this.saving = saving;
	}

	public Double getLocalTotal() {
		return localTotal;
	}

	public void setLocalTotal(Double localTotal) {
		this.localTotal = localTotal;
	}

	public Double getCurrencyTotal() {
		return currencyTotal;
	}

	public void setCurrencyTotal(Double currencyTotal) {
		this.currencyTotal = currencyTotal;
	}
}
