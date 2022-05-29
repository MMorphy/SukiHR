package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Saving;
import hr.petkovic.iehr.service.SavingsService;

@Controller
@RequestMapping("/savings")
public class SavingsController {

	private SavingsService savSer;

	public SavingsController(SavingsService savingsService) {
		this.savSer = savingsService;
	}

	@GetMapping("/")
	public String getSavings(Model model) {
		model.addAttribute("savings", savSer.findAllSavings());
		return "savings/list";
	}

	@GetMapping("/add")
	public String getSavingsAdding(Model model) {
		model.addAttribute("addSaving", savSer.getNewSaving());
		return "savings/add";
	}

	@PostMapping("/add")
	public String addSaving(Saving addSaving) {
		savSer.saveSaving(addSaving);
		return "redirect:/savings/";
	}

	@GetMapping("/edit/{id}")
	public String getSavingsEditing(@PathVariable(name = "id") Long id, Model model) {
		Saving editSaving = savSer.findSavingById(id);
		model.addAttribute("editSaving", editSaving);
		return "savings/edit";
	}

	@PostMapping("/edit/{id}")
	public String editSaving(@PathVariable(name = "id") Long id, Saving editSaving) {
		savSer.editSaving(id, editSaving);
		return "redirect:/savings/";
	}

	@PostMapping("/delete/{id}")
	public String deleteSaving(@PathVariable(name = "id") Long id) {
		savSer.deleteSavingById(id);
		return "redirect:/savings/";
}
}
