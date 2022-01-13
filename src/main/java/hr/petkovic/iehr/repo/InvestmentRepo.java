package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Investment;

public interface InvestmentRepo extends JpaRepository<Investment, Long> {

	public List<Investment> findAllByProject_Id(Long id);

	public void deleteAllByProject_id(Long id);
}
