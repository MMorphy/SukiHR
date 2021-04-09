package hr.petkovic.iehr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.entity.SiteDevices;
import hr.petkovic.iehr.repo.SiteDevicesRepo;

@Service
public class SiteDevicesService {
	Logger logger = LoggerFactory.getLogger(SiteDevicesService.class);

	private SiteDevicesRepo sdRepo;

	public SiteDevicesService(SiteDevicesRepo sdRepo) {
		this.sdRepo = sdRepo;
	}

	public SiteDevices save(SiteDevices sd) {
		return this.sdRepo.save(sd);
	}

	public void delete(SiteDevices sd) {
		this.sdRepo.delete(sd);
	}

	public SiteDevices setAmountToZero(Site s, Device d) {
		SiteDevices sd = this.sdRepo.findBySiteAndDevice(s, d);
		sd.setAmount(0);
		return this.save(sd);
	}

	public SiteDevices findBySiteAndDevice(Site s, Device d) {
		return this.sdRepo.findBySiteAndDevice(s, d);
	}

	public Integer findAmountForSiteAndDevice(Site s, Device d) {
		SiteDevices sd = this.sdRepo.findBySiteAndDevice(s, d);
		if (sd == null) {
			return 0;
		} else {
			return sd.getAmount();
		}
	}
}
