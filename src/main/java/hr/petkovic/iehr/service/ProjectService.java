package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.petkovic.iehr.DTO.ProjectDatabaseDTO;
import hr.petkovic.iehr.DTO.ProjectUIDTO;
import hr.petkovic.iehr.entity.Investment;
import hr.petkovic.iehr.entity.Project;
import hr.petkovic.iehr.repo.InvestmentRepo;
import hr.petkovic.iehr.repo.ProjectRepo;

@Service
public class ProjectService {

	private ProjectRepo pRepo;
	private InvestmentRepo iRepo;

	public ProjectService(ProjectRepo projectRepo, InvestmentRepo investmentRepo) {
		pRepo = projectRepo;
		iRepo = investmentRepo;
	}

	public Project getNewProject() {
		return new Project();
	}

	public Investment getNewInvestment() {
		return new Investment();
	}

	public List<ProjectUIDTO> getAllProjects() {
		List<ProjectDatabaseDTO> DBlist = pRepo.findAllProjects();
		List<ProjectUIDTO> returnList = new ArrayList<ProjectUIDTO>();
		for (ProjectDatabaseDTO DBDTO : DBlist) {
			if (DBDTO.getTotal() == null) {
				DBDTO.setTotal(new Double(0d));
			}
			returnList.add(new ProjectUIDTO(findById(DBDTO.getId()), DBDTO.getTotal()));
		}
		return returnList;
	}

	public Project findById(Long id) {
		try {
			return pRepo.findById(id).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public Investment findInvestmentById(Long id) {
		try {
			return iRepo.findById(id).get();
		} catch (Exception ex) {
			return null;
		}
	}

	public List<Investment> findAllInvestmentsForProjectId(Long id) {
		return iRepo.findAllByProject_Id(id);
	}

	public Project saveProject(Project addProject) {
		return pRepo.save(addProject);
	}

	public Investment addInvestment(Long id, Investment addInvestment) {
		Project project = findById(id);
		addInvestment.setProject(project);
		addInvestment.setId(null);
		return iRepo.save(addInvestment);
	}

	public Investment editInvestment(Long id, Investment editInvestment) {
		Investment investment = findInvestmentById(id);
		investment.setAmount(editInvestment.getAmount());
		investment.setDescription(editInvestment.getDescription());
		return iRepo.save(investment);
	}

	public void deleteInvestment(Long id) {
		iRepo.deleteById(id);
	}

	@Transactional
	public void deleteProject(Long id) {
		iRepo.deleteAllByProject_id(id);
		pRepo.deleteById(id);
	}

	public Project editProject(Long id, Project editProject) {
		Project proj = findById(id);
		proj.setDescription(editProject.getDescription());
		proj.setName(editProject.getName());
		return pRepo.save(proj);
	}

}
