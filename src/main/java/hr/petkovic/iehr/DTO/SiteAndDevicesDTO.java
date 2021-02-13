package hr.petkovic.iehr.DTO;

import java.util.HashMap;
import java.util.Map;

import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.entity.Site;

public class SiteAndDevicesDTO {

	private Site site;
	private Map<Device, Integer> devices;

	// Constructors
	public SiteAndDevicesDTO() {
		this.devices = new HashMap<>();
	}

	public SiteAndDevicesDTO(Site site, Map<Device, Integer> devices) {
		super();
		this.site = site;
		this.devices = devices;
	}

	// Getters and setters
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Map<Device, Integer> getDevices() {
		return devices;
	}

	public void setDevices(Map<Device, Integer> devices) {
		this.devices = devices;
	}
}
