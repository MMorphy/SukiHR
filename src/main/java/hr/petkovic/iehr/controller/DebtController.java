package hr.petkovic.iehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
