package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO;
import hr.petkovic.iehr.DTO.UserWithTotalDebtDTO;
import hr.petkovic.iehr.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	public List<Transaction> findAllByCreatedBy_Username(String username);

	public List<Transaction> findAllByCreatedBy_Roles_NameOrType_SubType(String roleName, String subtype);

	@Query("SELECT t FROM Transaction t WHERE t.amount <> 0")
	public List<Transaction> findAllWithValues();

	@Query("SELECT t FROM Transaction t WHERE t.amount <> 0 AND t.createdBy.username = ?1")
	public List<Transaction> findAllWithValuesForUser(String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO(t.site, SUM(t.debt.amount)) "
			+ "FROM Transaction t GROUP BY t.site")
	List<SiteWithTotalDebtDTO> findAllSitesAndDebt();

	@Query("SELECT new hr.petkovic.iehr.DTO.SiteWithTotalDebtDTO(t.site, SUM(t.debt.amount)) "
			+ "FROM Transaction t WHERE t.createdBy.username = :username  GROUP BY t.site")
	List<SiteWithTotalDebtDTO> findAllSitesAndDebtCreatedBy(@Param("username") String username);

	@Query("SELECT new hr.petkovic.iehr.DTO.UserWithTotalDebtDTO(t.createdBy, SUM(t.debt.amount)) "
			+ "FROM Transaction t GROUP BY t.createdBy")
	List<UserWithTotalDebtDTO> findAllDebtForUser();

	@Query("SELECT sum(t.amount) FROM Transaction t WHERE t.createdBy.username = :username AND t.type.mainType = :mainType")
	public Double findAllTransactionsOfMainTypeForUsername(@Param("username") String username, @Param("mainType") String mainType);
}
