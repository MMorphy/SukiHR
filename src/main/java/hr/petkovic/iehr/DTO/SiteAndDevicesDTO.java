package hr.petkovic.iehr.DTO;

import java.util.ArrayList;
import java.util.List;

import hr.petkovic.iehr.entity.Site;

public class SiteAndDevicesDTO {

	private Site site;
	private List<DeviceMapDTO> devices;

	// Constructors
	public SiteAndDevicesDTO() {
		this.devices = new ArrayList<>();
	}

	public SiteAndDevicesDTO(Site site, List<DeviceMapDTO> devices) {
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

	public List<DeviceMapDTO> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceMapDTO> devices) {
		this.devices = devices;
	}

}
