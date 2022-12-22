package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.SavingsPayment;

public interface SavingPaymentsRepo extends JpaRepository<SavingsPayment, Long>{

	List<SavingsPayment> findAllBySaving_Id(Long id);
}
