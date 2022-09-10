package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.FixedExpense;
import hr.petkovic.iehr.service.FixedExpenseService;

@Controller
@RequestMapping("/fixed")
public class FixedExpenseController {

	private FixedExpenseService fixServ;

	public FixedExpenseController(FixedExpenseService fixedExpenseService) {
		fixServ = fixedExpenseService;
	}

	@GetMapping("/")
	public String getFixedExpenseHome(Model model) {
		model.addAttribute("fixed", fixServ.getAllFixedExpenses());
		return "fixedExpense/list";
	}

	@GetMapping("/add")
	public String getFixedExpenseAdding(Model model) {
		model.addAttribute("addFixExpense", fixServ.getNewFixedExpense());
		return "fixedExpense/add";
	}

	@PostMapping("/add")
	public String addFixedExpense(FixedExpense addFixExpense) {
		fixServ.saveFixedExpense(addFixExpense);
		return "redirect:/fixed/";
	}

	@GetMapping("/edit/{id}")
	public String getFixedExpenseEditing(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editFixExpense", fixServ.findById(id));
		return "fixedExpense/edit";
	}

	@PostMapping("/edit/{id}")
	public String editFixedExpense(@PathVariable("id") Long id, FixedExpense editFixExpense) {
		fixServ.editFixedExpense(id, editFixExpense);
		return "redirect:/fixed/";
	}

	@PostMapping("/delete/{id}")
	public String deleteFixedExpense(@PathVariable("id") Long id) {
		fixServ.deleteFixedExpense(id);
		return "redirect:/fixed/";
	}
}
