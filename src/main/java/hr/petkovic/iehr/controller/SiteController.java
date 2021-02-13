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
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.service.DeviceService;
import hr.petkovic.iehr.service.SiteDevicesService;
import hr.petkovic.iehr.service.SiteService;
import hr.petkovic.iehr.service.UserService;

@Controller
@RequestMapping("/site")
public class SiteController {
	Logger logger = LoggerFactory.getLogger(UserService.class);

	private SiteService siteSer;
	private DeviceService deviceSer;
	private SiteDevicesService sdSer;

	public SiteController(SiteService siteService, DeviceService deviceService, SiteDevicesService sdService) {
		siteSer = siteService;
		deviceSer = deviceService;
		sdSer = sdService;
	}

	@GetMapping(value = { "/", "/{username}" })
	public String getAllSites(@PathVariable(required = false) String username, Model model) {
		if (username == null || username.isEmpty()) {
			model.addAttribute("sites", siteSer.findAllActiveSites());
		} else {
			model.addAttribute("sites", siteSer.findAllActiveSitesForUsername(username));
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

	//TODO dodati vracanje na stanje
	@PostMapping("/disable/{id}")
	public String disableSite(@PathVariable("id") Long id) {
		Site site = siteSer.findSiteById(id);
		if (site != null && site.getActive().equals(new Boolean(true))) {
			siteSer.disableSite(site);
		} else {
			logger.warn("Trying to disable an already inactive site: " + site.getName());
		}
		return "redirect:/";
	}
}
