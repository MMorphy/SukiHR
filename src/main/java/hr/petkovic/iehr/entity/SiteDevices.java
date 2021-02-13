package hr.petkovic.iehr.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class SiteDevices {

	@EmbeddedId
	SiteDeviceKey id;

	@ManyToOne
	@MapsId("siteId")
	@JoinColumn(name = "site_id")
	Site site;

	@ManyToOne
	@MapsId("deviceId")
	@JoinColumn(name = "device_id")
	Device device;

	Integer amount;

	// Constructors
	public SiteDevices() {
	}

	public SiteDevices(SiteDeviceKey id, Site site, Device device, Integer amount) {
		super();
		this.id = id;
		this.site = site;
		this.device = device;
		this.amount = amount;
	}

	// Getters and setters
	public SiteDeviceKey getId() {
		return id;
	}

	public void setId(SiteDeviceKey id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}