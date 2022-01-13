package hr.petkovic.iehr.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hr.petkovic.iehr.DTO.ProjectDatabaseDTO;
import hr.petkovic.iehr.entity.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {
	
	@Query("SELECT new hr.petkovic.iehr.DTO.ProjectDatabaseDTO(p.id, SUM(inv.amount)) "
			+ "FROM Project p "
			+ "LEFT JOIN p.investments inv "
			+ "GROUP BY p.id")
	public List<ProjectDatabaseDTO> findAllProjects();
}
