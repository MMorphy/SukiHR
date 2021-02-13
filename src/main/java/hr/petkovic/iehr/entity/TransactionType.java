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
@Table(name = "transaction_types")
public class TransactionType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String mainType;

	@Column(nullable = true)
	private String subType;

	@OneToMany(mappedBy = "type")
	private List<Transaction> transactions = new ArrayList<Transaction>();

	//Utility methods
	public void addTransaction(Transaction trans) {
		this.transactions.add(trans);
		trans.setType(this);
	}

	public void removeTransaction(Transaction trans) {
		if(this.transactions.remove(trans)) {
			trans.setType(null);
		}
	}
	//Constructors
	public TransactionType() {
	}

	public TransactionType(Long id, String mainType, String subType) {
		super();
		this.id = id;
		this.mainType = mainType;
		this.subType = subType;
	}

	//Getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
}
