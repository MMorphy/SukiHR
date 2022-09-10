package hr.petkovic.iehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.FixedExpense;

public interface FixedExpenseRepo extends JpaRepository<FixedExpense, Long> {

}
