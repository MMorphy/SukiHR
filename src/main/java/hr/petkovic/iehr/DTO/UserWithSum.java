package hr.petkovic.iehr.DTO;

public class UserWithSum {

	private UserWithTotalDebtDTO dto;

	private Double saldo;

	private Float pay;

	public UserWithSum() {
	}

	public UserWithSum(UserWithTotalDebtDTO dto, Double saldo) {
		super();
		this.dto = dto;
		this.saldo = saldo;
	}

	public UserWithSum(UserWithTotalDebtDTO dto, Double saldo, Float pay) {
		super();
		this.dto = dto;
		this.saldo = saldo;
		this.pay = pay;
	}

	public UserWithTotalDebtDTO getDto() {
		return dto;
	}

	public void setDto(UserWithTotalDebtDTO dto) {
		this.dto = dto;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Float getPay() {
		return pay;
	}

	public void setPay(Float pay) {
		this.pay = pay;
	}
}
