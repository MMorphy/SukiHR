package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Investment;
import hr.petkovic.iehr.entity.Project;
import hr.petkovic.iehr.service.ProjectService;

@Controller
@RequestMapping("/project")
public class ProjectController {

	private ProjectService projServ;

	public ProjectController(ProjectService pServ) {
		projServ = pServ;
	}

	@GetMapping("/")
	public String getProjectHome(Model model) {
		model.addAttribute("projects", projServ.getAllProjects());
		return "project/list";
	}

	@GetMapping("/add")
	public String getProjectAdding(Model model) {
		model.addAttribute("addProject", projServ.getNewProject());
		return "project/add";
	}

	@PostMapping("/add")
	public String addProject(Project addProject) {
		projServ.saveProject(addProject);
		return "redirect:/project/";
	}

	@GetMapping("/edit/{id}")
	public String getProjectEditing(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editProject", projServ.findById(id));
		return "project/edit";
	}

	@PostMapping("/edit/{id}")
	public String editProject(@PathVariable("id") Long id, Project editProject) {
		projServ.editProject(id, editProject);
		return "redirect:/project/";
	}

	@PostMapping("/delete/{id}")
	public String deleteProject(@PathVariable("id") Long id) {
		projServ.deleteProject(id);
		return "redirect:/project/";
	}

	@GetMapping("/investment/{id}")
	public String getInvestments(@PathVariable("id") Long id, Model model) {
		model.addAttribute("investments", projServ.findAllInvestmentsForProjectId(id));
		return "project/investmentList";
	}

	@GetMapping("/investment/add/{id}")
	public String getInvestmentAdding(@PathVariable("id") Long id, Model model) {
		model.addAttribute("addInvestment", projServ.getNewInvestment());
		model.addAttribute("id", id);
		return "project/investmentAdd";
	}

	@PostMapping("/investment/add/{id}")
	public String addInvestment(@PathVariable("id") Long id, Investment addInvestment) {
		projServ.addInvestment(id, addInvestment);
		return "redirect:/project/";
	}

	@GetMapping("/investment/edit/{id}")
	public String getInvestmentEditing(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editInvestment", projServ.findInvestmentById(id));
		return "project/investmentEdit";
	}

	@PostMapping("/investment/edit/{id}")
	public String editInvestment(@PathVariable("id") Long id, Investment editInvestment) {
		projServ.editInvestment(id, editInvestment);
		return "redirect:/project/";
	}

	@PostMapping("/investment/delete/{id}")
	public String deleteInvestment(@PathVariable("id") Long id) {
		projServ.deleteInvestment(id);
		return "redirect:/project/";
	}
}
