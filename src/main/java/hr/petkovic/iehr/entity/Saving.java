package hr.petkovic.iehr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "savings")
public class Saving {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String sign;

	// Not used in currrent implementation
	private Float amountInHRK = 0f;

	// Not used in currrent implementation
	private Float amountInCurrency = 0f;

	@OneToMany(mappedBy = "saving")
	private List<SavingsPayment> payments = new ArrayList<SavingsPayment>();

	// Constructors
	public Saving() {
	}

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public List<SavingsPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<SavingsPayment> payments) {
		this.payments = payments;
	}
}
