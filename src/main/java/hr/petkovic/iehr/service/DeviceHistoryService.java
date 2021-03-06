package hr.petkovic.iehr.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.DeviceHistory;
import hr.petkovic.iehr.repo.DeviceHistoryRepo;

@Service
public class DeviceHistoryService {

	private DeviceHistoryRepo deviceHistoryRepo;

	public DeviceHistoryService(DeviceHistoryRepo deviceHistoryRepo) {
		this.deviceHistoryRepo = deviceHistoryRepo;
	}

	public List<DeviceHistory> findAllDeviceHistory() {
		return this.deviceHistoryRepo.findAll();
	}

	public DeviceHistory saveDeviceHistory(DeviceHistory dh) {
		return this.deviceHistoryRepo.save(dh);
	}
}
