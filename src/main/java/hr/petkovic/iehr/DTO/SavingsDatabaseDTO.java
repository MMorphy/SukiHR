package hr.petkovic.iehr.DTO;

public class SavingsDatabaseDTO {

	private Long id;

	private Double localTotal;

	private Double currencyTotal;

	public SavingsDatabaseDTO() {
	}

	public SavingsDatabaseDTO(Long id, Double localTotal, Double currencyTotal) {
		super();
		this.id = id;
		this.localTotal = localTotal;
		this.currencyTotal = currencyTotal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
