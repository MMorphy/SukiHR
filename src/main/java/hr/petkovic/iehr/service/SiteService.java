package hr.petkovic.iehr.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.SiteAndDevicesDTO;
import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.entity.DeviceHistory;
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
	private DeviceHistoryService dhSer;

	public SiteService(SiteRepo siteRepo, DeviceService deviceService, SiteDevicesService sdService,
			UserService userService, DeviceHistoryService dhService) {
		this.siteRepo = siteRepo;
		deviceSer = deviceService;
		sdSer = sdService;
		userSer = userService;
		dhSer = dhService;
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
		return siteRepo.findAllByUser_username(username);
	}

	public Site disableSite(Site site) {
		site.setActive(false);
		site.setReleaseDate(new Date());
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

	public Site updateSiteWithDevices(SiteAndDevicesDTO oldDTO, SiteAndDevicesDTO newDTO) {
		Site oldSite = oldDTO.getSite();
		oldSite.setAddress(newDTO.getSite().getAddress());
		oldSite.setContact(newDTO.getSite().getContact());
		oldSite.setName(newDTO.getSite().getName());
		oldSite = saveSite(oldSite);
		for (Device newD : newDTO.getDevices().keySet()) {
			// old type of device which existed when the site was created
			if (oldDTO.getDevices().containsKey(newD)) {
				// device amount changed
				if (oldDTO.getDevices().get(newD) != newDTO.getDevices().get(newD)) {
					// new device amount set to 0, increase lager, delete site devices entry and
					// generate history
					if (newDTO.getDevices().get(newD) == 0) {
						sdSer.setAmountToZero(oldSite, newD);
						deviceSer.decreaseInUse(newD, oldDTO.getDevices().get(newD));
						DeviceHistory dh = new DeviceHistory();
						dh.setDevice(newD);
						dh.setAmount(oldDTO.getDevices().get(newD) - newDTO.getDevices().get(newD));
						dh.setDescription("Izmjena lokala: " + oldSite.getName());
						dhSer.saveDeviceHistory(dh);
					}
					// new device amount set to a larger number, change lager
					else if (newDTO.getDevices().get(newD) > oldDTO.getDevices().get(newD)) {
						SiteDevices sd = sdSer.findBySiteAndDevice(oldSite, newD);
						if (sd == null) {
							sd = new SiteDevices(new SiteDeviceKey(oldSite.getId(), newD.getId()), oldSite, newD, 0);
							sd = sdSer.save(sd);
						}
						Integer difference = Math.abs(
								sdSer.findBySiteAndDevice(oldSite, newD).getAmount() - newDTO.getDevices().get(newD));
						sd.setAmount(newDTO.getDevices().get(newD));
						sdSer.save(sd);
						deviceSer.increaseInUse(newD, difference);
					}
					// new device amount set to a smaller number, change lager and generate history
					else {
						SiteDevices sd = sdSer.findBySiteAndDevice(oldSite, newD);
						if (sd == null) {
							sd = new SiteDevices(new SiteDeviceKey(oldSite.getId(), newD.getId()), oldSite, newD, 0);
							sd = sdSer.save(sd);
						}
						Integer difference = Math.abs(
								sdSer.findBySiteAndDevice(oldSite, newD).getAmount() - newDTO.getDevices().get(newD));
						sd.setAmount(newDTO.getDevices().get(newD));
						sdSer.save(sd);
						deviceSer.decreaseInUse(newD, difference);
						DeviceHistory dh = new DeviceHistory();
						dh.setDevice(newD);
						dh.setAmount(difference);
						dh.setDescription("Izmjena lokala: " + oldSite.getName());
						dhSer.saveDeviceHistory(dh);
					}
				}
			}
			// new device type/amount added
			else {
				oldSite.addDevice(sdSer.save(new SiteDevices(new SiteDeviceKey(oldSite.getId(), newD.getId()), oldSite,
						newD, newDTO.getDevices().get(newD))));
				deviceSer.increaseInUse(newD, newDTO.getDevices().get(newD));
			}
		}
		return saveSite(oldSite);
	}
}
