package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.PotentialSite;

public interface PotentialSiteRepo extends JpaRepository<PotentialSite, Long> {

	List<PotentialSite> findAllByState_name (String name);

	List<PotentialSite> findAllByState_nameNot (String name);

	List<PotentialSite> findAllByState_nameAndUser_username (String name, String username);

	List<PotentialSite> findAllByState_nameNotAndUser_username (String name, String username);
}
