package hr.petkovic.iehr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.SiteAndDevicesDTO;
import hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO;
import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.entity.Role;
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.entity.SiteDeviceKey;
import hr.petkovic.iehr.entity.SiteDevices;
import hr.petkovic.iehr.repo.SiteRepo;

@Service
public class SiteService {
	Logger logger = LoggerFactory.getLogger(SiteService.class);

	private SiteRepo siteRepo;
	private DeviceService deviceSer;
	private SiteDevicesService sdSer;
	private UserService userSer;
	private TransactionService transSer;

	public SiteService(SiteRepo siteRepo, DeviceService deviceService, SiteDevicesService sdService,
			UserService userService, TransactionService transService) {
		this.siteRepo = siteRepo;
		deviceSer = deviceService;
		sdSer = sdService;
		userSer = userService;
		transSer = transService;
	}

	public Site findSiteById(Long id) {
		try {
			return siteRepo.findById(id).get();
		} catch (Exception ex) {
			logger.error("Exception occured in findSiteById", ex);
			return null;
		}
	}

	public List<Site> findAllSites() {
		return siteRepo.findAll();
	}

	public List<Site> findAllActiveSites() {
		return siteRepo.findAllByActive(true);
	}

	public List<Site> findAllSitesByUsernameRole(String username) {
		for (Role r : userSer.findUserByUsername(username).getRoles()) {
			if (r.getName().equals("ROLE_ADMIN")) {
				return findAllSites();
			}
		}
		return findAllActiveSitesForUsername(username);
	}

	public List<Site> findAllActiveSitesForUsername(String username) {
		return siteRepo.findAllByUser_usernameAndActive(username, true);
	}

	public Site disableSite(Site site) {
		site.setActive(false);
		return saveSite(site);
	}

	public Site saveSite(Site site) {
		return siteRepo.save(site);
	}

	public Site saveSiteWithDevices(SiteAndDevicesDTO dto) {
		Site site = dto.getSite();
		site.setUser(userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		saveSite(site);
		for (Device d : dto.getDevices().keySet()) {
			site.addDevice(sdSer.save(
					new SiteDevices(new SiteDeviceKey(site.getId(), d.getId()), site, d, dto.getDevices().get(d))));
			deviceSer.increaseInUse(d, dto.getDevices().get(d));
		}
		return saveSite(site);
	}
}
