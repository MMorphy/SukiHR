package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import antlr.collections.List;
import hr.petkovic.iehr.entity.Debt;
import hr.petkovic.iehr.service.DebtService;

@Controller
@RequestMapping("/debt")
public class DebtController {

	private DebtService debtSer;

	public DebtController(DebtService debtService) {
		this.debtSer = debtService;
	}

	@GetMapping(value = { "/", "/{username}" })
	public String getAllDebts(@PathVariable(required = false) String username, Model model) {
		if (username == null || username.isEmpty()) {
			model.addAttribute("debts", debtSer.findAllDebts());
		} else {
			model.addAttribute("debts", debtSer.findAllDebtsForUsername(username));
		}
		return "debt/list";
	}

	@GetMapping("/site/{siteId}")
	public String getAllDebtsForSite(@PathVariable(required = true) Long siteId, Model model) {
		model.addAttribute("debts",debtSer.findAllDebtsForSiteId(siteId));
		return "debt/list";
	}

	@GetMapping("/edit/{id}")
	public String getSiteDebtEditing(@PathVariable(required = true) Long id, Model model) {
		model.addAttribute("editDebt", debtSer.findById(id));
		return "debt/edit";
	}

	@PostMapping("/edit/{id}")
	public String editDebt(@PathVariable(required = true) Long id, Debt editDebt) {
		debtSer.editDebt(id, editDebt);
		return "redirect:/";
	}

	@PostMapping("/delete/{id}")
	public String deleteDebt(@PathVariable(required = true) Long id) {
		debtSer.deleteDebt(id);
		return "redirect:/";
	}
}
