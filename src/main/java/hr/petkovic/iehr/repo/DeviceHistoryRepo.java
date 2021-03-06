package hr.petkovic.iehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.DeviceHistory;

public interface DeviceHistoryRepo extends JpaRepository<DeviceHistory, Long> {

}
