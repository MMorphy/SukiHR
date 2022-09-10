package hr.petkovic.iehr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "potential_sites")
public class PotentialSite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date createDate;

	@Column(nullable = false)
	private String name = "";


	@Column(nullable = false)
	private String address = "";

	private String contact;

	private String owner;

	@ManyToOne(targetEntity = PotentialSiteState.class)
	private PotentialSiteState state;

	private Integer evaluation = 0;

	private String description = "";

	public PotentialSite() {
	}

	public PotentialSite(Long id, Date createDate, String name, String address, String contact, String owner,
			PotentialSiteState state, Integer evaluation, String description) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.name = name;
		this.address = address;
		this.contact = contact;
		this.owner = owner;
		this.state = state;
		this.evaluation = evaluation;
		this.description = description;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public PotentialSiteState getState() {
		return state;
	}

	public void setState(PotentialSiteState state) {
		this.state = state;
	}

	public Integer getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Integer evaluation) {
		this.evaluation = evaluation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
