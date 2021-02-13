package hr.petkovic.iehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SiteDeviceKey implements Serializable {

	private static final long serialVersionUID = -1603390612954177502L;

	@Column(name= "site_id")
	Long siteId;

	@Column(name="device_id")
	Long deviceId;


	// Constructors
	public SiteDeviceKey() {
	}

	public SiteDeviceKey(Long siteId, Long deviceId) {
		super();
		this.siteId = siteId;
		this.deviceId = deviceId;
	}

	// Getters and setters
	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	// HashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
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
		SiteDeviceKey other = (SiteDeviceKey) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		return true;
	}

	
	
}
