package hr.petkovic.iehr.entity;

import java.util.Date;

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
@Table(name = "savings_payments")
public class SavingsPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date createDate;

	private String note;

	private Float amountInHRK = 0f;

	private Float amountInCurrency = 0f;

	@ManyToOne(targetEntity = Saving.class)
	@JoinColumn(name = "fk_saving")
	private Saving saving;

	// Constructor
	public SavingsPayment() {
	}

	public SavingsPayment(Long id, Date createDate, String note, Float amountInHRK, Float amountInCurrency,
			Saving saving) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.note = note;
		this.amountInHRK = amountInHRK;
		this.amountInCurrency = amountInCurrency;
		this.saving = saving;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Saving getSaving() {
		return saving;
	}

	public void setSaving(Saving saving) {
		this.saving = saving;
	}
}
