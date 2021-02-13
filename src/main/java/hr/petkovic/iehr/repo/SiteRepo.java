package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {

	List<Site> findAllByUser_username(String username);

	List<Site> findAllByActive(Boolean active);

	List<Site> findAllByUser_usernameAndActive(String username, Boolean active);
}
