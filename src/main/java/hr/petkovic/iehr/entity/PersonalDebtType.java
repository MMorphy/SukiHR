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
@Table(name = "personal_debt_types")
public class PersonalDebtType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String type;

	@OneToMany(mappedBy = "type")
	private List<PersonalDebt> transactions = new ArrayList<PersonalDebt>();

	public PersonalDebtType() {
		this.transactions = new ArrayList<>();
	}

	public PersonalDebtType(String type, List<PersonalDebt> transactions) {
		super();
		this.type = type;
		this.transactions = transactions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PersonalDebt> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<PersonalDebt> transactions) {
		this.transactions = transactions;
	}

}
