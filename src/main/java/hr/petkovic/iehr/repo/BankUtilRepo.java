package hr.petkovic.iehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.BankUtil;

public interface BankUtilRepo extends JpaRepository<BankUtil, Long> {

}
