package hr.petkovic.iehr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

}
