package hr.petkovic.iehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Saving;

public interface SavingsRepo extends JpaRepository<Saving, Long> {

}
