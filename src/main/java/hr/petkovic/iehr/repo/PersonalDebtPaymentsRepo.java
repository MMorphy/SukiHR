package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.PersonalDebtPayments;

public interface PersonalDebtPaymentsRepo extends JpaRepository<PersonalDebtPayments, Long> {

	public List<PersonalDebtPayments> findAllByDebt_Id(Long id);

}
