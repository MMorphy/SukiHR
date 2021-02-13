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
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date createDate;

	@Column(nullable = false)
	private String description = "";

	@ManyToOne(targetEntity = TransactionType.class)
	@JoinColumn(name = "fk_type")
	private TransactionType type;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "fk_user")
	private User createdBy;

	@Column(nullable = false)
	private Float amount;

	@ManyToOne(targetEntity = Site.class)
	@JoinColumn(name="fk_site")
	private Site site;

	// Utility methods
	public void addType(TransactionType type) {
		if (this.getType() != null) {
			this.getType().removeTransaction(this);
			type.addTransaction(this);
		} else {
			type.addTransaction(this);
		}
	}

	public void addUser(User user) {
		if (this.getCreatedBy() != null) {
			this.getCreatedBy().removeTransaction(this);
			user.addTransaction(this);
		} else {
			user.addTransaction(this);
		}
	}

	// Constructors
	public Transaction() {
		super();
	}

	public Transaction(Long id, Date createDate, String description, TransactionType type, User createdBy,
			Float amount) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.description = description;
		this.type = type;
		this.createdBy = createdBy;
		this.amount = amount;
	}

	// Getters & Setters
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
