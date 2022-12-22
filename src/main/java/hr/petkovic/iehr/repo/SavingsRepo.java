package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hr.petkovic.iehr.DTO.SavingsDatabaseDTO;
import hr.petkovic.iehr.entity.Saving;

public interface SavingsRepo extends JpaRepository<Saving, Long> {

	@Query("SELECT new hr.petkovic.iehr.DTO.SavingsDatabaseDTO(s.id, SUM(pay.amountInHRK), SUM(pay.amountInCurrency)) "
			+ "FROM Saving s "
			+ "LEFT JOIN s.payments pay "
			+ "GROUP BY s.id")
	public List<SavingsDatabaseDTO> findAllSavingsDBDTOs();
}
