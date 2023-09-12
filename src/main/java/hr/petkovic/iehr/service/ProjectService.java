package hr.petkovic.iehr.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.petkovic.iehr.DTO.ProjectDatabaseDTO;
import hr.petkovic.iehr.DTO.ProjectUIDTO;
import hr.petkovic.iehr.DTO.report.ReportingBaseDTO;
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

	public Double findBankSum() {
		Double sum = 0d;
		List<Project> projects = findAllProjects();
		for (Project p : projects) {
			for (Investment i : p.getInvestments()) {
				sum += i.getAmount();
			}
		}
		return sum;
	}

	public Double findBankSumForYear(Integer year) {
		Double sum = 0d;
		List<Project> projects = findAllProjects();
		for (Project p : projects) {
			for (Investment i : p.getInvestments()) {
				if (i.getPaymentDate().getYear() == (year - 1900)) {
					sum += i.getAmount();
				}
			}
		}
		return sum;
	}

	public Double findBankSumForYearMonth(Integer year, Integer month) {
		Double sum = 0d;
		List<Project> projects = findAllProjects();
		for (Project p : projects) {
			for (Investment i : p.getInvestments()) {
				if (i.getPaymentDate().getYear() == (year - 1900) && i.getPaymentDate().getMonth() == (month - 1)) {
					sum += i.getAmount();
				}
			}
		}
		return sum;
	}

	private List<Project> findAllProjects() {
		return pRepo.findAll();
	}

	public ReportingBaseDTO getProjectsReportTotal() {
		return new ReportingBaseDTO("Izlaz", findBankSum());
	}

	public ReportingBaseDTO getProjectsReportTotalForYear(Integer year) {
		return new ReportingBaseDTO("Izlaz", findBankSumForYear(year));
	}

	public ReportingBaseDTO getProjectsReportTotalForYearMonth(Integer year, Integer month) {
		return new ReportingBaseDTO("Izlaz", findBankSumForYearMonth(year, month));
	}
}
