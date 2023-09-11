package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hr.petkovic.iehr.DTO.PersonalDebtDatabaseDTO;
import hr.petkovic.iehr.entity.PersonalDebt;
import hr.petkovic.iehr.entity.PersonalDebtType;

public interface PersonalDebtRepo extends JpaRepository<PersonalDebt, Long> {

	@Query("SELECT new hr.petkovic.iehr.DTO.PersonalDebtDatabaseDTO(pd.id,SUM(pay.amount)) "
			+ "FROM PersonalDebt pd "
			+ "LEFT JOIN pd.payments pay "
			+ "GROUP BY pd.id ")
	public List<PersonalDebtDatabaseDTO> findAllActiveDebts();
	
	@Query("SELECT new hr.petkovic.iehr.DTO.PersonalDebtDatabaseDTO(pd.id,SUM(pay.amount)) "
			+ "FROM PersonalDebt pd "
			+ "LEFT JOIN pd.payments pay "
			+ "WHERE pd.type =:type "
			+ "GROUP BY pd.id ")
	public List<PersonalDebtDatabaseDTO> findAllActiveDebtsByType(@Param("type") PersonalDebtType type);
	
	@Query("SELECT SUM(pay.amount)"
			+ "FROM PersonalDebt pd "
			+ "LEFT JOIN pd.payments pay "
			+ "WHERE pd.id = ?1 "
			+ "GROUP BY pd.id, pd.amount ")
	public Double getOutstandingAmountForId(Long id);

	public List<PersonalDebt> findByType(PersonalDebtType type);
}
