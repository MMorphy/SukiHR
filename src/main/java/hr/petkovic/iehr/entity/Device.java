package hr.petkovic.iehr.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "devices")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer amount = 0;

	@Column(nullable = false)
	private Integer inUse = 0;

	@JsonIgnore
	@OneToMany(mappedBy = "device")
	private Set<SiteDevices> sites;

	@OneToMany(mappedBy = "device")
	private List<DeviceHistory> history = new ArrayList<>();

	// Constructors
	public Device() {
		this.sites = new HashSet<SiteDevices>();
	}

	public Device(String name) {
		this.sites = new HashSet<SiteDevices>();
		this.name = name;
	}

	public Device(Long id, String name, Integer amount, Integer inUse, Set<SiteDevices> sites,
			List<DeviceHistory> history) {
		super();
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.inUse = inUse;
		this.sites = sites;
		this.history = history;
	}

	// Getters and Setters
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getInUse() {
		return inUse;
	}

	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	public Set<SiteDevices> getSites() {
		return sites;
	}

	public void setSites(Set<SiteDevices> sites) {
		this.sites = sites;
	}

	public List<DeviceHistory> getHistory() {
		return history;
	}

	public void setHistory(List<DeviceHistory> history) {
		this.history = history;
	}

}
