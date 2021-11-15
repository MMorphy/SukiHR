package hr.petkovic.iehr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "personal_debt_payments")
public class PersonalDebtPayments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date paymentDate;

	@Column(nullable = false, precision = 2)
	private Float amount = 0F;

	private String description = "";

	@ManyToOne(targetEntity = PersonalDebt.class)
	@JoinColumn(name = "fk_personal_debt")
	private PersonalDebt debt;

	public PersonalDebtPayments() {
	}

	public PersonalDebtPayments(Date paymentDate, Float amount, PersonalDebt debt, String description) {
		super();
		this.paymentDate = paymentDate;
		this.amount = amount;
		this.debt = debt;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public PersonalDebt getDebt() {
		return debt;
	}

	public void setDebt(PersonalDebt debt) {
		this.debt = debt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
