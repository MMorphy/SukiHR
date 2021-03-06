package hr.petkovic.iehr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.DTO.SiteAndDevicesDTO;
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
			model.addAttribute("sites", tServ.makeFullSiteList(siteSer.findAllSites(), tServ.findAllSitesWithDebt()));
		} else {
			model.addAttribute("sites", tServ.makeFullSiteList(siteSer.findAllSitesByUsernameRole(username),
					tServ.findAllSitesWithDebtForUsername(username)));
		}
		return "site/list";
	}

	@GetMapping("/add")
	public String getSiteAdding(Model model) {
		model.addAttribute("DTO", new SiteAndDevicesDTO(new Site(), deviceSer.getAvailableDeviceMap()));
		return "site/add";
	}

	@PostMapping("/add")
	public String getSiteAdding(Model model, SiteAndDevicesDTO siteAndDevices) {
		siteSer.saveSiteWithDevices(siteAndDevices);
		return "redirect:/";
	}

	// TODO dodati vracanje na stanje
	@PostMapping("/disable/{id}")
	public String disableSite(@PathVariable("id") Long id) {
		Site site = siteSer.findSiteById(id);
		if (site != null && site.getActive().equals(new Boolean(true))) {
			site = siteSer.disableSite(site);
			for (SiteDevices sd : site.getDevices()) {
				deviceSer.decreaseInUse(sd.getDevice(), sd.getAmount());
				DeviceHistory dh = new DeviceHistory();
				dh.setAmount(sd.getAmount());
				dh.setDevice(sd.getDevice());
				dh.setDescription("Povlaćenje lokala " + site.getName());
				dhSer.saveDeviceHistory(dh);
				sdSer.delete(sd);
			}
		} else {
			logger.warn("Trying to disable an already inactive site: " + site.getName());
		}
		return "redirect:/";
	}
}
