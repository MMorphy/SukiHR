package hr.petkovic.iehr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.service.DeviceService;
import hr.petkovic.iehr.service.SiteService;
import hr.petkovic.iehr.service.UserService;

@Controller
@RequestMapping("/device")
public class DeviceController {
	Logger logger = LoggerFactory.getLogger(UserService.class);

	private DeviceService deviceSer;
	private SiteService siteSer;

	public DeviceController(DeviceService deviceService, SiteService siteService) {
		deviceSer = deviceService;
		siteSer = siteService;
	}

	@GetMapping("/")
	public String getAllDevices(Model model) {
		model.addAttribute("sites", siteSer.findAllActiveSites());
		model.addAttribute("devices", deviceSer.findAllDevices());
		return "device/list";
	}

	@GetMapping("/{id}")
	public String getDevicesFromSite(@PathVariable(required = true) Long id, Model model) {
		model.addAttribute("sites", siteSer.findSiteById(id));
		model.addAttribute("devices", deviceSer.findAllDevices());
		return "device/list";
	}
	@GetMapping(value = { "/add/", "/add/{id}" })
	public String getDeviceAdding(@PathVariable(required = false) Long id, Model model) {
		if (id == null) {
			model.addAttribute("addDevice", new Device());
		} else {
			model.addAttribute("addDevice", new Device(deviceSer.findDeviceNamyById(id)));
		}
		return "device/add";
	}

	@PostMapping(value = { "/add/" })
	public String addDevice(Device addDevice) {
		Device dev = deviceSer.findDeviceByName(addDevice.getName());
		if (dev == null) {
			deviceSer.saveDevice(addDevice);
		} else {
			deviceSer.increaseNumberOfDevicesAndSave(dev, addDevice.getAmount());
		}
		return "redirect:/device/";
	}

}
