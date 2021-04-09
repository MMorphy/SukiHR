package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.DeviceHistory;

public interface DeviceHistoryRepo extends JpaRepository<DeviceHistory, Long> {

	public List<DeviceHistory> findAllByAmountNot(Integer amount);
}
