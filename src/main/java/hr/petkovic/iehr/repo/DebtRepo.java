package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hr.petkovic.iehr.entity.Debt;

public interface DebtRepo extends JpaRepository<Debt, Long> {

	public List<Debt> findAllByTransaction_CreatedBy_Username(String username);

	@Query("SELECT d FROM Debt d WHERE d.amount <> 0")
	public List<Debt> findAllNonZeroDebts();

	@Query("SELECT d FROM Debt d WHERE d.amount <> 0 AND d.transaction.createdBy.username = :username")
	public List<Debt> findAllNonZeroDebtsForUsername(@Param("username") String username);
}
