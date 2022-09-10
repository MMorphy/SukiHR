package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.PotentialSite;
import hr.petkovic.iehr.service.PotentialSiteService;

@Controller
@RequestMapping("/potential")
public class PotentialSiteController {

	private PotentialSiteService potServ;

	public PotentialSiteController(PotentialSiteService potentialSiteSerivce) {
		potServ = potentialSiteSerivce;
	}

	@GetMapping("/")
	public String getPotentialSiteHome(Model model) {
		model.addAttribute("potentialActive", potServ.getAllActivePotentialSites());
		model.addAttribute("potentialInctive", potServ.getAllInactivePotentialSites());
		return "potential/list";
	}

	@GetMapping("/add")
	public String getPotentialSiteAdding(Model model) {
		model.addAttribute("addPotentialSite", potServ.getNewPotentialSite());
		model.addAttribute("states", potServ.getStates());
		return "potential/add";
	}

	@PostMapping("/add")
	public String addPotentialSite(PotentialSite addPotentialSite) {
		potServ.savePotentialSite(addPotentialSite);
		return "redirect:/potential/";
	}

	@GetMapping("/edit/{id}")
	public String getPotentialSiteEditing(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editPotentialSite", potServ.findById(id));
		model.addAttribute("states", potServ.getStates());
		return "potential/edit";
	}

	@PostMapping("/edit/{id}")
	public String editFixedExpense(@PathVariable("id") Long id, PotentialSite editPotentialSite) {
		potServ.editPotentialSite(id, editPotentialSite);
		return "redirect:/potential/";
	}

	@PostMapping("/delete/{id}")
	public String deletePotentialSite(@PathVariable("id") Long id) {
		potServ.deletePotentialSite(id);
		return "redirect:/potential/";
	}
}
