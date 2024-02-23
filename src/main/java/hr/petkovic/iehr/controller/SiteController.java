package hr.petkovic.iehr.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.DTO.SiteAndDevicesDTO;
import hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO;
import hr.petkovic.iehr.entity.DeviceHistory;
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.entity.SiteDevices;
import hr.petkovic.iehr.service.DeviceHistoryService;
import hr.petkovic.iehr.service.DeviceService;
import hr.petkovic.iehr.service.SiteDevicesService;
import hr.petkovic.iehr.service.SiteService;
import hr.petkovic.iehr.service.TransactionService;

@Controller
@RequestMapping("/site")
public class SiteController {
	Logger logger = LoggerFactory.getLogger(SiteController.class);

	private SiteService siteSer;
	private DeviceService deviceSer;
	private SiteDevicesService sdSer;
	private TransactionService tServ;
	private DeviceHistoryService dhSer;

	public SiteController(SiteService siteService, DeviceService deviceService, SiteDevicesService sdService,
			TransactionService tService, DeviceHistoryService dhService) {
		siteSer = siteService;
		deviceSer = deviceService;
		sdSer = sdService;
		tServ = tService;
		dhSer = dhService;
	}

	@GetMapping(value = { "/", "/{username}" })
	public String getAllSites(@PathVariable(required = false) String username, Model model) {
		if (username == null || username.isEmpty()) {
			model.addAttribute("sites",
					tServ.makeFullSiteListActive(siteSer.findAllSites(), tServ.findAllSitesWithDebt()));
			model.addAttribute("inactive",
					tServ.makeFullSiteListInactive(siteSer.findAllSites(), tServ.findAllSitesWithDebt()));
		} else {
			model.addAttribute("sites", tServ.makeFullSiteListActive(siteSer.findAllSitesByUsernameRole(username),
					tServ.findAllSitesWithDebtForUsername(username)));
			model.addAttribute("inactive", tServ.makeFullSiteListInactive(siteSer.findAllSitesByUsernameRole(username),
					tServ.findAllSitesWithDebtForUsername(username)));
		}
		return "site/list";
	}

	@GetMapping(value = { "/debt", "/debt/{username}" })
	public String getAllSitesWithDebt(@PathVariable(required = false) String username, Model model) {
		if (username == null || username.isEmpty()) {
			model.addAttribute("sites",
					tServ.makeFullSiteListActive(new ArrayList<Site>(), tServ.findAllSitesWithDebt()));
			model.addAttribute("inactive",
					tServ.makeFullSiteListInactive(new ArrayList<Site>(), tServ.findAllSitesWithDebt()));
		} else {
			model.addAttribute("sites", tServ.makeFullSiteListActive(new ArrayList<Site>(),
					tServ.findAllSitesWithDebtForUsername(username)));
			model.addAttribute("inactive", tServ.makeFullSiteListInactive(new ArrayList<Site>(),
					tServ.findAllSitesWithDebtForUsername(username)));
		}
		return "site/list";
	}

	@GetMapping("/add")
	public String getSiteAdding(Model model) {
		SiteAndDevicesDTO dto = new SiteAndDevicesDTO();
		dto.setSite(new Site());
		dto.setDevices(deviceSer.getAvailableDeviceMapDTO());
		model.addAttribute("DTO", dto);
		return "site/add";
	}

	@PostMapping("/add")
	public String getSiteAdding(Model model, SiteAndDevicesDTO siteAndDevices) {
		siteSer.saveSiteWithDevices(siteAndDevices);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String getSiteEditing(@PathVariable Long id, Model model) {
		model.addAttribute("DTO", new SiteAndDevicesDTO(siteSer.findSiteById(id),
				deviceSer.getAvailableDeviceMapDTOForSiteId(siteSer.findSiteById(id))));
		return "site/edit";
	}

	@PostMapping("/edit/{id}")
	public String editSite(@PathVariable Long id, Model model, SiteAndDevicesDTO siteAndDevices) {
		siteSer.updateSiteWithDevices(new SiteAndDevicesDTO(siteSer.findSiteById(id),
				deviceSer.getAvailableDeviceMapDTOForSiteId(siteSer.findSiteById(id))), siteAndDevices);
		return "redirect:/";
	}

	@GetMapping("/disable/{id}")
	public String getSiteDisabling(@PathVariable("id") Long id, Model model) {
		model.addAttribute("DTO",
				tServ.findSiteAndDebtBySiteId(id));
		return "site/disable";
	}
	@PostMapping("/disable/{id}")
	public String disableSite(@PathVariable("id") Long id, SiteWithTotalDebtDTO siteAndDevices) {
		Site site = siteSer.findSiteById(id);
		if (site != null && site.getActive().equals(new Boolean(true))) {
			site = siteSer.disableSite(site, siteAndDevices);
			for (SiteDevices sd : site.getDevices()) {
				deviceSer.decreaseInUse(sd.getDevice(), sd.getAmount());
				DeviceHistory dh = new DeviceHistory();
				dh.setAmount(sd.getAmount());
				dh.setDevice(sd.getDevice());
				dh.setDescription("Povlaćenje lokala " + site.getName());
				dhSer.saveDeviceHistory(dh);
				sdSer.delete(sd);
			}
			BigDecimal bdValue = BigDecimal.valueOf(tServ.findSiteAndDebtBySiteId(site.getId()).getDebtTotal());
			bdValue = bdValue.setScale(2, BigDecimal.ROUND_DOWN);
			if (bdValue.compareTo(new BigDecimal(0) ) == -1) {
				//wipe old debt and generate new one 
				siteSer.generatePersonalDebt(site, bdValue);
				//tServ.addSiteClosureTrans(site,bdValue);
			}
		} else {
			logger.warn("Trying to disable an already inactive site: " + site.getName());
		}
		return "redirect:/";
	}
}
