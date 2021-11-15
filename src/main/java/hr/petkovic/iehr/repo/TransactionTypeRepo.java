package hr.petkovic.iehr.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.TransactionType;

public interface TransactionTypeRepo extends JpaRepository<TransactionType, Long> {

	Optional<TransactionType> findByMainTypeAndSubType(String mainType, String subType);

	List<TransactionType> findAllByMainType(String mainType);

	List<TransactionType> findBySubTypeIn(List<String> types);
}
