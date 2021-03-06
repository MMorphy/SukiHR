package hr.petkovic.iehr.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "sites")
public class Site {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastVisit;

	//TODO makni ovo i refaktoriraj
	@Column(precision = 2)
	private Float debt = 0F;

	private String contact;

	@Column(nullable = false)
	private Boolean active = true;

	@OneToMany(mappedBy = "site")
	@Fetch(FetchMode.SELECT)
	private Set<SiteDevices> devices;

	@ManyToOne(targetEntity = User.class)
	private User user;

	@OneToMany(mappedBy = "site")
	private Collection<Transaction> transactions = new ArrayList<Transaction>();

	// Utility methods
	public void addDevice(SiteDevices device) {
		this.devices.add(device);
	}

	public void removeDevice(SiteDevices device) {
		if (this.devices.remove(device)) {
		}
	}

	// Constructors
	public Site() {
		this.devices = new HashSet<SiteDevices>();
		this.transactions = new ArrayList<>();
	}

	public Site(Long id, String address, Date lastVisit, Float debt, String contact, Boolean active,
			Set<SiteDevices> devices, User user, String name, List<Transaction> transactions) {
		super();
		this.id = id;
		this.address = address;
		this.lastVisit = lastVisit;
		this.debt = debt;
		this.contact = contact;
		this.active = active;
		this.devices = devices;
		this.user = user;
		this.name = name;
		this.transactions = transactions;
	}

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	public Float getDebt() {
		return debt;
	}

	public void setDebt(Float debt) {
		this.debt = debt;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<SiteDevices> getDevices() {
		return devices;
	}

	public void setDevices(Set<SiteDevices> devices) {
		this.devices = devices;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
	}

	// Hashcode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((debt == null) ? 0 : debt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastVisit == null) ? 0 : lastVisit.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (debt == null) {
			if (other.debt != null)
				return false;
		} else if (!debt.equals(other.debt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastVisit == null) {
			if (other.lastVisit != null)
				return false;
		} else if (!lastVisit.equals(other.lastVisit))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
