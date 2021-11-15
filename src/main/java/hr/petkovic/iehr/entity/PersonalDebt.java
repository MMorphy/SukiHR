package hr.petkovic.iehr.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "personal_debts")
public class PersonalDebt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date createDate;

	@Column(nullable = false, precision = 2)
	private Float amount;

	@Column(nullable = false)
	private String user = "";

	private String description = "";

	@ManyToOne(targetEntity = PersonalDebtType.class)
	@JoinColumn(name = "fk_type")
	private PersonalDebtType type;

	@OneToMany(mappedBy = "debt")
	private List<PersonalDebtPayments> payments = new ArrayList<PersonalDebtPayments>();

	public PersonalDebt() {
		this.payments = new ArrayList<>();
	}

	public PersonalDebt(Long id, Date createDate, PersonalDebtType type, Float amount, String user,
			String description) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.type = type;
		this.amount = amount;
		this.user = user;
		this.description = description;
	}

	public PersonalDebt(String user, Float amount, List<PersonalDebtPayments> payments) {
		super();
		this.user = user;
		this.amount = amount;
		this.payments = payments;
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

	public PersonalDebtType getType() {
		return type;
	}

	public void setType(PersonalDebtType type) {
		this.type = type;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public List<PersonalDebtPayments> getPayments() {
		return payments;
	}

	public void setPayments(List<PersonalDebtPayments> payments) {
		this.payments = payments;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addPayment(PersonalDebtPayments addPayment) {
		getPayments().add(addPayment);
	}

}
