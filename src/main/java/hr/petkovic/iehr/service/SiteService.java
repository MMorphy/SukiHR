package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.DTO.DeviceMapDTO;
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

	public List<Site> findAllActiveSitesWithoutZeroDevices() {
		List<Site> siteList = findAllActiveSites();
		List<Site> returnList = new ArrayList<Site>();
		for (int i = 0; i < siteList.size(); i++) {
			Set<SiteDevices> set = new HashSet<>();
			for (SiteDevices d : siteList.get(i).getDevices()) {
				// TODO makni null ako radi nakon testa
				if (d.getAmount() != null && !d.getAmount().equals(0)) {
					set.add(d);
				}
			}
			Site s = siteList.get(i);
			s.setDevices(set);
			returnList.add(s);
		}
		return returnList;
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
		site = validateSite(site);
		site.setUser(userSer.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		saveSite(site);
		for (DeviceMapDTO dMap : dto.getDevices()) {
			Device d = deviceSer.findDeviceByName(dMap.getDevice().getName());
			site.addDevice(
					sdSer.save(new SiteDevices(new SiteDeviceKey(site.getId(), d.getId()), site, d, dMap.getAmount())));
			deviceSer.increaseInUse(d, dMap.getAmount());
		}
		return saveSite(site);
	}

	private Site validateSite(Site site) {
		if (site.getAddress() == null) {
			site.setAddress("");
		}
		if (site.getContact() == null) {
			site.setContact("");
		}
		return site;
	}

	public Site updateSiteWithDevices(SiteAndDevicesDTO oldDTO, SiteAndDevicesDTO newDTO) {
		Site oldSite = oldDTO.getSite();
		newDTO.setSite(validateSite(newDTO.getSite()));
		oldSite.setAddress(newDTO.getSite().getAddress());
		oldSite.setContact(newDTO.getSite().getContact());
		oldSite.setName(newDTO.getSite().getName());
		oldSite = saveSite(oldSite);

		for (DeviceMapDTO newDeviceMap : newDTO.getDevices()) {
			if (dtoExistsByNameInList(newDeviceMap, oldDTO.getDevices())) {
				DeviceMapDTO oldDeviceMap = findDtoInListByName(newDeviceMap, oldDTO.getDevices());
				if (isSameDeviceName(newDeviceMap, oldDeviceMap) && !isSameDeviceAmount(newDeviceMap, oldDeviceMap)) {
					if (isNewZeroAndOldIsnt(newDeviceMap, oldDeviceMap)) {
						sdSer.setAmountToZero(oldSite, newDeviceMap.getDevice());
						deviceSer.decreaseInUse(newDeviceMap.getDevice(), oldDeviceMap.getAmount());
						DeviceHistory dh = new DeviceHistory();
						dh.setDevice(newDeviceMap.getDevice());
						dh.setAmount(oldDeviceMap.getAmount() - newDeviceMap.getAmount());
						dh.setDescription("Izmjena lokala: " + oldSite.getName());
						dhSer.saveDeviceHistory(dh);
					} else if (isNewLessThanOld(newDeviceMap, oldDeviceMap)) {
						SiteDevices sd = sdSer.findBySiteAndDevice(oldSite, newDeviceMap.getDevice());
						if (sd == null) {
							sd = new SiteDevices(new SiteDeviceKey(oldSite.getId(), newDeviceMap.getDevice().getId()),
									oldSite, newDeviceMap.getDevice(), 0);
							sd = sdSer.save(sd);
						}
						Integer difference = Math
								.abs(sdSer.findBySiteAndDevice(oldSite, newDeviceMap.getDevice()).getAmount()
										- newDeviceMap.getAmount());
						sd.setAmount(newDeviceMap.getAmount());
						sdSer.save(sd);
						deviceSer.decreaseInUse(newDeviceMap.getDevice(), difference);
						DeviceHistory dh = new DeviceHistory();
						dh.setDevice(newDeviceMap.getDevice());
						dh.setAmount(difference);
						dh.setDescription("Izmjena lokala: " + oldSite.getName());
						dhSer.saveDeviceHistory(dh);
					} else if (isNewGreaterThanOld(newDeviceMap, oldDeviceMap)) {
						SiteDevices sd = sdSer.findBySiteAndDevice(oldSite, newDeviceMap.getDevice());
						if (sd == null) {
							sd = new SiteDevices(new SiteDeviceKey(oldSite.getId(), newDeviceMap.getDevice().getId()),
									oldSite, newDeviceMap.getDevice(), 0);
							sd = sdSer.save(sd);
						}
						Integer difference = Math
								.abs(sdSer.findBySiteAndDevice(oldSite, newDeviceMap.getDevice()).getAmount()
										- newDeviceMap.getAmount());
						sd.setAmount(newDeviceMap.getAmount());
						sdSer.save(sd);
						deviceSer.increaseInUse(newDeviceMap.getDevice(), difference);
					}
				}
			} else {
				oldSite.addDevice(
						sdSer.save(new SiteDevices(new SiteDeviceKey(oldSite.getId(), newDeviceMap.getDevice().getId()),
								oldSite, newDeviceMap.getDevice(), newDeviceMap.getAmount())));
				deviceSer.increaseInUse(newDeviceMap.getDevice(), newDeviceMap.getAmount());
			}
		}
		return saveSite(oldSite);
	}

	private boolean isNewLessThanOld(DeviceMapDTO newDeviceMap, DeviceMapDTO oldDeviceMap) {
		if (newDeviceMap.getAmount().compareTo(oldDeviceMap.getAmount()) == -1) {
			return true;
		}
		return false;
	}

	private boolean isNewGreaterThanOld(DeviceMapDTO newDeviceMap, DeviceMapDTO oldDeviceMap) {
		if (newDeviceMap.getAmount().compareTo(oldDeviceMap.getAmount()) == 1) {
			return true;
		}
		return false;
	}

	private boolean isSameDeviceName(DeviceMapDTO first, DeviceMapDTO second) {
		if (first.getDevice().getName().equals(second.getDevice().getName()))
			return true;
		return false;
	}

	private boolean isSameDeviceAmount(DeviceMapDTO first, DeviceMapDTO second) {
		if (first.getAmount().equals(second.getAmount()))
			return true;
		return false;
	}

	private boolean isNewZeroAndOldIsnt(DeviceMapDTO newer, DeviceMapDTO older) {
		if (newer.getAmount().equals(0) && !older.getAmount().equals(0))
			return true;
		return false;
	}

	private DeviceMapDTO findDtoInListByName(DeviceMapDTO dto, List<DeviceMapDTO> list) {
		for (DeviceMapDTO d : list) {
			if (d.getDevice().getName().equals(dto.getDevice().getName())) {
				return d;
			}
		}
		return null;
	}

	private boolean dtoExistsByNameInList(DeviceMapDTO dto, List<DeviceMapDTO> list) {
		for (DeviceMapDTO d : list) {
			if (d.getDevice().getName().equals(dto.getDevice().getName())) {
				return true;
			}
		}
		return false;
	}
}
