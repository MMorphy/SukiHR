package hr.petkovic.iehr.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.PersonalDebtType;

public interface PersonalDebtTypeRepo extends JpaRepository<PersonalDebtType, Long> {

	Optional<PersonalDebtType> findByType(String type);
}
