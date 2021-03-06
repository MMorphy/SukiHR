package hr.petkovic.iehr.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean enabled = true;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH })
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	@OneToMany(mappedBy = "createdBy")
	private List<Transaction> transactions = new ArrayList<Transaction>();

	@OneToMany(mappedBy = "user")
	private Set<Site> sites = new HashSet<Site>();

	private Float saldo = 0F;

	// Utility methods
	public void addTransaction(Transaction trans) {
		this.transactions.add(trans);
		trans.setCreatedBy(this);
	}

	public void removeTransaction(Transaction trans) {
		if (this.transactions.remove(trans)) {
			trans.setType(null);
		}
	}

	public void addSite(Site site) {
		this.sites.add(site);
		site.setUser(this);
	}

	public void removeSite(Site site) {
		if (this.sites.remove(site)) {
			site.setUser(null);
		}
	}

	public void increaseSaldo(Float amount) {
		this.saldo += amount;
	}

	public void decreaseSaldo(Float amount) {
		this.saldo -= amount;
	}
	// Constructors
	public User() {
		this.roles = new ArrayList<Role>();
		this.transactions = new ArrayList<Transaction>();
		this.sites = new HashSet<Site>();
	}

	public User(Long id, String username, String password, boolean enabled, Collection<Role> roles,
			List<Transaction> transactions, HashSet<Site> sites, Float saldo) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
		this.transactions = transactions;
		this.sites = sites;
		this.saldo = saldo;
	}

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Set<Site> getSites() {
		return sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

}