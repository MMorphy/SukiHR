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
import hr.petkovic.iehr.entity.DeviceHistory;
import hr.petkovic.iehr.service.DeviceHistoryService;
import hr.petkovic.iehr.service.DeviceService;
import hr.petkovic.iehr.service.SiteService;

@Controller
@RequestMapping("/device")
public class DeviceController {
	Logger logger = LoggerFactory.getLogger(DeviceController.class);

	private DeviceService deviceSer;
	private SiteService siteSer;
	private DeviceHistoryService dhSer;

	public DeviceController(DeviceService deviceService, SiteService siteService, DeviceHistoryService dhService) {
		deviceSer = deviceService;
		siteSer = siteService;
		dhSer = dhService;
	}

	@GetMapping("/")
	public String getAllDevices(Model model) {
		model.addAttribute("sites", siteSer.findAllActiveSitesWithoutZeroDevices());
		model.addAttribute("devices", deviceSer.findAllDevices());
		model.addAttribute("history", dhSer.findAllDeviceHistoryNotZero());
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
		DeviceHistory devHistory = new DeviceHistory();
		devHistory.setDescription("Novi aparat");
		if (dev == null) {
			addDevice = deviceSer.saveDevice(addDevice);
			devHistory.setDevice(addDevice);
			devHistory.setAmount(addDevice.getAmount());
			dhSer.saveDeviceHistory(devHistory);
			addDevice.getHistory().add(devHistory);
			deviceSer.saveDevice(addDevice);
		} else {
			devHistory.setDevice(dev);
			devHistory.setAmount(addDevice.getAmount());
			dhSer.saveDeviceHistory(devHistory);
			dev.getHistory().add(devHistory);
			deviceSer.increaseNumberOfDevicesAndSave(dev, addDevice.getAmount());
		}
		return "redirect:/device/";
	}

}
