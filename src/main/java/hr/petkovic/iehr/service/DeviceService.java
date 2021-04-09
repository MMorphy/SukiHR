package hr.petkovic.iehr.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Device;
import hr.petkovic.iehr.entity.Site;
import hr.petkovic.iehr.repo.DeviceRepo;

@Service
public class DeviceService {
	Logger logger = LoggerFactory.getLogger(DeviceService.class);

	private DeviceRepo deviceRepo;
	private SiteDevicesService sdServ;

	public DeviceService(DeviceRepo deviceRepository, SiteDevicesService sdService) {
		deviceRepo = deviceRepository;
		sdServ = sdService;
	}

	public List<Device> findAllDevices() {
		return deviceRepo.findAll();
	}

	public List<Device> findAllAvailableDevices() {
		return deviceRepo.findAllAvailableDevices();
	}

	public Device findDeviceByName(String name) {
		try {
			return deviceRepo.findByName(name).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public Device saveDevice(Device device) {
		return deviceRepo.save(device);
	}

	public Device increaseNumberOfDevicesAndSave(Device device, Integer number) {
		device.setAmount(device.getAmount() + number);
		return saveDevice(device);
	}

	public String findDeviceNamyById(Long id) {
		try {
			return deviceRepo.findById(id).get().getName();
		} catch (Exception ex) {
			return "";
		}
	}

	public Map<Device, Integer> getAvailableDeviceMap() {
		Map<Device, Integer> map = new LinkedHashMap<Device, Integer>();
		List<Device> availDevices = findAllAvailableDevices();
		for (Device dev : availDevices) {
			map.put(dev, 0);
		}
		return map;
	}

	public Map<Device, Integer> getDeviceMapForSiteId(Site s) {
		Map<Device, Integer> map = new LinkedHashMap<Device, Integer>();
		List<Device> availDevices = findAllAvailableDevices();
		for (Device dev : availDevices) {
			map.put(dev, sdServ.findAmountForSiteAndDevice(s, dev));
		}
		return map;
	}

	public Device increaseInUse(Device device, Integer amount) {
		Device dev = deviceRepo.findById(device.getId()).get();
		if (dev != null) {
			dev.setInUse(dev.getInUse() + amount);
			return saveDevice(dev);
		} else {
			logger.error("Want to increase usage of device " + device.getName() + " which can't be found in DB!");
			return null;
		}
	}

	public Device decreaseInUse(Device device, Integer amount) {
		Device dev = deviceRepo.findById(device.getId()).get();
		if (dev != null) {
			dev.setInUse(dev.getInUse() - amount);
			return saveDevice(dev);
		} else {
			logger.error("Want to decrease usage of device " + device.getName() + " which can't be found in DB!");
			return null;
		}
	}
}
