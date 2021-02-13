package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long>{

	public List<Transaction> findAllByCreatedBy_Username(String username);
}
