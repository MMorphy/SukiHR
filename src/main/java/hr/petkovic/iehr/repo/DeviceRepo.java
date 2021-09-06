package hr.petkovic.iehr.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hr.petkovic.iehr.DTO.DeviceMapDTO;
import hr.petkovic.iehr.entity.Device;

public interface DeviceRepo extends JpaRepository<Device, Long> {

	public Optional<Device> findByName(String name);

	@Query("SELECT d FROM Device d WHERE d.amount - d.inUse > 0")
	public List<Device> findAllAvailableDevices();
	
	@Query("SELECT d FROM Device d WHERE d.amount - d.inUse < 1")
	public List<Device> findAllUnavailableDevices();

	@Query("SELECT new hr.petkovic.iehr.DTO.DeviceMapDTO(d, 0) "
			+ "FROM Device d WHERE d.amount - d.inUse > 0")
	public List<DeviceMapDTO> findAvailableDeviceMapDTO();
}
