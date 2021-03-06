package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Debt;

public interface DebtRepo extends JpaRepository<Debt, Long> {

	public List<Debt> findAllByTransaction_CreatedBy_Username(String username);
}
